package Pov3D.display;

import Pov3D.util.*;

public class Slice extends ProcessingObject {
	public Row[] m_rows;
	
	// boolean needSend = false;

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