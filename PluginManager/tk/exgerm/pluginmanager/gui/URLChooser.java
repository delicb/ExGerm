package tk.exgerm.pluginmanager.gui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * Standardni dijalog za unos URL-a sa kojeg se instalira komponenta.
 */
@SuppressWarnings("serial")
public class URLChooser extends JDialog {

	private JTextField textField;
	String URL = "";

	public URLChooser() {
		super();

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		setResizable(false);
		setTitle("Plugin web install");
		setModal(true);
		setAlwaysOnTop(true);
		getContentPane().setLayout(null);
		setBounds(100, 100, 491, 198);
		
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}

		textField = new JTextField();
		textField.setFont(new Font("", Font.PLAIN, 14));
		textField.setBounds(25, 72, 434, 32);
		getContentPane().add(textField);

		final JLabel enterPluginRepositoryLabel = new JLabel();
		enterPluginRepositoryLabel.setText("Enter plugin repository URL:");
		enterPluginRepositoryLabel.setBounds(25, 22, 221, 16);
		getContentPane().add(enterPluginRepositoryLabel);

		ActionListener ok = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL = textField.getText().trim();
				URLChooser.this.setVisible(false);
			}
		};

		ActionListener cancel = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL = "";
				URLChooser.this.setVisible(false);
			}
		};

		final JButton okButton = new JButton();
		okButton.addActionListener(ok);
		okButton
				.registerKeyboardAction(ok, "EnterKey", KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_IN_FOCUSED_WINDOW);
		okButton.setText("OK");
		okButton.setBounds(25, 130, 106, 26);
		getContentPane().add(okButton);

		final JButton cancelButton = new JButton();
		cancelButton.addActionListener(cancel);
		cancelButton.registerKeyboardAction(cancel, "EscapeKey", KeyStroke
				.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(356, 130, 106, 26);
		getContentPane().add(cancelButton);

		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);
	}

	public String getURL() {
		return URL;
	}
}
