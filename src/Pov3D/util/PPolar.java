package Pov3D.util;

/**
 * Like a 3D Vector by with the X & Z in polar coordinates.
 * 
 * @author Brady Marks (furiousgreencloud@gmail.com)
 *
 */

public class PPolar {
	public float amplitude, angle, y;
	public PPolar(float amplitude, float angle, float y) {
		this.amplitude = amplitude;
		this.angle = angle;
		this.y = y;
	}
	
	@Override public String toString() {
	    StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");

	    result.append(this.getClass().getName() + " Object {" + NEW_LINE);
	    result.append(" (amplitude, angle): " +  amplitude + " ," + Util.linearmap(angle,0f,(float)Math.PI,0f,180f) + " (deg)" + NEW_LINE);
	    result.append("}");

	    return result.toString();
	  }
	
}
