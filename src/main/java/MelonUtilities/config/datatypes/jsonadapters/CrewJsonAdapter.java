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

		Crew crew = new Crew(obj.get("name").getAsString(), obj.get("owner").getAsString(), executives, members);

/*		crew.crewVersion = obj.has("crewVersion") ? obj.get("crewVersion").getAsInt() : 0;
		if(crew.crewVersion < MelonUtilities.crewConfigVersion){
			return legacyDeserialize(obj, crew.crewVersion);
		}*/

		return crew;
	}

	@Override
	public JsonElement serialize(Crew src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", src.name);
		obj.addProperty("owner", src.owner);
		return obj;
	}

	/*private Crew legacyDeserialize(JsonObject obj, int crewVersion){
		switch(crewVersion){
			case 0:
				return CrewLDs.legacyDeserialize0(obj);
		}
		throw new IllegalArgumentException("(Crew) legacy deserialize failed: no legacy deserializer present for current version!");
	}*/
}
