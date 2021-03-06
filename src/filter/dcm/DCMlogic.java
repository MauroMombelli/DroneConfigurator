package filter.dcm;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import sensor.math.Quaternion4f;
import sensor.math.Vector3d;

public class DCMlogic {

	private final Object sincronizzaUpdate = new Object();

	float sampleFreq = 100;
	public float q0 = 1, q1 = 0, q2 = 0, q3 = 0;

	//float twoKp = 2.0f * 1f;
	//float twoKi = 2.0f * 0.0f;
	private float KP_M = 2.0f;
	private float KP_A = 2.0f;	
	private float KI_M = 0;
	
	Vector3d gyro=new Vector3d(), acc=new Vector3d(), magn=new Vector3d(), simpleGyro=new Vector3d(), lastGoodAcc=new Vector3d(), lastGoodMagne=new Vector3d();
	private Vector3d magneEstimate = new Vector3d(), acceEstimate = new Vector3d();

	//long lastUp = System.nanoTime();
	long lastFreqUp=System.currentTimeMillis(), count=-1, countG=0, countM=0,countA=0;
	private float sampleFreqG=1600;
	private float sampleFreqA=1600;
	private float sampleFreqM=70;

	private float	qPred1;
	private float	qPred2;
	private float	qPred3;
	private float	qPred4;

	/*
	private float integralFBxA;
	private float integralFByA;
	private float integralFBzA;
	*/
	
	private float integralFBxM;
	private float integralFByM;
	private float integralFBzM;

	private float[] stmQuat = new float[4];

