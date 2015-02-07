package filter.basic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import filter.Basic3dVectorStatistic;
import filter.Vector3d;
import sensor.math.Vector3s;
import basic.SerieWrapper;

public class BasicFilter extends SerieWrapper {

	private enum CalibrationStatus {
		Gyro_Check_Zero, Accelerometer_Check, Normal_Run,
	};

	Vector3s lastGyro = null;
	Vector3s lastAcce = null;
	Vector3s lastMagne = null;
	
	short acceMidValue = 10;
	Vector3d midAcce = new Vector3d();
	
	CalibrationStatus status = CalibrationStatus.Normal_Run;

	Basic3dVectorStatistic gyro = new Basic3dVectorStatistic();
	Basic3dVectorStatistic acc[] = new Basic3dVectorStatistic[6];
	
	SensorZero zero = new SensorZero();

	long start = System.currentTimeMillis();

	FileWriter stillAccelerometer;

	public BasicFilter() throws IOException {
		stillAccelerometer = new FileWriter("/tmp/still.csv");
		
		BasicFilterGUI basicFilterGUI = new BasicFilterGUI();
		basicFilterGUI.addListener(zero);
		zero.addListener(basicFilterGUI);
		basicFilterGUI.setVisible(true);
		
		//load default value
		zero.setAcceZero( new Vector3d(-4089.385,5226.89,7490.706666666667) );
	}

	TreeMap<Short, List<Vector3s>> accByX = new TreeMap<>();
	TreeMap<Short, List<Vector3s>> accByY = new TreeMap<>();
	TreeMap<Short, List<Vector3s>> accByZ = new TreeMap<>();

	int volte = 0;

	@Override
	public void event(EventType t, Vector3s v, long packetNumber) {
		switch (t) {
		case acce:
			v.sub( zero.getAcceZero() );
			
			//mid of last 5 values
			Vector3d tmp = new Vector3d();
			tmp.add(v);
			
			midAcce.mult( (acceMidValue-1.0)/acceMidValue );
			tmp.mult( 1.0/acceMidValue );
			midAcce.add(tmp);
			
			v = new Vector3s((short)midAcce.getX(), (short)midAcce.getY(), (short)midAcce.getZ());
			lastAcce = v;
			

			if (status == CalibrationStatus.Accelerometer_Check && lastGyro != null) {
				if (lastGyro.getX() > -50 && lastGyro.getX() < 50) {
					if (lastGyro.getY() > -50 && lastGyro.getY() < 50) {
						if (lastGyro.getZ() > -50 && lastGyro.getZ() < 50) {

							try {
								stillAccelerometer.append(lastAcce + "\n");
							} catch (IOException e) {
								e.printStackTrace();
							}

							addTo(accByX, lastAcce.getX(), lastAcce);
							addTo(accByY, lastAcce.getY(), lastAcce);
							addTo(accByZ, lastAcce.getZ(), lastAcce);

							int N = 100;

							if (volte++ > N) {
								volte = 0;

								Vector3d sumX = new Vector3d();
								// get 100 biggest and smallest point by X
								sumLimit(sumX, accByX, N);

								//System.out.println("Actual mid by X is " + sumX.mult(1.0 / N));

								Vector3d sumY = new Vector3d();
								// get 100 biggest and smallest point by Y
								sumLimit(sumY, accByY, N);

								//System.out.println("Actual mid by Y is " + sumY.mult(1.0 / N));

								Vector3d sumZ = new Vector3d();
								// get 100 biggest and smallest point by Z
								sumLimit(sumZ, accByZ, N);

								//System.out.println("Actual mid by Z is " + sumZ.mult(1.0 / N));

								Vector3d sum = new Vector3d();
								
								//remove the highest value
								
								sum.add(sumX);
								sum.add(sumY);
								sum.add(sumZ);
								Vector3d mid = new Vector3d(sum);
								mid.mult(1.0 / (N * 2 * 3));

								System.out.println("Actual total mid is " +mid+" sum is: "+sum );
								
								try {
									if (System.in.available() > 0 && System.in.read() == 'S') {
										System.out.println("SAVING!");
										saveTail(accByX, N);
										saveTail(accByY, N);
										saveTail(accByZ, N);
								
										System.out.println("Actual total mid used as accelerometer zero is " +mid );
										
										zero.setAcceZero( mid);
										
										status = CalibrationStatus.Normal_Run;
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
								
							} else {
								// System.out.println("Waiting acce "+volte+" and "+accByX.size());
							}
						}
					}
				}
			}

			break;
		case gyro:
			if (status == CalibrationStatus.Gyro_Check_Zero) {
				gyro.add(v);
				if (gyro.getMidSampleNumber() > 1000) {
					System.out.println("gyroMin " + gyro.getMin());
					System.out.println("gyroMax " + gyro.getMax());
					System.out.println("gyroMid " + gyro.getMid() + " values: " + gyro.getMidSampleNumber());
					Vector3d math = new Vector3d();
					math.add(gyro.getMin());
					math.add(gyro.getMax());
					math.mult(1.0 / 2);
					System.out.println("gyroMinMaxMid " + math);
					zero.setGyroZero( new Vector3s((short) gyro.getMid().getX(), (short) gyro.getMid().getY(), (short) gyro.getMid().getZ()) );

					int next = status.ordinal() + 1;
					if (next < CalibrationStatus.values().length) {
						status = CalibrationStatus.values()[next];
					}
				}
			}
			v.sub( zero.getGyroZero() );
			lastGyro = v;
			break;
		case magne:
			lastMagne = v;
			break;
		}
		
		super.event(t, v, packetNumber);
	}

	private void saveTail(TreeMap<Short, List<Vector3s>> map, int N) throws IOException {
		try (FileWriter f = new FileWriter("/tmp/minMaxSave.csv", true)) {
			// get N bigger
			int i = 0;
			for (Short key : map.keySet()) {
				for (Vector3s v : map.get(key)) {
					f.append(v + "\n");
					if (i++ >= N) {
						break;
					}
				}
				if (i >= N) {
					break;
				}
			}

			i = 0;
			// get N smaller
			for (Short key : map.descendingKeySet()) {
				for (Vector3s v : map.get(key)) {
					f.append(v + "\n");
					if (i++ >= N) {
						break;
					}
				}
				if (i >= N) {
					break;
				}
			}
		}
	}

	private void addTo(TreeMap<Short, List<Vector3s>> map, short s, Vector3s value) {
		List<Vector3s> list = map.get(s);
		if (list == null) {
			list = new ArrayList<>();
			map.put(s, list);
		}
		list.add(value);
	}

	private void sumLimit(Vector3d sum, TreeMap<Short, List<Vector3s>> map, int N) {
		// get N bigger
		int i = 0;
		for (Short key : map.keySet()) {
			for (Vector3s v : map.get(key)) {
				sum.add(v);
				if (i++ >= N) {
					break;
				}
			}
			if (i >= N) {
				break;
			}
		}

		i = 0;
		// get N smaller
		for (Short key : map.descendingKeySet()) {
			for (Vector3s v : map.get(key)) {
				sum.add(v);
				if (i++ >= N) {
					break;
				}
			}
			if (i >= N) {
				break;
			}
		}
	}

}
