package filter;
/*
import java.awt.GridLayout;

import javax.swing.JFrame;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

import sensor.listener.AccelerometerListener;
import sensor.math.Vector3s;
import sensor.visualizer.FastPlotter;
import sensor.visualizer.TestLwjglGraph;

public class AccelerometerFilter implements AccelerometerListener {

	XYSeries xRaw = new XYSeries("x raw");
	XYSeries yRaw = new XYSeries("y raw");
	XYSeries zRaw = new XYSeries("z raw");
	
	XYSeries xFiltered = new XYSeries("x Filtered");
	XYSeries yFiltered = new XYSeries("y Filtered");
	XYSeries zFiltered = new XYSeries("z Filtered");

	TestLwjglGraph g = new TestLwjglGraph();
	
	int number = 10000;
	public AccelerometerFilter() {
		new Thread(g).start();
		
		xRaw.setMaximumItemCount(number);
		yRaw.setMaximumItemCount(number);
		zRaw.setMaximumItemCount(number);
		
		
		FastPlotter fpx = new FastPlotter("X Axis");
		fpx.addDataset(xRaw);
		//fpx.addDataset(xFiltered);
		
		FastPlotter fpy = new FastPlotter("Y Axis");
		fpy.addDataset(yRaw);
		//fpx.addDataset(yRaw);
		//fpy.addDataset(yFiltered);
		
		FastPlotter fpz = new FastPlotter("Z Axis");
		fpz.addDataset(zRaw);
		//fpx.addDataset(zRaw);
		//fpz.addDataset(zFiltered);
		
		
		JFrame chartsWindow = new JFrame("Accelerometer");
		chartsWindow.setSize(1024, 300);
		chartsWindow.setLayout(new GridLayout(3, 1));
		chartsWindow.add( fpx.getChart() );
		chartsWindow.add( fpy.getChart() );
		chartsWindow.add( fpz.getChart() );
		chartsWindow.setLocation(0, 300);
		chartsWindow.setState(JFrame.NORMAL);
		chartsWindow.setVisible(true);
	}
	
	@Override
	public void event(Vector3s a, long packetNumber) {
		xRaw.add(new XYDataItem(packetNumber, a.getX() ));
		yRaw.add(new XYDataItem(packetNumber, a.getY() ));
		zRaw.add(new XYDataItem(packetNumber, a.getZ() ));
		g.addPoint( (double)packetNumber, a.getX().doubleValue() );
	}

}
*/