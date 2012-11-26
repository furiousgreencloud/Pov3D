import java.util.ArrayList;
import java.util.Iterator;

import Pov3D.display.POVDisplay;
import Pov3D.library.Pov3DLibrary;
import Pov3D.shape.Box;
import Pov3D.shape.Poly;
import Pov3D.shape.Sphere;
import Pov3D.util.POVObject;
import Pov3D.util.POVObject.DrawMode;
import processing.core.*;

public class ThreeDeeMe extends PApplet {

	POVDisplay g_display = null;
	ArrayList<POVObject> world = null;

	public void setup() {
		size(500, 500, P3D);
		frameRate(12);
		Pov3DLibrary lib = new Pov3DLibrary(this);
		g_display = new POVDisplay("127.0.0.1", 180);
		POVObject.setDisplay(g_display);
		mouseX = mouseY = 250;
		world = new ArrayList<POVObject>();

	}

	public void animate() {
		world.clear();
		
		Poly shape = new Poly();
		int n = 8;

		for (int i = 0; i < n; i++) {
			g_display.pushMatrix();
			g_display.rotateX(radians(i * 360f / n));
			g_display.translate(0, -40, 0);
			shape.vertex();
			world.add(new Sphere(2, DrawMode.OUTLINE));
			world.add(new Box(10));
			g_display.popMatrix();
		}

		shape.close();
		world.add(shape);
		g_display.rotateY(radians(5));
	}

	
	public void draw() {

		float cameraTopBotAngle = map((float) mouseY, 0f, (float) height,
				-PI / 4, PI / 4);
		camera(map(mouseX, 0, width, 150, -150), // eye X
				sin(cameraTopBotAngle) * 220f, // eye Y
				cos(cameraTopBotAngle) * 220, // eye Z
				0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
				0.0f, 1.0f, 0.0f); // upX, upY, upZ
		background(0);

		animate();
		
		Iterator<POVObject> i = world.iterator();
		while (i.hasNext()) {
			i.next().draw();
		}
	}
}
