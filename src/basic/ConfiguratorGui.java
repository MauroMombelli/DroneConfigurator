package basic;
import java.io.IOException;

import sensor.SensorStreamReader;
import sensor.implementation.BasicReader;
import fastchart.Chart;
import fastchart.utils.GraphicMagic;
import filter.basic.BasicFilter;
import filter.dcm.MyDcm;


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
		SensorStreamReader listener = new BasicReader();
		
		GraphicMagic graph = new GraphicMagic("basic filter");
		
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
		
		MyDcm myDcm = new MyDcm();
		
		try {
			listener.start();
			
			listener.addListener(sw);
			
			listener.addListener(myDcm);
			
			//listener.addListener(new BasicFilter());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
