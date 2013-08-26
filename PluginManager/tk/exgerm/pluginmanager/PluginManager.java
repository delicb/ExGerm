package tk.exgerm.pluginmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.pluginmanager.ExGOSGiUpdateException.ErrorType;

public class PluginManager {

	public static String componentName;

	ICoreContext coreContext;
	BundleContext context;
	PluginManagerService service;

	public PluginManager(ICoreContext coreContext, BundleContext context,
			PluginManagerService service) {
		this.coreContext = coreContext;
		this.context = context;
		this.service = service;
	}

	public ICoreContext getCoreContext() {
		return coreContext;
	}

	public BundleContext getContext() {
		return context;
	}

	/**
	 * Vraća listu vektora Stringova sa podacima o bundlovima Pozivaju ga
	 * komanda 'plugins' i glavni prozor PM-a da bi ih ispisali.
	 * 
	 * @return lista vektora stringova sa podacima o bundlovima.
	 */
	public ArrayList<Vector<String>> getBundles() {
		ArrayList<Vector<String>> lista = new ArrayList<Vector<String>>();
		Bundle[] bundles = context.getBundles();
		String state;
		Vector<String> bundle;
		int stateInt;

		for (Bundle b : bundles) {
			if (b.getBundleId() == 0)
				continue;
			stateInt = b.getState();
			state = String.valueOf(b.getState());
			switch (stateInt) {
			case Bundle.ACTIVE:
				state = "Active";
				break;
			case Bundle.INSTALLED:
				state = "Installed";
				break;
			case Bundle.RESOLVED:
				state = "Resolved";
				break;
			case Bundle.STARTING:

				state = "Starting";
				break;
			case Bundle.STOPPING:
				state = "Stopping";
				break;
			}
			bundle = new Vector<String>();
			bundle.add(String.valueOf(b.getBundleId()));
			bundle.add(b.getSymbolicName());
			bundle.add(b.getLocation());
			bundle.add(state);
			lista.add(bundle);
		}
		return lista;
	}

	/**
	 * Instalira novi bundle iz filesystema, pri čemu ga kopira u plugins, pa
	 * tek potom instalira i pokreće.
	 * 
	 * @param path
	 *            putanja do jar paketa
	 * @return referencu na upravo instaliran Bundle
	 * @throws BundleException
	 *             u vezi sa osgi problemom u toku instalacije
	 * @throws IOException
	 *             greška u čitanju/pisanju sa medijuma
	 */
	public Bundle installFilesysetem(String _path) throws BundleException,
			IOException {
		String path = _path;
		String[] fileNameTokens = path.split(File.separator + File.separator);
		String filename = fileNameTokens[fileNameTokens.length - 1];
		String destFullPath = null;
		destFullPath = (new File(this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().getPath().replaceAll("%20", " ")
				.replace(this.getClass().getSimpleName() + ".jar", "")))
				+ File.separator + filename;

		if (!path.equals(destFullPath)) {
			FileInputStream input;
			input = new FileInputStream(path);
			FileOutputStream output = new FileOutputStream(destFullPath);
			ReadableByteChannel rbc = Channels.newChannel(input);
			output.getChannel().transferFrom(rbc, 0, 1 << 24);
			output.close();
		}
		return context.installBundle("FILE:" + destFullPath);
	}

	/**
	 * Instalira plugin sa neke web adrese.
	 * 
	 * @param url
	 *            koja vodi do jar paketa komponente
	 * @throws ExGOSGiUpdateException
	 *             ishod update-a
	 */
	public void installURL(String url) throws ExGOSGiUpdateException {
		Bundle bundle = null;
		try {
			String[] urlTokens = url.split("/");
			String filename = urlTokens[urlTokens.length - 1];
			String fullPath;
			fullPath = (new File(this.getClass().getProtectionDomain()
					.getCodeSource().getLocation().getPath().replaceAll("%20",
							" ").replace(
							this.getClass().getSimpleName() + ".jar", "")))
					+ File.separator + filename;
			if (!url.startsWith("http://") && !url.startsWith("https://")
					&& !url.startsWith("ftp://"))
				url = "http://" + url;
			URL repo = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(repo.openStream());
			FileOutputStream fos = new FileOutputStream(fullPath);
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
			fos.close();
			fullPath = "FILE:" + fullPath;
			bundle = context.installBundle(fullPath);
		} catch (MalformedURLException e) {
			throw new ExGOSGiUpdateException(bundle, ErrorType.BAD_URL,
					"Invalid update URL.");
		} catch (BundleException e) {
			throw new ExGOSGiUpdateException(bundle, ErrorType.BUNDLE_ERROR,
					"Update error: " + e.getMessage());
		} catch (IOException e) {
			throw new ExGOSGiUpdateException(bundle, ErrorType.IO_ERROR,
					"IO error: " + e.getMessage());
		}
	}

	public Bundle getBundle(Integer id) throws NumberFormatException {
		return context.getBundle(id);
	}

	public PluginManagerService getService() {
		return service;
	}

	/**
	 * Proverava da li ima novija verzija bundle-a. Ako ima, kopira je preko
	 * stare verzije i pokreće.
	 * 
	 * @param id
	 *            identifikator bundle-a kojeg updateujemo
	 * @throws ExGOSGiUpdateException
	 *             greška prilikom update-a
	 */
	public void updateBundle(Integer id) throws ExGOSGiUpdateException {
		Bundle bundle = context.getBundle(id);
		String oldVersion;
		try {
			oldVersion = bundle.getHeaders().get(Constants.BUNDLE_VERSION)
					.toString();
			String location = (new File(this.getClass().getProtectionDomain()
					.getCodeSource().getLocation().getPath().replaceAll("%20",
							" ").replace(
							this.getClass().getSimpleName() + ".jar", "")))
					+ File.separator + bundle.getSymbolicName() + ".jar";
			URL repo = new URL(bundle.getHeaders().get(
					Constants.BUNDLE_UPDATELOCATION).toString());
			ReadableByteChannel rbc = Channels.newChannel(repo.openStream());
			FileOutputStream fos = new FileOutputStream(location);
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
			fos.close();
			bundle.update(new FileInputStream(location));
			if (oldVersion.endsWith(bundle.getHeaders().get(
					Constants.BUNDLE_VERSION).toString()))
				throw new ExGOSGiUpdateException(bundle,
						ErrorType.ALLREADY_UPTODATE, "Component is up to date!");
		} catch (MalformedURLException e) {
			throw new ExGOSGiUpdateException(bundle, ErrorType.BAD_URL,
					"Invalid update URL.");
		} catch (BundleException e) {
			throw new ExGOSGiUpdateException(bundle, ErrorType.BUNDLE_ERROR,
					"Update error: " + e.getMessage());
		} catch (IOException e) {
			throw new ExGOSGiUpdateException(bundle, ErrorType.IO_ERROR,
					"IO error: " + e.getMessage());
		} catch (NullPointerException e) {
			throw new ExGOSGiUpdateException(bundle, ErrorType.NO_REPOSITORY,
					"Update site or version not defined. You must update this plugin manually.");
		}
	}

}
