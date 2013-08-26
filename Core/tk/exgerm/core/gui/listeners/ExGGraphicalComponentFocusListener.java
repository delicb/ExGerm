package tk.exgerm.core.gui.listeners;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import tk.exgerm.core.Core;
import tk.exgerm.core.plugin.ExGGraphicalComponent;

public class ExGGraphicalComponentFocusListener implements FocusListener {

	private static Component focusedComponent;

	private Component component;

	public ExGGraphicalComponentFocusListener(Component component) {
		this.component = component;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (!component.equals(focusedComponent)) {
			Core.getInstance().getEventDispatcher().raiseEvent(
					ExGGraphicalComponent.FOCUSED_COMPONENT_CHANGED, component);
			focusedComponent = component;
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
	}
}
