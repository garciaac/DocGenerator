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
 * Last Modified: Aug 15, 2012 1:39:17 PM
 */

package gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import event_handlers.DataCenterHandler;
import event_handlers.NetworkInfrastructureHandler;
import event_handlers.SecurityHandler;
import event_handlers.UnifiedCommunicationsHandler;
import event_handlers.WirelessHandler;

/**
 * 
 * This class defines the GUI that is displayed to the user. It inherits from
 * the GUIUtilitiesIO class, which handles all of the IO to and from the GUI.
 * This class is implemented as a singleton, which means that throughout the
 * life of the application, it is guaranteed that there is only ever one single
 * instance of this class.
 * 
 */
public class GUI extends GUIUtilitiesIO
{
	/* @formatter:off --> turns off my automatic formatter */
	//////////////////////////////////////////////////////////////
	//			Fields Inherited from GUIUtilitiesIO			//
	//////////////////////////////////////////////////////////////
	// protected JTextArea questionArea = null;					//
	// protected JTextArea answerArea = null;					//
	// protected JButton submit = null;							//
	// protected OptionPanel options = null;					//
	// protected JFrame window = null;							//
	// protected boolean isInputReady = false;					//
	//////////////////////////////////////////////////////////////
	//			Methods Inherited from GUIUtilitiesIO			//
	//////////////////////////////////////////////////////////////
	// protected void redirectStreams();						//
	// protected void submit() throws AWTException;				//
	//////////////////////////////////////////////////////////////
	/* @formatter:on */

	// The singleton instance of the class.
	private static volatile GUI instance = null;
	// The title for the GUI window
	private static String titleStr = "Doc Generator";
	// A header that can be displayed at the top of the window
	private JLabel header = null;
	// The menu bar that runs along the top of the window
	private JMenuBar menu = null;
	// This defines an object that listens for keystrokes. It is used by
	// GUIUtilitiesIO to read input.
	private KeyListener listener = new KeyListener()
	{
		/**
		 * This method defines what happens when a key is typed
		 * 
		 * @param e
		 *            The event that triggers the method call.
		 */
		@Override
		public void keyTyped(KeyEvent e)
		{
			// If the key typed is a newline, indicate that the input is ready
			// to read.
			if (e.getKeyChar() == '\n')
			{
				isInputReady = true;
			}
		}

		/**
		 * This method is required to be overridden when implementing a
		 * KeyListener. It defines what happens when a pressed key is released.
		 * It is never used in this program.
		 * 
		 * @param e
		 *            The event that triggers this method.
		 */
		@Override
		public void keyReleased(KeyEvent e)
		{}

		/**
		 * This method is required to be overridden when implementing a
		 * KeyListener. It defines what happens when a key is pressed down. It
		 * is never used in this program.
		 * 
		 * @param e
		 *            The event that triggers this method.
		 */
		@Override
		public void keyPressed(KeyEvent e)
		{}
	};

	// This defines an object that dictates what happens when the GUI window is
	// closed.
	private WindowAdapter wAdapt = new WindowAdapter()
	{
		/**
		 * This method defines what happens when the GUI window is closed. It
		 * displays a dialog indicating what will happen if the window is forced
		 * to close.
		 * 
		 * @param winEvt
		 *            The event that triggers the method call.
		 */
		@Override
		public void windowClosing(WindowEvent winEvt)
		{
			// This message will be included in the dialog window.
			String msg = "Are you sure? Prematurely exiting this window will not output a file.";
			// Shows a dialog that asks the user to confirm yes or no if he/she
			// wants to close the GUI
			int resp = JOptionPane.showConfirmDialog(null, msg, null, JOptionPane.YES_NO_OPTION);
			// If the answer is yes, then exit the application.
			if (resp == JOptionPane.YES_OPTION)
			{
				// GUI.this refers to the 'this' keyword of the outer class. If
				// it were 'this.exit()', then 'this' would refer to the
				// WindowAdapter object.
				GUI.this.exit();
			}
		}
	};

