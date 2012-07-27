package gui;

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
		panel.setSize(10, 10);
		num_rows = 1;
		selectedOptions = new ArrayList<String>();

	}

	public JPanel getPanel()
	{
		return panel;
	}

	public void add(JCheckBox option)
	{
		++num_rows;
		layout.setRows(num_rows);
		panel.setLayout(layout);
		panel.add(option);
		option.addItemListener(this);
	}

	public void clear()
	{
		panel.removeAll();
		selectedOptions.clear();
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
