package tk.exgerm.help;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
public class HelpView extends JEditorPane{
	
	public HelpView() {
		super();
		setName("Help");
		setEditable(false);
		setContentType("text/html");
		class MyHyperlinkListener implements HyperlinkListener {
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						setPage(evt.getURL());
					} catch (IOException e) {
					}
				}
			}
		}
		this.addHyperlinkListener(new MyHyperlinkListener());
	}
}
