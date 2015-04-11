package filter.dcm;

import sensor.listener.VectorListener;
import sensor.math.Quaternion4f;
import sensor.math.Vector3d;
import sensor.math.Vector3s;
import basic.SerieVector3;
import fastchart.PlainObject;
import fastchart.utils.GraphicMagic;

public class MyDcm implements VectorListener{
	GraphicMagic graph = new GraphicMagic("PC DCM", 200, 200);
	
	DCMlogic dcm = new DCMlogic();
	
	PlainObject p = new PlainObject("plane.obj");

	private SerieVector3 vMyDcm;
	
	public MyDcm(SerieVector3 vMyDcm) throws Exception{
		graph.addPanel(p);
		this.vMyDcm = vMyDcm;
		new Thread(graph).start(); //fire and forget!
	}

	Vector3d lastGyro;
	Vector3d lastAcce;
	Vector3d lastMagne;
	
	final float mdpsOverDigitAt250 = 8.75f;
	final float mdpsOverDigitAt500 = 17.5f;
	final float mdpsOverDigitAt2000 = 70;
	
	@Override
	public void event(EventType t, Vector3s a, long packetNumber) {
		switch (t) {
		case acce:
			lastAcce = new Vector3d();
			lastAcce.add(a);
			lastAcce.mult( 1/lastAcce.lenght() );
			break;
		case gyro:
			lastGyro = new Vector3d();
			lastGyro.add(a);
			//((int16 * mdpsOverDigitAt500) / 1000) * 0.0174532925f
			lastGyro.mult(mdpsOverDigitAt250);
			lastGyro.mult(1/1000.0);
			lastGyro.mult(0.0174532925);
			break;
		case magne:
			lastMagne = new Vector3d();
			lastMagne.add(a);
			lastMagne.mult( 1/lastMagne.lenght() );
			break;
		}
		if (lastGyro != null && (lastAcce!=null || lastMagne != null)){
			if (lastAcce == null)
				lastAcce = new Vector3d(0,0,1);
			
			//if (lastMagne == null)
				lastMagne = new Vector3d();
			
			dcm.update( 
					(float)lastGyro.getX(), (float)lastGyro.getY(), (float)lastGyro.getZ(),
					(float)lastAcce.getX(), (float)lastAcce.getY(), (float)lastAcce.getZ(),
					(float)lastMagne.getX(), (float)lastMagne.getY(), (float)lastMagne.getZ()
				);
			
			Quaternion4f dcmQuat = dcm.getQuaternion();
			Vector3d eulerAngles = dcmQuat.eulerAngles();
			
			Vector3d fromQuad = new Vector3d(
					getFromQuad(new Vector3d(1,0,0), new Vector3d(0,1,0), dcmQuat),
					getFromQuad(new Vector3d(0,1,0), new Vector3d(0,0,1), dcmQuat),
					getFromQuad(new Vector3d(0,0,1), new Vector3d(0,1,0), dcmQuat)
					);
			//Vector3d eulerAngles = dcm.getQuaternion();
			
			
			System.out.println("from euler: "+eulerAngles+" from quath math: "+fromQuad);
			
			
			//double[] array = eulerAngles.getArray();
			//double[] array = fromQuad.getArray();
			/*
			double d = array[1];
			array[1] = array[2];
			array[2] = d;
			
			d = array[0];
			array[0] = array[1];
			array[1] = d;
			/*THIS USE MATRIX
			double matrix[] = new double[16];
			
			dcm.getQuaternion().createMatrix(matrix);
			*/
			p.setRotation( fromQuad.getArray() );
			
			vMyDcm.addNextVector( fromQuad.getArray() );
			
			lastGyro = lastAcce = lastMagne = null;
		}
	}

	private double getFromQuad(Vector3d axis, Vector3d orto1, Quaternion4f dcm) {
		//from http://stackoverflow.com/questions/3684269/component-of-a-quaternion-rotation-around-an-axis
		
		Vector3d tmp = new Vector3d(orto1);
		
		tmp.transform( dcm );
		
		axis.mult(-tmp.dot(axis)); //negative as we will have to subtract it
		
		tmp.add( axis );
		
		boolean positive = true;
		/*
		if (orto1.getX() == 1 && tmp.getX()<0){
			positive = false;
		}
		if (orto1.getY() == 1 && tmp.getY()<0){
			positive = false;
		}
		if (orto1.getZ() == 1 && tmp.getZ()<0){
			positive = false;
		}
		*/
		tmp.normalize();
		
		return Math.acos(orto1.dot(tmp))*(positive?1:-1);
	}
}
