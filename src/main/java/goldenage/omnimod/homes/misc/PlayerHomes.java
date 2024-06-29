package goldenage.omnimod.homes.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import goldenage.omnimod.interfaces.JSONable;
import goldenage.omnimod.misc.Position;

import java.util.HashMap;
import java.util.Map;

public class PlayerHomes implements JSONable {
	private final int MAX_HOMES;
	private Map<String, Position> positions;

	public PlayerHomes(int max_homes) {
		MAX_HOMES = max_homes;
		positions = new HashMap<>();
	}

	public Position getHome(String name) {
		if (!positions.containsKey(name)) {
			return null;
		}

		return positions.get(name);
	}

	public boolean addHome(String name, Position position) {
		if (positions.size() == MAX_HOMES) {
			return false;
		}

		positions.put(name, position);

		return true;
	}

	public boolean removeHome(String name) {
		if (!positions.containsKey(name)) {
			return false;
		}

		positions.remove(name);

		return true;
	}

	public Map<String, Position> getAllHomes() {
		return positions;
	}

	@Override
	public JsonElement toJson() {
		JsonArray jsonArray = new JsonArray();

		for (String key : positions.keySet()) {
			JsonObject jsonObject = new JsonObject();

			jsonObject.addProperty("name", key);
			jsonObject.add("position", positions.get(key).toJson());

			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}
}
