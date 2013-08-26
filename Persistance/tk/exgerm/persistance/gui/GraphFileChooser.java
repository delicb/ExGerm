package tk.exgerm.persistance.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Jednostavna modifikacija swingovog {@link JFileChooser file chooser-a}, da bi
 * se smanjila redundantnost koda.
 * 
 * @author Tim 2
 */
@SuppressWarnings("serial")
public class GraphFileChooser extends JFileChooser {
	public GraphFileChooser(File f, int dialogType, String dialogTitle) {
		super(f);
		this.setDialogType(dialogType);
		this.setDialogTitle(dialogTitle);
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.addChoosableFileFilter(new FileNameExtensionFilter("ExGERM graph",
				"egg", "txt"));
	}
}
