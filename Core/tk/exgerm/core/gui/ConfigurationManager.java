package tk.exgerm.core.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import tk.exgerm.core.Core;
import tk.exgerm.core.plugin.ExGConfigPanel;
import tk.exgerm.core.service.ICoreContext;

public class ConfigurationManager extends ExGermDialog implements
		ComponentListener {

	private static final long serialVersionUID = 8221442124577336297L;

	private JPanel buttonBar;
	private JPanel carddPanel;
	private ButtonGroup bg = new ButtonGroup();
	private ExGButton selectedButton = null;

	private ArrayList<ExGConfigPanel> panels = new ArrayList<ExGConfigPanel>();

	public ConfigurationManager(Frame d, boolean b) {

		super(d, b);
		addComponentListener(this);
		setDialogMode(ExGermDialog.OK_CANCEL_DIALOG);
		setMinimumSize(new Dimension(800, 500));
		setTitle("Configuration");
		buttonBar = new JPanel();
		buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.PAGE_AXIS));
		buttonBar.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1,
				Color.BLACK), new EmptyBorder(5, 5, 5, 5)));
		buttonBar.setBackground(Color.WHITE);
		getContentPanel().add(buttonBar, BorderLayout.WEST);

		carddPanel = new JPanel();
		carddPanel.setLayout(new CardLayout());
		carddPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
		getContentPanel().add(carddPanel, BorderLayout.CENTER);

		centerOnScreen();
	}

	private JButton createButton(String text, Icon icon) {
		ExGButton button = new ExGButton(this, text, icon);
		bg.add(button);
		return button;
	}

	public void addConfigurationPanel(String componentName,
			ExGConfigPanel configPanel) {

		buttonBar.removeAll();
		configPanel.getPanel().setName(componentName);
		panels.add(configPanel);
		
		Collections.sort(panels, comparator);

		for (ExGConfigPanel panel : panels) {
			buttonBar.add(createButton(panel.getTitle(), panel.getIcon()));
		}
		
		carddPanel.add(configPanel.getPanel(), configPanel.getTitle());
	}

	Comparator<ExGConfigPanel> comparator = new Comparator<ExGConfigPanel>() {

		@Override
		public int compare(ExGConfigPanel o1, ExGConfigPanel o2) {
			return o1.getPosition() - o2.getPosition();
		}

	};

	public void removeConfigurationPanel(ExGConfigPanel configPanel) {
		for (Component c : buttonBar.getComponents()) {

			if (((AbstractButton) c).getText().equals(configPanel.getTitle()))
				buttonBar.remove(c);
		}
		panels.remove(configPanel);
		carddPanel.remove(configPanel.getPanel());
	}

	public void setSelectedButton(ExGButton button) {
		CardLayout cl = (CardLayout) (carddPanel.getLayout());
		cl.show(getCarddPanel(), button.getText());

		bg.setSelected(button.getModel(), true);
		selectedButton = button;
	}

	private void updateButtonsSize() {
		int maxWidth = 0;
		for (Component c : buttonBar.getComponents()) {
			JButton button = (JButton) c;
			if (button.getWidth() > maxWidth)
				maxWidth = button.getWidth();
		}

		for (Component c : buttonBar.getComponents()) {
			JButton button = (JButton) c;
			button.setMaximumSize(new Dimension(maxWidth, button.getHeight()));
			button
					.setPreferredSize(new Dimension(maxWidth, button
							.getHeight()));
		}

		buttonBar.revalidate();
	}

	@Override
	public void cancel() {
	}

	@Override
	public void ok() {
		for (Component c : carddPanel.getComponents()) {
			ExGConfigPanel configPanel = (ExGConfigPanel) c;
			configPanel.save();
			
			Core.getInstance().getEventDispatcher().raiseEvent(
					ICoreContext.CONFIGURATION_CHANGED, ((JPanel)configPanel).getName());
		}
	}

	public JPanel getCarddPanel() {
		return carddPanel;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
		if (buttonBar.getComponentCount() == 0) {
			buttonBar.setPreferredSize(new Dimension(100, 300));
			buttonBar.revalidate();
		} else {
			buttonBar.setPreferredSize(null);
			updateButtonsSize();
			if (selectedButton == null) {
				ExGButton button = (ExGButton) buttonBar.getComponent(0);
				setSelectedButton(button);
			}
		}
	}
}
