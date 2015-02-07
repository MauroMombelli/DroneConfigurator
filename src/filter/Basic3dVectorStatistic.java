package filter;

import sensor.math.Vector3s;

public class Basic3dVectorStatistic {
	Vector3s min = new Vector3s();
	Vector3s max = new Vector3s();
	
	long midValue = 0;
	Vector3d sumFormMid = new Vector3d();
	
	public void add(Vector3s d){
		if (d.getX() < min.getX()){
			min.setX(d.getX());
		}
		if (d.getY() < min.getY()){
			min.setY(d.getY());
		}
		if (d.getZ() < min.getZ()){
			min.setZ(d.getZ());
		}
		
		if (d.getX() > max.getX()){
			max.setX(d.getX());
		}
		if (d.getY() > max.getY()){
			max.setY(d.getY());
		}
		if (d.getZ() > max.getZ()){
			max.setZ(d.getZ());
		}
		
		sumFormMid.add(d);
		midValue++;
	}

	public Vector3s getMin() {
		return min;
	}

	public Vector3s getMax() {
		return max;
	}

	public Vector3d getMid() {
		Vector3d ris = new Vector3d(sumFormMid);
		ris.mult(1.0/midValue);
		return ris;
	}

	public long getMidSampleNumber() {
		return midValue;
	}
}
