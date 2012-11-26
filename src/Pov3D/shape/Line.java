package Pov3D.shape;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PVector;
import Pov3D.display.YPoint;
import Pov3D.util.POVObject;

public class Line extends POVObject {
	public PVector a, b;

	ArrayList<YPoint> pointCloud;

	public Line(PVector a, PVector b) {
		this.a = POV.project(a);
		this.b = POV.project(b);
		strokeLineInto(pointCloud, a, b);
	}

	public void draw() {
		Iterator<YPoint> i = pointCloud.iterator();
		while (i.hasNext()) {
			POV.dot(i.next());
		}
	}

}
