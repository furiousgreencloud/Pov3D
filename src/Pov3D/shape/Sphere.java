package Pov3D.shape;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

import Pov3D.display.YPoint;
import Pov3D.util.*;

public class Sphere extends POVObject {

	private PVector center;

	/**
	 * size: Radius
	 */
	private float size;
	private List<YPoint> points;
	public DrawMode drawMode;

	public Sphere(PVector center, int size, DrawMode drawMode) {
		super();
		this.center = POV.project(center);
		this.size = size;
		this.drawMode = drawMode;

		points = new ArrayList<YPoint>();
		update();
	}

	public Sphere(int size) {
		this(new PVector(0, 0, 0), size, DrawMode.SOLID);
	}

	public Sphere(int size, DrawMode drawMode) {
		this(new PVector(0, 0, 0), size, drawMode);
	}

	public Sphere(PVector center, int size) {
		this(center, size, DrawMode.SOLID);
	}

	public void setCenter(PVector p) {
		p = POV.project(p);
		center.set(p.x, p.y, p.z);
		update();
	}

	public void setCenter(float x, float y, float z) {
		setCenter(new PVector(x, y, z));
	}

	public void setDrawMode(DrawMode drawMode) {
		this.drawMode = drawMode;
		update();
	}

	public void setSize(float size) {
		this.size = size;
		update();
	}

	private boolean isPointPartOfSphere(int x, int y, int z) {
		PVector p = new PVector(x, y, z);
		return (drawMode == DrawMode.SOLID) ? PApplet.dist(center.x, center.y,
				center.z, x, y, z) <= size

		: PVector.dist(center, p) <= size
				&& PVector.dist(center, p) >= size - 1;
	}

	private void update() {
		points.clear();

		for (int x = (int) (center.x - size); x <= center.x + size; x++) {
			for (int y = (int) (center.y - size); y <= center.y + size; y++) {
				for (int z = (int) (center.z - size); z <= center.z + size; z++) {
					if (isPointPartOfSphere(x, y, z)) {
						points.add(new YPoint(x, y, z));
					}
				}
			}
		}
	}

	public void draw() {
		for (int i = 0; i < points.size(); i++) {
			YPoint point = (YPoint) points.get(i);
			POV.dot(point);
		}
	}

}
