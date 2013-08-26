package tk.exgerm.mp3player;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import tk.exgerm.core.plugin.ExGAction;

@SuppressWarnings("serial")
public class PlayAction extends ExGAction {
	MP3 mp3;

	public PlayAction() {
		putValue(NAME, "Play");
		putValue(SMALL_ICON, loadIcon("images/play.png"));
		putValue(SHORT_DESCRIPTION, "Play music");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
	}

	@Override
	public int getActionPosition() {
		return TOOLBAR;
	}

	@Override
	public String getToolbar() {
		return MAIN_TOOLBAR;
	}

	@Override
	public int getToolbarPosition() {
		return 1000000;
	}

	@Override
	public String getMenu() {
		return null;
	}

	@Override
	public int getMenuPostition() {
		return 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(new File(System
				.getProperty("user.home")));
		fc
				.addChoosableFileFilter(new FileNameExtensionFilter("Mp3 file",
						"mp3"));
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setDialogTitle("File to play");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		String f;
		try {
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				f = fc.getSelectedFile().getCanonicalPath();
				if (mp3 != null)
					mp3.stop();
				mp3 = new MP3(f);
				mp3.play();
			}
		} catch (IOException e1) {
			JOptionPane.showInternalMessageDialog(null,
					"Greska, nista od muzike!", "Greska!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

}
