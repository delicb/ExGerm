package tk.exgerm.core.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.plugin.ExGCommand;

public class CommandRegister {

	/**
	 * {@link java.util.Map Mapa} svih komandi koje postoje u sistemu.
	 * 
	 */
	private Map<String, ExGCommand> commands = new HashMap<String, ExGCommand>();

	/**
	 * Dodaje komandu koju može da izvrši neki
	 * {@link tk.exgerm.core.plugin.IAgorithm algorithm}.
	 * 
	 * @param command
	 *            Komanda koja se dodaje
	 */
	public void addCommand(ExGCommand command)
			throws ExGCommandAlreadyExistException {
		if (commands.containsKey(command.getKeyword()))
			throw new ExGCommandAlreadyExistException(
					"Pokušaj dodavanja komande koja već postoji: " + command);
		commands.put(command.getKeyword(), command);
	}

	/**
	 * Uklanja komandu iz registra
	 * 
	 * @param command
	 *            Komanda koja se ukljanja
	 */
	public void removeCommand(ExGCommand command) {
		commands.remove(command.getKeyword());
	}

	/**
	 * Vraća komandu
	 * 
	 * @param command
	 *            Naziv komande
	 * @return komanda
	 */
	public ExGCommand getCommand(String command) {
		return commands.get(command);
	}

	/**
	 * Vraća sve komande koje su registrovane u sistemu.
	 * 
	 * @return Sve registrovane komande
	 */
	public List<ExGCommand> getAllCommand() {
		return new ArrayList<ExGCommand>(commands.values());
	}
}
