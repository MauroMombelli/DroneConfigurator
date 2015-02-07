/**
 *                     ProScene (version 1.1.1)      
 *    Copyright (c) 2010-2012 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *           http://www.disi.unal.edu.co/grupos/remixlab/
 *                           
 * This java package provides classes to ease the creation of interactive 3D
 * scenes in Processing.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package filter;

/**
 * A 4 element unit quaternion represented by single precision floating point x,y,z,w coordinates.
 * 
 */

public class Quaternion {
	/**
	 * The x coordinate, i.e., the x coordinate of the vector part of the Quaternion.
	 */
	public float x;

	/**
	 * The y coordinate, i.e., the y coordinate of the vector part of the Quaternion.
	 */
	public float y;

	/**
	 * The z coordinate, i.e., the z coordinate of the vector part of the Quaternion.
	 */
	public float z;

	/**
	 * The w coordinate which corresponds to the scalar part of the Quaternion.
	 */
	public float w;

	/**
	 * Constructs and initializes a Quaternion to (0.0,0.0,0.0,1.0), i.e., an identity rotation.
	 */
	public Quaternion() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 1;
	}

	/**
	 * Default constructor for Quaternion(float x, float y, float z, float w, boolean normalize), with {@code normalize=true}.
	 * 
	 */
	public Quaternion(float x, float y, float z, float w) {
		this(x, y, z, w, true);
	}

	/**
	 * Constructs and initializes a Quaternion from the specified xyzw coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 * @param w
	 *            the w scalar component
	 * @param normalize
	 *            tells whether or not the constructed Quaternion should be normalized.
	 */
	public Quaternion(float x, float y, float z, float w, boolean normalize) {
		if (normalize) {
			float mag = (float) Math.sqrt(x * x + y * y + z * z + w * w);
			if (mag > 0.0f) {
				this.x = x / mag;
				this.y = y / mag;
				this.z = z / mag;
				this.w = w / mag;
			} else {
				this.x = 0;
				this.y = 0;
				this.z = 0;
				this.w = 1;
			}
		} else {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
	}

	/**
	 * Default constructor for Quaternion(float[] q, boolean normalize) with {@code normalize=true}.
	 * 
	 */
	public Quaternion(float[] q) {
		this(q, true);
	}

	/**
	 * Constructs and initializes a Quaternion from the array of length 4.
	 * 
	 * @param q
	 *            the array of length 4 containing xyzw in order
	 */
	public Quaternion(float[] q, boolean normalize) {
		if (normalize) {
			float mag = (float) Math.sqrt(q[0] * q[0] + q[1] * q[1] + q[2] * q[2] + q[3] * q[3]);
			if (mag > 0.0f) {
				this.x = q[0] / mag;
				this.y = q[1] / mag;
				this.z = q[2] / mag;
				this.w = q[3] / mag;
			} else {
				this.x = 0;
				this.y = 0;
				this.z = 0;
				this.w = 1;
			}
		} else {
			this.x = q[0];
			this.y = q[1];
			this.z = q[2];
			this.w = q[3];
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param q1
	 *            the Quaternion containing the initialization x y z w data
	 */
	public Quaternion(Quaternion q1) {
		set(q1);
	}

	/**
	 * Copy constructor. If {@code normalize} is {@code true} this Quaternion is {@link #normalize()}.
	 * 
	 * @param q1
	 *            the Quaternion containing the initialization x y z w data
	 */
	public Quaternion(Quaternion q1, boolean normalize) {
		set(q1, normalize);
	}

	/**
	 * Convenience function that simply calls {@code set(q1, true);}
	 * 
	 * @see #set(Quaternion, boolean)
	 */
	public void set(Quaternion q1) {
		set(q1, true);
	}

	/**
	 * Set this Quaternion from quaternion {@code q1}. If {@code normalize} is {@code true} this Quaternion is {@link #normalize()}.
	 */
	public void set(Quaternion q1, boolean normalize) {
		this.x = q1.x;
		this.y = q1.y;
		this.z = q1.z;
		this.w = q1.w;
		if (normalize)
			this.normalize();
	}

	/**
	 * Sets the value of this Quaternion to the conjugate of itself.
	 */
	public final void conjugate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	/**
	 * Sets the value of this Quaternion to the conjugate of Quaternion q1.
	 * 
	 * @param q1
	 *            the source vector
	 */
	public final void conjugate(Quaternion q1) {
		this.x = -q1.x;
		this.y = -q1.y;
		this.z = -q1.z;
		this.w = q1.w;
	}

	/**
	 * Negates all the coefficients of the Quaternion.
	 */
	public final void negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		this.w = -this.w;
	}

	/**
	 * Returns the "dot" product of this Quaternion and {@code b}:
	 * <p>
	 * {@code this.x * b.x + this.y * b.y + this.z * b.z + this.w * b.w}
	 * 
	 * @param b
	 *            the Quaternion
	 */
	public final float dotProduct(Quaternion b) {
		return this.x * b.x + this.y * b.y + this.z * b.z + this.w * b.w;
	}

	/**
	 * Returns the "dot" product of {@code a} and {@code b}:
	 * <p>
	 * {@code a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w}
	 * 
	 * @param a
	 *            the first Quaternion
	 * @param b
	 *            the second Quaternion
	 */
	public final static float dotProduct(Quaternion a, Quaternion b) {
		return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
	}

	/**
	 * Sets the value of this Quaternion to the Quaternion product of itself and {@code q1}, (i.e., {@code this = this * q1}).
	 * 
	 * @param q1
	 *            the other Quaternion
	 */
	public final void multiply(Quaternion q1) {
		float x, y, w;

		w = this.w * q1.w - this.x * q1.x - this.y * q1.y - this.z * q1.z;
		x = this.w * q1.x + q1.w * this.x + this.y * q1.z - this.z * q1.y;
		y = this.w * q1.y + q1.w * this.y - this.x * q1.z + this.z * q1.x;
		this.z = this.w * q1.z + q1.w * this.z + this.x * q1.y - this.y * q1.x;
		this.w = w;
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the Quaternion which is product of quaternions {@code q1} and {@code q2}.
	 * 
	 * @param q1
	 *            the first Quaternion
	 * @param q2
	 *            the second Quaternion
	 */
	public final static Quaternion multiply(Quaternion q1, Quaternion q2) {
		float x, y, z, w;
		w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z;
		x = q1.w * q2.x + q2.w * q1.x + q1.y * q2.z - q1.z * q2.y;
		y = q1.w * q2.y + q2.w * q1.y - q1.x * q2.z + q1.z * q2.x;
		z = q1.w * q2.z + q2.w * q1.z + q1.x * q2.y - q1.y * q2.x;
		return new Quaternion(x, y, z, w);
	}
	
	/**
	 * Normalizes the value of this Quaternion in place and return its {@code norm}.
	 */
	public final float normalize() {
		float norm = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
		if (norm > 0.0f) {
			this.x /= norm;
			this.y /= norm;
			this.z /= norm;
			this.w /= norm;
		} else {
			this.x = (float) 0.0;
			this.y = (float) 0.0;
			this.z = (float) 0.0;
			this.w = (float) 1.0;
		}
		return norm;
	}

	public Vector3d eulerAngles() {
		float roll, pitch, yaw;
		float test = x * y + z * w;
		if (test > 0.499) { // singularity at north pole
			pitch = 2 * (float) Math.atan2(x, w);
			yaw = (float) Math.PI / 2;
			roll = 0;
			return new Vector3d(roll, pitch, yaw);
		}
		if (test < -0.499) { // singularity at south pole
			pitch = -2 * (float) Math.atan2(x, w);
			yaw = -(float) Math.PI / 2;
			roll = 0;
			return new Vector3d(roll, pitch, yaw);
		}
		float sqx = x * x;
		float sqy = y * y;
		float sqz = z * z;
		pitch = (float) Math.atan2(2 * y * w - 2 * x * z, 1 - 2 * sqy - 2 * sqz);
		yaw = (float) Math.asin(2 * test);
		roll = (float) Math.atan2(2 * x * w - 2 * y * z, 1 - 2 * sqx - 2 * sqz);
		return new Vector3d(roll, pitch, yaw);
	}
}