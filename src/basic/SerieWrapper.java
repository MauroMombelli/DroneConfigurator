package basic;

import java.awt.geom.Point2D;

import sensor.listener.VectorListener;
import sensor.math.Vector3s;

public class SerieWrapper implements VectorListener {

	SerieVector3 acce = new SerieVector3();
	SerieVector3 gyro = new SerieVector3();
	SerieVector3 magne = new SerieVector3();

	@Override
	public void event(EventType t, Vector3s v, long packetNumber) {
		switch (t) {
		case acce:
			acce.x.addPoint( new Point2D.Double(packetNumber, v.getX()) );
			acce.y.addPoint( new Point2D.Double(packetNumber, v.getY()) );
			acce.z.addPoint( new Point2D.Double(packetNumber, v.getZ()) );
			break;
		case gyro:
			gyro.x.addPoint( new Point2D.Double(packetNumber, v.getX()) );
			gyro.y.addPoint( new Point2D.Double(packetNumber, v.getY()) );
			gyro.z.addPoint( new Point2D.Double(packetNumber, v.getZ()) );
			break;
		case magne:
			magne.x.addPoint( new Point2D.Double(packetNumber, v.getX()) );
			magne.y.addPoint( new Point2D.Double(packetNumber, v.getY()) );
			magne.z.addPoint( new Point2D.Double(packetNumber, v.getZ()) );
			break;

		}
	}
}