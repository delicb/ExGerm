package tk.exgerm.persistance.commands;

import java.io.File;
import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;
import tk.exgerm.persistance.builder.GDLBuilder;

public class SaveCommand extends PersistanceCommand {

	PersistanceService service;
	ICoreContext context;

	public SaveCommand(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length < 1 || params.length > 5)
			error("Wrong number of parameters. See help save");

		boolean overwrite = overwrite(params);
		boolean seperate = hasSwitch("-p", params);

		File f = getFile(params);
		System.out.println(f);
		IGraph g = getGraphToSave(params);
		if (g == null) {
			error("Graph does not exist!");
		}

		if (!seperate) {
			g = g.getFinalRoot();
		} else {
			if (f == null)
				error("If you want to save subgraph in seperate file you must provide\n"
						+ "file to save in.");
		}

		if (f == null) {
			error("Save where?");
		}

		if (f.exists() && !overwrite) {
			warning("Requested file exists. If you wand to override it use -o switch.");
		} else {
			try {
				service.saveGraph(g, new GDLBuilder(), f);
			} catch (ExGNameConflictException e) {
				error(e.getMessage());
			}
		}

		return null;
	}

	@Override
	public String getHelp() {
		return "Saves graph to file. "
				+ "First parameter is graph. If it is not provided active graph is used.\n"
				+ "If graph was not loaded from file, file must be specified.\n"
				+ "If file exists and -o switch is not provided, save will fail.\n"
				+ "\n"
				+ "If graph to save is subgraph, root graph of that graph will be saved \n"
				+ "unless switch -p is provided, in witch case subgraph will be saved in\n"
				+ "in it's own file (-f file must be provided)";
	}

	@Override
	public String getKeyword() {
		return "save";
	}

	@Override
	public String getSyntax() {
		return "save [graph] [-f file] [-o] [-p]";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

	private File getFile(String... params) {
		String passedFile = getValue("-f", params);

		// ako je prosleđen file, njega korisnimo
		if (passedFile != null) {
			if (passedFile.endsWith(".egg"))
				return getAbsoluteFile(passedFile);
			else
				return getAbsoluteFile(passedFile + ".egg");
		}
		else {
			IGraph g = getGraphToSave(params);
			if (g != null) {
				String graphToSave = g.getName();
				String loadedFrom = service.getGraphFile(graphToSave);
				if (loadedFrom == null)
					return null;
				else {
					return getAbsoluteFile(loadedFrom);
				}
			} else
				return null;
		}
	}

	private IGraph getGraphToSave(String... params) {
		IGraph graphToSave = null;
		// ime grafa može biti samo na na prvom mesto, pa ako je
		// na prvom mestu neki parametar, nije zadato ime fajla
		try {
			if (!params[1].startsWith("-")) {
				graphToSave = context.getGraph(params[1]);
			} else {
				graphToSave = getActiveGraph();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			graphToSave = getActiveGraph();
		}
		return graphToSave;
	}

	private File getAbsoluteFile(String file) {
		File f = null;
		File tmp = new File(file);
		if (tmp.isAbsolute())
			f = tmp;
		else
			f = new File(context.getData(CURRENT_FILESYSTEM_PATH)
					+ File.separator + file);
		return f;
	}

	private boolean overwrite(String... params) {
		if (hasSwitch("-o", params))
			return true;
		else {
			IGraph g = getGraphToSave(params);
			if (g != null) {
				String graphToSave = g.getName();
				return service.isGraphLoaded(graphToSave);
			} else
				return false;
		}
	}
	
	private IGraph getActiveGraph() {
		INode n = (INode)context.getData(CONSOLE_ACTIVE_NODE);
		if (n != null) {
			if (n instanceof IGraph)
				return (IGraph)n;
			else
				return n.getGraph();
		}
		else {
			return (IGraph)context.getData(CONSOLE_ACTIVE_GRAPH);
		}
	}

}
