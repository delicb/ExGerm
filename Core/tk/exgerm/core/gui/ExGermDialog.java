package tk.exgerm.core.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

/**
 * Apstraktna klasa koja realizuje dijalog. Dijalog moze biti OK_CANCEL_DIALOG
 * ili CLOSE_DIALOG. Dovaljno je implementirati metode ok() i cancel().
 * 
 * @author Tim 2
 * 
 */
public abstract class ExGermDialog extends JDialog {

	private static final long serialVersionUID = -527882707438328195L;
	
	public final static int OK_CANCEL_DIALOG = 0;
	public final static int CLOSE_DIALOG = 1;

	private JPanel dialogPane;
	private JPanel contentPanel;
	private JPanel buttonBar;

	private boolean cancelClicked;

	private JButton okButton;
	private JButton cancelOrCloseButton;
	private int mode;

	private Action okAction = new AbstractAction() {
		private static final long serialVersionUID = 3223285367854931864L;

		public void actionPerformed(ActionEvent e) {
			ok();
			cancelClicked = false;
			setVisible(false);
		}
	};

	private Action cancelOrCloseAction = new AbstractAction() {
		private static final long serialVersionUID = 62168627176122292L;

		public void actionPerformed(ActionEvent e) {
			cancel();
			cancelClicked = true;
			setVisible(false);
		}
	};

	public ExGermDialog() throws HeadlessException {
		super();
		buildUI();
	}

	public ExGermDialog(Dialog owner) throws HeadlessException {
		super(owner);
		buildUI();
	}

	public ExGermDialog(Dialog owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		buildUI();
	}

	public ExGermDialog(Frame owner) throws HeadlessException {
		super(owner);
		buildUI();
	}

	public ExGermDialog(Frame owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		buildUI();
	}

	public ExGermDialog(Dialog owner, String title) throws HeadlessException {
		super(owner, title);
		buildUI();
	}

	public ExGermDialog(Dialog owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		buildUI();
	}

	public ExGermDialog(Frame owner, String title) throws HeadlessException {
		super(owner, title);
		buildUI();
	}

	public ExGermDialog(Frame owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		buildUI();
	}

	public ExGermDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		buildUI();
	}

	public final Container getContentPanel() {
		return contentPanel;
	}

	public final Container getButtonBar() {
		return buttonBar;
	}

	public boolean ask() {
		setVisible(true);
		dispose();
		return !cancelClicked;
	}

	public abstract void ok();

	public abstract void cancel();

	public void setDialogMode(int mode) {
		if (!(mode == OK_CANCEL_DIALOG || mode == CLOSE_DIALOG)) {
			throw new IllegalArgumentException("invalid dialog mode");
		}

		if (okButton != null) {
			buttonBar.remove(okButton);
			okButton = null;
		}
		if (cancelOrCloseButton != null) {
			buttonBar.remove(cancelOrCloseButton);
			cancelOrCloseButton = null;
		}

		switch (mode) {
		case OK_CANCEL_DIALOG:
			okButton = new JButton();
			okButton.setText("OK");
			buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0,
					0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

			okButton.addActionListener(okAction);

			cancelOrCloseButton = new JButton();
			cancelOrCloseButton.setText("Cancel");
			buttonBar.add(cancelOrCloseButton, new GridBagConstraints(2, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			cancelOrCloseButton.addActionListener(cancelOrCloseAction);
			getRootPane().setDefaultButton(okButton);
			break;
		case CLOSE_DIALOG:
			cancelOrCloseButton = new JButton("close");

			buttonBar.add(cancelOrCloseButton, new GridBagConstraints(2, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			cancelOrCloseButton.addActionListener(cancelOrCloseAction);
			break;
		}

		this.mode = mode;
	}

	public int getDialogMode() {
		return mode;
	}

	public void centerOnScreen() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = this.getSize();
		this.setLocation((screenSize.width - size.width) / 2,
				(screenSize.height - size.height) / 2);
	}

	private void buildUI() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		buttonBar = new JPanel();

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		dialogPane.setLayout(new BorderLayout());

		contentPanel.setLayout(new BorderLayout());
		dialogPane.add(contentPanel, BorderLayout.CENTER);

		buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
		buttonBar.setLayout(new GridBagLayout());
		((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] { 0,
				85, 80 };
		((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] {
				1.0, 0.0, 0.0 };

		dialogPane.add(buttonBar, BorderLayout.SOUTH);
		contentPane.add(dialogPane, BorderLayout.CENTER);

		((JComponent) dialogPane).registerKeyboardAction(cancelOrCloseAction,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

		setDialogMode(OK_CANCEL_DIALOG);
		centerOnScreen();
	}

}
