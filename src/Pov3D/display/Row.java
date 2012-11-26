package Pov3D.display;

import Pov3D.util.ProcessingObject;

public class Row extends ProcessingObject {
	public byte[] m_bitField;

	public Row() {
		m_bitField = new byte[8];
	}

	public void clear() {
		m_bitField = new byte[8];
	}
}