package basic.visualizer;

import basic.SerieVector3;
import sensor.listener.QuaternionListener;
import sensor.math.Quaternion4f;
import sensor.math.Vector3d;
import fastchart.PlainObject;
import fastchart.utils.GraphicMagic;

public class DroneDcm implements QuaternionListener{
	GraphicMagic graph = new GraphicMagic("DRONE DCM", 200, 200);
	
	PlainObject p = new PlainObject("plane.obj");

	private SerieVector3 vDroneDcm; 
	
	public DroneDcm(SerieVector3 vDroneDcm) throws Exception{
		graph.addPanel(p);
		
		this.vDroneDcm = vDroneDcm;
		
		new Thread(graph).start(); //fire and forget!
	}

	@Override
	public void event(Quaternion4f dcm) {
		/*
		double[] matrix = new double[4*4];
		dcm.createMatrix(matrix);
		*/
		
		Vector3d eulerAngles = dcm.eulerAngles();
		double[] array = eulerAngles.getArray();
		/*
		double d = array[1];
		array[1] = array[2];
		array[2] = d;
		
		d = array[0];
		array[0] = array[1];
		array[1] = d;
		*/
		
		p.setRotationMatrix( array );
		vDroneDcm.addNextVector(array);
		
	}
}
