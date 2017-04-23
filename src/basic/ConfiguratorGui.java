package basic;
import java.awt.Color;
import java.io.IOException;

import basic.visualizer.DroneDcm;
import fastchart.Chart;
import fastchart.utils.GraphicMagic;
import filter.basic.BasicFilter;
import filter.dcm.MyDcm;
import sensor.SensorStreamReader;
import sensor.implementation.FileRader;
import sensor.listener.FileRecoder;

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
		//SensorStreamReader listener = new BasicReader2();
		SensorStreamReader listener = new FileRader();
		
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
		
		SerieWrapper realtimeChart = new BasicFilter();//new SerieWrapper();
		accelerometer.addSerie(realtimeChart.acce.x);
		accelerometer.addSerie(realtimeChart.acce.y);
		accelerometer.addSerie(realtimeChart.acce.z);
		//accelerometer.addSerie(sw.acce.valid);
		
		magnetometer.addSerie(realtimeChart.magne.x);
		magnetometer.addSerie(realtimeChart.magne.y);
		magnetometer.addSerie(realtimeChart.magne.z);
		
		gyroscope.addSerie(realtimeChart.gyro.x);
		gyroscope.addSerie(realtimeChart.gyro.y);
		gyroscope.addSerie(realtimeChart.gyro.z);
		
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
			
			listener.addListener(realtimeChart);
			
			listener.addListener(myDcm);
			
			listener.addListener(droneDcm);
			
			listener.addListener(new FileRecoder());
			
			//listener.addListener(new BasicFilter());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
