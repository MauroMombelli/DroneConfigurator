package basic;
import fastchart.Serie;

public class SerieVector3 {
	public final Serie x = new Serie();
	public final Serie y = new Serie();
	public final Serie z = new Serie();
	public final Serie valid = new Serie();

	public SerieVector3() {
		this.x.setLineColor(1, 0, 0);
		this.y.setLineColor(0, 1, 0);
		this.z.setLineColor(0, 0, 1);

		this.valid.setLineColor(0.1, 0.1, 0.1);
	}
}