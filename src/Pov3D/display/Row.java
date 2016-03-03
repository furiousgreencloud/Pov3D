package Pov3D.display;

import Pov3D.util.ProcessingObject;

/**
 * Row Elementary part of a slice, a row is a bit field for the real POV display
 * to display light up
 * 
 * these are sent via UDP to the real display.
 * 
 * the real display has no idea of 3D space, only slices, rows, and the number of slices in total.
 * 
 * it just picks the best slice to show.
 *   
 * 
 * @author Brady Marks (furiousgreencloud@gmail.com)
 *
 */

public class Row extends ProcessingObject {
	public byte[] m_bitField;

	public Row() {
		m_bitField = new byte[8];
	}

	public void clear() {
		m_bitField = new byte[8];
	}
}