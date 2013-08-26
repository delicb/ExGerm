package tk.exgerm.visualiser.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import tk.exgerm.core.plugin.ExGConfigPanel;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.VisualiserConfig;
import tk.exgerm.visualiser.navigator.Navigator;

/**
 * Predstavlja konfiguracioni prozor Visualiser komponente. U njemu se nalaze
 * podešavanja kontrole Navigatora.
 * 
 * @author Tim 2
 * 
 */
public class VisualiserConfigWindow extends JPanel implements ExGConfigPanel {

	private static final long serialVersionUID = 1956945301455398443L;

	private ICoreContext coreContext;
	private JPanel panel1;
	private JLabel label1;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JRadioButton radioButton3;
	private JPanel panel2;
	private JLabel label2;
	private JSpinner spinner1;
	private JLabel label3;
	private JSpinner spinner2;
	private JLabel label4;
	private JSlider slider1;

	public VisualiserConfigWindow(ICoreContext coreContext) {
		this.coreContext = coreContext;
		initComponents();
	}

	private void initComponents() {
		this.panel1 = new JPanel();
		this.label1 = new JLabel();
		this.radioButton1 = new JRadioButton();
		this.radioButton2 = new JRadioButton();
		this.radioButton3 = new JRadioButton();
		this.panel2 = new JPanel();
		this.label2 = new JLabel();
		this.spinner1 = new JSpinner();
		this.label3 = new JLabel();
		this.spinner2 = new JSpinner();
		this.label4 = new JLabel();
		this.slider1 = new JSlider();

		setBorder(new CompoundBorder(new TitledBorder(new MatteBorder(1, 0, 0,
				0, Color.black), "Visualiser", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14)),
				new EmptyBorder(5, 5, 5, 5)));
		setLayout(new GridBagLayout());
		((GridBagLayout) getLayout()).columnWidths = new int[] { 0, 0, 0 };
		((GridBagLayout) getLayout()).rowHeights = new int[] { 0, 0, 0, 0 };
		((GridBagLayout) getLayout()).columnWeights = new double[] { 0.0, 1.0,
				1.0E-4 };
		((GridBagLayout) getLayout()).rowWeights = new double[] { 0.0, 0.0,
				0.0, 1.0E-4 };

		this.panel1.setBorder(new CompoundBorder(new TitledBorder(
				new LineBorder(Color.black, 1, true), "Navigator",
				TitledBorder.LEADING, TitledBorder.TOP), new EmptyBorder(5, 10,
				10, 10)));
		this.panel1.setLayout(new GridBagLayout());
		((GridBagLayout) this.panel1.getLayout()).columnWidths = new int[] { 0,
				0, 0, 0 };
		((GridBagLayout) this.panel1.getLayout()).rowHeights = new int[] { 0,
				0, 10, 0, 0 };
		((GridBagLayout) this.panel1.getLayout()).columnWeights = new double[] {
				0.0, 0.0, 0.0, 1.0E-4 };
		((GridBagLayout) this.panel1.getLayout()).rowWeights = new double[] {
				0.0, 0.0, 0.0, 0.0, 1.0E-4 };

		this.label1.setText("Show Navigation:");
		this.panel1.add(this.label1, new GridBagConstraints(0, 0, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

		this.radioButton1.setText("Automatically");
		this.radioButton1.setSelected(true);
		this.panel1.add(this.radioButton1, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

		this.radioButton2.setText("Always");
		this.panel1.add(this.radioButton2, new GridBagConstraints(1, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

		this.radioButton3.setText("Never");
		this.panel1.add(this.radioButton3, new GridBagConstraints(2, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

		this.panel2.setLayout(new GridBagLayout());
		((GridBagLayout) this.panel2.getLayout()).columnWidths = new int[] { 0,
				64, 0, 50, 0 };
		((GridBagLayout) this.panel2.getLayout()).rowHeights = new int[] { 0,
				15, 0, 0, 0 };
		((GridBagLayout) this.panel2.getLayout()).columnWeights = new double[] {
				0.0, 0.0, 0.0, 0.0, 1.0E-4 };
		((GridBagLayout) this.panel2.getLayout()).rowWeights = new double[] {
				0.0, 0.0, 0.0, 0.0, 1.0E-4 };

		this.label2.setText("Horizontal gap:");
		this.panel2.add(this.label2, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

		this.spinner1.setModel(new SpinnerNumberModel(60, 0, null, 1));
		this.panel2.add(this.spinner1, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

		this.label3.setText("Zoom bar gap:");
		this.panel2.add(this.label3, new GridBagConstraints(2, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

		this.spinner2.setModel(new SpinnerNumberModel(20, 0, null, 1));
		this.panel2.add(this.spinner2, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

		this.label4.setText("Fade factor (%):");
		this.panel2.add(this.label4, new GridBagConstraints(0, 2, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

		this.slider1.setMinorTickSpacing(10);
		this.slider1.setPaintLabels(true);
		this.slider1.setPaintTicks(true);
		this.slider1.setMajorTickSpacing(50);
		this.panel2.add(this.slider1, new GridBagConstraints(0, 3, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.panel1.add(this.panel2, new GridBagConstraints(0, 3, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		add(this.panel1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 5, 0), 0, 0));

		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(this.radioButton1);
		buttonGroup1.add(this.radioButton2);
		buttonGroup1.add(this.radioButton3);

		load();
	}

	@Override
	public Icon getIcon() {
		return loadIcon("images/visualizer48.png");
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
		return "Visualiser";
	}

	@Override
	public void load() {
		switch (VisualiserConfig.getInstanse().getShowNavigation()) {
		case 0:
			radioButton1.setSelected(true);
			break;
		case 1:
			radioButton2.setSelected(true);
			break;
		case 2:
			radioButton3.setSelected(true);
			break;
		default:
			break;
		}

		spinner1.setValue(VisualiserConfig.getInstanse().getHorizontalGap());
		spinner2.setValue(VisualiserConfig.getInstanse().getZoombarGap());
		slider1.setValue((int) VisualiserConfig.getInstanse().getFadeFactor());
	}

	@Override
	public void save() {
		if (radioButton1.isSelected())
			coreContext.putConfigData("show_navigation", String
					.valueOf(Navigator.SHOW_AUTOMATICALLY));
		else if (radioButton2.isSelected())
			coreContext.putConfigData("show_navigation", String
					.valueOf(Navigator.SHOW_ALWAYS));
		else if (radioButton3.isSelected())
			coreContext.putConfigData("show_navigation", String
					.valueOf(Navigator.SHOW_NEVER));

		coreContext.putConfigData("navigator_gap", String.valueOf(spinner1
				.getValue()));
		coreContext.putConfigData("navigator_zoombar_gap", String
				.valueOf(spinner2.getValue()));
		coreContext.putConfigData("navigator_fade_factor", String
				.valueOf(slider1.getValue()));
	}

	@Override
	public int getPosition() {
		return 1000;
	}
}
