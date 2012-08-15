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
 * Last Modified: Aug 15, 2012 1:38:30 PM
 */

package gui;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * 
 * This class defines all of the IO functionality needed by the GUI class. The
 * reason for this class is that GUI IO in Java is very tricky in a class
 * hierarchy like this one. When a menu item is clicked, the GUI thread will
 * block IO until control is returned to the GUI's scope. The IO functions here
 * mostly push a runnable object onto the GUI scope's queue to be executed when
 * the GUI is ready. Control is returned to the GUI by running application code
 * in a separate thread (see GenericEventHandler), and then that thread calls
 * these methods, which push the runnables to the GUI, who is ready to execute
 * since it is not waiting for the application code to return.
 * 
 */
public abstract class GUIUtilitiesIO
{
	// The output area of the GUI.
	protected JTextArea questionArea = null;
	// The user input area of the GUI.
	// NOTE: This is no longer visible to the user. It is controlled by a robot.
	// See submit() method.
	protected JTextArea answerArea = null;
	// The submit button that moves the user from one question to the next.
	protected JButton submit = null;
	// The area where the checkboxes are located.
	protected OptionPanel options = null;
	// The window where all the components reside.
	protected JFrame window = null;
	// Keeps track of whether or not the input in answerArea is ready to be
	// read.
	protected boolean isInputReady = false;
	// The object that listens for the event indicating the input is ready to be
	// read.
	private KeyListener listener = null;

	/**
	 * This method assigns an object to the 'listener' field.
	 * 
	 * @param listener_
	 *            The key listener that keeps track of keyboard events and
	 *            handles them.
	 */
	public void setKeyListener(KeyListener listener_)
	{
		listener = listener_;
	}

	/**
	 * This method reads the input from the user.
	 * 
	 * @param prompt
	 *            A message to display to the user prompting for what
	 *            information to provide.
	 * @return The user's input. NOTE: This is controlled by a robot now.
	 */
	public synchronized String getInput(String prompt)
	{
		String tmp = null;
		answerArea.addKeyListener(listener);
		// Display the prompt if there is one.
		if (prompt != null)
		{
			this.out(prompt);
		}
		// Waits until isInputReady is set to true. This is done by the key
		// listener.
		while (!isInputReady)
		{
			tmp = answerArea.getText();
		}
		// Clears the text in the answer area and indicates the input is not
		// ready to be read yet. Even though the answerArea is controlled by a
		// robot, it STILL NEEDS TO BE CLEARED or else questions will get
		// skipped because newlines are still in the buffer.
		answerArea.setText("");
		isInputReady = false;
		return tmp;
	}

	/**
	 * This method reads input without a prompt. This is used mostly for pausing
	 * the application while the user reads a message.
	 * 
	 * @return The user's input.
	 */
	public synchronized String getInput()
	{
		return this.getInput(null);
	}

	/**
	 * This method displays text to the output area of the GUI.
	 * 
	 * @param str
	 *            The message to display.
	 */
	public void out(final String str)
	{
		// Creates a Runnable and tells the GUI to execute it when the GUI is
		// ready.
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				questionArea.setText(questionArea.getText() + "\n" + str);
			}
		});
	}

	/**
	 * This method clears the question area of any text.
	 */
	public void cls()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				questionArea.setText("");
			}
		});
	}

	/**
	 * Adds a new checkbox option to the window.
	 * 
	 * @param option
	 *            The checkbox to add.
	 */
	public void addOption(final JCheckBox option)
	{
		// Creates a Runnable and tells the GUI to execute it when the GUI is
		// ready.
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				options.add(option);
				window.revalidate();
			}
		});
	}

	/**
	 * This method adds an editable field to the options menu.
	 * 
	 * @param option
	 *            The checkbox to add. The editable text field will be supplied
	 *            by the OptionPanel class.
	 */
	public void addEditableField(final JCheckBox option)
	{
		// Creates a Runnable and tells the GUI to execute it when the GUI is
		// ready.
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				options.addEditableField(option);
				window.revalidate();
			}
		});
	}

	/**
	 * Deletes all of the current checkboxes form the screen.
	 */
	public void clearOptions()
	{
		// Creates a Runnable and tells the GUI to execute it when the GUI is
		// ready.
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				options.clear();
				window.repaint();
			}
		});
	}

	/**
	 * This method gives access to the user's selected options.
	 * 
	 * @return An array of strings that the user selected.
	 */
	public ArrayList<String> getSelectedOptions()
	{
		return options.getSelectedOptions();
	}

	/**
	 * This method redirects System.out and System.err to the GUI. If any code
	 * included System.out.print... then it would print to the GUI instead of
	 * standard output.
	 */
	protected void redirectStreams()
	{
		// Creates a new output stream and overrides the write methods to output
		// to the GUI.
		OutputStream outs = new OutputStream()
		{
			@Override
			public void write(int b) throws IOException
			{
				GUIUtilitiesIO.this.out(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException
			{
				GUIUtilitiesIO.this.out(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException
			{
				GUIUtilitiesIO.this.out(new String(b, 0, b.length));
			}
		};

		// Redirect the System.out and System.err streams to point to outs.
		System.setOut(new PrintStream(outs, true));
		System.setErr(new PrintStream(outs, true));
	}

	/**
	 * This method sets the font in the question area to be bold and bigger. It
	 * is used to display the important messages at the end of the questionaire
	 * files located in the practice directories.
	 */
	public void setBoldFont()
	{
		questionArea.setFont(new Font("Times New Roman", Font.BOLD, 20));
	}

	/**
	 * This method sets the font back to the default.
	 */
	public void setDefaultFont()
	{
		questionArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
	}

	/**
	 * This method is called when the submit button is pressed. The original
	 * design was based around the user pressing enter in an editable text field
	 * for each question. This method works around having to redesign that code
	 * by having a robot press enter in the same field where the user would
	 * have. NOTE: The editable text field "answerArea" is invisible to the
	 * user.
	 * 
	 * @throws AWTException
	 *             When the creation of the Robot object fails.
	 */
	protected void submit() throws AWTException
	{
		// Tell the OptionPanel class that the user pressed submit.
		options.submit();
		// Creates a robot and presses the enter key.
		Robot robot = new Robot();
		answerArea.requestFocus(); // Puts the focus of the GUI in the
									// answerArea.
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
}
