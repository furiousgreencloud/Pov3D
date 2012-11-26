/* 
 * Thanks for Matt Parker who initally authoured this example 
 */

// CONSTANTS

int SCREEN_WIDTH = 640;
int SCREEN_HEIGHT = 360;
int FRAME_RATE = 25;
int ANIMATE_INTERVAL_MS = 80;
int FLAKE_FALL_PER_SEC = 20;
int INT_MAX = 2147483647;
int INT_MIN = -2147483648;

String BEAGLE_IP = "10.10.1.99"; // Wifi
//String BEAGLE_IP = "192.168.7.2"; // Serial
int BEAGLE_PORT = 0x1A0A; // 6666

// LINBRARIES / IMPORTS

import Pov3D.library.*;
import Pov3D.display.*;
import Pov3D.util.*;
import Pov3D.shape.*;

// GLOBALS

POVDisplay g_display = null;
int g_last = 0;

Sphere sphere = null;
PVector center = null;
PVector velocity;
int maxSpeed = 5;
int size = 12; // 7 - 12


float radtodeg(float r) {
  return Util.linearmap(r, 0f, PI, 0f, 180f);
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

  println("setup");
  Pov3DLibrary lib = new Pov3DLibrary(this); 
  ProcessingObject.setPApplet(this);
  g_display = new POVDisplay(BEAGLE_IP, 90);
  POVObject.setDisplay(g_display);

  center = new PVector();
  velocity = new PVector(
  random(-1, 1), 
  random(-1, 1), 
  random(-1, 1));

  velocity.normalize();
  velocity.mult(maxSpeed);

  sphere = new Sphere(center, size, Sphere.DrawMode.OUTLINE);
}

// Update Function Called every _INTERVAL_MS
void Animate() {
  boolean bounce = false;
  center.add(velocity);
  sphere.setSize(size);


  if (center.y - size <= POVDisplay.MIN || center.y + size >= POVDisplay.MAX) {
    velocity.y *= -1;
    bounce = true;
  }

  if (center.z -size <= POVDisplay.SAFE_MIN || center.z + size >= POVDisplay.SAFE_MAX) {
    velocity.z *= -1;
    bounce = true;
  }

  if (center.x - size <= POVDisplay.SAFE_MIN || center.x + size  >= POVDisplay.SAFE_MAX) {
    velocity.x *= -1;
    bounce = true;
  }


  /*
   PPolar ball_polar = Util.cartopol(center);
   
   if ( ball_polar.amplitude >= POVDisplay.MAX_AMPITUDE) { // hit wall
   PPolar vel_polar = Util.cartopol(velocity);
   
   print("Ball Pos:"); println(ball_polar);
   print("Vel:"); println(vel_polar);
   // To reflect the angle of the particles on this surface, we subtract their
   // current angle from two times the tangent.
   //(http://www.petercollingridge.co.uk/pygame-physics-simulation/collisions)
   
   float cylinder_wall_angle = ball_polar.angle - PI/2;
   println("Wall Angle:" + radtodeg(cylinder_wall_angle));
   
   float reflection_angle = vel_polar.angle - cylinder_wall_angle;
   
   vel_polar.angle = cylinder_wall_angle - 2 * reflection_angle;
   
   //vel_polar.angle = 2*cylinder_wall_angle -  vel_polar.angle;   
   
   velocity = Util.poltocar(vel_polar);
   print("New Vel:");  println(vel_polar);
   
   bounce = true;
   }
   */
  if (bounce == true) {
    center.add(velocity);
    //sphere.setSize(size * 2);
  }

  /*
  PPolar c = Util.cartopol(center);
   if (Util.cartopol(center).amplitude > Util.MAX_AMPITUDE) {
   println("OUT OF BOUNDS");
   println(c);
   println(center);
   //exit();
   //center = new PVector(0,0,0);
   }
   */
  sphere.setCenter(center);
}


void draw() {  
  boolean needExport = false;
  lights();
  background(0);

  float cameraTopBotAngle = (float)Util.linearmap(mouseY, 0, SCREEN_HEIGHT, -Math.PI/4, Math.PI/4);
  camera(Util.linearmap(mouseX, 0, SCREEN_WIDTH, 150, -150), // eye X
    sin(cameraTopBotAngle)*220f, // eye Y
    cos(cameraTopBotAngle)*220, // eye Z
    0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
    0.0f, 1.0f, 0.0f); // upX, upY, upZ

  int now = millis();
  if (TimeDiff(g_last, now) >= ANIMATE_INTERVAL_MS) {
    Animate();
    needExport = true;
    g_last = now;
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
  
  //g_display.dot(0,50,50); // Low Front Reference Pixel
  //g_display.dot(-50,50,0); // Low Left ( on X plane) Reference Pixel
  //g_display.dot(0,50,-50); // Low Far Back ( on Z plane) Reference Pixel
  g_display.dot(50,50,0); // Low Right ( on X plane) Reference Pixel

  // Draw Snow Flakes
  stroke(255);
  sphere.draw();

  if (needExport) g_display.export(); // Send to POV Display
}

