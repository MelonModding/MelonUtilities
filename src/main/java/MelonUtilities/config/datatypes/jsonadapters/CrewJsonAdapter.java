package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Crew;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CrewJsonAdapter implements JsonDeserializer<Crew>, JsonSerializer<Crew> {

	@Override
	public Crew deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();

		List<String> members = new ArrayList<>();
		for(JsonElement e : obj.getAsJsonArray("members")){
			members.add(e.getAsJsonObject().getAsString());
		}

		List<String> executives = new ArrayList<>();
		for(JsonElement e : obj.getAsJsonArray("executives")){
			executives.add(e.getAsJsonObject().getAsString());
		}

		return new Crew(
			obj.get("name").getAsString(),
			obj.get("owner").getAsString(),
			executives,
			members
		);
	}

	@Override
	public JsonElement serialize(Crew src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", src.name);
		obj.addProperty("owner", src.owner);
		return obj;
	}
}
