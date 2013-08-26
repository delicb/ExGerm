package tk.exgerm.core.gui.tabmanager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Predstavlja dugme za zatvaranje tab-a
 * 
 * @author Tim 2
 */
public class CloseButton extends JButton {

	private static final long serialVersionUID = 1723665346577102822L;

	/**
	 * Konstruktor
	 */
	public CloseButton() {
		int size = 17;
		setPreferredSize(new Dimension(size, size));
		setToolTipText("Close");
		setUI(new BasicButtonUI());
		// transparentno
		setContentAreaFilled(false);
		setFocusable(false);
		setBorder(BorderFactory.createEtchedBorder());
		setBorderPainted(false);
		setRolloverEnabled(true);
	}

	public void updateUI() {}

	/**
	 * Iscrtava X na dugmetu
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		// pomeri ako je pritisnuto
		if (getModel().isPressed()) {
			g2.translate(1, 1);
		}
		g2.setStroke(new BasicStroke(3, BasicStroke.JOIN_ROUND,
				BasicStroke.CAP_ROUND));
		g2.setColor(Color.BLACK);
		if (getModel().isRollover()) {
			g2.setColor(Color.RED);
		}

		// rastojanje od ivica dugmeta
		int delta = 4;
		g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta
				- 1);
		g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta
				- 1);
		g2.dispose();
	}
}
