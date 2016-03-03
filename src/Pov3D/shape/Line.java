package Pov3D.shape;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PVector;
import Pov3D.display.YPoint;
import Pov3D.util.POVObject;

public class Line extends POVObject {
	
	public PVector a, b;
	
	public Line(PVector a, PVector b) {		
		this.a = a;
		this.b = b;		
	}

	public void draw() {
		ArrayList<YPoint> pointCloud  = new ArrayList<YPoint>(100);
		PVector l1 = POV.project(a);
		PVector l2 = POV.project(b);
		strokeLineInto(pointCloud, l1, l2);
		Iterator<YPoint> i = pointCloud.iterator();
		while (i.hasNext()) {
			POV.dot(i.next());
		}
	}

}
