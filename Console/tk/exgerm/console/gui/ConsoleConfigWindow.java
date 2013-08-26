package tk.exgerm.console.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import tk.exgerm.core.plugin.ExGConfigPanel;
import tk.exgerm.core.service.ICoreContext;

import com.l2fprod.common.swing.JFontChooser;

public class ConsoleConfigWindow extends JPanel implements ExGConfigPanel {

	private static final long serialVersionUID = 8653069887547726867L;

	ICoreContext coreContext;
	JFontChooser fontChooser = new JFontChooser();
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JPanel panel1;
	private JLabel consoleSimpleText;
	private JButton chooseFontBtn;
	private JPanel panel2;
	private JLabel label6;
	private JTextField syntaxColorSimple;
	private JButton syntaxColorBtn;
	private JLabel label7;
	private JTextField semanticColorSimple;
	private JButton semanticColorBtn;
	private JLabel label8;
	private JTextField commandColorSimple;
	private JButton commandColorBtn;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	public ConsoleConfigWindow(ICoreContext coreContext) {
		this.coreContext = coreContext;
		initComponents();
		
		load();
	}

	private void chooseFontBtnActionPerformed(ActionEvent e) {
		
		Font font = JFontChooser.showDialog(this, "Choose font", consoleSimpleText.getFont());
		
		if(font != null)
			consoleSimpleText.setFont(font);
	}

	private void syntaxColorBtnActionPerformed(ActionEvent e) {
		Color color = JColorChooser.showDialog(this, "Chose color", syntaxColorSimple.getBackground());
		
		if(color != null)
			syntaxColorSimple.setBackground(color);
	}

	private void semanticColorBtnActionPerformed(ActionEvent e) {
		Color color = JColorChooser.showDialog(this, "Chose color", semanticColorSimple.getBackground());
		
		if(color != null)
			semanticColorSimple.setBackground(color);
	}

	private void commandColorBtnActionPerformed(ActionEvent e) {
		Color color = JColorChooser.showDialog(this, "Chose color", commandColorSimple.getBackground());
		
		if(color != null)
			commandColorSimple.setBackground(color);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		this.panel1 = new JPanel();
		this.consoleSimpleText = new JLabel();
		this.chooseFontBtn = new JButton();
		this.panel2 = new JPanel();
		this.label6 = new JLabel();
		this.syntaxColorSimple = new JTextField();
		this.syntaxColorBtn = new JButton();
		this.label7 = new JLabel();
		this.semanticColorSimple = new JTextField();
		this.semanticColorBtn = new JButton();
		this.label8 = new JLabel();
		this.commandColorSimple = new JTextField();
		this.commandColorBtn = new JButton();

		setBorder(new CompoundBorder(
			new TitledBorder(new MatteBorder(1, 0, 0, 0, Color.black), "Console", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.BOLD, 14)),
			new EmptyBorder(5, 5, 5, 5)));
		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

