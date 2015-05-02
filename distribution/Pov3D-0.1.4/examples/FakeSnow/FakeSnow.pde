// CONSTANTS

int FRAME_RATE = 12;
int FLAKES = 50;
int FLAKE_FALL_PER_SEC = 15;
int ANIMATE_INTERVAL_MS = 1000/FRAME_RATE;
int SLICES = 180;

String POV_IP = "10.10.1.99"; // Wifi
//String POV_IP_NDIS = "192.168.7.2"; // Serial
int POV_PORT = 6666;

// LIBRARIES / IMPORTS

import Pov3D.library.*;
import Pov3D.display.*;
import Pov3D.util.*;

// GLOBALS

POVDisplay g_display = null;
ArrayList<PVector> snowFlakes;

// UTIL FUNCTIONS


// Here we go !

void setup() {
  size(800, 600, P3D);
  fill(204);
  frameRate(FRAME_RATE); // fps

  Pov3DLibrary lib = new Pov3DLibrary(this); 
  g_display = new POVDisplay(POV_IP,SLICES);
  POVObject.setDisplay(g_display);


  snowFlakes = new ArrayList();  
  for (int i = 0; i < FLAKES; i++) { 
    PVector flake = new PVector(random(-50, 50), random(-50, 50), random(-50, 50));
    //println(flake);
    snowFlakes.add(flake);
  }
}

// Update Function Called every ANIMATE_INTERVAL_MS
void animate() {
  //println("Animate");
  for (int i = snowFlakes.size() - 1 ; i >= 0; i--) { 
    PVector flake = snowFlakes.get(i);
    float y = flake.y;
    y  += map( (float)ANIMATE_INTERVAL_MS + random (-2,2), 0.0, 1000.0, 0.0, FLAKE_FALL_PER_SEC );
    if (y >= 50.0) {
      flake.y = -50;
      flake.x = random(-50, 50);
      flake.z = random(-50, 50);
    } else {
      flake.y = y;
    }
  }
}



void draw() {  

  lights();
  background(0);

  float cameraTopBotAngle = (float)Util.linearmap(mouseY, 0, height, -Math.PI/4, Math.PI/4);
   camera(
			Util.linearmap(mouseX, 0, width, 150, -150), // eye X
			sin(cameraTopBotAngle)*220f, // eye Y
			cos(cameraTopBotAngle)*220, // eye Z
		0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
		0.0f, 1.0f, 0.0f); // upX, upY, upZ
  

  animate();
  
  g_display.clear();

  stroke(255, 0, 0);
  // Draw Static Cursor Points
  point(-50, -50, 50);
  point(-50, 50, 50);
  point( 50, -50, 50);
  point( 50, 50, 50);

  point(-50, -50, -50);
  point(-50, 50, -50);
  point( 50, -50, -50);
  point( 50, 50, -50);


  // Draw Snow Flakes
  for (int i = snowFlakes.size() -1; i >= 0; i--) { 
    PVector flake = snowFlakes.get(i);
    g_display.dot(flake);
  }

  g_display.dot(0, -50, -50); // reference pixel

  g_display.export(); // Send to POV Display
}

