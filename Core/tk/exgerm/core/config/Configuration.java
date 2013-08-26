package tk.exgerm.core.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Klasa koja ce sadržati sve konfiguracije i brinuti o njima. Singletone je
 * šablona i statički joj se može pristupati.
 * 
 */
public class Configuration {

	/**
	 * Hashtabela koja sadrzi tabele koje predstavljaju kategorije.Svaka
	 * kategorija predstavlja jednu komponentu.
	 */
	private Hashtable<String, Hashtable<String, String>> config = new Hashtable<String, Hashtable<String, String>>();

	public Configuration() {
		BufferedReader in = null;

		File file = new File(System.getProperty("user.home") + File.separator
				+ "exgerm" + File.separator + "config.ini");
		if (file.exists()) {
			try {
				in = new BufferedReader(new FileReader(file));
				readINI(in);
				in.close();
			} catch (Exception e) {
				System.out
						.println("Exception occured while reading configuration file!");
			}
		} else
			System.out
					.println("Config.ini file does not exist. New file will be created on exit.");
	};

	/**
	 * Vraca vrednost datog parametra u obliku stringa. Ukoliko pod zadatim
	 * ključem nema vrednosti vraćena vrednost će biti <em>null</em>.
	 * 
	 * @param category
	 *            Kategorija (komponenta) u kojoj se nalazi parametar
	 * @param key
	 *            Naziv parametra
	 * @return String koji sadrzi vrednost parametra
	 */
	public String get(String category, String key) {
		Hashtable<String, String> hm = config.get(category);
		if (hm == null)
			return null;
		else
			return (String) hm.get(key);
	}

	public String remove(String category, String key){
		Hashtable<String, String> hm = config.get(category);
		if (hm == null)
			return null;
		else
			return (String) hm.remove(key);
	}
	
	/**
	 * Dodaje pod kategoriju <em>category</em> i pod ključem <em>key</em>
	 * vrednost <em>value</em>. Ukoliko kategorija ne postoji, dodaće se nova,
	 * koja je zadata. Ukoliko u zadatoj kategoriji pod zadatim ključem postoji
	 * neka vrednost, ta vrednost će biti zamenjena novom.
	 * 
	 * @param category
	 *            Kategorija u koju se dodaje
	 * @param key
	 *            Ključ za koji se dodaje
	 * @param value
	 *            Vrednost koja se dodaje
	 */
	public void put(String category, String key, String value) {
		if (config.get(category) == null)
			config.put(category, new Hashtable<String, String>());
		config.get(category).put(key, value);
	}

	/**
	 * Metoda koja cita iz fajla konfiguraciju i kreira mape sa potrebnim
	 * sadržajem koji je rezultat parsiranja.
	 * 
	 * @param in
	 */
	private void readINI(BufferedReader in) {
		String line, key, value;
		StringTokenizer st;
		String currentCategory = null;
		Hashtable<String, String> currentMap = new Hashtable<String, String>();
		try {
			if(in.ready())
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf(';') == 0)
					continue;
				if (line.charAt(0) == '[') {
					currentCategory = line.substring(1, line.length() - 1);
					currentMap = new Hashtable<String, String>();
					config.put(currentCategory, currentMap);
				} else {
					st = new StringTokenizer(line, "=");
					if (st.hasMoreTokens()) {
						key = st.nextToken().trim();
						boolean hastoks = false;
						value = "";
						while (st.hasMoreTokens()) {
							value += (hastoks ? "=" : "")
									+ st.nextToken().trim();
							hastoks = true;
						}
						currentMap.put(key, value);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void save() {
		BufferedWriter out;
		File file = new File(System.getProperty("user.home")
							+ File.separator + "exgerm" + File.separator
							+ "config.ini");
		
		try {
			File dir = new File(file.getParent());
			if(!dir.exists()){
				dir.mkdir();
			    file.createNewFile();
			}				
			else 
				if(!file.exists())
					file.createNewFile();
			out = new BufferedWriter(
					new FileWriter(file));

			Collection<Hashtable<String, String>> categories = config.values();
			Set<String> components = config.keySet();

			Iterator<Hashtable<String, String>> component = categories
					.iterator();
			Iterator<String> cat = components.iterator();

			while (cat.hasNext() && component.hasNext()) {
				// upisujemo kategoriju na pocetku
				Hashtable<String, String> current = component.next();
				out.write("[" + cat.next() + "]");
				out.newLine();

				// vadim set i vrednosti
				Iterator<String> values = current.values().iterator();
				Iterator<String> names = current.keySet().iterator();
				while (values.hasNext() && names.hasNext()) {
					out.write(names.next() + "=" + values.next());
					out.newLine();
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
