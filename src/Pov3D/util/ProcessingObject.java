package Pov3D.util;

import processing.core.PApplet;

/**
 * A Static Class from which objects are derived if the need access to the Processing PApplet
 * 
 * i.e. they are Processing Dependent Objects
 * 
 * @author Brady Marks (furiousgreencloud@gmail.com)
 * 
 */
public class ProcessingObject {

	public static PApplet pApplet;
	
	public static void setPApplet(PApplet app) {
		ProcessingObject.pApplet = app;
	}
}
