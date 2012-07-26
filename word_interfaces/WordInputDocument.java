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
 * Last Modified: Jul 20, 2012 4:11:20 PM
 */

package word_interfaces;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 
 * This class is designed to represent the questionaire files. It provides a
 * simple interface to extract all of the text from the file.
 * 
 */
public class WordInputDocument
{
	// The name of the questionaire file. Only needed for the accessor method.
	private String filename = "";
	// An intermediate object used to build the extractor.
	private XWPFDocument fileLink = null;
	// The text extractor.
	private XWPFWordExtractor document = null;

	/**
	 * The constructor creates a series of objects that build on each other to
	 * make the extractor.
	 * 
	 * @param filename_
	 *            The name of the input file, which binds to the 'filename'
	 *            field.
	 * @throws IOException
	 *             Throws an IOException if there is a problem creating the
	 *             XWPFDocument around the FileInputStream.
	 * @throws FileNotFoundException
	 *             Throws a FileNotFoundException if there is a problem creating
	 *             the FileInputStream.
	 */
	public WordInputDocument(String filename_) throws IOException, FileNotFoundException
	{
		filename = filename_;
		FileInputStream instream = new FileInputStream(filename_);
		fileLink = new XWPFDocument(instream);
		document = new XWPFWordExtractor(fileLink);
	}

	/**
	 * Extracts ALL of the text from the questionaire file.
	 * 
	 * @return A single string that contains the questionaire file in its
	 *         entirety. It needs to be parsed with a BufferedReader.
	 */
	public String getQuestions()
	{
		return document.getText();
	}

	/**
	 * Accessor for the name of the questionaire file. Used mostly for error
	 * handling.
	 * 
	 * @return A string containing the name of the file.
	 */
	public String getFilename()
	{
		return filename;
	}
}
