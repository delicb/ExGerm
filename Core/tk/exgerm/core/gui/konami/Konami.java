package tk.exgerm.core.gui.konami;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Konami implements KeyEventDispatcher {

	Vector<Integer> seq = new Vector<Integer>();
	Vector<Integer> s = new Vector<Integer>();

	public Konami() {
		seq.add(KeyEvent.VK_UP);
		seq.add(KeyEvent.VK_UP);
		seq.add(KeyEvent.VK_DOWN);
		seq.add(KeyEvent.VK_DOWN);
		seq.add(KeyEvent.VK_LEFT);
		seq.add(KeyEvent.VK_RIGHT);
		seq.add(KeyEvent.VK_LEFT);
		seq.add(KeyEvent.VK_RIGHT);
		seq.add(KeyEvent.VK_B);
		seq.add(KeyEvent.VK_A);
		seq.add(KeyEvent.VK_ENTER);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED) {
			if (seq.contains(e.getKeyCode())) {
				s.add(e.getKeyCode());
				if (! matchSoFar()) {
					s.clear();
				}
				else
					if (match()) {
						JOptionPane.showMessageDialog(null, "Wise guy, a?");
						s.clear();
					}
			}
			else {
				s.clear();
			}
				
		}
		return false;
	}
	
	private boolean matchSoFar() {
		for (int i = 0; i < s.size(); i++) {
			if (s.get(i) != seq.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean match() {
		return matchSoFar() && seq.size() == s.size();
	}

}
