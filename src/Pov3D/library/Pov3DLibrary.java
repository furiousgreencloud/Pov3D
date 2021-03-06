/**
 * This is the custom Processing Library for the VIVO+Brady Marks
 * 3D Volumetric Display. 
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		##author##
 * @modified	##date##
 * @version		##version##
 */

package Pov3D.library;


import Pov3D.util.POVObject;
import Pov3D.util.ProcessingObject;
import processing.core.PApplet;

/**
 * This is a template class there is nothing much of interest here, if just links
 * the PApplet to the ProcessingObject static class that way we avoid a APpplet global
 * and let all the derived classes that are ProcessingObject use the APapplet when they need it
 * 
 *  POVDisplay, Row, Slice and YPoint are all ProcessingObject
 *  
 *  @author Brady Marks (furiousgreencloud@gmail.com)
 */

public class Pov3DLibrary {
	
	// myParent is a reference to the parent sketch
	public static PApplet myParent;

	/*
	 * int myVariable = 0; 
	 */
	
	public final static String VERSION = "##version##";

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public Pov3DLibrary(PApplet theParent) {
		myParent = theParent;
		ProcessingObject.setPApplet(myParent);
		welcome();
	}
	
	private void welcome() {
		System.out.println("##name## ##version## by ##author##");
	}
		
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * 
	 * @param theA
	 *          the width of test
	 * @param theB
	 *          the height of test
	
	public void setVariable(int theA, int theB) {
		myVariable = theA + theB;
	}
	 */
	
	/**
	 * 
	 * @return int
	 
	public int getVariable() {
		return myVariable;
	}
	 */
}

