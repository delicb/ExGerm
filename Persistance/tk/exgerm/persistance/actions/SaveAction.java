package tk.exgerm.persistance.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;
import tk.exgerm.persistance.builder.GDLBuilder;

@SuppressWarnings("serial")
public class SaveAction extends PersistanceAction {

	PersistanceService service;
	ICoreContext context;

	public SaveAction(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;

		putValue(NAME, "Save...");
		putValue(SHORT_DESCRIPTION, "Save graph");
		putValue(SMALL_ICON, loadIcon("icons/save.png"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
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
		IGraph g = (IGraph) context.getData(IGraph.ACTIVE_GRAPH);
		if (g == null) {
			info("None graph is visible.", "No active graph");
			return;
		} else {
			if (service.isGraphLoaded(g.getName())) {
				File f = new File(service.getGraphFile(g.getName()));
				try {
					service.saveGraph(g, new GDLBuilder(), f);
				} catch (ExGNameConflictException e1) {
					error(e1.getMessage(), "Error");
				}
			} else {
				JFileChooser fc = getFileChooser(JFileChooser.SAVE_DIALOG,
						"Where to save!");
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File chosenFile = fc.getSelectedFile();
					try {
						if (!chosenFile.getCanonicalPath().endsWith(".egg"))
							chosenFile = new File(chosenFile.getCanonicalPath() + ".egg");
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					if (chosenFile.exists()) {
						if (question(
								"File already exists. Are you sure you want to overwrite it?",
								"Overwrite file?")) {
							try {
								service.saveGraph(g, new GDLBuilder(),
										chosenFile);
							} catch (ExGNameConflictException e1) {
								error(e1.getMessage(), "Error");
							}
						}
					} else {
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

	@Override
	public int getMenuPostition() {
		return 300;
	}

	@Override
	public int getToolbarPosition() {
		return 300;
	}
}
