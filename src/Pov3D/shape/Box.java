package Pov3D.shape;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PVector;
import Pov3D.display.YPoint;
import Pov3D.util.POVObject;
import Pov3D.util.POVObject.DrawMode;

public class Box extends POVObject {

	ArrayList<YPoint> pointCloud;
	ArrayList<PVector> outlineFrontFace;
	ArrayList<PVector> outlineBackFace;

	// DrawMode drawMode;

	public Box(float size) {
		this(size, size, size);
	}

	public Box(float width, float height, float depth /* , DrawMode drawMode */) {

		outlineFrontFace = new ArrayList<PVector>();
		outlineBackFace = new ArrayList<PVector>();
		pointCloud = new ArrayList<YPoint>();

		// Names for vars are Top||Bottom Left||Right || Back||Font
		PVector tlf = new PVector();
		PVector trf = new PVector();

		PVector blf = new PVector();
		PVector brf = new PVector();

		PVector blb = new PVector();
		PVector brb = new PVector();

		PVector tlb = new PVector();
		PVector trb = new PVector();

		tlf.add(width / 2, -height / 2, depth / 2);
		trf.add(-width / 2, -height / 2, depth / 2);

		blf.add(width / 2, height / 2, depth / 2);
		brf.add(-width / 2, height / 2, depth / 2);

		blb.add(width / 2, height / 2, -depth / 2);
		brb.add(-width / 2, height / 2, -depth / 2);

		tlb.add(width / 2, -height / 2, -depth / 2);
		trb.add(-width / 2, -height / 2, -depth / 2);

		// this.drawMode = drawMode;

		outlineFrontFace.add(POV.project(tlf));
		outlineFrontFace.add(POV.project(trf));
		outlineFrontFace.add(POV.project(brf));
		outlineFrontFace.add(POV.project(blf));

		outlineBackFace.add(POV.project(tlb));
		outlineBackFace.add(POV.project(trb));
		outlineBackFace.add(POV.project(brb));
		outlineBackFace.add(POV.project(blb));

		addFaceToPointCloud(outlineFrontFace);
		addFaceToPointCloud(outlineBackFace);
		joinFaces();
	}

	private void addFaceToPointCloud(ArrayList<PVector> face) {
		Iterator<PVector> i = face.iterator();

		if (face.size() > 1) {
			PVector start = i.next();
			while (i.hasNext()) {
				PVector end = i.next();
				strokeLineInto(pointCloud, start, end);
				start = end;
			}
			if (face.size() > 2) { // close shape which is more complex than a
									// line
				strokeLineInto(pointCloud, start /* which is now end */,
						face.get(0));
			}
		}

	}

	private void joinFaces() {
		assert (outlineFrontFace.size() == outlineBackFace.size());
		Iterator<PVector> front = outlineFrontFace.iterator();
		Iterator<PVector> back = outlineBackFace.iterator();
		while (front.hasNext()) {
			strokeLineInto(pointCloud, front.next(), back.next());
		}
	}

	public void draw() {
		Iterator<YPoint> i = pointCloud.iterator();
		while (i.hasNext()) {
			POV.dot(i.next());
		}
	}
}
