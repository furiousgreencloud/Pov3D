package Pov3D.display;

import Pov3D.util.*;

/**
 * Row Elementary part of a Volume,
 * 
 * these are sent via UDP to the real display.
 * 
 * the real display has no idea of 3D space, only slices, rows, and the number of slices in total.
 * 
 * it just picks the best slice to show.
 * 
 * * @author Brady Marks (furiousgreencloud@gmail.com)
 *
 */

public class Slice extends ProcessingObject {
	public Row[] m_rows;
	
	public Slice() {
		m_rows = new Row[Util.ROWS];
		for (int i = 0; i < Util.ROWS; i++) {
			m_rows[i] = new Row();
		}
	}

	public void clear() {
		for (int i = 0; i < Util.ROWS; i++) {
			m_rows[i].clear();
		}
	}
}