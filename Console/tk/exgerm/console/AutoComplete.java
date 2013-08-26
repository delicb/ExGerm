package tk.exgerm.console;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import tk.exgerm.console.gui.Console;
import tk.exgerm.core.plugin.ExGCommand;

public class AutoComplete {

	Document doc;
	Console console;
	String typedWord;
	List<String> previousResults;
	String resultBeforeListPurge;

	public AutoComplete(Console console) {
		doc = console.getTextPane().getDocument();
		this.console = console;
		typedWord = new String();
		previousResults = new ArrayList<String>();
	}

	/**
	 * Započinje pretragu za (sledećim) kandidatom za autoComplete
	 */
	public void run() {
		int count = console.findPreviousSpace() - 1;
		String result = "";
		boolean queryAll = false;

		if (typedWord == null) {
			try {
				if (console.getPreviousSymbols(1).equals(" ")) {
					typedWord = "";
					queryAll = true;
				} else
					typedWord = doc.getText(
							console.getTextPane().getCaretPosition() - count,
							count).trim();
			} catch (BadLocationException e) {
				e.printStackTrace();
				return;
			}
		}

		List<ExGCommand> list = console.getContext().getAllCommands();

		if (console.getParser().isShowPathInPrompt()) {
			File dir = new File(console.getContext().getData(
					ExGCommand.CURRENT_FILESYSTEM_PATH).toString());
			for (File f : dir.listFiles()) {
				String file = f.getName();
				if (file.startsWith(typedWord)
						&& !previousResults.contains("\"" + file + "\"")) {
					result = "\"" + file + "\"";
				}

			}
		}

		if (result.length() == 0) {
			if ("graph".startsWith(typedWord)
					&& !previousResults.contains("graph"))
				result = "graph";
			else if ("node".startsWith(typedWord)
					&& !previousResults.contains("node"))
				result = "node";
			else if ("edge".startsWith(typedWord)
					&& !previousResults.contains("edge"))
				result = "edge";
			else {
				for (ExGCommand command : list) {
					if (command.getKeyword().startsWith(typedWord)
							&& !previousResults.contains(command.getKeyword())) {
						result = command.getKeyword();
						break;
					}
				}
				if (result.equals("")) {
					Iterator<String> it = console.getAliases().keySet()
							.iterator();
					while (it.hasNext()) {
						String command = it.next();
						if (command.startsWith(typedWord)
								&& !previousResults.contains(command)) {
							result = command;
							break;
						}
					}
				}
			}
		}
		if (result != null && result.length() != 0) {
			if (!queryAll) {
				String previousResult;
				if (previousResults.size() > 0)
					previousResult = previousResults
							.get(previousResults.size() - 1);
				else {
					previousResult = resultBeforeListPurge;
					resultBeforeListPurge = null;
				}
				if (previousResult != null && previousResult.startsWith("\"")
						&& previousResult.endsWith("\""))
					console.removeLastChars(previousResult.length());
				else
					console.removeLeftWord();
			}

			console.insertIntoCarretPostition(result);
			previousResults.add(result);
		} else if (previousResults.size() > 0) {
			resultBeforeListPurge = previousResults
					.get(previousResults.size() - 1);
			previousResults.clear();
			run();
		}
	}

	/**
	 * Briše prethodne rezultate pretrage i započinje novu (usled nekog
	 * događaja)
	 */
	public void reset() {
		typedWord = null;
		previousResults.clear();
	}

}