		this.panel1.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Color.black), "Console font:", TitledBorder.LEADING, TitledBorder.TOP));
		this.panel1.setLayout(new GridBagLayout());
		((GridBagLayout)this.panel1.getLayout()).columnWidths = new int[] {15, 158, 0, 0};
		((GridBagLayout)this.panel1.getLayout()).rowHeights = new int[] {0, 5, 0};
		((GridBagLayout)this.panel1.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)this.panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

		this.consoleSimpleText.setText(" Simple text");
		this.consoleSimpleText.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
		this.consoleSimpleText.setFont(new Font("Consolas", Font.PLAIN, 15));
		this.panel1.add(this.consoleSimpleText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		this.chooseFontBtn.setText("Choose");
		this.chooseFontBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFontBtnActionPerformed(e);
			}
		});
		this.panel1.add(this.chooseFontBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));
		add(this.panel1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		this.panel2.setBorder(new CompoundBorder(
			new EmptyBorder(5, 0, 0, 0),
			new TitledBorder(new MatteBorder(1, 1, 1, 1, Color.black), "Error colors:", TitledBorder.LEADING, TitledBorder.TOP)));
		this.panel2.setLayout(new GridBagLayout());
		((GridBagLayout)this.panel2.getLayout()).columnWidths = new int[] {15, 0, 50, 0, 0};
		((GridBagLayout)this.panel2.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
		((GridBagLayout)this.panel2.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)this.panel2.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

		this.label6.setText("Syntax:");
		this.panel2.add(this.label6, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		this.syntaxColorSimple.setBorder(null);
		this.syntaxColorSimple.setEditable(false);
		this.syntaxColorSimple.setBackground(new Color(0, 150, 0));
		this.panel2.add(this.syntaxColorSimple, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		this.syntaxColorBtn.setText("Choose");
		this.syntaxColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				syntaxColorBtnActionPerformed(e);
			}
		});
		this.panel2.add(this.syntaxColorBtn, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		this.label7.setText("Semantic:");
		this.panel2.add(this.label7, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		this.semanticColorSimple.setBorder(null);
		this.semanticColorSimple.setEditable(false);
		this.semanticColorSimple.setBackground(Color.blue);
		this.panel2.add(this.semanticColorSimple, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		this.semanticColorBtn.setText("Choose");
		this.semanticColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				semanticColorBtnActionPerformed(e);
			}
		});
		this.panel2.add(this.semanticColorBtn, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		this.label8.setText("Command:");
		this.panel2.add(this.label8, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		this.commandColorSimple.setBackground(Color.red);
		this.commandColorSimple.setBorder(null);
		this.commandColorSimple.setEditable(false);
		this.panel2.add(this.commandColorSimple, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		this.commandColorBtn.setText("Choose");
		this.commandColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandColorBtnActionPerformed(e);
			}
		});
		this.panel2.add(this.commandColorBtn, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));
		add(this.panel2, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		// //GEN-END:initComponents
	}

	@Override
	public Icon getIcon() {
		return loadIcon("images/consoleConfig48.png");
	}

	/**
	 * Učitava ikonicu.
	 * 
	 * @param path
	 *            putanja do ikonice
	 * @return ikonica
	 */
	protected Icon loadIcon(String path) {
		URL imageURL = getClass().getResource(path); //$NON-NLS-1$ //$NON-NLS-2$
		Icon icon = null;

		if (imageURL != null) {
			icon = new ImageIcon(imageURL);
		} else {
			System.err.println("Greška pri učitavanju slike " + path); //$NON-NLS-1$
		}

		return icon;
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public String getTitle() {
		return "Console";
	}

	@Override
	public void load() {
		Font font;
		String fontName;
		int fontStyle;
		int fontSize;
		
		try {
			fontName = coreContext.getConfigData("console_font_name");
			fontStyle = Integer.parseInt(coreContext.getConfigData("console_font_style"));
			fontSize = Integer.parseInt(coreContext.getConfigData("console_font_size"));
		
			font =  new Font(fontName, fontStyle, fontSize);
		} catch (Exception e) {
			font =  new Font("Consolas", Font.PLAIN, 15);
		}	
		consoleSimpleText.setFont(font);
		
		Color syntaxColor;
		try {
			syntaxColor =  Color.decode(coreContext.getConfigData("console_syntax_color"));
		} catch (Exception e) {
			syntaxColor =  new Color(0, 150, 0);
		} 
		syntaxColorSimple.setBackground(syntaxColor);
		
		Color semanticColor;
		try {
			semanticColor = Color.decode(coreContext.getConfigData("console_semantic_color"));
		} catch (Exception e) {
			semanticColor = Color.BLUE;
		}
		semanticColorSimple.setBackground(semanticColor);
		
		Color commandColor;
		try {
			commandColor = Color.decode(coreContext.getConfigData("console_command_color"));
		} catch (Exception e) {
			commandColor = Color.RED;
		}
		
		commandColorSimple.setBackground(commandColor);
	}

	@Override
	public void save() {
		coreContext.putConfigData("console_font_name", consoleSimpleText.getFont().getFontName());
		coreContext.putConfigData("console_font_style", String.valueOf(consoleSimpleText.getFont().getStyle()));
		coreContext.putConfigData("console_font_size", String.valueOf(consoleSimpleText.getFont().getSize()));
		
		coreContext.putConfigData("console_syntax_color", String.valueOf(syntaxColorSimple.getBackground().getRGB()));
		coreContext.putConfigData("console_semantic_color", String.valueOf(semanticColorSimple.getBackground().getRGB()));
		coreContext.putConfigData("console_command_color", String.valueOf(commandColorSimple.getBackground().getRGB()));
	}

	@Override
	public int getPosition() {
		return 200;
	}
}
