package tk.exgerm.console.parser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import tk.exgerm.console.gui.Console;
import tk.exgerm.console.listeners.ConsoleParameterChangedListener;
import tk.exgerm.console.listeners.GraphOrNodeRemovedListener;
import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGUnknownCommandException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.core.service.IPipeLine;

public class Parser {

	ICoreContext context;
	PrintStream output;
	Console console;
	String separator = new String(">");

	StringTokenizer semicolumnTokens, pipeTokens, commandTokens;
	String[] tokens;
	IPipeLine pipeLine;

	IGraph activeGraph = null;
	INode activeNode = null;
	String fileSystemPath = null;
	Boolean showPathInPrompt = false;

	public Parser(ICoreContext context, PrintStream output, Console console) {
		this.console = console;
		this.output = output;
		this.context = context;

		try {
			fileSystemPath = new File(System.getProperty("user.home"))
					.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setPrompt();
		context.addData(ExGCommand.CURRENT_FILESYSTEM_PATH, fileSystemPath);

		IListener parameterChangedListener = new ConsoleParameterChangedListener(
				this, output);
		IListener removeListener = new GraphOrNodeRemovedListener(this);
		context.listenEvent(ExGCommand.ACTIVE_GRAPH_CHANGED,
				parameterChangedListener);
		context.listenEvent(ExGCommand.ACTIVE_NODE_CHANGED,
				parameterChangedListener);
		context.listenEvent(ExGGraphRegister.GRAPH_REMOVED, removeListener);
		context.listenEvent(IGraph.GRAPH_NODE_REMOVED, removeListener);
		context.listenEvent(ExGCommand.FILESYSTEM_PATH_CHANGED,
				parameterChangedListener);
		context.listenEvent(ExGCommand.SHOW_PATH_IN_PROMPT_CHANGED,
				parameterChangedListener);
	}

	public void run(String lastLine) {

		String commands, command;

		// cepa po ;
		semicolumnTokens = new StringTokenizer(lastLine, ";");

		// prolazi kroz sve tokene (znaci komande same ili vishe razdvojene |)
		while (semicolumnTokens.hasMoreTokens()) {

			commands = semicolumnTokens.nextToken().trim();
			// ako je samo jedna komanda nju izvrsavamo
			if (commands != null && !commands.contains("|")) {
				doCommand(split(commands));
			} else {

				pipeLine = context.createPipeLine();
				pipeTokens = new StringTokenizer(commands, "|");

				while (pipeTokens.hasMoreTokens()) {

					command = pipeTokens.nextToken().trim();
					tokens = split(command);
					try {
						addCommandToPipeline(tokens, pipeLine);
					} catch (ExGUnknownCommandException e) {

						console
								.printError(
										"Pipe Error! Unknown command: "
												+ tokens[0],
										ExGCommandErrorException.CommandErrorType.ERROR);

						return;
					}

				}
				pipeLine.run();
				pipeLine = null;
			}
		}
	}

	/**
	 * Parsira string komande iz konzole i kreira
	 * {@link tk.exgerm.console.commands komandu}
	 * 
	 * @param tokens
	 *            string same komande onakve kakvu ju je korisnik uneo u konzolu
	 */
	private void doCommand(String[] tokens) {

		// ovo se ne bi trebalo desiti sem kada postoji prazna komanda pa
		// ignorisemo
		if (tokens == null || tokens.length == 0) {
			console.printError("Empty Command!",
					ExGCommandErrorException.CommandErrorType.WARNING);
			return;
		}
		String name = tokens[0].trim();
		// trazimo komandu
		ExGCommand command = context.getCommand(name);
		// ako je nadjena komanda
		if (command != null) {
			try {
				// pokusavamo da izvrsimo komandu
				command.execute(output, tokens);
			} catch (ExGCommandErrorException e) {
				console.printError(e.getMessage(), e.getErrorType());
			} catch (Throwable e1) {
				console.printError("Command " + command.getKeyword()
						+ " crashed!", CommandErrorType.CRITICAL_ERROR);
				e1.printStackTrace();
			}
		} else {
			// potreban mehanizam na koji doci do alijasa
			String alias = console.getAliases().get(tokens[0]);
			// sada treba rasparchati vraceni alijas komande (sama komanda)
			// i napraviti novi string...ako alijas nije null
			if (alias != null) {
				String aliasCommand = new String();
				for (int i = 0; i < tokens.length; i++)
					aliasCommand += (i == 0) ? alias : " " + tokens[i];
				// sada ponovo pozovemo doCommand jer moze alijas alijasa da
				// bude
				doCommand(split(aliasCommand));
			} else
				console.printError("Error! Unknown command: " + name,
						ExGCommandErrorException.CommandErrorType.ERROR);
		}
	}

	/**
	 * Rastavlja prosleđeni tekts na niz stringova tako ga cepa po razmacima
	 * koji se ne nalaze pod znacima navoda i uklanja znake navoda.
	 * 
	 * @param text
	 *            Tekst koji se cepa
	 * @return Niz stringova - delova komandi
	 */
	private String[] split(String text) {

		boolean inQuoth = false;
		int qStart = 0, begin = 0;
		ArrayList<String> ret = new ArrayList<String>();

		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '"') {
				if (inQuoth == false) {
					inQuoth = true;
					qStart = i;
				} else {
					inQuoth = false;
					// preuzeti kao poseban token u ret
					ret.add(text.substring(qStart, i).replace('"', ' ').trim());
					begin = ++i;
				}
			} else {
				if (text.charAt(i) == ' ') {
					if (inQuoth == true)
						continue;
					if (!text.substring(begin, i).trim().isEmpty())
						ret.add(text.substring(begin, i).trim());
					begin = i;
				}
				if (i == text.length() - 1 && text.charAt(i) != '"') {
					ret.add(text.substring(begin).trim());
				}
			}
		}

