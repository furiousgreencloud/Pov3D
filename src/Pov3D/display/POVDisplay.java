package Pov3D.display;

import java.util.Stack;

import processing.core.*;
import Pov3D.util.*;
import hypermedia.net.*;

/**
 * POVDisplay the main class that corresponds to a physical POV Display
 * 
 * see http://furiousgreencloud.com/pov/ for complete general documentation
 *  
 * 
 * @author Brady Marks (furiousgreencloud@gmail.com)
 *
 */
public class POVDisplay extends Pov3D.util.ProcessingObject {
	
	public static float MIN = - Util.HALF_WORLD_DIM;
	public static float MAX =   Util.HALF_WORLD_DIM;
	
	/*
	public static float MAX_AMPITUDE_s = 70.71067811865476f; // hypotenuse of HALF_WORLD_DIMxHALF_WORLD_DIM triangle
											// aka, sqrt(HALF_WORLD_DIM*HALF_WORLD_DIM + HALF_WORLD_DIM*HALF_WORLD_DIM);
	*/
	public static float MAX_AMPITUDE = (float) Math.sqrt(Util.HALF_WORLD_DIM * Util.HALF_WORLD_DIM * 2);
	
	public static float SAFE_MAX = (float) Math.sin(Math.PI/2) * Util.HALF_WORLD_DIM;
	public static float SAFE_MIN = (float) - Math.sin(Math.PI/2) * Util.HALF_WORLD_DIM;
	
	public static int ORANGE =  0xEEB400; // this ORANGE is used to display clipped points on the processing canvas
	
	private Slice[] m_slices;
	
	public int m_pointCountInDisplay = 0; // The number of non clipped points that the display will show
	//public int m_exportCount = 0; // DEBUG
	
	public String m_deviceIP; // = "127.0.0.1";
	
	private UDP m_udp = null;
	
	private PMatrix3D m_matrix = null;
	private Stack<PMatrix3D> m_matrixStack;
	
	/**
	 *  The simplest Constructor of a POVDisplay object
	 *  
	 *  see the POVDisplay(String deviceIP, int sliceCount) for a complete description
	 *  
	 *  this version creates Util.MAX_SLICES (150) slices , by default.
	 *  
	 *	@param	deviceIP  a string like "10.10.1.99", this is the IP given out by
	 * 				your router to the display via DHCP
	 * 	@see	POVDisplay(String deviceIP, int sliceCount)
	 */
	public POVDisplay(String deviceIP) {
		super();
		
		m_deviceIP = deviceIP;
		m_matrix = new PMatrix3D();
		m_matrixStack = new Stack<PMatrix3D>(); // top/most recent matrix is the front i.e. at index 0
		m_matrixStack.add(m_matrix);
		PApplet.println("Will Look for POV Display at: " 
					+ deviceIP 
					+ ":"
					+ Util.PORT_TX);
		m_udp = new UDP(this,Util.PORT_RX);
		m_slices = new Slice[Util.MAX_SLICES];
		for (int i = 0; i < Util.MAX_SLICES; i++)
			m_slices[i] = new Slice();
	}

	/**
	 * The preferred Constructor a POVDisplay, you'll likely just
	 * need one (global). 
	 * 
	 * @param	deviceIP  a string like "10.10.1.99", this is the IP given out by
	 * 			your router to the display via DHCP
	 * 
	 * @param  	sliceCount	
	 * 
	 * 			the number of slices you want the display to use,
	 * 			80 is a safe number you could get up to 120 if your 3D models are
	 * 			simple. The higher the number the more "resolution" you get. i.e.
	 * 			The Display will try to draw more slices as it spins. The current
	 * 			slice that it draws is always the best slice calculated from the
	 * 			current time, so are possibility skipped, with little adverse effects.
	 * 			
	 * 			the slice count is limited to Util.MAX_SLICES (150)
	 * 
	 * @return	reference to your POVDisplay object
	 */
	public POVDisplay(String deviceIP, int sliceCount) {
		this(deviceIP);
		Util.s_sliceCount = Math.min(sliceCount,Util.MAX_SLICES);
	}

	/** Receive Data Handler
	 * 
	 * This is a stub, the display does not send any data back to the processing sketch.
	 * 
	 */
	public void callReceiveHandler(byte[] data) {
		PApplet.println("Warning: Got UDP DATA from Display - ignored, Are you sure you have the right IP of the display");
	}
	
	/**
	 *  clear out voxels from display object, similar to background() in processing.
	 *  
	 *  Note: clear does NOT reset the matrixStack as you might not want to do that.
	 * 
	 *  As you draw into the display object, the object accumulates VOXELs (Volume Elements)
	 *  which are then sent to the display when the export() method. After that you
	 *  SHOULD clear() the display object with this method, otherwise you will accumulate
	 *  voxels and the display with eventual become solidly lit
	 *  
	 *  @see export(), resetMatrix()
	 */
	public void clear() {
		assert (m_slices != null);
		for (int i = 0; i < Util.MAX_SLICES; i++)
			m_slices[i].clear();
		/*
		m_matrix = new PMatrix3D();
		m_matrixStack.clear();
		m_matrixStack.add(m_matrix);
		*/
		m_pointCountInDisplay = 0;
	}

