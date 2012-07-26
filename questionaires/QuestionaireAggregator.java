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
 * Last Modified: Jul 20, 2012 3:52:31 PM
 */

package questionaires;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * This class is responsible for combining all of the questionaire files in a
 * directory structure into a single container. This enables users to simply add
 * or delete questionaires as needed without having to change any of the code.
 * 
 */
public class QuestionaireAggregator
{
	/**
	 * This method aggregates all of the questionaire files in a directory
	 * structure and returns an array of them.
	 * 
	 * @param directory
	 *            The path to the root directory that contains the questionaire
	 *            files.
	 * @throws IOException
	 *             Throws an IOException if there is an error looping through
	 *             the directory.
	 * @return An ArrayList full of Questionaire objects representing the files
	 *         in the directory structure.
	 */
	public static ArrayList<Questionaire> aggregate(String directory) throws IOException
	{
		// Creates a File object around the root directory
		File files = new File(directory);
		// Initializes the ArrayList so that questionaires can be added to it.
		ArrayList<Questionaire> container = new ArrayList<Questionaire>();

		// Only executes if the File object is a directory.
		if (files.isDirectory())
		{
			try
			{
				// Adds the files
				addFiles(files, container);
			}
			catch (IOException e)
			{
				throw new IOException("There was an error loading the input files in folder "
									+ directory, e);
			}
		}
		// Throw an exception if the File object is not a directory.
		else
			throw new IllegalArgumentException("The path provided is not a directory.");

		return container;
	}

	/**
	 * This method recursively searches the directory structure for questionaire
	 * files and adds them all to the container.
	 * 
	 * @param directory
	 *            The root directory in the form of a File object.
	 * @param container
	 *            The container to add all of the questionaire files to.
	 * @throws IOException
	 *             Passes on the IOException if it is raised when creating the
	 *             Questionaire object.
	 */
	private static void addFiles(File directory, ArrayList<Questionaire> container)
						throws IOException

	{
		// Creates an array of File objects representing all of the files in the
		// directory.
		File[] files = directory.listFiles();
		for (int ii = 0; ii < files.length; ++ii)
		{
			// Recursive call is the current file is a directory.
			if (files[ii].isDirectory())
				addFiles(files[ii], container);
			else
			{
				// Add a Questionaire object using the file path contained in
				// the File object.
				container.add(new Questionaire(files[ii].getCanonicalPath()));
			}
		}
	}
}
