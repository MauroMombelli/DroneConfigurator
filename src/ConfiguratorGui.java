import java.awt.geom.Point2D;

import sensor.SensorStreamReader;
import sensor.implementation.BasicReader;
import sensor.listener.VectorListener;
import sensor.math.Vector3s;
import fastchart.Chart;
import fastchart.Serie;
import fastchart.utils.GraphicMagic;


public class ConfiguratorGui {

	public static void main(String args[]){
		new ConfiguratorGui();		
	}
	
	public ConfiguratorGui() {
		SensorStreamReader listener = new BasicReader();
		
		GraphicMagic graph = new GraphicMagic();
		Chart c = new Chart();
		graph.addPanel(c);
		
		new Thread(graph).start(); //fire and forget!
		
		
		SerieWrapper sw = new SerieWrapper();
		c.addSerie(sw.x);
		c.addSerie(sw.y);
		c.addSerie(sw.z);
		
		
		Serie s = new Serie();
		c.addSerie(s);

		Chart c2 = new Chart();
		graph.addPanel(c2);
		
		try {
			listener.start();
			
			listener.addListener(sw);
			/*
			int count = 0;
			while(true){
				s.addPoint(new Point2D.Double(count++, 1.8));
				Thread.sleep(1000);
				s.addPoint(new Point2D.Double(count++, -1.8));
				Thread.sleep(1000);
			}*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class SerieWrapper implements VectorListener{
		Serie x = new Serie();
		Serie y = new Serie();
		Serie z = new Serie();
		
		public SerieWrapper(){
			x.setLineColor(1, 0, 0);
			y.setLineColor(0, 1, 0);
			z.setLineColor(0, 0, 1);
		}
		
		@Override
		public void event(EventType t, Vector3s a, long packetNumber) {
			System.out.println("readed "+t.toString());
			if (t.equals(EventType.acce)){
				System.out.println("acce readed "+a);
				x.addPoint( new Point2D.Double(packetNumber, a.getX().doubleValue() ) );
				y.addPoint( new Point2D.Double(packetNumber, a.getY().doubleValue() ) );
				z.addPoint( new Point2D.Double(packetNumber, a.getZ().doubleValue() ) );
			}
		}
		
	}
}