	/**
	 * Send current Volume to the POV display
	 * 
	 * this should happen often enough for the animation to happen
	 * but not so often as to overwhelm the display.
	 * 
	 * In practice this is every 80-250 ms for a Volume/Animation
	 * rate of 12.5 to 4 volumes per second.
	 * 
	 */
	public void export() {
		/*
		// DEBUG code show how often a volume is exported  
		if (m_exportCount >= 100) {
			PApplet.print(".\n");
			m_exportCount = 0;
		} else {
			PApplet.print(".");
		}
		*/
		
		int byteCount = (8 * Util.ROWS * Util.s_sliceCount) + 2;
		byte[] buffer = new byte[byteCount];
		
		int bufferIndex = 0;
		
		buffer[bufferIndex++] = (byte)Util.s_sliceCount;

		for (int slice = 0; slice < Util.s_sliceCount; slice++) {
			for (int row = 0; row < Util.ROWS; row++) {
				for (int i = 0; i < 8; i++) {
					buffer[bufferIndex++] = m_slices[slice].m_rows[row].m_bitField[i];
				}
			}
		}
		buffer[bufferIndex++] = 0; // null terminate
		assert(bufferIndex == byteCount);
		m_udp.send(buffer, m_deviceIP, Util.PORT_TX);
		//m_exportCount++; // DEBUG
	}

	/**
	 * Light/Erase a single Point in the 3D space of the POV display object
	 * you likely don't want to call this, hence it's privatness.
	 * 
	 * Rather call dot(x,y,z) or dot(PVector p) to light a point
	 * 
	 * @param p position of the special YPoint type
	 * @param value boolean to switch a point on/off
	 * @see YPoint
	 * @see dot
	 */
	private void writePoint(YPoint p, boolean value) {
		assert (m_slices != null);
		assert (p != null);
		
	    if (p.m_clipped) return;

		int sliceIndex = p.m_slice;
		assert (sliceIndex < Util.s_sliceCount);
		assert (sliceIndex >= 0);

		Slice slice = m_slices[sliceIndex];
		assert (slice != null);

		int xPos = p.m_slice_x;
		int yPos = p.m_slice_y;

		if (xPos >= 25) {
			xPos += (32 - 25);
		}

		int charIndex = xPos / 8;
		int bitIndex = xPos % 8;

		assert (charIndex <= 8);
		assert (bitIndex <= 8);

		assert (yPos >= 0);
		assert (yPos < Util.ROWS);
		assert (slice.m_rows[yPos] != null);

		if (value) {
			slice.m_rows[yPos].m_bitField[charIndex] |= 1 << bitIndex;
		} else {
			slice.m_rows[yPos].m_bitField[charIndex] &= ~(1 << bitIndex);
		}
	}

	/**
	 * Light a point, at x,y,z
	 *  
	 * @param x float from -50.0 to 50.0 
	 * @param y float from -50.0 to 50.0
	 * @param z float from -50.0 to 50.0
	 */
	public boolean dot(float x, float y, float z) {
		return dot(new PVector(x, y, z));
	}

	/**
	 * Convenience method to light a point
	 * @param x pos
	 * @param y pos
	 * @param z pos
	 * @param c Color value as int
	 */
	public boolean dot(float x, float y, float z, int c) {
		return dot(new PVector(x, y, z),c);
	}

	
	/**
	 * Light point at point p in the POVDisplay object, 
	 * ALSO draws a white dot on the sketch canvas
	 * 
	 *	@param p	a PVector point
	 * 
	 *	@see		POVDisplay
	 */
	public boolean dot(PVector p) {
		YPoint yp = new YPoint(project(p));
		return dot(yp);
	}
	
	/**
	 * Light point at point p in the POVDisplay object, 
	 * ALSO draws a coloured dot on the sketch canvas
	 * 
	 *	@param p	a PVector point
	 *	@param c	colour (int) of dot in on canvas
	 * 
	 *	@see		POVDisplay
	 */
	public boolean dot(PVector p, int c) {
		YPoint yp = new YPoint(project(p));
		return dot(yp, c);
	}
	
	/**
	 * Light up a series of poins in the POVDisplay object, to form a
	 * sphere, 
	 * ALSO draws a white sphere on the sketch canvas.
	 * 
	 * Internal uses the Pov3D.shape.Sphere object
	 * 
	 *	@param pos	a PVector point, for the center of the sphere
	 *	@param size (int) the radius of the sphere
	 * 
	 *	@see		SpherePov3D.shape.Sphere
	 */
	public void sphere(PVector pos, int size) {
		Pov3D.shape.Sphere s = new Pov3D.shape.Sphere(pos,size);
		s.draw();
	}
	
