package tk.exgerm.persistance.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;
import tk.exgerm.persistance.parser.InternalParseException;
import tk.exgerm.persistance.parser.GDLParser.GDL;

@SuppressWarnings("serial")
public class OpenAsAction extends PersistanceAction {

	PersistanceService service;
	ICoreContext context;

	public OpenAsAction(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;

		putValue(NAME, "Open As...");
		putValue(SHORT_DESCRIPTION, "Load graph from file with different name");
		putValue(SMALL_ICON, loadIcon("icons/openas.png"));
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
			File f = fileChooser.getSelectedFile();

			String newName = input("Under witch name should graph be opend?",
					"Graph name");
			if (newName != null)
				if (newName.equals(""))
					newName = null;

			if (newName != null) {
				if (context.getGraph(newName) != null) {
					error(
							"Graph with that name already exsists in registry. Try another name.",
							"Error loading graph");
					return;
				}
			}

			try {
				service.parseFile(new GDL(context, f, newName), fileChooser
						.getSelectedFile(), true);
			} catch (FileNotFoundException e1) {
				error("Error! File does not exist!", "File error");
			} catch (ExGNameConflictException e1) {
				error(
						"Graph with same name already exsists in registry. Try another name. ",
						"Error loading graph");
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
