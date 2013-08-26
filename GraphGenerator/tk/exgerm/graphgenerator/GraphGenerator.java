package tk.exgerm.graphgenerator;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class GraphGenerator extends JDialog {

	/**
	 * Ova konstanta predstavlja ime komponente i služi za lakše identifikovanje
	 * komponente koja ispaljuje HELP_REQUESTED event.
	 */
	public static String componentName;
	private ICoreContext context;
	private Generate generate;

	/**
	 * Lista imena svih tipova grafova koje generator nudi kao opciju
	 */
	private String[] graphs;
	/**
	 * Lista kratkih objašnjenja tipova grafova koje ova kompoenenta nudi
	 */
	private String[] explanations;
	
	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	private JLabel lblParameterN = new JLabel("N:");
	private JLabel lblParameterM = new JLabel("M:");
	private JLabel lblGraphName = new JLabel("Graph name:");
	private JTextField tfParameterN = new JTextField(10);
	private JTextField tfParameterM = new JTextField(10);
	private JTextField tfGraphName = new JTextField(20);
	private JLabel lblSelect = new JLabel(
			"Select the type of graph you want to generate:");
	private JLabel lblInsert = new JLabel("Insert the parameters:");
	private ButtonGroup bgGraphs = new ButtonGroup();
	private Box graphsBox = Box.createVerticalBox();
	private Box explanationsBox = Box.createVerticalBox();
	private Box parametersBox = Box.createHorizontalBox();
	private Box mBox = Box.createVerticalBox();
	private Box nBox = Box.createVerticalBox();
	private Box buttonBox = Box.createHorizontalBox();
	private boolean dialogResult;

	public GraphGenerator(ICoreContext context) {
		super(null, ModalityType.DOCUMENT_MODAL);
		this.context = context;
		this.generate = new Generate(context);
		initializeGraphs();
		initializeExplanations();
		initializeBoxes();
		initializeGroups();
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		setTitle("GraphGenerator");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		setLayout(new GridBagLayout());
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/generator.png")));
		} catch (IOException e1){}

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_F1
								&& e.getID() == KeyEvent.KEY_PRESSED
								&& GraphGenerator.this.isActive()) {
							e.consume();
							GraphGenerator.this.context.raise(
									ExGHelp.HELP_REQUESTED,
									GraphGenerator.componentName,
									GraphGenerator.componentName, true);
						}
						return false;
					}
				});

		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enterPressed();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escapePressed();
			}
		});

		Container container = getContentPane();

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 20, 0, 20);

		container.add(lblGraphName, c);

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 5, 0, 20);

		container.add(tfGraphName, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 20);

		container.add(lblSelect, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 20);

		container.add(graphsBox, c);

		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 0, 0, 20);

		container.add(explanationsBox, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 20);

		container.add(lblInsert, c);

		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 0);

		container.add(parametersBox, c);

		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 15, 15, 20);

		container.add(buttonBox, c);
		this.pack();
		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);

	}

	/**
	 * Metoda inicijalizuje imena svih tipova grafova koje će ova komponenta da
	 * ponudi korisniku za generisanje
	 */
	public void initializeGraphs() {
		this.graphs = new String[12];
		graphs[0] = "Path";
		graphs[1] = "Cycle";
		graphs[2] = "Complete Binary Tree";
		graphs[3] = "Random Tree";
		graphs[4] = "Cycle of Cliques";
		graphs[5] = "Triangular Mesh";
		graphs[6] = "Square Mesh";
		graphs[7] = "Torus";
		graphs[8] = "Hypercube";
		graphs[9] = "Complete Biparite Graph";
		graphs[10] = "Complete Graph";
		graphs[11] = "Random Connected Graph";
	}

	/**
	 * Metoda inicijalizuje kratka objašnjenja svih tipova grafova koje će ova 
	 * komponenta da ponudi korisniku za generisanje.
	 */
	public void initializeExplanations() {
		this.explanations = new String[12];
		explanations[0] = "N nodes";
		explanations[1] = "N nodes";
		explanations[2] = "2^N - 1 nodes, 2^N - 2 edges";
		explanations[3] = "N nodes, N - 1 edges";
		explanations[4] = "N * M nodes";
		explanations[5] = "N(N - 1)/2 nodes";
		explanations[6] = "N^2 nodes";
		explanations[7] = "N^2 nodes";
		explanations[8] = "2^N nodes";
		explanations[9] = "2N nodes";
		explanations[10] = "N nodes";
		explanations[11] = "N nodes, M edges";
	}

	/**
	 * Metoda inicijalizuje i popunjava sve Box kontejnere koji se nalaze na 
	 * prozoru.
	 */
	public void initializeBoxes() {
		nBox.add(lblParameterN);
		nBox.add(Box.createVerticalStrut(10));
		nBox.add(tfParameterN);
		mBox.add(lblParameterM);
		mBox.add(Box.createVerticalStrut(10));
		mBox.add(tfParameterM);
		parametersBox.add(nBox);
		parametersBox.add(Box.createHorizontalStrut(20));
		parametersBox.add(mBox);
		buttonBox.add(btnOK);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(btnCancel);
	}

	/**
	 * Metoda inicijalzuje radio grupu na ovom prozoru. Za svaki uneti tip grafa
	 * i njegovo objašnjenje generiše po jedno radio dugme i jednu labelu sa tekstom
	 * objašnjenja.
	 */
	public void initializeGroups() {
		JRadioButton rbGraph;
		for (int i = 0; i != graphs.length; i++) {
			rbGraph = new JRadioButton(graphs[i]);
			rbGraph.setActionCommand(graphs[i]);
			bgGraphs.add(rbGraph);
			graphsBox.add(rbGraph);
			graphsBox.add(Box.createVerticalStrut(5));
		}
		JLabel lblExplanation;
		for (int j = 0; j != explanations.length; j++) {
			lblExplanation = new JLabel(explanations[j]);
			explanationsBox.add(lblExplanation);
			explanationsBox.add(Box.createVerticalStrut(8));
		}

	}

	/**
	 * Metoda poziva adekvatan generator grafa u zavisnosti od unetih parametara
	 */
	public void enterPressed() {

		if (checkData()) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (getSelectedGraph().equals(graphs[0]))
				generate.path(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[1]))
				generate.cycle(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[2]))
				generate.completeBinaryTree(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[3]))
				generate.randomConnectedGraph(getParameterN(), getParameterN()-1, getGraphName());
			else if (getSelectedGraph().equals(graphs[4]))
				generate.cycleOfCliques(getParameterN(), getParameterM(), getGraphName());
			else if (getSelectedGraph().equals(graphs[5]))
				generate.triangularMesh(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[6]))
				generate.squareMesh(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[7]))
				generate.thorus(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[8]))
				generate.hypercube(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[9]))
				generate.completeBipartiteGraph(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[10]))
				generate.completeGraph(getParameterN(), getGraphName());
			else if (getSelectedGraph().equals(graphs[11]))
				generate.randomConnectedGraph(getParameterN(), getParameterM(), getGraphName());

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			setDialogResult(true);
			setVisible(false);
		}
	}

	/**
	 * Metoda gasi prozor u slučaju odustajanja od generisanja grafa.
	 */
	public void escapePressed() {
		setDialogResult(false);
		setVisible(false);
	}

	public void setDialogResult(boolean result) {
		this.dialogResult = result;
	}

	public int getParameterN() {
		int par = -1;
		try {
			par = Integer.parseInt(tfParameterN.getText());
		} catch (Exception e) {
			par = 0;
		}
		return par;
	}

	public int getParameterM() {
		int par = -1;
		try {
			par = Integer.parseInt(tfParameterM.getText());
		} catch (Exception e) {
			par = 0;
		}
		return par;
	}

	public String getGraphName() {
		return tfGraphName.getText();
	}

	public boolean getDialogResult() {
		return dialogResult;
	}

	public String getSelectedGraph() {
		return bgGraphs.getSelection().getActionCommand();
	}

	/**
	 * Metoda proverava da li su svi potrebni podaci za generisanje nekog grafa
	 * uneti na dijalogu. Ukoliko nisu svi podaci uneti, prijavljuje se greška 
	 * adekvatnom porukom i fraća se FALSE kao rezultat. Ukoliko su svi podaci
	 * uredu metoda vraća TRUE vrednost.
	 * 
	 * @return - metoda vraća boolean vrednost u zavisnosti od ispravnosti unetih 
	 * podataka u dijalogu
	 */
	public boolean checkData() {
		String command = "";

		if (getGraphName().length() == 0) {
			JOptionPane.showMessageDialog(this,
					"You must enter the name of the graph first.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			for (int i = 0; i != context.getAllGraphs().size(); i++) {
				if (context.getAllGraphs().get(i).getName().equals(
						getGraphName())) {
					JOptionPane
							.showMessageDialog(
									this,
									"A graph with this name already exists./nPlese choose another.",
									"Warning", JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
		}

		try {
			command = bgGraphs.getSelection().getActionCommand();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Please select the type of graph you want to generate.",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		try {
			Integer.parseInt(tfParameterN.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Parameter N must be an integer value.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (command.equals(graphs[4])|| command.equals(graphs[11])) {
			try {
				Integer.parseInt(tfParameterM.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						"Parameter M must be an integer value.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}

		return true;
	}
}
