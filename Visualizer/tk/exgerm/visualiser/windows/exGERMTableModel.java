package tk.exgerm.visualiser.windows;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class exGERMTableModel extends DefaultTableModel implements TableModel {
	
	public exGERMTableModel(String[] collumnNames){
		super(collumnNames,0);
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		super.addTableModelListener(arg0);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return super.getColumnClass(arg0);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int index) {
		switch(index){
			case 0 : return "Name";
			case 1 : return "Value";
			case 2 : return "Type";
			default : return null;
		}
	}

	@Override
	public int getRowCount() {
		return super.getRowCount();
	}
	
	public int getRowIndex(String name){
		int index = -1;
		for(int i = 0; i != getRowCount(); i++){
			if(getValueAt(i, 0).equals(name))
				index = i;
		}
		return index;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return super.getValueAt(arg0, arg1);
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		if(getColumnName(arg1).equals("Type") || getColumnName(arg1).equals("Name"))
			return false;
		else 
			return true;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		super.removeTableModelListener(arg0);
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		super.setValueAt(arg0, arg1, arg2);
	}
	
	public void setValue(String attName, Object attValue){
		int colIndex = 1;
		int rowIndex = getRowIndex(attName);
		if(rowIndex != -1)
			super.setValueAt(attValue, rowIndex, colIndex);
	}
	
	public void insertRow(String attName, Object attValue, String attType){
		if(attType.equals("String"))
			super.insertRow(getRowCount(), new Object[]{attName, (String)attValue, attType});
		else if(attType.equals("Integer"))
			super.insertRow(getRowCount(), new Object[]{attName, (Integer)attValue, attType});
		else if(attType.equals("Double"))
			super.insertRow(getRowCount(), new Object[]{attName, (Double)attValue, attType});
		else if(attType.equals("Boolean"))
			super.insertRow(getRowCount(), new Object[]{attName, (Boolean)attValue, attType});
	}

}
