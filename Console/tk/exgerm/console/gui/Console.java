package tk.exgerm.console.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.PrintStream;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import tk.exgerm.console.AutoComplete;
import tk.exgerm.console.Clipboard;
import tk.exgerm.console.actions.ClearConsoleAction;
import tk.exgerm.console.actions.CopyAction;
import tk.exgerm.console.actions.CutAction;
import tk.exgerm.console.actions.PasteAction;
import tk.exgerm.console.listeners.ActiveTabChangedListener;
import tk.exgerm.console.listeners.ConfigurationChangedListener;
import tk.exgerm.console.parser.DocumentOutputStream;
import tk.exgerm.console.parser.Parser;
import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class Console extends JScrollPane {

	public static String componentName;
	private Parser parser;
	private PrintStream output;
	private int historyCounter;
	private StyledDocument doc;
	private Element rootElem;
	private ICoreContext context;
	private JTextPane textPane;
	private Map<String, String> aliases;
	private AutoComplete autoComplete;
	private JPopupMenu popup;

	private Color warningColor;
	private Color errorColor;
	private Color criticalErrorColor;

	private IListener focusChange;
	private IListener configChange;

	/**
	 * Vektor koji sadrži istoriju unetih komandi.
	 */
	Vector<String> history;
	/**
	 * Trenutno aktuelan prompt.
	 */
	String prompt = new String();

	/**
	 * Kontruktor konzole. Kreira i vrši inicijalizaciju istorije, parsera i
	 * textPane-e.
	 * 
	 * @param context
	 *            ICoreContext prosleđen od ConsoleService-a.
	 * @param name
	 *            iIme konzole
	 */
	public Console(ICoreContext context, Map<String, String> aliases,
			String name) {
		this.context = context;
		this.aliases = aliases;

		history = new Vector<String>();
		textPane = new JTextPane();
		updateConfiguration();
		popup = new JPopupMenu();
		popup.add(new CutAction(this));
		popup.add(new CopyAction(this));
		popup.add(new PasteAction(this));
		popup.add(new JSeparator());
		popup.add(new ClearConsoleAction(this));
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		textPane.setOpaque(true);
		textPane.setCaretColor(Color.BLACK);
		doc = textPane.getStyledDocument();
		rootElem = doc.getDefaultRootElement();

		textPane.addKeyListener(new ConsoleKeyListener());
		MouseListener ml = new MouseListener();
		textPane.addMouseListener(ml);
		textPane.addMouseWheelListener(ml);

		this.getViewport().add(textPane);
		output = new PrintStream(new DocumentOutputStream(doc));
		autoComplete = new AutoComplete(this);
		parser = new Parser(context, output, this);

		setName(name);
		print(prompt);

		focusChange = new ActiveTabChangedListener(context, this, parser);
		configChange = new ConfigurationChangedListener(this);

		context.listenEvent(ExGGraphicalComponent.FOCUSED_COMPONENT_CHANGED,
				focusChange);
		context.listenEvent(ICoreContext.CONFIGURATION_CHANGED, configChange);
	}

	class MouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			if (e.getButton() == MouseEvent.BUTTON3)
				popup.show(Console.this, e.getX(), e.getY());
			autoComplete.reset();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.isControlDown()) {
				int newValue = textPane.getFont().getSize()
						- e.getUnitsToScroll();
				if (newValue < 6)
					newValue = 6;
				context.putConfigData("console_font_size", String
						.valueOf(newValue));
				updateConfiguration();
				super.mouseWheelMoved(null);
			} else
				Console.this.getMouseWheelListeners()[0].mouseWheelMoved(e);
		}
	}

	class ConsoleKeyListener extends KeyAdapter {

		/**
		 * Listener otkucanih znakova. Reaguje samo na enter kada prosleđuje
		 * unetu komandu parseru i dodaje je u istoriju. Ako je uneta prazna
		 * komanda neće biti nikakve reakcije. Ukoliko su uneti samo razmaci,
		 * komanda se prosleđuje u parser ali se ne pamti u istoriji. Vodi se
		 * računa da se prompt ne prosledi parseru odnosno da se ne zapamti u
		 * istoriji kao deo komande.
		 * 
		 */
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				String lastLine = getLastLine().replace(prompt, "");
				if (!lastLine.equals("\n") && !lastLine.trim().equals("")) {
					textPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					parser.run(lastLine);
					history.add(lastLine.substring(0, lastLine.length() - 1)
							.trim());
					textPane.setCursor(Cursor.getDefaultCursor());
				}
				print(prompt);
				e.consume();
			}
		}

		/**
		 * Listener pritisnutog znaka. Služi za prepoznavanje specijalnih
		 * tastera (strelice, backspace...) i obrađuje ih na poseban način.
		 * Takođe vodi računa da se ne sme menjati prethodno unet sadržaj u
		 * konzoli. Potrebne su dalje dorade za veću funkcionalnost.
		 */
		public void keyPressed(KeyEvent e) {
			if (e.isControlDown()) { // događaji sa CTRL
				if (e.getKeyCode() == KeyEvent.VK_X)
					doCut();
				else if (e.getKeyCode() == KeyEvent.VK_C)
					doCopy();
				else if (e.getKeyCode() == KeyEvent.VK_V)
					doPaste();
				else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					removeLeftWord();
					autoComplete.reset();
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					removeRightWord();
					autoComplete.reset();
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT)
					moveCaretToLeftSpace();
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					moveCaretToRightSpace();
				else if (e.getKeyChar() == '+') {
					context.putConfigData("console_font_size", String
							.valueOf(textPane.getFont().getSize() + 2));
					updateConfiguration();
				} else if (e.getKeyChar() == '-') {
					context.putConfigData("console_font_size", String
							.valueOf(textPane.getFont().getSize() - 2));
					updateConfiguration();
				}
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) { // strelice
				e.consume();
				int historyCountLimiter = history.size() - historyCounter;
				if (historyCountLimiter <= history.size()
						&& historyCountLimiter > 0) {
					historyCounter++;
					removeCurrentLine();
					print(prompt + history.get(history.size() - historyCounter));
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				e.consume();
				if (historyCounter > 1) {
					historyCounter--;
					removeCurrentLine();
					print(prompt + history.get(history.size() - historyCounter));
				} else if (historyCounter == 1) {
					removeCurrentLine();
					print(prompt);
					historyCounter--;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT
					|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (getPreviousSymbols(1).equals("\n")
						|| getPreviousSymbols(prompt.length()).equals(prompt)) {
					e.consume();
				}
				autoComplete.reset();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				textPane.setCaretPosition(doc.getLength());
				autoComplete.reset();
			} else if (e.getKeyChar() == KeyEvent.VK_TAB) {
				e.consume();
				autoComplete.run();
			} else if (e.getKeyChar() == KeyEvent.VK_SPACE) {
				autoComplete.reset();
			} else if (e.getKeyCode() == KeyEvent.VK_F1) {
				context.raise(ExGHelp.HELP_REQUESTED, Console.componentName,
						"Konzola");
			} else {
				if (isCaretBeforePromptEnd())
					textPane.setCaretPosition(getCurrentLineStart());
				if (textPane.getSelectedText() != null
						&& !textPane.getSelectedText().equals("")
						&& !e.isShiftDown())
					try {
						doc.remove(textPane.getSelectionStart(), textPane
								.getSelectionEnd()
								- textPane.getSelectionStart());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
			}
		}
	}

	/**
	 * Daje poslednju uneti liniju (linija pre one na kojoj je caret trenutno).
	 * 
	 * @return poslednja uneta linija u Document doc
	 */
	private String getLastLine() {
		String result = new String();
		int numLines = rootElem.getElementCount();
		Element lineElem = rootElem.getElement(numLines - 2);
		int lineStart = lineElem.getStartOffset();
		int lineEnd = lineElem.getEndOffset();
		try {
			result = doc.getText(lineStart, lineEnd - lineStart);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Uklanja liniju na kojoj se caret trenutno nalazi. Primena: kada se
	 * koristi history (strelice gore i dole).
	 */
	private void removeCurrentLine() {
		int numLines = rootElem.getElementCount();
		Element lineElem = rootElem.getElement(numLines - 1);
		int lineStart = lineElem.getStartOffset();
		int lineEnd = lineElem.getEndOffset();
		try {
			doc.remove(lineStart, lineEnd - lineStart - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Računa početak tekuće linije UKLJUČUJUĆI prompt.
	 * 
	 * @return prvo mesto posle prompta
	 */
	private int getCurrentLineStart() {
		int numLines = rootElem.getElementCount();
		Element lineElem = rootElem.getElement(numLines - 1);
		return lineElem.getStartOffset() + prompt.length();
	}

	/**
	 * Određuje da li se caret nalazi pre kraja prompta
	 * 
	 * @return da li se nalazi pre kraja prompta
	 */
	private boolean isCaretBeforePromptEnd() {
		int lineStart = rootElem.getElement(rootElem.getElementCount() - 1)
				.getStartOffset();
		if (textPane.getCaretPosition() >= lineStart + prompt.length())
			return false;
		return true;
	}

	/**
	 * Daje prethodnih count simbola pre careta. Primena: identifikacija prompta
	 * i novog reda ("\n").
	 * 
	 * @param count
	 *            broj znakova koji nam trebaju
	 * @return string znakova
	 */
	public String getPreviousSymbols(int count) {
		String result = null;
		try {
			result = doc.getText(textPane.getCaretPosition() - count, count);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Realizuje cutovanje teksta sa konzole (doCopy+delete)
	 */
	public void doCut() {
		int start = textPane.getSelectionStart();
		int offset = textPane.getSelectionEnd() - start;
		doCopy();
		try {
			if (!isCaretBeforePromptEnd())
				doc.remove(start, offset);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realizuje kopiranje teksta sa konzole (CTRL+C)
	 */
	public void doCopy() {
		String text = textPane.getSelectedText();
		if (text != null && !text.equals(""))
			Clipboard.setClipboard(text);
	}

	/**
	 * Realizuje pastovanje teksta na konzolu (CTRL+V)
	 */
	public void doPaste() {
		if (isCaretBeforePromptEnd())
			textPane.setCaretPosition(getCurrentLineStart());
		try {
			doc.remove(textPane.getSelectionStart(), textPane.getSelectionEnd()
					- textPane.getSelectionStart());
			doc.insertString(textPane.getCaretPosition(), Clipboard
					.getClipboard(), null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Vrši ispis u textPane
	 * 
	 * @param text
	 *            koji želimo da ispišemo.
	 */
	protected void print(String text) {
		output.print(text);
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public Map<String, String> getAliases() {
		return aliases;
	}

	public JTextPane getTextPane() {
		return textPane;
	}

	public ICoreContext getContext() {
		return context;
	}

	public Parser getParser() {
		return parser;
	}

	/**
	 * Pomera caret u levo, na početak (prethodne) reči
	 * 
	 * @return koliko znakova se u levo pomerio
	 */
	private void moveCaretToLeftSpace() {
		int moveLeft = findPreviousSpace();
		if (moveLeft == 0)
			return;
		textPane.setCaretPosition(textPane.getCaretPosition() - moveLeft + 1);
	}

	/**
	 * Pomera caret u desno, na kraj (sledeće) reči
	 * 
	 * @return koliko znakova se u desno pomerio
	 */
	private void moveCaretToRightSpace() {
		int moveRight = findNextSpace();
		textPane.setCaretPosition(textPane.getCaretPosition() + moveRight);
	}

	/**
	 * Određuje koliko znakova pre careta se nalazi prvi space. Ako taj space ne
	 * postoji, vraća 0.
	 * 
	 * @return broj znakova do prvog space-a
	 */
	public int findPreviousSpace() {
		String lastLine = null;
		try {
			lastLine = doc.getText(getCurrentLineStart(), textPane
					.getCaretPosition()
					- getCurrentLineStart());
		} catch (BadLocationException e) {
			return 0;
		}
		return lastLine.length() - lastLine.trim().lastIndexOf(" ");
	}

	/**
	 * Određuje koliko znakova posle careta se nalazi prvi space. Ako taj space
	 * ne postoji, ide na kraj unosa.
	 * 
	 * @return broj znakova do prvog space-a
	 */
	private int findNextSpace() {
		String lastLine = null;
		try {
			if (doc.getLength() - textPane.getCaretPosition() - 1 < 0)
				return 0;
			lastLine = doc.getText(textPane.getCaretPosition(), doc.getLength()
					- textPane.getCaretPosition() - 1);

		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		int result = lastLine.trim().indexOf(" ") + 1;
		if (result == 0 && textPane.getCaretPosition() < doc.getLength())
			result = doc.getLength() - textPane.getCaretPosition();
		return result;
	}

	/**
	 * Uklanja poslednju unetu reč (CTRL+BACKSPACE)
	 */
	public void removeLeftWord() {
		int count = findPreviousSpace() - 1;
		try {
			doc.remove(textPane.getCaretPosition() - count, count);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Uklanja narednu reč (CTRL+DEL)
	 */
	protected void removeRightWord() {
		int count = findNextSpace();
		try {
			doc.remove(textPane.getCaretPosition(), count);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void removeLastChars(int count) {
		try {
			doc.remove(textPane.getCaretPosition() - count, count);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ubacuje tekst ispred carreta
	 * 
	 * @param text
	 *            za ubacivanje
	 */
	public void insertIntoCarretPostition(String text) {
		try {
			doc.insertString(textPane.getCaretPosition(), text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ispisuje poruku greške na konzoli.
	 * 
	 * @param err
	 *            Poruka o greški
	 */
	public void printError(String err,
			ExGCommandErrorException.CommandErrorType type) {
		int start = textPane.getCaretPosition();
		int end = textPane.getCaretPosition() + err.length();
		textPane.setSelectionStart(start);
		textPane.setSelectionEnd(end);
		textPane.replaceSelection(err);
		textPane.setSelectionStart(textPane.getCaretPosition());
		if (type == ExGCommandErrorException.CommandErrorType.CRITICAL_ERROR)
			color(start, end, criticalErrorColor);
		if (type == ExGCommandErrorException.CommandErrorType.ERROR)
			color(start, end, errorColor);
		if (type == ExGCommandErrorException.CommandErrorType.WARNING)
			color(start, end, warningColor);
		output.println();
		StyleConstants
				.setForeground(textPane.getInputAttributes(), Color.BLACK);
	}

	/**
	 * set the prescribed color to all characters in a given range
	 */
	public void color(int i, int j, Color color) {
		StyleConstants.setForeground(textPane.getInputAttributes(), color);
		doc.setCharacterAttributes(i, j - i, textPane.getInputAttributes(),
				true);
	}

	/**
	 * Briše kompletan sadržaj konzole.
	 */
	public void clearConsole() {
		try {
			doc.remove(0, doc.getLength());
			print(prompt);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void addFocusListener(FocusListener l) {
		textPane.addFocusListener(l);
	}

	/**
	 * Postavlja sve konfigurabilne vrednosti konzole. Poziva se na pojavu
	 * eventa (usled izmene konfiguracije) odnosno u toku inicijalizacije
	 * konzole.
	 */
	public void updateConfiguration() {
		String font = context.getConfigData("console_font_name");
		int size = Integer.parseInt(context.getConfigData("console_font_size"));
		int style = Integer.parseInt(context
				.getConfigData("console_font_style"));
		int warningColor = Integer.parseInt(context
				.getConfigData("console_syntax_color"));
		int errorColor = Integer.parseInt(context
				.getConfigData("console_semantic_color"));
		int criticalErrorColor = Integer.parseInt(context
				.getConfigData("console_command_color"));
		textPane.setFont(new Font(font, style, size));
		this.warningColor = new Color(warningColor);
		this.errorColor = new Color(errorColor);
		this.criticalErrorColor = new Color(criticalErrorColor);
	}

	/**
	 * Uklanja sve listenere konkretne konzole po zatvaranju taba
	 */
	public void removeListeners() {
		context.removeListener(configChange);
		context.removeListener(focusChange);
	}
}
