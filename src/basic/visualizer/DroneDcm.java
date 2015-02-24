package basic.visualizer;

import sensor.listener.QuaternionListener;
import sensor.math.Quaternion4f;
import sensor.math.Vector3d;
import fastchart.PlainObject;
import fastchart.utils.GraphicMagic;

public class DroneDcm implements QuaternionListener{
	GraphicMagic graph = new GraphicMagic("DRONE DCM", 200, 200);
	
	PlainObject p = new PlainObject("plane.obj"); 
	
	public DroneDcm() throws Exception{
		graph.addPanel(p);
		new Thread(graph).start(); //fire and forget!
	}

	@Override
	public void event(Quaternion4f dcm) {
		Vector3d eulerAngles = dcm.eulerAngles();
		double[] array = eulerAngles.getArray();
		
		double d = array[1];
		array[1] = array[2];
		array[2] = d;
		
		d = array[0];
		array[0] = array[1];
		array[1] = d;
		
		//System.out.println("drone dcm "+dcm);
		
		p.setRotation( array );
	}
}
