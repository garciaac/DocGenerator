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
 * Last Modified: Aug 7, 2012 10:45:30 AM
 */

package questionaires;

import gui.GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JCheckBox;

import word_interfaces.WordInputDocument;
import word_interfaces.WordOutputDocument;

/**
 * 
 * This class represents a questionaire to be included in the document. Each
 * questionaire is linked to one Word document, and results in one titled
 * section of the output document. The QuestionaireAggregator class is
 * responsible for combining multiple questionaires to be used by the
 * EventHandlers. The format of the questionaire files is very important because
 * this application depends on knowing where the different components of the
 * file are located. See the README for additional information.
 * 
 */
public class Questionaire
{
	// Creates an object to represent the questionaire file.
	private WordInputDocument questions = null;
	// The header value for this questionaire. It will be inserted into the
	// output file to denote the section title associated with this
	// questionaire.
	private String header = "";
	// This is the object used to read through the contents of the input file.
	private BufferedReader input = null;

	/**
	 * This is the constructor for the questionaire. It constructs a
	 * WordInputDocument object that represents the Word document containing the
	 * questions, and then initializes the BufferedReader and reads the section
	 * title contained in the document. Again, see README for formatting
	 * guidelines for the questionaire files.
	 * 
	 * @param filename
	 *            The name of the file containing the questions.
	 * @throws IOException
	 *             Passes on the IOException if it is raised by creating the
	 *             WordInputDocument object.
	 */
	public Questionaire(String filename) throws IOException
	{
		try
		{
			// Create the object to represent the questionaire file
			questions = new WordInputDocument(filename);
		}
		catch (IOException e)
		{
			throw new IOException("There was a problem accessing an input file. Please make " +
										"sure none of them are open in another program.", e);
		}

		// Initializes the BufferedReader. It is created around a StringReader,
		// which is created around the plaintext data extracted form the Word
		// file via questions.getQuestions(). All fonts, colors, sizes and other
		// formatting details are ignored. Only the line breaks, text, and
		// indents are preserved.
		input = new BufferedReader(new StringReader(questions.getQuestions()));
		// Reads the header which should be on the first line.
		header = input.readLine();
		// Eat the blank line under the header.
		input.readLine();
	}

	/**
	 * This method executes the dialog with the user. For each question, it asks
	 * the question, and then records the user's answer, and then writes both
	 * the question and answer to the output file.
	 * 
	 * @param output
	 *            The output Word file, which is the final deliverable after the
	 *            application is done executing
	 * @throws IOException
	 *             Throws an IOException in case there is a read failure on the
	 *             BufferedReader object.
	 */
	public void collectInfo(WordOutputDocument output) throws IOException
	{
		GUI.getInstance().setDefaultFont();
		// Read the first question.
		String question = input.readLine();
		// Keeps asking and reading questions until the file ends or a blank
		// line is detected. See README for formatting guidelines for Word
		// files.
		while (question != null && question.length() != 0)
		{
			// Clear any previous text.
			GUI.getInstance().cls();
			// Clear out any checkboxes on screen.
			GUI.getInstance().clearOptions();
			// Record question and read options.
			output.createQuestion(question);
			GUI.getInstance().out(question);
			String option = input.readLine();
			// Loop until there are no more options.
			while (option != null && option.length() != 0)
			{
				// Add each option to the GUI.
				GUI.getInstance().addOption(new JCheckBox(option));
				// Read the next one.
				option = input.readLine();
			}
			// Wait for user to press enter. TODO --> make this into a submit button
			GUI.getInstance().getInput();
			// Add all of the selected options to the output file.
			for (int ii=0; ii<GUI.getInstance().getSelectedOptions().size(); ++ii)
				output.createAnswer(GUI.getInstance().getSelectedOptions().get(ii) + "\n");

			// Read next question.
			question = input.readLine();
			// Print a blank line
			GUI.getInstance().out("");
		}

		// Clear the output area in preparation for a new message.
		GUI.getInstance().cls();
		GUI.getInstance().clearOptions();
		GUI.getInstance().setBoldFont();

		// Additional information that should be displayed to the user, but
		// isn't a question is displayed here. See README for formatting
		// guidelines of input Word documents.
		String additionalInfo = "";
		// Loop until there are no more lines in the document. If there is no
		// additional information after the questions in the file, then this
		// loop won't execute.
		while ((additionalInfo = input.readLine()) != null)
		{
			GUI.getInstance().out(additionalInfo);
		}

		GUI.getInstance().out("Press enter to contiue.");
		GUI.getInstance().getInput();
		// Close streams to the input document.
		input.close();
	}

	/**
	 * Accessor method for the 'header' field.
	 * 
	 * @return The section title.
	 */
	public String getHeader()
	{
		return header;
	}

	/**
	 * Accessor method for the 'filename' field. It is mainly used for providing
	 * error information in exception handling.
	 * 
	 * @return The questionaire file name.
	 */
	public String getFilename()
	{
		return questions.getFilename();
	}
}
