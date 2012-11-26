package Pov3D.shape;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PVector;

import Pov3D.display.YPoint;
import Pov3D.util.POVObject;

public class Poly extends POVObject {
	ArrayList<PVector> outline;
	ArrayList<YPoint> pointCloud;

	public Poly() {
		outline = new ArrayList<PVector>();
		pointCloud = new ArrayList<YPoint>();
	}

	public void vertex(PVector p) {
		outline.add(POV.project(p));
	}

	public void close() {
		outline.add(outline.get(0));
		updatePointCloud();
	}

	public void end() {
		updatePointCloud();
	}

	private void updatePointCloud() {
		Iterator<PVector> i = outline.iterator();

		if (outline.size() > 1) {
			PVector start = i.next();
			while (i.hasNext()) {
				PVector end = i.next();

				PVector delta = PVector.sub(end, start);
				for (float interp = 0f; interp <= 1; interp += 1f / 50f) {
					PVector p = PVector.add(start, PVector.mult(delta, interp));
					pointCloud.add(new YPoint(p));
				}
				start = end;
			}
		}
	}

	public void draw() {
		Iterator<YPoint> i = pointCloud.iterator();
		while (i.hasNext()) {
			POV.dot(i.next());
		}
	}

	/**
	 * create a point at the origin of the current matrix
	 * 
	 */
	public void vertex() {
		outline.add(POV.project(new PVector()));
	}
}