	/**
	 * Process a PVector point (p) into it's final position via the stacked matrixes
	 * inside the POVDisplay object, and then return that point.
	 *  
	 * @param p Point (PVector)
	 * @return PVector at it's final resting place
	 */
	public PVector project(PVector p) {
		PVector finalPos = new PVector();
		
		int index = m_matrixStack.size() - 1;
		while(index >= 0) {
			m_matrixStack.get(index--).mult(p,finalPos);
			p = finalPos;
		}
		
		return finalPos;
	}
	
	/**
	 * Light a Dot on the display, a YPoint unlike a PVector is not translated and rotated on the matrix stack.
	 * 
	 * Likley you don't want to use this method but rather dot(PVector p) or dot(x,y,z), 
	 * it is used by other shape primitive to draw more directly in the display object.
	 * 
	 * ALSO draws a point onto the canas of colout c
	 * 
	 * @param yp (YPoint) direct point to light
	 * @param c colour of point on canvas
	 * @return true if the point is currently in the display cylinder, i.e. was not cliped
	 * @see dot(PVector p)
	 */
	public boolean dot(YPoint yp, int c) {
		writePoint(yp, true);
		pApplet.stroke(c);
		if (!yp.m_clipped) { 
			m_pointCountInDisplay++;
		}
		pApplet.point(yp.m_x,yp.m_y,yp.m_z);
		return !yp.m_clipped;
	}
	
	/**
	 * Light a Dot on the display, a YPoint unlike a PVector is not translated and rotated on the matrix stack.
	 * 
	 * Likley you don't want to use this method but rather dot(PVector p) or dot(x,y,z), 
	 * it is used by other shape primitive to draw more directly in the display object.
	 * 
	 * ALSO draws a point onto the canvas White if it is in the Displays VOLUME, Orange if not, i.e. clipped.
	 * 
	 * @param yp (YPoint) direct point to light
	 * @return true if the point is currently in the display cylinder, i.e. was not cliped
	 * @see dot(PVector p)
	 */
	public boolean dot(YPoint yp) {
		writePoint(yp, true);
		if (yp.m_clipped) { 
			pApplet.stroke(ORANGE);
		} else {
			pApplet.stroke(255);
			m_pointCountInDisplay++;
		}
		pApplet.point(yp.m_x,yp.m_y,yp.m_z);
		return !yp.m_clipped;
	}

	/** 
	 * add a new Identity matrix to the internal matrix stack of the POVDisplay Object
	 */
	public void pushMatrix() {
		PMatrix3D m = new PMatrix3D();
		m_matrixStack.push(m);
		m_matrix = m;
	}
	
	/** 
	 * remove the top internal matrix stack of the POVDisplay Object
	 * 
	 * will assert if you remove the last (default) matrix
	 */
	public void popMatrix() {
		if (m_matrixStack.size() >= 2) {
			m_matrixStack.pop();
			m_matrix = m_matrixStack.peek();
		}
		assert(m_matrixStack.size() > 0);
		assert(m_matrix != null);
	}
	
	/**
	 * resets BOTH the internal matrix stack to a single identity matrix
	 * and calls pApplet.resetMatrix() to reset the processing/canvas matrix
	 */
	public void resetMatrix() {
		m_matrix = new PMatrix3D();
		m_matrixStack = new Stack<PMatrix3D>(); // top/most recent matrix is the front i.e. at index 0
		m_matrixStack.add(m_matrix);
		pApplet.resetMatrix();
	}
	
	/**
	 * Add a rotation to the current/top matrix
	 * 
	 * @param angle in radians (float) around the X axis
	 */
	public void rotateX(float angle) {
		m_matrix.rotateX(angle);
	}

	/**
	 * Add a rotation to the current/top matrix
	 *  
	 * @param angle in radians(float) around the Y axis
	 */
	public void rotateY(float angle) {
		m_matrix.rotateY(angle);
	}

	/**
	 * Add a rotation to the current/top matrix 
	 * @param angle in radians (float) around the Y axis
	 */
	public void rotateZ(float angle) {
		m_matrix.rotateZ(angle);
	}
	
	/** 
	 * Add a translation to the current/top matrix
	 * 
	 * @param p
	 */
	public void translate(PVector p) {
		m_matrix.translate(p.x, p.y, p.z);
	}
	
	/** 
	 * Add a translation to the current/top matrix
	 * 
	 * @param tx, ty, tz translation components
	 */
	public void translate(float tx, float ty, float tz) {
		m_matrix.translate(tx, ty, tz);
	}
	/**
	 * Add a uniform translation to the current/top matrix
	 * @param s scale factor 1.0 => unity, 2.0 => double size
	 */
	public void scale(float s) {
		m_matrix.scale(s);
	}
	
	/**
	 * Add possibly non-uniform scale to the current/top matrix
	 * @param x,y,z scale factor 1.0 => unity, 2.0 => double size
	 */
	public void scale(float x, float y, float z) {
		m_matrix.scale(x,y,z);
	}
}