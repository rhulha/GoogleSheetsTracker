package net.raysforge.gst;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

public class SheetsCache {
	
	HashMap<String, List<List<Object>>> sheets = new HashMap<String, List<List<Object>>>();

	public List<List<Object>> getValues(String spreadsheetId, String range) {
		
		String key = spreadsheetId + "<-:->" + range;
		
		if (!sheets.containsKey(key)) {
			
			try {
				sheets.put(key, SheetsClient.getValues(spreadsheetId, range));
			} catch (GeneralSecurityException | IOException e) {
				throw new RuntimeException(e);
			}
		}

		return sheets.get(key);
	}

	public void clear() {
		sheets.clear();
	}

}
