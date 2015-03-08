package basic;
import java.awt.geom.Point2D;

import fastchart.Serie;

public class SerieVector3 {
	public final Serie x;
	public final Serie y;
	public final Serie z;

	public SerieVector3(double scaleY) {
		x = new Serie(scaleY);
		y = new Serie(scaleY);
		z = new Serie(scaleY);
		
		this.x.setLineColor(1, 0, 0);
		this.y.setLineColor(0, 1, 0);
		this.z.setLineColor(0, 0, 1);
	}
	double count = 0;
	public void addNextVector(double[] array) {
		x.addPoint(new Point2D.Double(count, array[0]));
		y.addPoint(new Point2D.Double(count, array[1]));
		z.addPoint(new Point2D.Double(count, array[2]));
		count++;
	}
}