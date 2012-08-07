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
 * Last Modified: Aug 7, 2012 10:57:14 PM
 */

package word_interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * 
 * This class represents the output document, which is the final deliverable. It
 * contains all of the section titles, questions and answers when the
 * application terminates.
 * 
 */
public class WordOutputDocument
{
	// Name of the output file. Only needed for the write() method.
	private String filename = "";
	// Object which will store all of the text to be written to the output file.
	XWPFDocument document = null;

	/**
	 * The constructor initializes the 'filename' and 'document' fields, and
	 * then creates a paragraph in the document object so that text is ready to
	 * be written.
	 * 
	 * @param filename_
	 *            Name to bind to the 'filename' field.
	 */
	public WordOutputDocument(String filename_)
	{
		filename = filename_;
		document = new XWPFDocument();
		// Creates an initial paragraph so that getLastParagraph() won't throw
		// an error when the first text is written.
		document.createParagraph();
	}

	/**
	 * This method outputs all of the stored information in the document object
	 * into a Word file.
	 * 
	 * @throws FileNotFoundException
	 *             Throws FileNotFoundException if there is a problem creating
	 *             the FileOutputStream.
	 * @throws IOException
	 *             Throws an IOException if there is a problem writing to the
	 *             FileOutputStream.
	 */
	public void write() throws FileNotFoundException, IOException
	{
		document.write(new FileOutputStream(new File(filename + ".docx")));
	}

	/**
	 * Creates a new paragraph in document object.
	 * 
	 * @return A reference to the paragraph object.
	 */
	public XWPFParagraph createParagraph()
	{
		return document.createParagraph();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	//
	// The only real difference in the create() methods is the formatting for
	// the output document.
	//
	// //////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method adds a title to a section in the document object.
	 * 
	 * @param header
	 *            The string which represents a section title. Usually this is
	 *            the first line of a questionaire file.
	 */
	public void createHeader(String header)
	{
		// Adds text to the last paragraph in the document object.
		XWPFRun text = document.getLastParagraph().createRun();
		text.setText(header);
		text.addCarriageReturn(); // Need to manually add a newline
		text.setFontSize(20);
		text.setBold(true);
		text.setItalic(true);
		text.setColor("000066"); // Hexadecimal string "RRGGBB"
	}

	/**
	 * This method adds a question to the document object.
	 * 
	 * @param question
	 *            The string which represents the question to be asked.
	 */
	public void createQuestion(String question)
	{
		this.createParagraph();
		XWPFRun text = document.getLastParagraph().createRun();
		text.setText(question);
		text.addCarriageReturn();
		text.setFontSize(14);
		text.setBold(true);
	}

	/**
	 * This method adds an answer to the document object.
	 * 
	 * @param answer
	 *            The string which represents the answer to a question.
	 */
	public void createAnswer(String answer)
	{
		XWPFRun text = document.getLastParagraph().createRun();
		text.setText(answer);
		text.setFontSize(12);
		text.addCarriageReturn();
	}
}
