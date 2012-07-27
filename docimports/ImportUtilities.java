package docimports;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import driver.GUI;

public class ImportUtilities
{
	/**
	 * This method obtains an output file according to the user's choice.
	 * 
	 * @return A File object pointing at the user's desired file.
	 */
	public static String getOutputFile()
	{
		// Builds a JFileChooser, which will provide the user with a GUI to
		// navigate the file system.
		JFrame fileChooserWindow = new JFrame();
		JFileChooser fc = new JFileChooser();
		// Doesn't allow the user to choose directories or multiple files.
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);

		File outputFile = null;
		int overwrite = JOptionPane.YES_OPTION;
		do
		{
			// Get the output file
			outputFile = getOutputFileLocation(fc, fileChooserWindow);
			// If the file already exists, prompt the user if he/she wants to
			// overwrite it.
			if (outputFile.exists())
			{
				overwrite = JOptionPane.showConfirmDialog(new JFrame(),
									"File already exists. Do you want to overwrite it?", null,
									JOptionPane.INFORMATION_MESSAGE);
				if (overwrite == JOptionPane.YES_OPTION)
					break;
			}
		} while (outputFile.exists());

		return FilenameUtils.removeExtension(outputFile.getAbsolutePath());
	}

	/**
	 * This method prompts the user to choose a location for an output file.
	 * 
	 * @param fc
	 *            The FileChooser object that handles the GUI operations.
	 * @param fileChooserWindow
	 *            The window which will contain the FileChooser
	 * @return The file that the user selects.
	 */
	private static File getOutputFileLocation(JFileChooser fc, JFrame fileChooserWindow)
	{
		int buttonPressed = JOptionPane.NO_OPTION;
		do
		{
			// Figure out which button in the FileChooser the user clicked.
			buttonPressed = fc.showSaveDialog(fileChooserWindow);
			// If the user clicked 'Cancel' then confirm that the user wants to
			// exit.
			if (buttonPressed == JFileChooser.CANCEL_OPTION)
			{
				int cancelled = JOptionPane.showConfirmDialog(new JFrame(),
									"Are you sure? The application will exit.", null,
									JOptionPane.INFORMATION_MESSAGE);

				// If the user still wants to cancel, then exit the application.
				if (cancelled == JOptionPane.YES_OPTION)
					GUI.getInstance().finishWithError(
										new Exception("User manually exited application"));
			}
			// Loop until the user clicks the save button.
		} while (buttonPressed == JFileChooser.CANCEL_OPTION);

		return fc.getSelectedFile();
	}

//	public static void main(String[] args) throws IOException
//	{
//		JFileChooser fc = new JFileChooser();
//		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//		fc.setMultiSelectionEnabled(true);
//
//		JFrame frame = new JFrame("FileChooserDemo");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.add(fc);
//
//		fc.showOpenDialog(frame);
//		File[] files = fc.getSelectedFiles();
//
//		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		fc.setMultiSelectionEnabled(false);
//
//		fc.showSaveDialog(frame);
//		File dest = fc.getSelectedFile();
//
//		for (int ii = 0; ii < files.length; ++ii)
//			FileUtils.copyFileToDirectory(files[ii], dest);
//	}
}