	private float[] yprBypass = new float[3];

	
	
	
	public DCMlogic(){
		final JTextField kpMtxt = new JTextField(KP_M+"");
		final JTextField kpAtxt = new JTextField(KP_A+"");
		JButton apply = new JButton("apply");
		apply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String textA = kpAtxt.getText();
				String textM = kpMtxt.getText();
				try{
					KP_A = Float.parseFloat(textA);
					kpAtxt.setBackground(Color.white);
				}catch(Exception e1){
					e1.printStackTrace();
					kpAtxt.setBackground(Color.red);
				}
				
				try{
					KP_M = Float.parseFloat(textM);
					kpMtxt.setBackground(Color.white);
				}catch(Exception e1){
					e1.printStackTrace();
					kpMtxt.setBackground(Color.red);
				}
			}
		});
		JFrame windows = new JFrame();
		windows.setLayout( new GridLayout(3, 1) );
		windows.add(kpAtxt);
		windows.add(kpMtxt);
		windows.add(apply);
		windows.setSize(100, 200);
		windows.setVisible(true);
	}
	
	public void FreeIMUUpdate(float gx, float gy, float gz, float ax, float ay, float az, float mx, float my, float mz){
		
		/* DINAMIC FREQUENCY! */
		if (count == -1){ //just the first time!
			lastFreqUp = System.currentTimeMillis();
		}
		count ++;
		if (System.currentTimeMillis()-lastFreqUp>=1000){
			System.out.println("Frequenza: "+count+" G: "+countG+" A: "+countA+" M: "+countM );
			sampleFreq = count;
			sampleFreqA = countA;
			sampleFreqM = countM;
			sampleFreqG = countG;
			count=countG=countA=countM=0;
			lastFreqUp = System.currentTimeMillis();
		}
		/* END DINAMIC FREQUENCY! */
		
		Vector3d simpleIntegration = new Vector3d(gx, gy, gz);
		simpleIntegration.mult(1.0f/sampleFreqG);
		gyro.add( simpleIntegration );//lol, simple integration?
		
		simpleGyro = new Vector3d(gx, gy, gz);
		simpleGyro.mult(1.0f/sampleFreqG);

		acc = new Vector3d(ax, ay, az);

		magn = new Vector3d(mx, my, mz);

		
		float recipNorm;
		float q0q0, q0q1, q0q2, q0q3, q1q1, q1q2, q1q3, q2q2, q2q3, q3q3;
		float halfex = 0.0f, halfey = 0.0f, halfez = 0.0f;
		float qa, qb, qc;

		// Auxiliary variables to avoid repeated arithmetic
		q0q0 = q0 * q0;
		q0q1 = q0 * q1;
		q0q2 = q0 * q2;
		q0q3 = q0 * q3;
		q1q1 = q1 * q1;
		q1q2 = q1 * q2;
		q1q3 = q1 * q3;
		q2q2 = q2 * q2;
		q2q3 = q2 * q3;
		q3q3 = q3 * q3;

		// Use magnetometer measurement only when valid (avoids NaN in magnetometer normalisation)
		float halfwx=0, halfwy=0, halfwz=0;
		
		if(mx != 0.0f || my != 0.0f || mz != 0.0f) {
			float hx, hy, bx, bz;
			
			countM++;

			lastGoodMagne = new Vector3d(magn);
			
			// Normalise magnetometer measurement
			recipNorm = invSqrt(mx * mx + my * my + mz * mz);
			mx *= recipNorm;
			my *= recipNorm;
			mz *= recipNorm;

			// Reference direction of Earth's magnetic field
			hx = 2.0f * (mx * (0.5f - q2q2 - q3q3) + my * (q1q2 - q0q3) + mz * (q1q3 + q0q2));
			hy = 2.0f * (mx * (q1q2 + q0q3) + my * (0.5f - q1q1 - q3q3) + mz * (q2q3 - q0q1));
			bx = (float) Math.sqrt(hx * hx + hy * hy);
			bz = 2.0f * (mx * (q1q3 - q0q2) + my * (q2q3 + q0q1) + mz * (0.5f - q1q1 - q2q2));

			// Estimated direction of magnetic field
			halfwx = bx * (0.5f - q2q2 - q3q3) + bz * (q1q3 - q0q2);
			halfwy = bx * (q1q2 - q0q3) + bz * (q0q1 + q2q3);
			halfwz = bx * (q0q2 + q1q3) + bz * (0.5f - q1q1 - q2q2);
			
			magneEstimate = new Vector3d(halfwx,halfwy,halfwz);
			
			float norm = invSqrt(halfwx*halfwx+halfwy*halfwy+halfwz*halfwz);
			halfwx*=norm;
			halfwy*=norm;
			halfwz*=norm;
			
			
			float freqMagne = (1.0f / sampleFreqM);
			integralFBxM += halfex * freqMagne * KI_M;
			integralFByM += halfey * freqMagne * KI_M;
			integralFBzM += halfez * freqMagne * KI_M;
			
			
			halfex += KP_M * (my * halfwz - mz * halfwy) * freqMagne + integralFBxM;
			halfey += KP_M * (mz * halfwx - mx * halfwz) * freqMagne + integralFByM;
			halfez += KP_M * (mx * halfwy - my * halfwx) * freqMagne + integralFBzM;
			
		}

		// Compute feedback only if accelerometer measurement valid (avoids NaN in accelerometer normalisation)
		float halfvx=0, halfvy=0, halfvz=0;
		if(ax != 0.0f || ay != 0.0f || az != 0.0f) {
			countA++;
			
			
			lastGoodAcc = new Vector3d(acc);
			
			// Normalise accelerometer measurement
			recipNorm = invSqrt(ax * ax + ay * ay + az * az);
			ax *= recipNorm;
			ay *= recipNorm;
			az *= recipNorm;

			// Estimated direction of gravity
			halfvx = q1q3 - q0q2;
			halfvy = q0q1 + q2q3;
			halfvz = q0q0 - 0.5f + q3q3;
			
			acceEstimate = new Vector3d(halfvx, halfvy, halfvz);
			
			halfex += KP_A * (ay * halfvz - az * halfvy) * (1.0f / sampleFreqA);
			halfey += KP_A * (az * halfvx - ax * halfvz) * (1.0f / sampleFreqA);
			halfez += KP_A * (ax * halfvy - ay * halfvx) * (1.0f / sampleFreqA);
		}
		
		countG++;
		// Integrate rate of change of quaternion
		gx *= (1.0f / sampleFreqG);   // pre-multiply common factors
		gy *= (1.0f / sampleFreqG);
		gz *= (1.0f / sampleFreqG);
		
		
		/*
		// Compute and apply integral feedback if enabled	
		if(twoKi > 0.0f) {
			integralFBx += twoKi * halfex;  // integral error scaled by Ki
			integralFBy += twoKi * halfey;
			integralFBz += twoKi * halfez;
			gx += integralFBx;  // apply integral feedback
			gy += integralFBy;
			gz += integralFBz;
		}
		*/
		
		// Apply error feedback
		gx += halfex;
		gy += halfey;
		gz += halfez;

		qa = q0;
		qb = q1;
		qc = q2;
		q0 += (-qb * gx - qc * gy - q3 * gz);
		q1 += (qa * gx + qc * gz - q3 * gy);
		q2 += (qa * gy - qb * gz + q3 * gx);
		q3 += (qa * gz + qb * gy - qc * gx);

		// Normalise quaternion
		recipNorm = invSqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
		q0 *= recipNorm;
		q1 *= recipNorm;
		q2 *= recipNorm;
		q3 *= recipNorm;

	}
	
	// ---------------------------------------------------------------------------------------------------
	// Fast inverse square-root
	// See: http://en.wikipedia.org/wiki/Fast_inverse_square_root
	// also this is needed because otherwise we could get a NAN (it's also a bit
	// faster i guess)
	strictfp static float invSqrt(float x) {
		/*
		float xhalf = 0.5f * x;
		int i = Float.floatToRawIntBits(x); // convert integer to keep the
		// representation IEEE 754

		i = 0x5f3759df - (i >> 1);
		x = Float.intBitsToFloat(i);
		x = x * (1.5f - xhalf * x * x);

		return x;*/
		return (float) (1/Math.sqrt(x));
	}

	public Quaternion4f getQuaternion() {
		//System.out.println("STM "+stmQuat[0]+" "+stmQuat[1]+" "+stmQuat[2]+" "+stmQuat[3]);
		//System.out.println("JAVA "+q0+" "+q1+" "+q2+" "+q3);
		synchronized (sincronizzaUpdate) {
			return new Quaternion4f(q0, q1, q2, q3);
		}
	}

	public Quaternion4f getPredictedQuaternion() {
		synchronized (sincronizzaUpdate) {
			return new Quaternion4f(qPred1, qPred2, qPred3, qPred4);
		}
	}

	public Vector3d getGyro() {
		return gyro;
	}

	public Vector3d getSimpleGyro() {
		synchronized (sincronizzaUpdate) {
			return new Vector3d(simpleGyro); //copy!
		}
	}

	public Vector3d getAcc() {
		return lastGoodAcc;
	}

	public Vector3d getMagn() {
		return lastGoodMagne; //copy!
	}

	public void setStmBypass(float[] q) {
		synchronized (stmQuat) {
			stmQuat = q;
		}
	}

	public Quaternion4f getQuaternionStm() {
		synchronized (stmQuat) {
			return new Quaternion4f(stmQuat[0],stmQuat[1], stmQuat[2], stmQuat[3]);
		}
	}

	public void setYprStm(float[] ypr) {
		yprBypass = ypr;
	}

	public float[] getYprStm() {
		return yprBypass;
	}
	/*
	public void update(float x, float y, float z, float x2, float y2, float z2, int i, int j, int k) {
		FreeIMUUpdate(x, y, z, x2, y2, z2, i, j, k);
	}*/

	public void update(float x, float y, float z, float x2, float y2, float z2, float x3, float y3, float z3) {
		FreeIMUUpdate(x, y, z, x2, y2, z2, x3, y3, z3);
	}

	public Vector3d getMagnEstimate() {
		return magneEstimate;
	}
	
	public Vector3d getAcceEstimate() {
		return acceEstimate;
	}
}