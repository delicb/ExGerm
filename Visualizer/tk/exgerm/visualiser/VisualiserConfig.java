package tk.exgerm.visualiser;

import tk.exgerm.core.service.ICoreContext;

/**
 * Singleton koji upravlja učitavanjem i upisom konfiguracionig parametara
 * visualiser komponente.
 * 
 * @author Tim 2
 * 
 */
public class VisualiserConfig {
	private static VisualiserConfig instance = null;
	private static ICoreContext coreContext;

	public static VisualiserConfig getInstanse() {
		return instance;
	}

	static void createConfiguration(ICoreContext context) {
		if (instance == null)
			instance = new VisualiserConfig();
		coreContext = context;
	}

	public int getShowNavigation() {
		try {
			return Integer.parseInt(coreContext
					.getConfigData("show_navigation"));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public int getHorizontalGap() {
		try {
			return Integer.parseInt(coreContext.getConfigData("navigator_gap"));
		} catch (Exception e) {
			return 100;
		}
	}

	public int getZoombarGap() {
		try {
			return Integer.parseInt(coreContext
					.getConfigData("navigator_zoombar_gap"));
		} catch (Exception e) {
			return 20;
		}
	}

	public float getFadeFactor() {
		try {
			return Float.parseFloat(coreContext
					.getConfigData("navigator_fade_factor"));
		} catch (Exception e) {
			return 50f;
		}
	}

	public void putValue(String key, String value) {
		coreContext.putConfigData(key, value);
	}

	public void loadConfiguration() {
		coreContext.raise(ICoreContext.CONFIGURATION_CHANGED, "Visualiser");
	}
}
