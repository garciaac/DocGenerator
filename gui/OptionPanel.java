package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class OptionPanel implements ItemListener
{
	private JPanel panel = null;
	private GridLayout layout = null;
	private int num_rows = 0;
	private ArrayList<String> selectedOptions = null;

	public OptionPanel()
	{
		layout = new GridLayout();
		panel = new JPanel(layout);
		num_rows = 1;
		selectedOptions = new ArrayList<String>();
	}

	public JPanel getPanel()
	{
		return panel;
	}

	public void add(JCheckBox option)
	{
		option.setBackground(Color.WHITE);
		++num_rows;
		layout.setRows(num_rows);
		panel.setLayout(layout);
		panel.add(option);
		option.addItemListener(this);
		panel.setSize(panel.getPreferredSize());
	}
	
//	public void addOtherField()
//	{
//		JCheckBox other = new JCheckBox("Other");
//		other.setBackground(Color.WHITE);
//		panel.add(other);
//		++num_rows;
//		layout.setRows(num_rows);
//		panel.setLayout(layout);
//		JTextArea input = new JTextArea(2, 60);
//		input.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//		panel.add(input);
//	}

	public void clear()
	{
		num_rows = 1;
		layout.setRows(num_rows);
		panel.removeAll();
		selectedOptions.clear();
		panel.setLayout(layout);
		panel.setSize(panel.getPreferredSize());
	}
	
	public ArrayList<String> getSelectedOptions()
	{
		return selectedOptions;
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getItemSelectable() instanceof JCheckBox)
		{
			if (e.getStateChange() == ItemEvent.DESELECTED)
				selectedOptions.remove(((JCheckBox) e.getItemSelectable()).getText());
			else
				selectedOptions.add(((JCheckBox) e.getItemSelectable()).getText());
		}
	}

}
