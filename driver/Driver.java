/** PRESIDIO CONFIDENTIAL
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

import gui.GUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * 
 * This is the class that actually runs the application. Most of the
 * functionality is encapsulated in the other classes.
 * 
 */
public class Driver
{
	public static void main(String[] args)
	{
		try
		{
			// Sets the look and feel of the GUI windows to the user's native
			// system.
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			// If it fails, just ignore the errors and continue with the default
			// Java look and feel.
			catch (Exception e)
			{}

			// Grabs the singleton instance of the GUI. It is automatically
			// displayed on screen, and the rest of the application is event
			// driven by the GUI.
			GUI.getInstance();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Critical Error",
								JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}
}
