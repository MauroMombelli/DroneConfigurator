package filter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import sensor.math.Vector3s;

public class Vector3d {
	private double arr[] = new double[3];
	
	public Vector3d() {
	}
	
	public Vector3d(double x, double y, double z) {
		arr[0] = x;
		arr[1] = y;
		arr[2] = z;
	}

	public Vector3d(Vector3d sumFormMid) {
		arr[0] = sumFormMid.arr[0];
		arr[1] = sumFormMid.arr[1];
		arr[2] = sumFormMid.arr[2];
	}

	public void add(Vector3s d) {
		arr[0] += d.getX();
		arr[1] += d.getY();
		arr[2] += d.getZ();
	}

	public void mult(double d) {
		arr[0] *= d;
		arr[1] *= d;
		arr[2] *= d;
	}

	public static Vector3d fromString(String s) throws NumberFormatException, IOException{
		s = s.trim();
		if ( s.charAt(0) != '{' || s.charAt(s.length()-1) != '}' ){
			throw new IOException("{} not found at beginning and end");
		}
		
		s = s.substring(1, s.length()-1);
		String[] split = s.split(",");
		if (split.length != 3){
			throw new IOException("not found three value divided by ,");
		}
		
		return new Vector3d(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
	}
	
	@Override
	public String toString(){
		return "{"+arr[0]+","+arr[1]+","+arr[2]+"}";
	}

	public void add(Vector3d d) {
		arr[0] += d.arr[0];
		arr[1] += d.arr[1];
		arr[2] += d.arr[2];
	}

	public double getX() {
		return arr[0];
	}
	
	public double getY() {
		return arr[1];
	}
	
	public double getZ() {
		return arr[2];
	}

	public double lenght() {
		return Math.sqrt(arr[0]*arr[0]+arr[1]*arr[1]+arr[2]*arr[2]);
	}

	public double[] getArray() {
		return Arrays.copyOf(arr, 3);
	}
}
