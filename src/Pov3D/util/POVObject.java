package Pov3D.util;

import java.util.List;

import processing.core.PVector;
import Pov3D.display.POVDisplay;
import Pov3D.display.YPoint;


/**
 * A Static Class from which objects are derived if the need access to the POVDisplay
 * 
 * i.e. they are POVDisplay Dependent Objects
 * 
 * these are generally 3D Primitives lines box, line, sphere etc.
 * 
 * @author Brady Marks (furiousgreencloud@gmail.com)
 * 
 */

public abstract class POVObject extends ProcessingObject {

	public enum DrawMode {
		SOLID, OUTLINE
	}

	public static POVDisplay POV;

	public static void setDisplay(POVDisplay display) {
		POV = display;
	}

	/**
	 * Populates a point cloud 'destination' with all the points from 'start' to 'end'
	 * 
	 * @param destination
	 * @param start
	 * @param end
	 */
	protected void strokeLineInto(List<YPoint> destination, PVector start,
			PVector end) {
		/*
		 * Points along line are:
		 * 
		 * p = (0->1)*(b-a) + a
		 */
		if (start.equals(end)) {
			destination.add(new YPoint(start));
		}

		PVector delta = PVector.sub(end, start);
		YPoint yPrev = null;
		for (float interp = 0f; interp <= 1; interp += 1f / 50f) {
			PVector p = PVector.add(start, PVector.mult(delta, interp));
			YPoint newYPoint = new YPoint(p);
			if (yPrev != null && yPrev.equalSlicePos(newYPoint)) {
				 // prev Point is the same as current point, don't add it to destination
			} else {
				destination.add(newYPoint);
				yPrev = newYPoint;
			}
		}
		
		/*
		 * OPTIMAZATION: since the interpolation resolution is display dependent here 
		 * (via the 1/50f fudge factor)
		 * and the YPoints are "discrete" which *should* check to see if previous and new 
		 * generated YPoints are same by value and then not add duplicates to the destination
		 * 
		 * - OK first part done! - fgc
		 * 
		 * Also the fudge factor is too crudely defined, it should be smarter i.e. related to
		 * the magnitude of the line for starters.
		 *
		 * this would speed up the processing sketch draw methods
		 */
	}

	/**
	 *  Abstract, most classes will overwrite this draw method 
	 */
	abstract public void draw();
}
