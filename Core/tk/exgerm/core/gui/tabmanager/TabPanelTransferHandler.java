package tk.exgerm.core.gui.tabmanager;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.TransferHandler;

/**
 * Klasa omogućava opdršku drag n drop funkcionalnosti između {@link TabPanel
 * oldTabPanel-a} Koristi se za rukovanje transfera podataka od i do
 * {@link TabPanel oldTabPanel-a}. Podatak koja se prenosi je tipa
 * {@link Transferable} i predstavlja komponentu koja se nalazi u tab-u.
 * 
 * @author Tim 2
 * 
 */
public class TabPanelTransferHandler extends TransferHandler {

	private static final long serialVersionUID = -5094621262424895296L;

	/**
	 * Tip podataka koji prihvata oldTabPanel
	 */
	private final DataFlavor FLAVOR = new DataFlavor(
			DataFlavor.javaJVMLocalObjectMimeType, "exGERM Tab");

	/**
	 * Komponenta koja se prenosi
	 */
	private Component dragComponent;

	private TabPanel oldTabPanel = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.TransferHandler#canImport(javax.swing.TransferHandler.
	 * TransferSupport)
	 */
	public boolean canImport(TransferHandler.TransferSupport support) {
		JComponent dragComponent = null;
		try {
			dragComponent = (JComponent) support.getTransferable()
					.getTransferData(FLAVOR);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		TabPanel srcTabPanel = null;
		TabPanel desTabPanel = null;

		try {
			srcTabPanel = (TabPanel) dragComponent.getParent();
			desTabPanel = (TabPanel) support.getComponent();
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}

		if (!support.isDataFlavorSupported(FLAVOR)) {
			desTabPanel.hideTabDragIndicator();
			return false;
		}

		if (oldTabPanel != null && !desTabPanel.equals(oldTabPanel))
			oldTabPanel.hideTabDragIndicator();

		Point point = support.getDropLocation().getDropPoint();
		int targetIndex = getTargetTabIndex(desTabPanel, point);
		int dragTabIndex = srcTabPanel.indexOfComponent(dragComponent);

		if ((!srcTabPanel.equals(desTabPanel) && targetIndex >= 0)
				|| (srcTabPanel.equals(desTabPanel) && (targetIndex >= 0
						&& targetIndex != dragTabIndex && targetIndex != dragTabIndex + 1))) {

			desTabPanel.showTabDragIndicator(getDragIndicatorLocation(
					desTabPanel, targetIndex));
			oldTabPanel = desTabPanel;
		} else {
			desTabPanel.hideTabDragIndicator();
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	protected Transferable createTransferable(JComponent c) {

		TabPanel srcTabPanel = null;
		try {
			srcTabPanel = (TabPanel) c;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}

		this.dragComponent = srcTabPanel.getSelectedComponent();

		Transferable t = new Transferable() {

			public Object getTransferData(DataFlavor flavor) {
				return dragComponent;
			}

			public DataFlavor[] getTransferDataFlavors() {
				DataFlavor[] f = new DataFlavor[1];
				f[0] = FLAVOR;
				return f;
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return flavor.getHumanPresentableName().equals("exGERM Tab");
			}
		};

		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.TransferHandler#importData(javax.swing.TransferHandler.
	 * TransferSupport)
	 */
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!support.isDrop()) {
			return false;
		}

		Transferable t = support.getTransferable();
		Component dragComponent = null;
		try {
			dragComponent = (Component) t.getTransferData(FLAVOR);
			getVisualRepresentation(t);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
			return false;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}

		Point point = support.getDropLocation().getDropPoint();
		TabPanel srcTabPanel = null;
		TabPanel desTabPanel = null;

		try {
			srcTabPanel = (TabPanel) dragComponent.getParent();
			desTabPanel = (TabPanel) support.getComponent();
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}

		int dropIndex = getTargetTabIndex(desTabPanel, point);

		if (srcTabPanel.equals(desTabPanel)) {
			desTabPanel.setTabChangedEventDisabled();
		}

		TabComponent srcTabComponent = null;
		try {
			srcTabComponent = (TabComponent) srcTabPanel
					.getTabComponentAt(srcTabPanel
							.indexOfComponent(dragComponent));
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}

		desTabPanel.add(dragComponent, dropIndex);
		try {
			desTabPanel.setTabComponentAt(desTabPanel
					.indexOfComponent(dragComponent), new TabComponent(
					desTabPanel, srcTabComponent.isTabNameChangeable()));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
		desTabPanel.setTabChangedEventEnabled();

		try {
			desTabPanel.setSelectedComponent(dragComponent);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Određje na koju poziciju se premešta tab na osnovu trenutne pozicije miša
	 * prilikom drag operacije.
	 * 
	 * @param oldTabPanel
	 *            {@link TabPanel oldTabPanel} na koji se premesta tab
	 * @param point
	 *            pozicija miša iznad oldTabPanel-a
	 * 
	 * @return pozicija na koju se premešta tab
	 */
	private int getTargetTabIndex(TabPanel tabPanel, Point point) {
		boolean isHorizontalPlacement = tabPanel.getTabPlacement() == JTabbedPane.TOP
				|| tabPanel.getTabPlacement() == JTabbedPane.BOTTOM;

		if (tabPanel.getTabCount() == 0)
			return 0;

		for (int i = 0; i < tabPanel.getTabCount(); i++) {
			Rectangle r = null;
			try {
				r = tabPanel.getBoundsAt(i);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return -1;
			}

			if (isHorizontalPlacement)
				r.setRect(r.x - r.width / 2, r.y, r.width, r.height);
			else
				r.setRect(r.x, r.y - r.height / 2, r.width, r.height);

			if (r.contains(point))
				return i;
		}

		Rectangle r = null;
		Rectangle r1 = null;
		try {
			r = tabPanel.getBoundsAt(tabPanel.getTabCount() - 1);
			r1 = tabPanel.getBounds();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return -1;
		}

		if (isHorizontalPlacement)
			r.setRect(r.x + r.width / 2, r.y, r1.width, r.height);
		else
			r.setRect(r.x, r.y + r.height / 2, r.width, r1.height);

		return r.contains(point) ? tabPanel.getTabCount() : -1;
	}

	/**
	 * Određuje lokaciju u vidu podatka tipa {@link Point} na kojoj će se
	 * isctrati indikator u okviru {@link TabPanel TabPanel-a}.
	 * 
	 * @param tabPanel
	 *            {@link TabPanel TabPanel} na kojem se iscrtava indikator
	 * @param indexOfTargetTab
	 *            pozicija na koji se premešta tab
	 * @return lokacija u vidu podatka tipa {@link Point} na kojoj će se
	 *         isctrati indikator
	 */
	private Point getDragIndicatorLocation(TabPanel tabPanel,
			int indexOfTargetTab) {
		Rectangle r = null;

		if (indexOfTargetTab == -1)
			return null;

		if (indexOfTargetTab == 0) {
			if (tabPanel.getTabCount() > 0) {
				try {
					r = tabPanel.getBoundsAt(0);
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					return null;
				}
				return new Point(r.x - 5, 0);
			} else
				return new Point(0, 0);
		}

		try {
			r = tabPanel.getBoundsAt(indexOfTargetTab - 1);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}

		return new Point(r.x + r.width - 5, 0);
	}
}
