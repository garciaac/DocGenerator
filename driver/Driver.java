/* PRESIDIO CONFIDENTIAL
 * __________________
 * 
 * Copyright (c) [2012] Presidio Networked Solutions 
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Presidio Networked Solutions. The intellectual and 
 * technical concepts contained herein are proprietary to Presidio Networked 
 * Solutions and may be covered by U.S. and Foreign Patents, patents in process, 
 * and are protected by trade secret or copyright law. Dissemination of this 
 * information or reproduction of this material is strictly forbidden unless 
 * prior written permission is obtained from Presidio Networked Solutions.
 * 		
 * Author: 	Andrew Garcia
 * Email:	agarcia@presido.com
 * Last Modified: Jul 20, 2012 2:04:30 PM
 */

package driver;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * This is the class that actually runs the application. Most of the
 * functionality is encapsulated in the GUI and EventHandler classes.
 * 
 */
public class Driver
{
	public static void main(String[] args)
	{
		try
		{
			// Grabs the singleton instance of the GUI. It is automatically
			// shown on screen.
			GUI.getInstance();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(new JFrame(), 
										e.getMessage(), 
										"Critical Error",
										JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}
}
