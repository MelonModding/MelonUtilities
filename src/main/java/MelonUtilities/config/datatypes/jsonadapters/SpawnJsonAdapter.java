package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Spawn;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SpawnJsonAdapter implements JsonDeserializer<Spawn>, JsonSerializer<Spawn> {

	@Override
	public Spawn deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		return new Spawn(obj.get("x").getAsDouble(), obj.get("y").getAsDouble(), obj.get("z").getAsDouble(), obj.get("dimID").getAsInt());
	}

	@Override
	public JsonElement serialize(Spawn src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("x", src.x);
		obj.addProperty("y", src.y);
		obj.addProperty("z", src.z);
		obj.addProperty("dimID", src.dimID);
		return obj;
	}



}
