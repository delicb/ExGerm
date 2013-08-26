package tk.exgerm.core.register;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.exception.ExGIteratorAlreadyExsistException;
import tk.exgerm.core.exception.ExGIteratorDoesNotExsistException;
import tk.exgerm.core.plugin.ExGIterator;

/**
 * Registar svih postojećih iteratora.
 * 
 * @author Tim 2
 */
public class IteratorRegister {
	protected Map<String, ExGIterator> iterators = new HashMap<String, ExGIterator>();

	public void addIterator(ExGIterator iterator)
			throws ExGIteratorAlreadyExsistException {
		if (iterators.containsKey(iterator.getName()))
			throw new ExGIteratorAlreadyExsistException("Iterator with name "
					+ iterator.getName() + " is already registerd");

		iterators.put(iterator.getName(), iterator);

	}

	public ExGIterator getIterator(String name)
			throws ExGIteratorDoesNotExsistException {
		if (!iterators.containsKey(name))
			throw new ExGIteratorDoesNotExsistException("Iterator with name "
					+ name + " does not exist");
		try {
			return iterators.get(name).getClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Interanal exception");
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Interanal exception");
		}
	}

	public void removeIterator(String name) {
		iterators.remove(name);
	}
}
