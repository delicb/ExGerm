package tk.exgerm.core.gui;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import tk.exgerm.core.plugin.ExGStatusbarContent;

/**
 * Klasa za prikaz statusnih poruka, pomoćnih dugmića i bilo čega drugog što
 * nasleđuje klasu {@link javax.swing.JComponent}
 * 
 * @author Tim 2
 */
public class StatusBar extends JPanel {

	private static final long serialVersionUID = -916885483705249840L;

	/**
	 * Mapa svih komponenti za prikaz po delu status bara gde se prikazuju.
	 */
	protected Map<Integer, JPanel> components = new HashMap<Integer, JPanel>();

	public StatusBar() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		JPanel p;

		p = new JPanel();
		p.setBorder(new BevelBorder(BevelBorder.RAISED));
		components.put(ExGStatusbarContent.LEFT, p);
		this.add(p);

		p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p.setBorder(new BevelBorder(BevelBorder.RAISED));
		components.put(ExGStatusbarContent.CENTER, p);
		this.add(p);

		p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p.setBorder(new BevelBorder(BevelBorder.RAISED));
		components.put(ExGStatusbarContent.RIGHT, p);
		this.add(p);
	}

	public void addComponent(ExGStatusbarContent content) {
		this.components.get(content.getPosition()).add(content.getContent());
		updateUI();
	}

	public void removeComponent(ExGStatusbarContent content) {
		this.components.get(content.getPosition()).remove(content.getContent());
		updateUI();
	}
	
}
