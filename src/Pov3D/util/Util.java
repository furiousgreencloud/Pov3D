package Pov3D.util;

import java.awt.Point;

import processing.core.PApplet;
import processing.core.PVector;

public class Util extends ProcessingObject{

	// GLOBALS
	
	public static int ROWS = 50;
	public static int COLS = 50;
	public static int s_sliceCount = 35;
	public static int MAX_AMPITUDE = 71; // 70.71067811865476 = sqrt(50^2+50^2)
	public static int PORT_RX = 6667;
	public static int PORT_TX = 6666;
	public static int MAX_SLICES = 150;
	public static float HALF_WORLD_DIM = 50f;
	
	public static double TWOPI = Math.PI*2;
	
	public static PVector min = new PVector(-HALF_WORLD_DIM,  HALF_WORLD_DIM,  -HALF_WORLD_DIM);
	public static PVector max = new PVector( HALF_WORLD_DIM, -HALF_WORLD_DIM,  HALF_WORLD_DIM); 
	/* -HALF_WORLD_DIM in previous line because UP is negative in Processing */
	
	// UTIL FUNCTIONS

	public static int linearmap(int x, int in_min, int in_max, int out_min,
			int out_max) {
		return ((x - (in_min)) * ((out_max) - (out_min))
				/ ((in_max) - (in_min)) + (out_min));
	}

	public static float linearmap(float x, float in_min, float in_max,
			float out_min, float out_max) {
		return ((x - (in_min)) * ((out_max) - (out_min))
				/ ((in_max) - (in_min)) + (out_min));
	}

	public static double linearmap(double x, double in_min, double in_max,
			double out_min, double out_max) {
		return ((x - (in_min)) * ((out_max) - (out_min))
				/ ((in_max) - (in_min)) + (out_min));
	}
	
	
	public static PVector linearmap(PVector p, PVector loIn, PVector hiIn, PVector lowOut,PVector hiOut) {
		return new PVector(
				linearmap(p.x,loIn.x,hiIn.x,lowOut.x,hiOut.x),
				linearmap(p.y,loIn.y,hiIn.y,lowOut.y,hiOut.y),
				linearmap(p.z,loIn.z,hiIn.z,lowOut.z,hiOut.z)
				);
	}
	
	
	
	public static float hypot(float x, float y) {
		return PApplet.sqrt(x * x + y * y);
	}
	
	public static int hypot(int x, int y) {
		return (int)(PApplet.sqrt(x * x + y * y) + 0.5);
	}
	
	public static PVector poltocar(PPolar p) {
		float x = (float)(p.amplitude * Math.cos(p.angle));
		float z = (float)(-p.amplitude * Math.sin(p.angle));
		return new PVector(x,p.y,z);
	}

	public static PPolar cartopol(PVector v) {
		 float amp = hypot(v.x, v.z);
		 float ang =(float)Math.atan2(v.x, v.z);
		 return new PPolar(amp, ang, v.y);
	}
	
	public static int TimeDiff(int start, int end) {
		if (end >= start) {
			return end - start;
		} else {
			return Integer.MAX_VALUE - start + end;
		}
	}
	
	public static void Assert(boolean needsToBeTrue,  String helpText) {
		if (!needsToBeTrue) {
			PApplet.println(helpText);
			assert(false);
		}
	}
	
	public static void Warn(boolean shouldBeTrue,  String helpText) {
		if (!shouldBeTrue) {
			PApplet.println(helpText);
		}
	}
}
