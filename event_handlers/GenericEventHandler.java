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
 * Last Modified: Jul 26, 2012 02:26:42 PM
 */

package event_handlers;

import gui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import questionaires.Questionaire;
import questionaires.QuestionaireAggregator;
import word_interfaces.WordOutputDocument;
import docimports.ImportUtilities;

/**
 * 
 * This class defines common functionality for all of the EventHandler classes.
 * It is an abstract class, so it cannot be instantiated directly. You need to
 * provide an instance of one of the other EventHandlers, which will inherit all
 * of this functionality. It implements the ActionListener interface, so that
 * the GUI will automatically invoke the actionPerformed method when a menu item
 * is clicked. It implements the Runnable interface so that threads can be
 * created out of this class.
 * 
 */
public abstract class GenericEventHandler implements ActionListener, Runnable
{
	// This is a collection of all of the questionaire files.
	protected ArrayList<Questionaire> questions;
	private String directory = null;

	/**
	 * This constructor aggregates all of the questionaire files and stores them
	 * in 'questions'.
	 * 
	 * @param directory
	 *            The string which represents the relative or absolute path
	 * @throws IOException
	 *             Throws an exception if there is a problem looping through the
	 *             directory.
	 */
	public GenericEventHandler(String directory_) throws IOException
	{
		directory = directory_;
	}

	/**
	 * This method executes when a menu item is clicked.
	 * 
	 * @param e
	 *            The event that triggers the method call.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Uses the QuestionaireAggregator class to create the ArrayList
		// which contains all of the questionaires to be used.
		try
		{
			questions = QuestionaireAggregator.aggregate(directory);
		}
		catch (IOException e1)
		{
			GUI.getInstance().finishWithError(e1);
		}
		// Creates a new thread to run the code so that the GUI IO will work
		// correctly.
		new Thread(this).start();
	}

	/**
	 * This method is what executes when the thread starts. It executes the
	 * dialog with the user, and stores all of the necessary information in the
	 * output file.
	 */
	@Override
	public void run()
	{
		// The output document object.
		WordOutputDocument outputDoc = null;
		// Allow the user to input text.
		GUI.getInstance().setEditable();
		GUI.getInstance().setBoldFont();
		GUI.getInstance().setHeader("");
		// Hide the menu so the user can't spawn another thread while this one
		// is executing.
		GUI.getInstance().hideMenu();
		GUI.getInstance().cls();

		// @formatter:off --> turns off my automatic formatter
		// Tells the user to choose a location for the output file
		JOptionPane.showMessageDialog(new JFrame(), 
							"Please choose a location for the output file",
							null, 
							JOptionPane.INFORMATION_MESSAGE);

		// Create an output document
		outputDoc = new WordOutputDocument(ImportUtilities.getOutputFile());

		// Loop through all of the questionaire files.
		for (int ii = 0; ii < questions.size(); ++ii)
		{
			// Clear the screen for each section and add a section header.
			GUI.getInstance().cls();
			GUI.getInstance().setHeader(questions.get(ii).getHeader());
			outputDoc.createHeader(questions.get(ii).getHeader());
			try
			{
				// Executes the dialog with the user.
				questions.get(ii).collectInfo(outputDoc);
			}
			catch (FileNotFoundException fileException)
			{
				JOptionPane.showMessageDialog(new JFrame(), 
												fileException.getMessage(),
												"Critical Error", 
												JOptionPane.ERROR_MESSAGE);

				System.err.println("File " + questions.get(ii).getFilename() + 
									" could not be found. System will continue without it.");
			}
			catch (IOException ioException)
			{
				JOptionPane.showMessageDialog(new JFrame(), 
												ioException.getMessage(),
												"Critical Error", 
												JOptionPane.ERROR_MESSAGE);

				System.err.println("An error occured while communicating the the user. " + 
									"System will continue. Check output file after termination.");
			}

			// Creates a new section in the output document.
			outputDoc.createParagraph();
		}
		try
		{
			// Write all of the necessary information to the output document.
			outputDoc.write();
		}
		catch (IOException e)
		{			
			GUI.getInstance().finishWithError(
								new IOException("An error occured while writing to output file. " +
										"Please make sure it is not open in another program " +
										"and try again.", e));
		}

		// Exit the application.
		GUI.getInstance().finish();
	}
}
