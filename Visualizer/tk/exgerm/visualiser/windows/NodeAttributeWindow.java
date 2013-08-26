package tk.exgerm.visualiser.windows;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class NodeAttributeWindow extends JDialog {

	private ICoreContext context;
	private Container container = getContentPane();
	private INode node;
	private String name;
	private boolean dialogResult = false;
	private ArrayList<Object> attValues = new ArrayList<Object>();
	private ArrayList<String> attNames = new ArrayList<String>();
	private ArrayList<String> attTypes = new ArrayList<String>();

	private Map<String, Object> attributes;

	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	private Box okBox = Box.createHorizontalBox();
	private JButton btnNewProperty = new JButton("Add a new attribute");
	private JButton btnDelProperty = new JButton("Delete attribute");
	private JButton btnCopyAtt = new JButton("Copy attributes");
	private JButton btnPasteAtt = new JButton("Paste attributes");
	private JTextField tfName = new JTextField(20);
	private JTextField tfGraph = new JTextField(20);

	private String[] collumnNames = new String[] { "Name", "Value", "Type" };
	private exGERMTableModel model = new exGERMTableModel(collumnNames);
	private JTable tbAttributes = new JTable(model);
	private JScrollPane tableScroll = new JScrollPane(tbAttributes);
	private ArrayList<Object[]> commands = new ArrayList<Object[]>();

	protected JLabel lblGraph;
	private static String AA = "ATT_ADDED";
	private static String AR = "ATT_REMOVED";

	public NodeAttributeWindow(INode node, ICoreContext context) {
		this.context = context;
		this.node = node;
		this.name = node.getName();
		this.attributes = node.getAllAttributes();
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}
		
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		setModal(true);
		setTitle("Node properties");
		setLayout(new GridBagLayout());
		setResizable(false);

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 20, 0, 10);

		container.add(new JLabel("Name:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 10, 0, 20);

		container.add(tfName, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		this.lblGraph = new JLabel("Graph:");
		container.add(lblGraph, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 10, 0, 20);

		container.add(tfGraph, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("Attributes:"), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 20, 0, 20);

		tableScroll.setPreferredSize(new Dimension(150, 200));
		container.add(tableScroll, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(btnDelProperty, c);

		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 10, 0, 20);

		container.add(btnNewProperty, c);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(btnCopyAtt, c);

		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 10, 0, 20);

		container.add(btnPasteAtt, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 20, 20);

		okBox.add(btnOK);
		okBox.add(Box.createHorizontalStrut(10));
		okBox.add(btnCancel);
		container.add(okBox, c);

		ActionListener ok = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				enterPressed();
			}
		};
		
		ActionListener cancel = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				escapePressed();
			}
		};

		
		btnOK.addActionListener(ok);       

		btnCancel.addActionListener(cancel);
		
		btnOK.registerKeyboardAction(ok, "EnterKey", 
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0 , false), 
				JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		btnCancel.registerKeyboardAction(cancel, "EscapeKey", 
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0 , false), 
				JComponent.WHEN_IN_FOCUSED_WINDOW); 

		btnDelProperty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexes[] = tbAttributes.getSelectedRows();
				if (indexes.length != 0) {
					for (int i = indexes.length - 1; i != -1; i--) {
						String name = (String) model.getValueAt(i, 0);
						int index = -1;
						for (int j = 0; j != attNames.size(); j++) {
							if (attNames.get(j).equals(name))
								index = j;
						}
						if (index != -1) {
							attNames.remove(index);
							attValues.remove(index);
							attTypes.remove(index);
						}
						model.removeRow(i);
						commands.add(new Object[] { AR, name, null });
					}
				}
			}
		});

		btnNewProperty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewAttributeWindow n = new NewAttributeWindow(attNames);
				n.setVisible(true);
				if(n.isDialogResult()){
					String type = n.getType();
					String name = n.getAttName();
					if (type != null) {
						if (type.equals("String")) {
							model.insertRow(name, "", type);
							attNames.add(name);
							attValues.add("");
							attTypes.add(type);
							commands.add(new Object[] { AA, name, null });
						} else if (type.equals("Integer")) {
							model.insertRow(name, new Integer(0), type);
							attNames.add(name);
							attValues.add(0);
							attTypes.add(type);
							commands.add(new Object[] { AA, name, null });
						} else if (type.equals("Double")) {
							model.insertRow(name, new Double(0), type);
							attNames.add(name);
							attValues.add(0);
							attTypes.add(type);
							commands.add(new Object[] { AA, name, null });
						} else if (type.equals("Boolean")) {
							model.insertRow(name, new Boolean(false), type);
							attNames.add(name);
							attValues.add(false);
							attTypes.add(type);
							commands.add(new Object[] { AA, name, null });
						}
					}
				}
			}
		});

		btnCopyAtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToClipboard();
			}
		});

		btnPasteAtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pasteFromClipboard();
			}
		});

		if (context.getData("nodeattributes") == null)
			btnPasteAtt.setEnabled(false);
		else
			btnPasteAtt.setEnabled(true);

		initializeData();

		pack();
		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);

	}

	public void initializeData() {

		String boolf = "False";
		String boolt = "True";

		tfName.setText(this.name);
		if (this.node.getGraph() != null)
			tfGraph.setText(this.node.getGraph().getName());
		tfGraph.setEditable(false);

		Iterator<String> it = attributes.keySet().iterator();
		while (it.hasNext()) {
			String attName = it.next();
			this.attNames.add(attName);
			Object attValue = this.attributes.get(attName);
			this.attValues.add(attValue);
			String attType = "";
			if (attValue instanceof Boolean || attValue.equals(boolf)
					|| attValue.equals(boolt)
					|| attValue.equals(boolf.toLowerCase())
					|| attValue.equals(boolt.toLowerCase()))
				attType = "Boolean";
			else if (attValue instanceof Integer)
				attType = "Integer";
			else if (attValue instanceof Double)
				attType = "Double";
			else if (attValue instanceof String)
				attType = "String";
			this.model.insertRow(attName, attValue, attType);
			attTypes.add(attType);
		}

	}
	
	public INode getNode(){
		return this.node;
	}

	public String getTfName() {
		return tfName.getText();
	}

	public void setTfName(String name) {
		this.tfName.setText(name);
	}

	public String getTfGraph() {
		return tfGraph.getText();
	}

	public boolean isDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(boolean dialogResult) {
		this.dialogResult = dialogResult;
	}

	public void enterPressed() {
		boolean OK = true;
		OK = checkData();
		if (OK == true) {
			setDialogResult(true);
			makeChanges();
			try {
				node.setName(getTfName().toString());
			} catch (ExGNameConflictException e) {
				e.printStackTrace();
			}
			setVisible(false);
		}
	}

	public void escapePressed() {
		setDialogResult(false);
		setVisible(false);
	}

	public void makeChanges() {
		for (int i = 0; i != commands.size(); i++) {
			if ((commands.get(i))[0].equals(AA)) {
				node.setAttribute((String) (commands.get(i))[1], (commands
						.get(i))[2]);
			} else if ((commands.get(i))[0].equals(AR)) {
				node.removeAttribute((String) (commands.get(i))[1]);
			}
		}
		for (int j = 0; j != model.getRowCount(); j++) {
			node.setAttribute((String) model.getValueAt(j, 0), model
					.getValueAt(j, 1));
		}
	}

	public boolean checkData() {
		@SuppressWarnings("unused")
		Object p = "";
		for (int j = 0; j != model.getRowCount(); j++) {
			if (((String) model.getValueAt(j, 2)).equals("Integer")) {
				try {
					p = (Integer) model.getValueAt(j, 1);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane
							.showMessageDialog(
									this,
									"An attribute value you entered is not of the type Integer.\nPlease check your data.",
									"Attribute type missmatch",
									JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			} else if (((String) model.getValueAt(j, 2)).equals("Double")) {
				try {
					p = (Double) model.getValueAt(j, 1);
				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(
									this,
									"An attribute value you entered is not of the type Double.\nPlease check your data.",
									"Attribute type missmatch",
									JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			} else if (((String) model.getValueAt(j, 2)).equals("Boolean")) {
				try {
					Boolean.parseBoolean(model.getValueAt(j, 1).toString());
				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(
									this,
									"An attribute value you entered is not of the type Boolean.\nPlease check your data.",
									"Attribute type missmatch",
									JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}

		}
		return true;
	}

	public void copyToClipboard() {
		if (model.getRowCount() > 0) {
			Map<String, ArrayList<Object>> attribs = new HashMap<String, ArrayList<Object>>();
			for (int i = 0; i != model.getRowCount(); i++) {
				String name = (String) model.getValueAt(i, 0);
				Object value = model.getValueAt(i, 1);
				String type = (String) model.getValueAt(i, 2);
				ArrayList<Object> temp = new ArrayList<Object>();
				temp.add(name);
				temp.add(value);
				temp.add(type);
				attribs.put(name, temp);
			}
			this.context.addData("nodeattributes", attribs);
			btnPasteAtt.setEnabled(true);
		}
	}

	@SuppressWarnings("unchecked")
	public void pasteFromClipboard() {
		Map<String, ArrayList<Object>> attribs = (Map<String, ArrayList<Object>>) this.context
				.getData("nodeattributes");
		if (attribs != null) {
			boolean check = false;
			Iterator<String> it = attribs.keySet().iterator();
			while (it.hasNext()) {
				String name = it.next();
				if(model.getRowIndex(name) == -1){
					ArrayList<Object> temp = new ArrayList<Object>();
					temp = attribs.get(name);
					model.insertRow((String) temp.get(0), temp.get(1),
							(String) temp.get(2));
				}else {
					check = true;
				}
			}
			if (check) {
				JOptionPane
						.showMessageDialog(
								this,
								"One or more attributes with the same name was found.\nOld values of those attributes were saved.",
								"Copying attributes",
								JOptionPane.WARNING_MESSAGE);
			}
		}

	}
}
