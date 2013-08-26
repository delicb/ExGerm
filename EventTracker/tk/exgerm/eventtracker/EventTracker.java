package tk.exgerm.eventtracker;

import java.awt.Color;
import java.awt.Font;
import java.io.PrintStream;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.Element;

import tk.exgerm.core.plugin.ExGGraphicalComponent;


public class EventTracker extends JScrollPane implements ExGGraphicalComponent {

	private static final long serialVersionUID = 4129221142231743118L;

	JTextPane textPane;
	PrintStream out;
	Document doc;
	Element rootElem;
	
	public EventTracker() {
		textPane = new JTextPane();
		textPane.setBackground(Color.BLACK);
		textPane.setForeground(Color.BLACK);
		textPane.setFont(new Font("Serif", Font.PLAIN, 15));
		doc = textPane.getStyledDocument();	
		rootElem = doc.getDefaultRootElement();
		
		this.getViewport().add(textPane);
		out = new PrintStream(new DocumentOutputStream(doc));
		
		setName("EventTracker");
	}
	
	public PrintStream getPrintStream() {
		return out;
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public int getPosition() {
		return ExGGraphicalComponent.SOUTH;
	}

	@Override
	public boolean isTabNameChangeable() {
		return true;
	}

}
