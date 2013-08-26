package tk.exgerm.pluginmanager.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.osgi.framework.BundleException;

import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.pluginmanager.ExGOSGiUpdateException;
import tk.exgerm.pluginmanager.PluginManager;
import tk.exgerm.pluginmanager.ExGOSGiUpdateException.ErrorType;
import tk.exgerm.pluginmanager.gui.PluginsTableModel.UpdateStatus;

/*
 * JDialog kreiran sa http://www.instantiations.com/windowbuilder/
 */
/**
 * Tip akcije koji se izvršava.
 */
enum ActionType {
	START, STOP, UNINSTALL, UPDATE
}

/**
 * Tip progres bara koji je prikazan
 */
enum ProgressType {
	SINGLE_UPDATE, MULTI_UPDATE, DOWNLOAD
}

/**
 * Glavni prozor PM-a
 * 
 */
@SuppressWarnings("serial")
public class PluginManagerMainWindow extends JDialog {

	private JTable table;
	private PluginManager manager;
	private PluginsTableModel tableModel;
	private boolean cancelFlag = false;
	final JProgressBar progressBar;
	final JLabel mainLabel;
	final JLabel currentPluginLabel;
	final JPanel updatePanel;
	private PluginFileChooser fileChooser;
	private URLChooser URLChooserDialog;

	private HashMap<PluginsTableModel.UpdateStatus, ImageIcon> stateImages = new HashMap<PluginsTableModel.UpdateStatus, ImageIcon>();

