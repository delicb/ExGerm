package tk.exgerm.pluginmanager.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Standardan prozor za pronalaženje jar paketa za instalaciju.
 *
 */
@SuppressWarnings("serial")
public class PluginFileChooser extends JFileChooser {
	public PluginFileChooser(File f, int dialogType, String dialogTitle) {
		super(f);
		this.setDialogType(dialogType);
		this.setDialogTitle(dialogTitle);
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.addChoosableFileFilter(new FileNameExtensionFilter(
				"ExGERM Plugin JAR", "jar"));
	}
}