	/**
	 * The constructor builds the GUI window and adds all of the necessary
	 * components. The constructor is private so that instances can only be
	 * created within this class.
	 * 
	 * @param title
	 *            The title of the GUI window.
	 * @throws IOException
	 *             Throws an exception if there is a problem creating the event
	 *             handlers inside the menu.
	 */
	private GUI(String title) throws IOException
	{
		// Create the menu bar that runs along the top of the window.
		menu = this.createMenuBar();

		// Creates a header that will go right below the menu bar.
		header = new JLabel();
		// Defines the font, size and style of the header
		header.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 32));

		// Defines the text area where the questions will be displayed.
		questionArea = new JTextArea(3, 60);
		// Makes sure that text can't be typed in the output area.
		questionArea.setEditable(false);
		// Makes it so the GUI output will line wrap instead of having to scroll
		// side to side.
		questionArea.setLineWrap(true);
		// Pads the borders of the GUI so the text is not right up against them.
		questionArea.setMargin(new Insets(10, 10, 10, 10));
		// Put the question area into a panel to use later.
		JPanel questionPanel = new JPanel();
		questionPanel.add(questionArea);
		questionPanel.setBackground(Color.WHITE);

		// Initialize the options menu.
		options = new OptionPanel();
		options.getPanel().setBackground(Color.WHITE);

		// Combine the question area and options field into one panel.
		JPanel optionWindow = new JPanel(new BorderLayout());
		optionWindow.setBackground(Color.WHITE);
		optionWindow.add(options.getPanel(), BorderLayout.CENTER);
		optionWindow.add(questionPanel, BorderLayout.NORTH);

		// Defines the area where the user used to be able to type input. This
		// is invisible to the user now and is controlled by a robot in the
		// GUIUtilitiesIO.submit() method.
		answerArea = new JTextArea(0, 0);
		answerArea.setEditable(false);

		// Creates the submit button at the bottom of the GUI.
		submit = new JButton("Submit");
		// Defines what action to take when the button is pressed.
		submit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					GUI.super.submit();
				}
				catch (AWTException e)
				{
					GUI.this.finishWithError(e);
				}
			}
		});

		// Combine the answerArea and submit button into one panel.
		JPanel answerWindow = new JPanel(new BorderLayout());
		answerWindow.add(answerArea, BorderLayout.CENTER);
		answerWindow.add(submit, BorderLayout.SOUTH);

		// Allows GUIUtilitiesIO access to the KeyListener.
		super.setKeyListener(listener);
		// Specifies which font to use initially (There are multiple fonts for
		// questionArea. See methods below).
		this.setBoldFont();

		// Creates the window and adds all of the components.
		window = new JFrame(title);
		window.setSize(new Dimension(1000, 800));
		// The default action when exiting the window is to do nothing. The
		// window adapter defined earlier actually handles what happens, but
		// this is needed in order to keep the window open when the user clicks
		// 'No' in the confirmation dialog.
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.setJMenuBar(menu);
		window.add(optionWindow, BorderLayout.CENTER);
		window.add(answerWindow, BorderLayout.SOUTH);// TODO
		window.add(header, BorderLayout.NORTH);
		window.addWindowListener(wAdapt);
		window.setVisible(true);

		// Displays an initial message.
		this.out("Select a practice from the menu.");
	}

	/**
	 * The 'de-facto' constructor. This method either returns the existing GUI
	 * instance or a new one if a GUI has not been instantiated yet.
	 * 
	 * @return The reference to the singleton GUI object.
	 */
	public static GUI getInstance()
	{
		// If the 'instance' field has not been instantiated, create a GUI
		// object and assign it to 'instance'.
		if (instance == null)
		{
			try
			{
				instance = new GUI(titleStr);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Critical Error",
									JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		}

		return instance;
	}

	/**
	 * This method hides the menu bar in the GUI window. This is just a lazy way
	 * to ensure the user does not spawn any additional threads by clicking a
	 * new menu item while another is executing (that would cause the program to
	 * freeze because of the threads' blocking IO).
	 */
	public void hideMenu()
	{
		menu.removeAll();
		window.setJMenuBar(menu);
		window.validate();
	}

	/**
	 * This method displays text in the header field of the GUI window.
	 * 
	 * @param label
	 *            The text to display.
	 */
	public void setHeader(final String label)
	{
		header.setText(label);
	}

	/**
	 * This method creates the menu bar, which will go at the top of the GUI
	 * window.
	 * 
	 * @throws IOException
	 *             Throws an exception if there is a problem creating the event
	 *             handlers.
	 * @return A JMenuBar object, which represents a menu bar at the top of a
	 *         GUI window as well as its contents.
	 * 
	 */
	private JMenuBar createMenuBar() throws IOException
	{
		// Creates the menu bar which spans the top of the GUI.
		JMenuBar bar = new JMenuBar();
		// Creates a dropdown menu and names it 'Practice'.
		JMenu menu = new JMenu("Practice");
		bar.add(menu);

		// This object will be reused for each dropdown menu item to save
		// memory.
		JMenuItem item = null;

		// Creates and gives name to menu item
		item = new JMenuItem("Data Center");
		// Sets a hotkey so user can circumvent clicking the dropdown menu. In
		// the Java swing library, keys are represented by VK_?, where the
		// question mark is replaced with the alphanumeric character the key
		// represents. E.g. in this statement, VK_D represents the 'D' key. VK_?
		// is case insensitive.
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		// Indicates which class contains the code to run when the item is
		// clicked.
		item.addActionListener(new DataCenterHandler());
		// This copies 'item' into the menu, so it can be overwritten.
		menu.add(item);

		item = new JMenuItem("Network Infrastructure");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		item.addActionListener(new NetworkInfrastructureHandler());
		menu.add(item);

		item = new JMenuItem("Unified Communications");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		item.addActionListener(new UnifiedCommunicationsHandler());
		menu.add(item);

		item = new JMenuItem("Wireless");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		item.addActionListener(new WirelessHandler());
		menu.add(item);

		item = new JMenuItem("Security");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		item.addActionListener(new SecurityHandler());
		menu.add(item);

		// Return the menu bar after all of the items have been added to it.
		return bar;
	}

	/**
	 * This method is called after the application is done executing. It
	 * displays a dialog box indicating that the output document is ready, and
	 * the window will exit.
	 */
	public void finish()
	{
		String msg = "Finished processing document. Window will close now.";
		JOptionPane.showMessageDialog(window, msg, null, JOptionPane.PLAIN_MESSAGE);
		this.exit();
	}

	/**
	 * This method is called when the application should exit due to an error.
	 * 
	 * @param e
	 *            The error that caused the application to terminate.
	 */
	public void finishWithError(Exception e)
	{
		String msg = "Output document could not be created due to an error. "
							+ "Please try again later.\n";

		if (e.getMessage() != null)
			msg += e.getMessage();

		JOptionPane.showMessageDialog(window, msg, null, JOptionPane.PLAIN_MESSAGE);
		this.exit();
	}

	/**
	 * Gracefully exits the application. System.exit() needs to be called to
	 * relinquish all of the application's resources otherwise the process will
	 * continue to run.
	 */
	private void exit()
	{
		window.setVisible(false);
		window.dispose();
		System.exit(0);
	}
}
