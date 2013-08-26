package tk.exgerm.pluginmanager.gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import tk.exgerm.pluginmanager.PluginManager;

/**
 * TableModel za manipulaciju sa podacima tabele {@link PluginManagerMainWindow}
 * prozora
 */
@SuppressWarnings("serial")
public class PluginsTableModel extends DefaultTableModel implements TableModel {

	/**
	 * Trenutni status komponente
	 */
	public static enum UpdateStatus {
		UNCHECKED, UPTODATE, UPDATED, ERROR
	}

	private Vector<String> header;
	private PluginManager manager;
	private PluginManagerMainWindow mw;
	private ArrayList<UpdateStatus> updateState;
	private boolean init = true;

	public PluginsTableModel(PluginManager manager, PluginManagerMainWindow mw) {
		super();
		this.manager = manager;
		this.mw = mw;
		header = new Vector<String>();
		updateState = new ArrayList<UpdateStatus>();
		header.add("ID");
		header.add("Name");
		header.add("Location");
		header.add("Status");
		header.add("Update");
		refresh();
		init = false;
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		super.addTableModelListener(l);

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return header.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return header.get(columnIndex);
	}

	@Override
	public int getRowCount() {
		return super.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return super.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		super.removeTableModelListener(l);

	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		super.setValueAt(value, rowIndex, columnIndex);
	}

	/**
	 * Vrši osvežavanje podataka o svim bundle-ovima u sistemu.
	 */
	public void refresh() {
		while (getRowCount() > 0)
			removeRow(0);
		for (Vector<String> bundle : manager.getBundles()) {
			insertRow(getRowCount(), bundle);
			if (init) {
				updateState.add(getRowCount() - 1, UpdateStatus.UNCHECKED);
				setValueAt(UpdateStatus.UNCHECKED, getRowCount() - 1, 4);
			} else {
				setValueAt(updateState.get(getRowCount() - 1),
						getRowCount() - 1, 4);
				mw.packAllCollumns();
			}
		}

	}

	/**
	 * Osvežava {@link UpdateStatus} određene komponente
	 * 
	 * @param tableRow
	 *            red komponente čiji status osvežavamo
	 * @param status
	 *            trenutno stanje {@link UpdateStatus}
	 */
	public void refreshUpdateSymbol(int tableRow, UpdateStatus status) {
		updateState.set(tableRow, status);
	}

	/**
	 * Dodaje novi red u tabelu u kojem će biti nova komponentu i postavlja
	 * default status {@link UpdateStatus} unchecked
	 */
	public void addNewPlugin() {
		updateState.add(getRowCount(), UpdateStatus.UNCHECKED);
		setValueAt(UpdateStatus.UNCHECKED, getRowCount() - 1, 4);
	}

	/**
	 * Uklanja status deinstaliranih komponenti iz tabele
	 * 
	 * @param tableRows
	 */
	public void removePlugin(int[] tableRows) {
		for (int row : tableRows) {
			updateState.remove(row);
		}
	}
}
