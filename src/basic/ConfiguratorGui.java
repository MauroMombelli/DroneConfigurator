package basic;
import java.awt.Color;
import java.io.IOException;

import basic.visualizer.DroneDcm;
import fastchart.Chart;
import fastchart.utils.GraphicMagic;
import filter.basic.BasicFilter;
import filter.dcm.MyDcm;
import sensor.SensorStreamReader;
import sensor.implementation.BasicReader2;


public class ConfiguratorGui {

	public static void main(String args[]){
		try {
			new ConfiguratorGui();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public ConfiguratorGui() throws Exception {
		SensorStreamReader listener = new BasicReader2();
		
		GraphicMagic graph = new GraphicMagic("basic filter", 1200, 800);
		
		Chart accelerometer = new Chart();
		Chart magnetometer = new Chart();
		Chart gyroscope = new Chart();

		//Chart filter = new Chart();
		
		//graph.addPanel(magnetometer);
		graph.addPanel(accelerometer);
		graph.addPanel(gyroscope);
		
		//graph.addPanel(new Chart() );
		
		new Thread(graph).start(); //fire and forget!
		
		SerieWrapper sw = new BasicFilter();//new SerieWrapper();
		accelerometer.addSerie(sw.acce.x);
		accelerometer.addSerie(sw.acce.y);
		accelerometer.addSerie(sw.acce.z);
		//accelerometer.addSerie(sw.acce.valid);
		
		magnetometer.addSerie(sw.magne.x);
		magnetometer.addSerie(sw.magne.y);
		magnetometer.addSerie(sw.magne.z);
		
		gyroscope.addSerie(sw.gyro.x);
		gyroscope.addSerie(sw.gyro.y);
		gyroscope.addSerie(sw.gyro.z);
		
		/*
		try {
			graph.addPanel( new PlainObject("bunny.obj") );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		
		//Serie s = new Serie();
		//accelerometer.addSerie(s);

		//Chart c2 = new Chart();
		//graph.addPanel(c2);
		
		GraphicMagic graphDCM = new GraphicMagic("dcm output", 1200, 800);
		Chart dcmX = new Chart();
		Chart dcmY = new Chart();
		Chart dcmZ = new Chart();
		graphDCM.addPanel(dcmX);
		graphDCM.addPanel(dcmZ);
		graphDCM.addPanel(dcmY);
		
		new Thread(graphDCM).start(); //fire and forget!
		
		
		SerieVector3 vMyDcm = new SerieVector3(1/8.0, Color.RED, Color.RED, Color.RED);
		MyDcm myDcm = new MyDcm(vMyDcm);
		
		
		SerieVector3 vDroneDcm = new SerieVector3(1/8.0, Color.BLUE, Color.BLUE, Color.BLUE);
		DroneDcm droneDcm = new DroneDcm(vDroneDcm);
		
		dcmX.addSerie(vMyDcm.x);
		dcmX.addSerie(vDroneDcm.x);
		
		dcmY.addSerie(vMyDcm.y);
		dcmY.addSerie(vDroneDcm.y);
		
		dcmZ.addSerie(vMyDcm.z);
		dcmZ.addSerie(vDroneDcm.z);
		
		try {
			listener.start();
			
			listener.addListener(sw);
			
			listener.addListener(myDcm);
			
			listener.addListener(droneDcm);
			
			//listener.addListener(new BasicFilter());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
