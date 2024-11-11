package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Home;
import MelonUtilities.config.datatypes.data.User;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

public class UserJsonAdapter implements JsonDeserializer<User>, JsonSerializer<User> {
	@Override
	public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		JsonObject homeDataObj = obj.getAsJsonObject("Home Data");
		JsonObject helperDataObj = obj.getAsJsonObject("Helper Data");
		JsonObject lockDataObj = obj.getAsJsonObject("Lock Data");
		JsonObject userDataObj = obj.getAsJsonObject("User Data");

		User user = new User(UUID.fromString(userDataObj.get("playerUUID").getAsString()));

		JsonArray homes = homeDataObj.getAsJsonArray("homeData");
		for(JsonElement element : homes){
			user.homeData.add(context.deserialize(element, Home.class));
		}

		user.isHelper = helperDataObj.get("isHelper").getAsBoolean();

		user.lockOnBlockPlaced = lockDataObj.get("lockOnBlockPlaced").getAsBoolean();
		user.lockOnBlockPunched = lockDataObj.get("lockOnBlockPunched").getAsBoolean();
		user.lockBypass = lockDataObj.get("lockBypass").getAsBoolean();
		JsonArray playersTrustedToAllContainers = lockDataObj.getAsJsonArray("playersTrustedToAllContainers");
		for(JsonElement element : playersTrustedToAllContainers){
			user.uuidsTrustedToAllContainers.add(UUID.fromString(element.getAsString()));
		}

		user.uuid = UUID.fromString(userDataObj.get("playerUUID").getAsString());

		return user;
	}

	@Override
	public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();

		JsonObject homeData = new JsonObject();
		JsonArray homes = new JsonArray();
		for(Home home : src.homeData){
			homes.add(context.serialize(home));
		}
		homeData.add("kitArmorStacks", homes);
		obj.add("Home Data", homeData);

		JsonObject helperData = new JsonObject();
		helperData.addProperty("isHelper", src.isHelper);
		obj.add("Helper Data", helperData);

		JsonObject lockData = new JsonObject();
		lockData.addProperty("lockOnBlockPlaced", src.lockOnBlockPlaced);
		lockData.addProperty("lockOnBlockPunched", src.lockOnBlockPunched);
		lockData.addProperty("lockBypass", src.lockBypass);
		JsonArray playersTrustedToAllContainers = new JsonArray();
		for(UUID uuid : src.uuidsTrustedToAllContainers){
			playersTrustedToAllContainers.add(uuid.toString());
		}
		lockData.add("playersTrustedToAllContainers", playersTrustedToAllContainers);
		obj.add("Lock Data", lockData);

		JsonObject playerData = new JsonObject();
		playerData.addProperty("playerUUID", String.valueOf(src.uuid));
		obj.add("Player Data", playerData);

		return obj;
	}
}
