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
		// no need to use 'Project' because the first point
		// has been projected all ready.
		outline.add(outline.get(0));
		updatePointCloud();
	}

	public void end() {
		updatePointCloud();
	}

	private void updatePointCloud() {
		pointCloud.clear();
		Iterator<PVector> i = outline.iterator();

		if (outline.size() > 1) {
			PVector start = i.next();
			while (i.hasNext()) {
				PVector end = i.next();
				strokeLineInto(pointCloud,start,end);
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
