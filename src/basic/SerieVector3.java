package basic;
import java.awt.Color;
import java.awt.geom.Point2D;

import fastchart.Serie;

public class SerieVector3 {
	public final Serie x;
	public final Serie y;
	public final Serie z;

	public SerieVector3(double scaleY) {
		this(scaleY, Color.red, Color.green, Color.blue);
	}
	public SerieVector3(double scaleY, Color xColor, Color yColor, Color zColor) {
		x = new Serie(scaleY);
		y = new Serie(scaleY);
		z = new Serie(scaleY);
		
		this.x.setLineColor(xColor);
		this.y.setLineColor(yColor);
		this.z.setLineColor(zColor);
	}
	double count = 0;
	public void addNextVector(double[] array) {
		x.addPoint(new Point2D.Double(count, array[0]));
		y.addPoint(new Point2D.Double(count, array[1]));
		z.addPoint(new Point2D.Double(count, array[2]));
		count++;
	}
}