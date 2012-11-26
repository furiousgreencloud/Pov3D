package Pov3D.display;

import Pov3D.util.ProcessingObject;

import Pov3D.util.Util;
import processing.core.PApplet;

public class YPoint extends ProcessingObject {

	public float m_x, m_y, m_z;
	public int m_slice, m_slice_x, m_slice_y;
	public int theta;
	
	boolean m_clipped; // Don't Draw it's out side display

	@Override public String toString() {
		    StringBuilder result = new StringBuilder();
		    String NEW_LINE = System.getProperty("line.separator");

		    result.append(this.getClass().getName() + " Object {" + NEW_LINE);
		    result.append(" (x,y,z): " + m_x + " ," + m_y + " ," + m_z + NEW_LINE);
		    result.append("}");

		    return result.toString();
		  }
	
	/* Set Point position in Cartesian Coordinates */
	public void setCar(float x, float y, float z) {
		
		//Util.Warn(x >= -Util.HALF_WORLD_DIM && x <= Util.HALF_WORLD_DIM , "X out of Range:" + x);
		//Util.Warn(y >= -Util.HALF_WORLD_DIM && y <= Util.HALF_WORLD_DIM,  "Y out of Range:" + y);
		//Util.Warn(z >= -Util.HALF_WORLD_DIM && z <= Util.HALF_WORLD_DIM , "Z out of Range:" + z);
		
		m_x = x;
		m_y = y;
		m_z = z;
		
		if ( !	(
					(x >= -Util.HALF_WORLD_DIM && x <= Util.HALF_WORLD_DIM) &&
					(y >= -Util.HALF_WORLD_DIM && y <= Util.HALF_WORLD_DIM) &&
					(z >= -Util.HALF_WORLD_DIM && z <= Util.HALF_WORLD_DIM)
				)
			)
		{
			//PApplet.println("Point Cliped");
			m_clipped = true;
			return;
		}

		float am = Util.hypot(x, z);
		float ph = PApplet.atan2(-x, z);

		int deg = (int) Util.linearmap(ph, 0, PApplet.TWO_PI, 0, 360);
		if (deg < 0) {
			deg += 360;
		}
		int radius = (int) Util.linearmap(am, 0.0f, Util.HALF_WORLD_DIM, 1.0f, Util.ROWS/2f);
		int height = (int) Util.linearmap(y, -Util.HALF_WORLD_DIM, Util.HALF_WORLD_DIM, 0f, Util.ROWS - 1);

		setPol_private(deg, radius, height);
	}

	/* Set Point position in Polar Coordinates */
	public void setPol(float theta, // radians
			float am, // 0 to 50
			float height // -50 to 50
	) {
		assert (false); // TODO
	}

	public void setPol_private(
			int theta, // degrees 0-360
			int radius, // 0-24
			int height // 0-49
	) {
		Util.Assert(
		   theta >= 0
		&& theta < 360
		
		&& radius > 0
		&& radius <= 35
		
		&& height >= 0
		&& height < Util.ROWS,
		"Polar Coordinates Out of Range (theta,radius,hieght): " + 
				theta + "," + radius + "," + height);
		
		/*
		assert (theta >= 0);
		assert (radius >= 0);
		assert (height >= 0);
		assert (radius <= 35); // 35.35533905932738
		*/
		
	    if (m_clipped = radius > 25) { 
	        // PApplet.println("Warning: Point Outside Cylinder but inside Box");
	        return;
	    }
	  
		/*
		assert (theta <= 360);
		assert (radius < Slice.COLS / 2);
		assert (height < ROWS);
		*/
	    
		if (theta < 180) {
			m_slice = Util.linearmap(theta, 0, 180, 0, Util.s_sliceCount);
			m_slice_x = Util.COLS/2 + radius - 1;
		} else {
			m_slice = Util.linearmap(theta - 180, 0, 180, 0, Util.s_sliceCount);
			m_slice_x = Util.COLS/2 - radius;
		}
		
		m_slice_y =  height;

		Util.Assert(
				m_slice_y >= 0
			&&	m_slice_y < Util.ROWS
			&&	m_slice_x >= 0
			&&	m_slice_x < Util.COLS
			&&  m_slice < Util.s_sliceCount
			&&	m_slice >= 0, "Point Location Calculation Incorrect: (slice,x,y): " 
					+ m_slice + ","
					+ m_slice_x + ","
					+ m_slice_y
					+ " from (theta,radius,hieght): " + 
						theta + "," + radius + "," + height
				);		
			
	}

	public YPoint(float x, float y, float z) {
		setCar(x, y, z);
	}
	
	public YPoint(processing.core.PVector p) {
		this(p.x, p.y, p.z);
	}

	/* Default is to Create a Point at the Origin */
	public YPoint() {
		setCar(0, 0, 0);
	}
}