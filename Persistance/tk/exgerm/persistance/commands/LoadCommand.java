package tk.exgerm.persistance.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;
import tk.exgerm.persistance.parser.InternalParseException;
import tk.exgerm.persistance.parser.GDLParser.GDL;

/**
 * Učitava graf iz fajla i ubacuje ga u GraphRegister. Ima mogućnost učitavanja
 * grafa pod drugačijim imenom.
 * 
 * @author Tim 2
 */
public class LoadCommand extends PersistanceCommand {

	private PersistanceService service;
	private ICoreContext context;

	public LoadCommand(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {

		if (params.length != 2 && params.length != 4)
			error("load [file name] or load [file name] as [graph name] expected");

		File toParse = null;
		File f = null;
		boolean as_syntax = false;

		f = new File(params[1]);
		if (f.isAbsolute()) {
			toParse = f;
		} else {
			toParse = new File(context.getData(CURRENT_FILESYSTEM_PATH)
					+ File.separator + params[1]);
		}

		String graphName = null;
		if (params.length == 4) {
			if (!params[2].equalsIgnoreCase("as"))
				error("Valid sintax is load [file name] as [graph name]. "
						+ "See help load for details.");
			graphName = params[3];
			as_syntax = true;
		}

		// iako ce ExGNameConfictException biti bacen ako graf sa istim imenom
		// vec postoji, ne smemo ni da pocnemo parsiranje, jer ce nodovi
		// biti dodati u postojeci graf sa istim imenom
		if (context.getGraph(graphName) != null) {
			error("Graph with the same name already exsists in graph registry. \n"
					+ "Try load [file name] as [graph name] to change name of the graph");
		}

		IGraph g = null;
		try {
			g = service.parseFile(new GDL(context, toParse, graphName),
					toParse, as_syntax);
		} catch (ExGNameConflictException e) {
			error("Graph with the same name already exsists in graph registry. \n"
					+ "Try load [file name] as [graph name] to change name of the graph");
		} catch (FileNotFoundException e) {
			try {
				error("File " + toParse.getCanonicalPath() + " does not exist");
			} catch (IOException e1) {
				// nadamo se da se nece desiti
			}
		} catch (InternalParseException e) {
			error(e.getMessage());
		}

		return g;
	}

	@Override
	public String getHelp() {
		return "Load command reader file with graph definition and loads it into program. \n"
				+ "Receives file name (relative or absolute) from witch to read, \n"
				+ "and optionaly name of graf, if it should be different from one \n"
				+ "defined in file (this way use can load same file twice).";
	}

	@Override
	public String getKeyword() {
		return "load";
	}

	@Override
	public String getSyntax() {
		return "load <file> [as <graph_name>] - Loads graf from file <file> under name <grapg_name>";
	}

	@Override
	public boolean returnsGraph() {
		return true;
	}

}
