package Pov3D.util;

import java.util.List;

import processing.core.PVector;
import Pov3D.display.POVDisplay;
import Pov3D.display.YPoint;

public class POVObject extends ProcessingObject {

	public enum DrawMode {
		SOLID, OUTLINE
	}

	public static POVDisplay POV;

	public static void setDisplay(POVDisplay display) {
		POV = display;
	}

	protected void strokeLineInto(List<YPoint> destination, PVector start,
			PVector end) {
		/*
		 * Points along line are:
		 * 
		 * p = (0->1)*(b-a) + a
		 */

		PVector delta = PVector.sub(end, start);
		for (float interp = 0f; interp <= 1; interp += 1f / 50f) {
			PVector p = PVector.add(start, PVector.mult(delta, interp));
			destination.add(new YPoint(p));
		}
	}

	public void draw() {
	}
}
