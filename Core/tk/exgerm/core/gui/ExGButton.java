package tk.exgerm.core.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ExGButton extends JButton implements ActionListener{

	private static final long serialVersionUID = 3927853128326962460L;

	private static Color blueishBackgroundOver = new Color(224, 232, 246);
	private static Color blueishBorderOver = new Color(152, 180, 226);

	private static Color blueishBackgroundSelected = new Color(193, 210, 238);
	private static Color blueishBorderSelected = new Color(49, 106, 197);

	private ConfigurationManager configMan;
	
	public ExGButton(ConfigurationManager configMan, String text, Icon icon) {
		this.configMan = configMan;
		
		setText(text);
		setIcon(icon);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setRolloverEnabled(true);
		setContentAreaFilled(false);
		setFocusPainted(false);
		addActionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		JComponent c = this;
		AbstractButton button = (AbstractButton) c;
		if (button.getModel().isRollover() || button.getModel().isArmed()
				|| button.getModel().isSelected()) {
			Color oldColor = g.getColor();
			if (button.getModel().isSelected()) {
				g.setColor(blueishBackgroundSelected);
			} else {
				g.setColor(blueishBackgroundOver);
			}
			g.fillRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

			if (button.getModel().isSelected()) {
				g.setColor(blueishBorderSelected);
			} else {
				g.setColor(blueishBorderOver);
			}
			g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
			
			g.setColor(oldColor);
		}

		super.paint(g);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		configMan.setSelectedButton(this);
	}

}
