package tk.exgerm.persistance.actions;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.persistance.gui.GraphFileChooser;

/**
 * Sloj između {@link ExGAction} i konkretnih akcije. Sadrži neke često
 * korišćene stvari i smanjuje redundantsnost koda.
 * 
 * @author Tim 2
 */
public abstract class PersistanceAction extends ExGAction {

	private static final long serialVersionUID = -4391389990251978106L;

	protected JFileChooser getFileChooser(int type, String title) {
		return new GraphFileChooser(new File(System.getProperty("user.home")),
				type, title);
	}

	protected void error(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	protected void info(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected String input(String message, String title) {
		return JOptionPane.showInputDialog(null, message, title,
				JOptionPane.QUESTION_MESSAGE);
	}

	protected boolean question(String question, String title) {
		int res = JOptionPane.showConfirmDialog(null, question, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == 0)
			return true;
		else 
			return false;
	}
}
