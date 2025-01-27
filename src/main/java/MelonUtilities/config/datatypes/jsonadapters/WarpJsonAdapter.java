package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Warp;
import com.google.gson.*;

import java.lang.reflect.Type;

public class WarpJsonAdapter implements JsonDeserializer<Warp>, JsonSerializer<Warp> {

	@Override
	public Warp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
        return new Warp(obj.get("name").getAsString(), obj.get("x").getAsDouble(), obj.get("y").getAsDouble(), obj.get("z").getAsDouble(), obj.get("dimID").getAsInt());
	}

	@Override
	public JsonElement serialize(Warp src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", src.name);
		obj.addProperty("x", src.x);
		obj.addProperty("y", src.y);
		obj.addProperty("z", src.z);
		obj.addProperty("dimID", src.dimID);
		return obj;
	}

}
