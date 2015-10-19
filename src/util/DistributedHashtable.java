package util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DistributedHashtable {

	public static ArrayList<String> readConfigFile() throws IOException {
		// read from a JSON
		JSONParser parser = new JSONParser();
		ArrayList<String> peerList = new ArrayList<String>();

		try {

			Object obj = parser.parse(new FileReader("config.json"));

			JSONObject jsonObject = (JSONObject) obj;

			// loop array
			JSONArray msg = (JSONArray) jsonObject.get("peers");
			Iterator<?> iterator = msg.iterator();

			while (iterator.hasNext()) {
				peerList.add((String) iterator.next());
			}

		} catch (Exception e) {
			System.out.println("Couldn't read from config file");
			e.printStackTrace();
			return null;
		}

		return peerList;
	}

	public static int hash(String key, int numPeers) {
		int sum = 0;
		for (int i = 0; i < key.length(); i++)
			sum += key.charAt(i);

		return sum % numPeers;
	}
}