		return ret.toArray(new String[ret.size()]);
	}

	private void addCommandToPipeline(String[] tokens, IPipeLine line)
			throws ExGUnknownCommandException {

		if (tokens == null || tokens.length == 0) {
			console.printError("Empty Command!",
					ExGCommandErrorException.CommandErrorType.WARNING);
			return;
		}

		ExGCommand command = context.getCommand(tokens[0].trim());
		if (command != null)
			line.addCommand(command);
		else
			console.printError("Error! Unknown command pased to pipe: "
					+ tokens[0],
					ExGCommandErrorException.CommandErrorType.ERROR);
	}

	public IGraph getActiveGraph() {
		return activeGraph;
	}

	/**
	 * Postavlja aktivan graf. Ukoliko je aktivan graf null, automatski se
	 * postavlja i aktivan node na null!
	 * 
	 * @param graph
	 *            koji se postavlja
	 */
	public void setActiveGraph(IGraph graph) {
		context.addData(ExGCommand.CONSOLE_ACTIVE_GRAPH, graph);
		activeGraph = graph;

		if (graph == null) {
			context.addData(ExGCommand.CONSOLE_ACTIVE_NODE, null);
			activeNode = null;
		}
	}

	public INode getActiveNode() {
		return (INode) context.getData(ExGCommand.CONSOLE_ACTIVE_NODE);
	}

	public void setActiveNode(INode node) {
		context.addData(ExGCommand.CONSOLE_ACTIVE_NODE, node);
		activeNode = node;
	}

	public String getFileSystemPath() {
		return fileSystemPath;
	}

	public void setFileSystemPath(String fileSystemPath) {
		this.fileSystemPath = fileSystemPath;
		context.addData(ExGCommand.CURRENT_FILESYSTEM_PATH, fileSystemPath);
	}

	public Boolean isShowPathInPrompt() {
		return showPathInPrompt;
	}

	public void setShowPathInPrompt(Boolean showPathInPrompt) {
		this.showPathInPrompt = showPathInPrompt;
	}

	/**
	 * Generiše deo prompta u slučaju rada sa podgrafom ili nodom na većoj
	 * dubini. Praktično je poziva samo setPrompt() metoda ako ima potrebe.
	 * 
	 * @return deo stabla prompta
	 */
	private String useGraphPromptSet() {

		String promptPart = activeNode.getName();
		INode step = activeNode;
		while (step.getGraph() != null) {
			promptPart = step.getGraph().getName() + separator + promptPart;
			step = step.getGraph();
		}
		return promptPart;
	}

	/**
	 * Postavlja novi prompt na osnovu trenutno aktivnog grafa i noda.
	 * 
	 */
	public void setPrompt() {
		String newPrompt = "";
		if (showPathInPrompt) {
			newPrompt = "[" + fileSystemPath + "]";
			console.setPrompt(newPrompt);
		}
		if (activeGraph == null)
			console.setPrompt(newPrompt + separator);
		else {
			if (activeNode == null)
				console
						.setPrompt(newPrompt + activeGraph.getName()
								+ separator);
			else
				console.setPrompt(newPrompt + useGraphPromptSet() + separator);
		}
	}

	/**
	 * Ispisuje na izlaz novu liniju sa promptom. Korisno kada se prompt promeni
	 * zbog nekog događaja pa je potrebno staviti korisniku do znanja da se to
	 * desilo. Primer: obrisan graf ili node koji je bio aktivan.
	 */
	public void forceNewPrompt() {
		setPrompt();
		output.print("\n" + console.getPrompt());
	}

}
