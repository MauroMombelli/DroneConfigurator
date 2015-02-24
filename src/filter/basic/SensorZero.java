package filter.basic;

import sensor.math.Vector3d;
import sensor.math.Vector3s;

public class SensorZero {
	Vector3s gyroZero = new Vector3s();
	Vector3s acceZero = new Vector3s();
	
	private BasicFilterGUI listener;
	
	public Vector3s getAcceZero() {
		synchronized (acceZero) {
			return acceZero;
		}
	}
	public void setAcceZero(Vector3d mid) {
		synchronized (acceZero) {
			acceZero = new Vector3s((short)mid.getX(), (short)mid.getY(), (short)mid.getZ());
			if (listener != null){
				listener.setAcceZero(mid);
			}
		}
	}
	public Vector3s getGyroZero() {
		synchronized (gyroZero) {
			return gyroZero;
		}
	}
	public void setGyroZero(Vector3s mid) {
		synchronized (gyroZero) {
			gyroZero = mid;
			if (listener != null){
				listener.setGyroZero(mid);
			}
		}
	}
	public void addListener(BasicFilterGUI basicFilterGUI) {
		this.listener = basicFilterGUI;
	}
}
