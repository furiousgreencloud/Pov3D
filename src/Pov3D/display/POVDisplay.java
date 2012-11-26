package Pov3D.display;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.*;
import Pov3D.util.*;
import hypermedia.net.*;

public class POVDisplay extends Pov3D.util.ProcessingObject {
	
	public static float MIN = - Util.HALF_WORLD_DIM;
	public static float MAX =   Util.HALF_WORLD_DIM;
	public static float MAX_AMPITUDE = 70.71067811865476f;
	public static float SAFE_MAX = (float) Math.sin(Math.PI/2) * Util.HALF_WORLD_DIM;
	public static float SAFE_MIN = (float) - Math.sin(Math.PI/2) * Util.HALF_WORLD_DIM;
	
	public static int ORANGE =  0xEEB400;
	
	private Slice[] m_slices;
	public int m_exportCount = 0;
	
	public String m_deviceIP; // = "127.0.0.1";
	
	private UDP m_udp = null;
	
	private PMatrix3D m_matrix = null;
	private ArrayList<PMatrix3D> m_matrixStack;
	
	public POVDisplay(String deviceIP) {
		super();
		m_deviceIP = deviceIP;
		m_matrix = new PMatrix3D();
		m_matrixStack = new ArrayList<PMatrix3D>(); // top/most recent matrix is the front i.e. at index 0
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
	
	public POVDisplay(String deviceIP, int sliceCount) {
		this(deviceIP);
		Util.s_sliceCount = Math.min(sliceCount,Util.MAX_SLICES);
	}

	public void callReceiveHandler(byte[] data) {
		PApplet.println("Got UDP DATA");
	}
	
	public void clear() {
		assert (m_slices != null);
		for (int i = 0; i < Util.MAX_SLICES; i++)
			m_slices[i].clear();
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
		m_exportCount++;
	}

	/**
	 * Light a Point in the 3D space of the POV display
	 * 
	 * @param p position
	 * @param value boolean to switch a point on/off
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
		// slice.needSend = true;
	}

	/**
	 * Convenience method to light a point
	 * @param x pos
	 * @param y pos
	 * @param z pos
	 */
	public void dot(float x, float y, float z) {
		dot(new PVector(x, y, z));
	}

	/**
	 * Light point at point p
	 * @param yp
	 */
	public void dot(PVector p) {
		YPoint yp = new YPoint(project(p));
		dot(yp);
	}
	
	/**
	 * Process a PVector point (p) into it's final position via the stacked matrixes
	 * and return that point
	 *  
	 * @param p Point as PVector
	 * @return PVector at it's final resting place
	 */
	public PVector project(PVector p) {
		PVector finalPos = new PVector();
		
		Iterator<PMatrix3D> i = m_matrixStack.iterator();
		while(i.hasNext()) {
			i.next().mult(p,finalPos);
			p = finalPos;
		}
		
		return finalPos;
	}
	
	/**
	 * Light a Dot on the display, a YPoint unlike a PVector is not translated and rotated on the matrix stack.
	 * @param yp direct point to light
	 */
	public void dot(YPoint yp) {
		writePoint(yp, true);
		if (yp.m_clipped) 
			pApplet.stroke(ORANGE);
		else
			pApplet.stroke(255);
		pApplet.point(yp.m_x,yp.m_y,yp.m_z);
	}

	public void pushMatrix() {
		PMatrix3D m = new PMatrix3D();
		m_matrixStack.add(0, m);
		m_matrix = m;
	}
	
	public void popMatrix() {
		int stackSize = m_matrixStack.size();
		if (stackSize >= 2) {
			m_matrixStack.remove(0);
			m_matrix = m_matrixStack.get(0);
		}
		assert(m_matrixStack.size() > 0);
		assert(m_matrix != null);
	}
	
	/**
	 * 
	 * @param angle in radians around the X axis
	 */
	public void rotateX(float angle) {
		m_matrix.rotateX(angle);
	}

	/**
	 * 
	 * @param angle in radians around the Y axis
	 */
	public void rotateY(float angle) {
		m_matrix.rotateY(angle);
	}

	/**
	 * 
	 * @param angle in radians around the Y axis
	 */
	public void rotateZ(float angle) {
		m_matrix.rotateZ(angle);
	}
	
	public void translate(PVector p) {
		m_matrix.translate(p.x, p.y, p.z);
	}
	
	public void translate(float tx, float ty, float tz) {
		m_matrix.translate(tx, ty, tz);
	}
}