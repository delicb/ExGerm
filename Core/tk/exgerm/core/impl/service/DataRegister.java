package tk.exgerm.core.impl.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa čuva sve podatke koje joj komponente dostave da ih čuva da bi ih ista
 * ili druga komponenta preuzela kasnije.  
 * 
 * @author Tim 2
 */
public class DataRegister {
	protected Map<String, Object> data = new HashMap<String, Object>();
	
	public void put(String key, Object value) {
		data.put(key, value);
	}
	
	public Object get(String key) {
		return data.get(key);
	}
}
