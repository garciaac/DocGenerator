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
 * Last Modified: Jul 27, 2012 8:40:23 AM
 */

package docimports;

import gui.GUI;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

/**
 * 
 * This class provides some utility function that produces the dialog window
 * where the user chooses a location for the output file.
 * 
 */
public class ImportUtilities
{
	/**
	 * This method obtains an output file by providing the user with a dialog
	 * window.
	 * 
	 * @return A string containing the path to the desired file.
	 */
	public static String getOutputFile()
	{
		// Builds a JFileChooser, which will provide the user with a window to
		// navigate the file system.
		JFrame fileChooserWindow = new JFrame();
		JFileChooser fc = new JFileChooser();
		// Doesn't allow the user to choose directories or multiple files.
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);

		File outputFile = null;
		// Initializes the variable that indicates whether the user wants to
		// overwrite an existing file.
		int overwrite = JOptionPane.YES_OPTION;
		// Loop until the user picks a non-existing file or overwrites an
		// existing one.
		do
		{
			// Get the output file object
			outputFile = getOutputFileLocation(fc, fileChooserWindow);
			// If the file already exists, prompt the user if he/she wants to
			// overwrite it.
			if (outputFile.exists())
			{
				// JOptionPane encapsulates the process of obtaining the user's
				// choice.
				overwrite = JOptionPane.showConfirmDialog(new JFrame(),
									"File already exists. Do you want to overwrite it?", null,
									JOptionPane.INFORMATION_MESSAGE);
				// Exit loop if the user wants to overwrite the file.
				if (overwrite == JOptionPane.YES_OPTION)
					break;
			}
		} while (outputFile.exists());

		// Return the full path to the file without its extension.
		return FilenameUtils.removeExtension(outputFile.getAbsolutePath());
	}

	/**
	 * This method prompts the user to choose a location for an output file.
	 * 
	 * @param fc
	 *            The FileChooser object that handles the user interactions.
	 * @param fileChooserWindow
	 *            The window which will contain the FileChooser.
	 * @return The file that the user selects.
	 */
	private static File getOutputFileLocation(JFileChooser fc, JFrame fileChooserWindow)
	{
		// Initializes the variable holding the value associated with the button
		// that the user clicked on in the window.
		int buttonPressed = JOptionPane.NO_OPTION;
		// Loop until the user selects something other than cancel (in this case
		// 'save');
		do
		{
			// Display a file-choosing dialog window and determine which button
			// the user clicks on.
			buttonPressed = fc.showSaveDialog(fileChooserWindow);
			// If the user clicked 'Cancel' then confirm that the user wants to
			// exit.
			if (buttonPressed == JFileChooser.CANCEL_OPTION)
			{
				int cancelled = JOptionPane.showConfirmDialog(new JFrame(),
									"Are you sure? The application will exit.", null,
									JOptionPane.INFORMATION_MESSAGE);

				// If the user still wants to cancel, then exit the application.
				if (cancelled == JOptionPane.YES_OPTION)
					GUI.getInstance().finishWithError(
										new Exception("User manually exited application"));
			}
			// Loop until the user clicks the save button.
		} while (buttonPressed == JFileChooser.CANCEL_OPTION);

		// Return the selected file in the form of a File object.
		return fc.getSelectedFile();
	}
}