	public PluginManagerMainWindow(PluginManager manager) {
		super(null, ModalityType.DOCUMENT_MODAL);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_F1
								&& e.getID() == KeyEvent.KEY_PRESSED
								&& PluginManagerMainWindow.this.isActive()) {
							e.consume();
							PluginManagerMainWindow.this.manager
									.getCoreContext().raise(
											ExGHelp.HELP_REQUESTED,
											PluginManager.componentName,
											"Početna stranica", true);
						}
						return false;
					}
				});

		this.manager = manager;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		setResizable(false);
		setTitle("Plugin Manager");
		setName("Plugin Manager");
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}
		getContentPane().setLayout(null);

		stateImages.put(PluginsTableModel.UpdateStatus.UNCHECKED,
				getImageIcon("unchecked.png"));
		stateImages.put(PluginsTableModel.UpdateStatus.UPDATED,
				getImageIcon("updated.png"));
		stateImages.put(PluginsTableModel.UpdateStatus.UPTODATE,
				getImageIcon("uptodate.png"));
		stateImages.put(PluginsTableModel.UpdateStatus.ERROR,
				getImageIcon("error.png"));

		final JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(10, 10, 748, 500);
		getContentPane().add(panel);

		tableModel = new PluginsTableModel(manager, this);

		table = new JTable(tableModel);

		table.setFont(new Font("Consolas", Font.PLAIN, 11));
		table.setBounds(0, 63, 634, 437);
		panel.add(table);
		table.setBorder(new LineBorder(Color.black, 1, false));
		table.setRowHeight(24);
		table.getColumnModel().getColumn(4).setCellRenderer(stateRender);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		packAllCollumns();

		final JButton buttonOfflineInstall = new JButton();
		buttonOfflineInstall.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (fileChooser == null)
					fileChooser = new PluginFileChooser(new File(System
							.getProperty("user.home")),
							JFileChooser.OPEN_DIALOG, "Install New Plugin");
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						PluginManagerMainWindow.this.manager
								.installFilesysetem(fileChooser
										.getSelectedFile().getCanonicalPath());
						tableModel.addNewPlugin();
						tableModel.refresh();
					} catch (BundleException e1) {
						JOptionPane
								.showMessageDialog(null,
										"OSGi error occured:\n"
												+ e1.getMessage(),
										"Installation Error",
										JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null,
								"Filesystem Error.\n"
										+ e1.getMessage(),
								"Filesystem Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		buttonOfflineInstall.setBounds(640, 472, 108, 28);
		panel.add(buttonOfflineInstall);
		buttonOfflineInstall.setToolTipText("Install from local drive...");
		buttonOfflineInstall.setText("Offline Install...");

		final JButton buttonWebInstall = new JButton();
		buttonWebInstall.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (URLChooserDialog == null)
					URLChooserDialog = new URLChooser();
				URLChooserDialog.setVisible(true);

				if (!URLChooserDialog.getURL().equals("")) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					setProgressType(ProgressType.DOWNLOAD);
					try {
						PluginManagerMainWindow.this.manager
								.installURL(URLChooserDialog.getURL());
						tableModel.addNewPlugin();
						tableModel.refresh();
					} catch (ExGOSGiUpdateException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"Install Error" + " Error",
								JOptionPane.ERROR_MESSAGE);
					}
					updatePanel.setVisible(false);
					cancelFlag = false;
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
		buttonWebInstall.setBounds(640, 438, 108, 28);
		panel.add(buttonWebInstall);
		buttonWebInstall.setToolTipText("Install from internet...");
		buttonWebInstall.setText("Web Install...");

		final JButton buttonUninstall = new JButton();
		buttonUninstall.setToolTipText("Uninstall selected component(s)");
		buttonUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				tableModel.removePlugin(table.getSelectedRows());
				performOSGIAction(ActionType.UNINSTALL);
			}
		});
		buttonUninstall.setBounds(640, 163, 108, 28);
		panel.add(buttonUninstall);
		buttonUninstall.setText("Uninstall");

		final JButton buttonStop = new JButton();
		buttonStop.setToolTipText("Stop selected plugin(s)");
		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				performOSGIAction(ActionType.STOP);
			}
		});
		buttonStop.setBounds(640, 97, 108, 28);
		panel.add(buttonStop);
		buttonStop.setText("Stop");

		final JButton buttonStart = new JButton();
		buttonStart.setToolTipText("Start selected plugin(s)");
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				performOSGIAction(ActionType.START);
			}
		});
		buttonStart.setBounds(640, 63, 108, 28);
		panel.add(buttonStart);
		buttonStart.setText("Start");

		final JLabel labelTitle = new JLabel();
		labelTitle.setFont(new Font("", Font.BOLD, 12));
		labelTitle.setBounds(0, 10, 522, 28);
		panel.add(labelTitle);
		labelTitle.setText("List of all installed plugins");

		final JButton updateSelectedButton = new JButton();
		updateSelectedButton.setToolTipText("Update selected plugin(s)");
		updateSelectedButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				updateSelected();
				setCursor(Cursor.getDefaultCursor());
			}
		});
		updateSelectedButton.setText("Update");
		updateSelectedButton.setBounds(640, 131, 106, 26);
		panel.add(updateSelectedButton);

		final JButton closeButton = new JButton();
		ActionListener close = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PluginManagerMainWindow.this.dispose();
			}
		};
		closeButton.addActionListener(close);

		closeButton.registerKeyboardAction(close, "EscapeKey", KeyStroke
				.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		closeButton.setBounds(650, 543, 108, 26);
		getContentPane().add(closeButton);
		closeButton.setText("Close");

		final JButton buttonUpdates = new JButton();
		buttonUpdates.setToolTipText("Update all plugins");
		buttonUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				table.addRowSelectionInterval(0, getTableRowCount() - 1);
				updateSelected();
				setCursor(Cursor.getDefaultCursor());
			}
		});
		buttonUpdates.setBounds(10, 543, 108, 26);
		getContentPane().add(buttonUpdates);
		setSize(new Dimension(771, 602));
		buttonUpdates.setText("Update All");

		updatePanel = new JPanel();
		updatePanel.setLayout(null);
		updatePanel.setBounds(160, 520, 445, 53);
		updatePanel.setVisible(false);
		getContentPane().add(updatePanel);

		mainLabel = new JLabel();
		mainLabel.setBounds(10, 5, 122, 16);
		updatePanel.add(mainLabel);
		mainLabel.setText("New JLabel");

		currentPluginLabel = new JLabel();
		currentPluginLabel.setBounds(138, 5, 218, 16);
		updatePanel.add(currentPluginLabel);
		currentPluginLabel.setText("New JLabel");

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 29, 425, 14);
		updatePanel.add(progressBar);

		final JButton cancelButton = new JButton();
		cancelButton.setBounds(362, 0, 73, 26);
		updatePanel.add(cancelButton);
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				cancelFlag = true;
				setVisible(false);
			}
		});

		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);
		packAllCollumns();
	}

	/**
	 * @see DefaultTableCellRenderer
	 */
	private DefaultTableCellRenderer stateRender = new DefaultTableCellRenderer() {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			JLabel label = (JLabel) super.getTableCellRendererComponent(table,
					null, isSelected, hasFocus, row, column);
			if (value instanceof PluginsTableModel.UpdateStatus) {
				label.setToolTipText(value.toString());
				label.setIcon(stateImages
						.get((PluginsTableModel.UpdateStatus) value));
			}

			return this;
		}
	};

	private ImageIcon getImageIcon(String fileName) {
		return new ImageIcon(getClass().getResource("images/" + fileName));
	}

	/**
	 * Izvršava određenu OSGI akciju nad selektovanim komponentama
	 * 
	 * @param action
	 *            koju je potrebno izvršiti tipa {@link ActionType}
	 */
	private void performOSGIAction(ActionType action) {
		int[] rows = table.getSelectedRows();
		String errorMessage = "";
		String actionError = "";
		for (int i = 0; i < rows.length; i++) {
			try {
				if (action.equals(ActionType.STOP)) {
					PluginManagerMainWindow.this.manager.getBundle(
							Integer.valueOf(table.getValueAt(rows[i], 0)
									.toString())).stop();
					actionError = "Stopping";
				} else if (action.equals(ActionType.START)) {
					PluginManagerMainWindow.this.manager.getBundle(
							Integer.valueOf(table.getValueAt(rows[i], 0)
									.toString())).start();
					actionError = "Starting";
				} else if (action.equals(ActionType.UNINSTALL)) {
					PluginManagerMainWindow.this.manager.getBundle(
							Integer.valueOf(table.getValueAt(rows[i], 0)
									.toString())).uninstall();
					actionError = "Uninstalling";
				} else if (action.equals(ActionType.UPDATE)) {
					actionError = "Update";
					manager.updateBundle(Integer.valueOf(table.getValueAt(
							rows[i], 0).toString()));
					tableModel.refreshUpdateSymbol(rows[i],
							UpdateStatus.UPDATED);
				}
			} catch (NumberFormatException e) {
			} catch (BundleException e) {
				errorMessage += "\n" + e.getMessage();
			} catch (ExGOSGiUpdateException e) {
				ErrorType type = e.getType();
				if (type.equals(ErrorType.ALLREADY_UPTODATE)) {
					tableModel.refreshUpdateSymbol(rows[i],
							UpdateStatus.UPTODATE);
				} else if (type.equals(ErrorType.BAD_URL)
						|| type.equals(ErrorType.BUNDLE_ERROR)
						|| type.equals(ErrorType.NO_REPOSITORY)) {
					errorMessage += "\n" + e.getBundle().getSymbolicName()
							+ ": " + e.getMessage();
					tableModel.refreshUpdateSymbol(rows[i], UpdateStatus.ERROR);
				} else if (type.equals(ErrorType.CONNECTION_ERROR)) {
					actionError = "Connection";
					errorMessage += "There is no internet connection, or it is not working properly!";
					break;
				} else if (type.equals(ErrorType.IO_ERROR)) {
					actionError = "IO";
					errorMessage += "\n" + e.getBundle().getSymbolicName()
							+ ": " + e.getMessage();
					tableModel.refreshUpdateSymbol(rows[i], UpdateStatus.ERROR);
				}
			}
		}
		tableModel.refresh();
		// vršim selekciju komponenti koje su ranije bile selektovane
		// osim ako je UNINSTALL jer onda te komponente ne postoje na listi
		if (!action.equals(ActionType.UNINSTALL))
			for (int i = 0; i < rows.length; i++)
				table.addRowSelectionInterval(rows[i], rows[i]);
		// ukoliko je došlo do greške, njen ispis
		if (errorMessage.length() > 0)
			JOptionPane.showMessageDialog(null, errorMessage, actionError
					+ " Error", JOptionPane.ERROR_MESSAGE);
	}

	public String check4Updates(int row) {
		table.removeRowSelectionInterval(0, getTableRowCount() - 1);
		table.addRowSelectionInterval(row, row);
		performOSGIAction(ActionType.UPDATE);
		return table.getValueAt(row, 1).toString();
	}

	public void updateSelected() {
		if (getSelectedPluginIDs().length == 1)
			setProgressType(ProgressType.SINGLE_UPDATE);
		else {
			progressBar.setMaximum(getSelectedPluginIDs().length);
			setProgressType(ProgressType.MULTI_UPDATE);
		}
		for (int i : getSelectedPluginIDs()) {
			currentPluginLabel.setText(PluginManagerMainWindow.this
					.check4Updates(i));
			progressBar.setValue(progressBar.getValue() + 1);
			if (cancelFlag) {
				updatePanel.setVisible(false);
				cancelFlag = false;
				return;
			}
		}
		updatePanel.setVisible(false);
	}

	public int getTableRowCount() {
		return table.getRowCount();
	}

	/**
	 * Postavlja novi tip progres bara i prikazuje ga.
	 * 
	 * @param type
	 *            tip progres bara tipa {@link ProgressType}
	 */
	private void setProgressType(ProgressType type) {
		if (type.equals(ProgressType.SINGLE_UPDATE)) {
			progressBar.setIndeterminate(true);
			mainLabel.setText("Plugin Update");
			currentPluginLabel.setText("");
		} else if (type.equals(ProgressType.MULTI_UPDATE)) {
			progressBar.setIndeterminate(false);
			mainLabel.setText("Plugins Update:");
			currentPluginLabel.setText("");
		} else if (type.equals(ProgressType.DOWNLOAD)) {
			progressBar.setIndeterminate(true);
			mainLabel.setText("Downloading new plugin...");
			currentPluginLabel.setText("");
		}
		updatePanel.setVisible(true);
	}

	public int[] getSelectedPluginIDs() {
		return table.getSelectedRows();
	}

	public JTable getTable() {
		return table;
	}

	public void packColumns(JTable table, int margin) {
		for (int c = 0; c < table.getColumnCount(); c++) {
			packColumn(table, c, 2);
		}
	}

	/**
	 * Vrši prilagođavanje širina kolona spram podataka u njima.
	 * 
	 * @param table
	 *            sa kojom radi
	 * @param vColIndex
	 *            index kolone
	 * @param margin
	 *            margina
	 */
	public void packColumn(JTable table, int vColIndex, int margin) {
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
				.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;

		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(table, col
				.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		// Get maximum width of column data
		for (int r = 0; r < table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(table, table
					.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		// Add margin
		width += 2 * margin;

		// Set the width
		col.setPreferredWidth(width);
	}

	/**
	 * Prilagođava širine svih kolona u tabeli {@link PluginManagerMainWindow}
	 * prozora
	 */
	public void packAllCollumns() {
		for (int i = 0; i != table.getColumnCount(); i++) {
			packColumn(table, i, 0);
		}
	}
}
