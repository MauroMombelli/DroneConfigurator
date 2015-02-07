package filter.dcm;

import sensor.listener.VectorListener;
import sensor.math.Vector3s;
import fastchart.PlainObject;
import fastchart.utils.GraphicMagic;
import filter.Vector3d;

public class MyDcm implements VectorListener{
	GraphicMagic graph = new GraphicMagic("basic filter");
	
	DCMlogic dcm = new DCMlogic();
	
	PlainObject p = new PlainObject("plane.obj"); 
	
	public MyDcm() throws Exception{
		graph.addPanel(p);
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
			lastGyro.mult(mdpsOverDigitAt500);
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
				lastAcce = new Vector3d();
			
			if (lastMagne == null)
				lastMagne = new Vector3d();
			
			dcm.update( 
					(float)lastGyro.getX(), (float)lastGyro.getY(), (float)lastGyro.getZ(),
					(float)lastAcce.getX(), (float)lastAcce.getY(), (float)lastAcce.getZ(),
					(float)lastMagne.getX(), (float)lastMagne.getY(), (float)lastMagne.getZ()
				);
			Vector3d eulerAngles = dcm.getQuaternion().eulerAngles(); 
			p.setRotation( eulerAngles.getArray() );
			lastGyro = lastAcce = lastMagne = null;
		}
	}
}
