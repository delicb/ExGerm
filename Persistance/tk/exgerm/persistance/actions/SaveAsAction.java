package tk.exgerm.persistance.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;
import tk.exgerm.persistance.builder.GDLBuilder;

@SuppressWarnings("serial")
public class SaveAsAction extends PersistanceAction {
	
	PersistanceService service;
	ICoreContext context;
	
	public SaveAsAction(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;
		
		putValue(NAME, "Save As...");
		putValue(SHORT_DESCRIPTION, "Save graph in different file");
		putValue(SMALL_ICON, loadIcon("icons/saveas.png"));
	}
	
	@Override
	public int getActionPosition() {
		return MENU | TOOLBAR;
	}

	@Override
	public String getToolbar() {
		return MAIN_TOOLBAR;
	}

	@Override
	public int getToolbarPosition() {
		return 300;
	}

	@Override
	public String getMenu() {
		return FILE_MENU;
	}

	@Override
	public int getMenuPostition() {
		return 300;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IGraph g = (IGraph) context.getData(IGraph.ACTIVE_GRAPH);
		if (g == null) {
			info("None graph is visible.", "No active graph");
			return;
		}
		else {
			JFileChooser fc = getFileChooser(JFileChooser.SAVE_DIALOG,
					"Where to save!");
			if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				File chosenFile = fc.getSelectedFile();
				if (chosenFile.exists()) {
					if (question(
							"File already exists. Are you sure you want to overwrite it?",
							"Overwrite file?")) {
						try {
							service.saveGraph(g, new GDLBuilder(), chosenFile);
						} catch (ExGNameConflictException e1) {
							error(e1.getMessage(), "Error");
						}
					}
				}
				else {
					try {
						service.saveGraph(g, new GDLBuilder(), chosenFile);
					} catch (ExGNameConflictException e1) {
						error(e1.getMessage(), "Error");
					}
				}
			}
		}
	}

}
