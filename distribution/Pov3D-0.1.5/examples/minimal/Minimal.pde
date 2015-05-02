// CONSTANTS

int FRAME_RATE = 12;
String POV_IP = "10.10.1.99"; // Wifi
int POV_PORT = 6666; // 6666
int SLICES = 180;

// LINBRARIES / IMPORTS

import Pov3D.library.*;
import Pov3D.display.*;
import Pov3D.util.*;
import Pov3D.shape.*;

// GLOBALS

POVDisplay g_display = null;

void setup() {
  size(640, 480, P3D);
  frameRate(FRAME_RATE); // fps

  println("setup");
  Pov3DLibrary lib = new Pov3DLibrary(this); 
  ProcessingObject.setPApplet(this);
  g_display = new POVDisplay(POV_IP, SLICES);
  POVObject.setDisplay(g_display); // sets the display for all the POV Objects 
                                   // like sphere and line
}


void draw() {  
  lights();
  background(0);

  float cameraTopBotAngle = (float)Util.linearmap(mouseY, 0, height, -Math.PI/4, Math.PI/4);
  camera(Util.linearmap(mouseX, 0, width, 150, -150), // eye X
    sin(cameraTopBotAngle)*220f, // eye Y
    cos(cameraTopBotAngle)*220, // eye Z
    0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
    0.0f, 1.0f, 0.0f); // upX, upY, upZ

  g_display.clear();
  PVector c = new PVector(0,0,0);
  g_display.dot(c);
  g_display.dot(-10,0,0);
  g_display.dot(10,0,0);
  g_display.scale(1.1);
  g_display.export(); // Send to POV Display
}

