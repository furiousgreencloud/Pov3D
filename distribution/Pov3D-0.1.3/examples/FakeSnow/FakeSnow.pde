// CONSTANTS

int SCREEN_WIDTH = 640;
int SCREEN_HEIGHT = 360;
int FRAME_RATE = 12;
int FLAKES = 50;
int ANIMATE_INTERVAL_MS = 120;
int FLAKE_FALL_PER_SEC = 0;
int INT_MAX = 2147483647;
int INT_MIN = -2147483648;

String BEAGLE_IP = "10.10.1.99"; // Wifi
//String BEAGLE_IP = "192.168.7.2"; // Serial
int BEAGLE_PORT = 0x1A0A; // 6666

// LIBRARIES / IMPORTS

import Pov3D.library.*;
import Pov3D.display.*;
import Pov3D.util.*;

// GLOBALS

POVDisplay g_display = null;
int g_lastAnimate = 0;
ArrayList<PVector> snowFlakes;

// UTIL FUNCTIONS

int linearmap(int x, int in_min, int in_max, int out_min, int out_max) {
  return ((x - (in_min)) * ((out_max) - (out_min)) / ((in_max) - (in_min)) + (out_min));
}

float linearmap(float x, float in_min, float in_max, float out_min, float out_max) {
  return ((x - (in_min)) * ((out_max) - (out_min)) / ((in_max) - (in_min)) + (out_min));
}

int TimeDiff(int start, int end) {
  if (end >= start) {
    return end - start;
  } 
  else {
    return INT_MAX - start + end;
  }
}

// Here we go !

void setup() {
  size(SCREEN_WIDTH, SCREEN_HEIGHT, P3D);
  fill(204);
  frameRate(FRAME_RATE); // fps

  Pov3DLibrary lib = new Pov3DLibrary(this); 
  g_display = new POVDisplay(BEAGLE_IP,180);
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
    y  += linearmap( (float)ANIMATE_INTERVAL_MS + random (-2,2), 0.0, 1000.0, 0.0, FLAKE_FALL_PER_SEC );
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

  boolean needExport = false;
  lights();
  background(0);

  float cameraTopBotAngle = (float)Util.linearmap(mouseY, 0, SCREEN_HEIGHT, -Math.PI/4, Math.PI/4);
   camera(
			Util.linearmap(mouseX, 0, SCREEN_WIDTH, 150, -150), // eye X
			sin(cameraTopBotAngle)*220f, // eye Y
			cos(cameraTopBotAngle)*220, // eye Z
		0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
		0.0f, 1.0f, 0.0f); // upX, upY, upZ
  

  int now = millis();
  if (TimeDiff(g_lastAnimate, now) >= ANIMATE_INTERVAL_MS) {
    animate();
    needExport = true;
    g_lastAnimate = now;
  }

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


  g_display.rotateX(PI/2);
  // Draw Snow Flakes
  for (int i = snowFlakes.size() -1; i >= 0; i--) { 
    PVector flake = snowFlakes.get(i);
    g_display.dot(flake);
  }


  g_display.dot(new YPoint(0, -50, -50));

  if (needExport) g_display.export(); // Send to POV Display
}

