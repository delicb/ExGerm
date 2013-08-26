package tk.exgerm.core.plugin;

import tk.exgerm.core.service.ICoreContext;

public interface IComponent {
	/**
	 * Osobine koju može da koristi (i treba) svaka komponenta.
	 */
	public static String NAME_PROPERTY = "component.name";
	
	public static String APPLICATION_CLOSING = "core.application_closing";

	/**
	 * <p>
	 * Postavlja {@link tk.exgerm.core.service.ICoreContext kontekst}
	 * komponenti. Ovo nije isto što i {@link org.osgi.framework.BundleContext
	 * BundeContext}.
	 * <p>
	 * Konkretne komponente u telu ove metode treba da registruju sve što žele u
	 * Core, kao i da vode evidenciju o svemu što su registrovale da bi na
	 * zaustavljanju komponente mogle da počiste za sobom...
	 * 
	 * @param context
	 *            Kontekst <em>Core</e> komponente.
	 */
	public void setContext(ICoreContext context);

}