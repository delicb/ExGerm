package tk.exgerm.graphtree;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import tk.exgerm.core.plugin.ExGConfigPanel;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class GraphTreeConfiguration extends JPanel implements ExGConfigPanel {

	private ICoreContext coreContext;
	private JPanel configPanel;
	@SuppressWarnings("unused")
	private JLabel lblTreeWidth;
	private JTextField tfTreeWidth;

	public GraphTreeConfiguration(ICoreContext coreContext) {
		this.coreContext = coreContext;
		initializePanel();
	}

	private void initializePanel() {
		this.configPanel = new JPanel();
		this.lblTreeWidth = new JLabel("Tree width");
		this.tfTreeWidth = new JTextField(5);
		setBorder(new CompoundBorder(new TitledBorder(new MatteBorder(1, 0, 0,
				0, Color.black), "GraphTree", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14)),
				new EmptyBorder(5, 5, 5, 5)));
		setLayout(new GridBagLayout());
		add(this.configPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public String getTitle() {
		return "GraphTree";
	}

	@Override
	public void load() {
		int treeWidth;
		 try {
			 treeWidth =
		 Integer.parseInt(coreContext.getConfigData("tree_width"));
		 } catch (NumberFormatException e) {
			 treeWidth = -1;
		 }		 
		 if(treeWidth != -1)
			 this.tfTreeWidth.setText(""+treeWidth);
		 else
			 this.tfTreeWidth.setText(""+150);
	}

	@Override
	public void save() {
		coreContext.putConfigData("tree_width", tfTreeWidth.getText());
	}

	@Override
	public int getPosition() {
		return 500;
	}

}
