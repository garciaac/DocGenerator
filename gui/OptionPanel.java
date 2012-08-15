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
 * Last Modified: Aug 15, 2012 1:39:01 PM
 */

package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 
 * This class provides much of the functionality needed to provide checkbox
 * options inside of the GUI. It displays the options and records which ones are
 * selected/deselected.
 * 
 */
public class OptionPanel implements ItemListener
{
	// The JPanel in which the checkboxes will reside.
	private JPanel panel = null;
	// A GridLayout used for placing elements in the panel.
	private GridLayout layout = null;
	// The number of items contained in the GridLayout. This is
	// needed so that I can arrange items vertically in rows.
	private int num_rows = 1;
	// The container for the selected options.
	private ArrayList<String> selectedOptions = null;
	// The editable area for editable options.
	private JTextArea textInput = null;

	/**
	 * Constructor that initializes all of the fields except textInput.
	 */
	public OptionPanel()
	{
		layout = new GridLayout();
		panel = new JPanel(layout);
		num_rows = 1;
		selectedOptions = new ArrayList<String>();
	}

	/**
	 * Accessor for the JPanel object.
	 * 
	 * @return The panel which contains the checkboxes.
	 */
	public JPanel getPanel()
	{
		return panel;
	}

	/**
	 * Adds an option to the JPanel.
	 * 
	 * @param option
	 *            A JTextbox object which will be added to the panel.
	 */
	public void add(JCheckBox option)
	{
		option.setBackground(Color.WHITE);
		// Increment num_rows and adjust GridLayout and reset the JPanel's
		// layout.
		++num_rows;
		layout.setRows(num_rows);
		panel.setLayout(layout);
		panel.add(option);
		option.addItemListener(this);
	}

	/**
	 * Adds an option with an editable text field attached to it.
	 * 
	 * @param option
	 *            A JTextbox object which will be added to the panel.
	 */
	public void addEditableField(JCheckBox option)
	{
		// Initialize the text area.
		textInput = new JTextArea("If other, enter information here.", 4, 10);
		textInput.setEditable(false);
		textInput.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		// Adds the checkbox to the panel.
		this.add(option);
		layout.setColumns(2);
		panel.setLayout(layout);
		panel.add(textInput);
		// The other add() method associates OptionPanel's item listening
		// capabilities, so they need to be changed to EditableFieldListener's.
		option.removeItemListener(this);
		option.addItemListener(new EditableFieldListener());
	}

	/**
	 * Gets rid of all the options in the panel.
	 */
	public void clear()
	{
		// Reset number of rows and adjust GridLayout and reset JPanel's layout.
		num_rows = 1;
		layout.setRows(num_rows);
		panel.removeAll();
		panel.setLayout(layout);
		// Clear out the buffer of selected options.
		selectedOptions.clear();
	}

	/**
	 * Accessor for the selected options.
	 * 
	 * @return The selected options buffer.
	 */
	public ArrayList<String> getSelectedOptions()
	{
		return selectedOptions;
	}

	/**
	 * This method handles the necessary actions for when one of the boxes is
	 * checked/unchecked.
	 * 
	 * @param e
	 *            The object that spawns the method call.
	 */
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		// Need to ensure that the object that spawns this call is a JCheckBox.
		if (e.getItemSelectable() instanceof JCheckBox)
		{
			// If it is deselected, remove it from the buffer. (remove() does
			// nothing if the desired element is not in the container.)
			if (e.getStateChange() == ItemEvent.DESELECTED)
				selectedOptions.remove(((JCheckBox) e.getItemSelectable()).getText());
			else
				selectedOptions.add(((JCheckBox) e.getItemSelectable()).getText());
		}
	}

	/**
	 * 
	 * This class is the one that handles the necessary actions for when a box
	 * with an editable text field is checked/unchecked.
	 * 
	 */
	private class EditableFieldListener implements ItemListener
	{
		/**
		 * This method handles the necessary actions for when the box is
		 * checked/unchecked.
		 * 
		 * @param e
		 *            The object that spawns the method call.
		 */
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getItemSelectable() instanceof JCheckBox)
			{
				// If it is selected. Clear out whatever is there and let the
				// user edit it.
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					textInput.setText("");
					textInput.setEditable(true);
				}
				else
					textInput.setEditable(false);
			}
		}
	}

	/**
	 * This method is called by GUIUtilitiesIO when the user clicks the submit
	 * button. It reads the editable text supplied by the user and adds it to
	 * the selectedOptions list.
	 */
	protected void submit()
	{
		// textInput is not initialized in the constructor, so you need to see
		// if it is null first.
		if (textInput != null)
			// If it is editable, that means the user has it selected.
			if (textInput.isEditable())
				selectedOptions.add(textInput.getText());
	}
}
