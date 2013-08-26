package tk.exgerm.persistance.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;
import tk.exgerm.persistance.parser.InternalParseException;
import tk.exgerm.persistance.parser.GDLParser.GDL;

/**
 * Akcija za otvaranje fajla i parsiranje njegovog sadrzaja.
 * 
 * @author Tim2
 * 
 *         TODO: Ikonica, ide i u toolbar, treba pratiti i sve otvorene fajlove
 *         da bismo znali gde kasnije da sacuvamo isti graf...
 */
@SuppressWarnings("serial")
public class OpenAction extends PersistanceAction {

	PersistanceService service;
	ICoreContext context;

	public OpenAction(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;

		putValue(NAME, "Open...");
		putValue(SHORT_DESCRIPTION, "Load graph from file");
		putValue(SMALL_ICON, loadIcon("icons/open.png"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
	}

	@Override
	public int getActionPosition() {
		return MENU | TOOLBAR;
	}

	@Override
	public String getMenu() {
		return FILE_MENU;
	}

	@Override
	public String getToolbar() {
		return MAIN_TOOLBAR;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = getFileChooser(JFileChooser.OPEN_DIALOG,
				"Choose file to open");

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				File f = fileChooser.getSelectedFile();
				service.parseFile(new GDL(context, f), fileChooser
						.getSelectedFile(), false);
			} catch (ExGNameConflictException e1) {
				error(
						"Graph with same name already exsists in registry. Try Open As... ",
						"Error loading graph");
			} catch (FileNotFoundException e1) {
				error("Error! File does not exist!", "File error");
			} catch (InternalParseException e1) {
				error(e1.getMessage(), "Parse error");
			}
		}
	}

	@Override
	public int getMenuPostition() {
		return 300;
	}

	@Override
	public int getToolbarPosition() {
		return 300;
	}

}
