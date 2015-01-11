package filter;

/*
public class MagnetometerFilter implements MagnetometerListener {

	XYSeries xRaw = new XYSeries("x raw");
	XYSeries yRaw = new XYSeries("y raw");
	XYSeries zRaw = new XYSeries("z raw");
	
	XYSeries xFiltered = new XYSeries("x Filtered");
	XYSeries yFiltered = new XYSeries("y Filtered");
	XYSeries zFiltered = new XYSeries("z Filtered");
	
	int number = 10000;
	public MagnetometerFilter() {
		xRaw.setMaximumItemCount(number);
		yRaw.setMaximumItemCount(number);
		zRaw.setMaximumItemCount(number);
		
		FastPlotter fpx = new FastPlotter("X Axis");
		fpx.addDataset(xRaw);
		fpx.addDataset(xFiltered);
		
		FastPlotter fpy = new FastPlotter("Y Axis");
		fpy.addDataset(yRaw);
		fpy.addDataset(yFiltered);
		
		FastPlotter fpz = new FastPlotter("Z Axis");
		fpz.addDataset(zRaw);
		fpz.addDataset(zFiltered);
		
		
		JFrame chartsWindow = new JFrame("Magnetometer");
		chartsWindow.setSize(1024, 300);
		chartsWindow.setLayout(new GridLayout(3, 1));
		chartsWindow.add( fpx.getChart() );
		chartsWindow.add( fpy.getChart() );
		chartsWindow.add( fpz.getChart() );
		chartsWindow.setLocation(0, 600);
		chartsWindow.setState(JFrame.NORMAL);
		chartsWindow.setVisible(true);
	}
	
	@Override
	public void event(Vector3s a, long packetNumber) {
		xRaw.add(new XYDataItem(packetNumber, a.getX() ));
		yRaw.add(new XYDataItem(packetNumber, a.getY() ));
		zRaw.add(new XYDataItem(packetNumber, a.getZ() ));
	}

}
*/

