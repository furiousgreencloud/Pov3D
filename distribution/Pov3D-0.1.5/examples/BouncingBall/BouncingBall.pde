/* 
 * Classic Bouncing Ball Sample
 *
 * for POV 3D Display
 *
 * http://furiousgreencloud.com/pov
 *
 * Brady Marks 2015
 */

// CONSTANTS

int FRAME_RATE = 12;
String BEAGLE_IP = "10.10.2.99"; // WIFI IP of Display
int BEAGLE_PORT = 0x1A0A;        // 6666
int SLICES = 90;                 // 90 - 150, more slice makes a denser render on the display

// LINBRARIES / IMPORTS

import Pov3D.library.*;
import Pov3D.display.*;
import Pov3D.util.*;
import Pov3D.shape.*;

// GLOBALS

POVDisplay g_display = null; // Display "Sigleton"
int g_last = 0;

Sphere sphere = null;        // Our Ball
PVector center = null;       // the Center Point of the Ball
PVector velocity;            // Speed and Direction of Ball
int maxSpeed = 5;            // Max Magnitude of velocity
int size = 12;               // 7 - 12 Work Nicely

void setup() {
  size(640, 480, P3D);
  fill(204);
  frameRate(FRAME_RATE); // fps

  println("setup");
  Pov3DLibrary lib = new Pov3DLibrary(this); 
  ProcessingObject.setPApplet(this);
  g_display = new POVDisplay(BEAGLE_IP, SLICES /* number of slices */ );
  POVObject.setDisplay(g_display); // sets the display for all the POV Objects 
                                   // like sphere and line

  center = new PVector();
  
  velocity = new PVector(
    random(-1, 1), 
    random(-1, 1), 
    random(-1, 1));

  velocity.normalize(); // make random vector 'unity' size (i.e. having magnitude of 1)
  velocity.mult(maxSpeed); // scale up to maxSpeed magnitude

  sphere = new Sphere(center, size, Sphere.DrawMode.OUTLINE);
}

void Animate() {
  boolean bounce = false; // remember if we bounced or not
  center.add(velocity); // move center by adding 'velocity' vector to current possition
  sphere.setSize(size);


  /* lazy bouncing, we bounce against a cube, not a cylinder, this is bad form and results
     in the ball going outside of the display in the "corners", which is instuctional but silly
  */
  
  if (center.y - size <= POVDisplay.MIN || center.y + size >= POVDisplay.MAX) {
    velocity.y *= -1; // flip direction of velocity vector, on Y plane
    bounce = true;
  }

  if (center.z -size <= POVDisplay.SAFE_MIN || center.z + size >= POVDisplay.SAFE_MAX) {
    velocity.z *= -1;  // flip direction of velocity vector, on Z plane
    bounce = true;
  }

  if (center.x - size <= POVDisplay.SAFE_MIN || center.x + size  >= POVDisplay.SAFE_MAX) {
    velocity.x *= -1; // flip direction of velocity vector, on X plane
    bounce = true;
  }


  /*
   Attempt at Realy ball and cylinder interaction
  
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
    // if we bounced we went too far, so come back in
    center.add(velocity);
  }

  sphere.setCenter(center); // update position
}


void draw() {  
  lights();
  background(0);

  // Camera Move on mouse position
  float cameraTopBotAngle = (float)Util.linearmap(mouseY, 0, height, -Math.PI/4, Math.PI/4);
  camera(Util.linearmap(mouseX, 0, width, 150, -150), // eye X
    sin(cameraTopBotAngle)*220f, // eye Y
    cos(cameraTopBotAngle)*220, // eye Z
    0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
    0.0f, 1.0f, 0.0f); // upX, upY, upZ

  Animate(); // Move the Ball

  g_display.clear(); // Clear the Volume, like bacground(0), but the POV version

  // Draw Static Cursor Points (in Processing, NOT on the POV Display)
  stroke(255, 0, 0);

  point(-50, -50, 50);
  point(-50, 50, 50);
  point( 50, -50, 50);
  point( 50, 50, 50);

  point(-50, -50, -50);
  point(-50, 50, -50);
  point( 50, -50, -50);
  point( 50, 50, -50);
  
  /*
  Draw Reference Points in the POV Display
  g_display.dot(0,50,50); // Low Front Reference Pixel
  g_display.dot(-50,50,0); // Low Left ( on X plane) Reference Pixel
  g_display.dot(0,50,-50); // Low Far Back ( on Z plane) Reference Pixel
  g_display.dot(50,50,0); // Low Right ( on X plane) Reference Pixel
  */

  stroke(255);
  sphere.draw(); // draw the sphere in the POV Display Volume
  g_display.export(); // Send Current Volume to POV Display
}
