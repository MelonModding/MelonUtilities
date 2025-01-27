package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Home;
import MelonUtilities.config.datatypes.data.User;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class UserJsonAdapter implements JsonDeserializer<User>, JsonSerializer<User> {
	@Override
	public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		JsonObject homeDataObj = obj.getAsJsonObject("Home Data");
		JsonObject helperDataObj = obj.getAsJsonObject("Helper Data");
		JsonObject lockDataObj = obj.getAsJsonObject("Lock Data");
		JsonObject userDataObj = obj.getAsJsonObject("User Data");

		User userData = new User(UUID.fromString(userDataObj.get("userUUID").getAsString()));

		JsonArray homes = homeDataObj.getAsJsonArray("homes");
		for(JsonElement element : homes){
			userData.homeData.add(context.deserialize(element, Home.class));
		}

		userData.isHelper = helperDataObj.get("isHelper").getAsBoolean();

		userData.lockOnBlockPlaced = lockDataObj.get("lockOnBlockPlaced").getAsBoolean();
		userData.lockOnBlockPunched = lockDataObj.get("lockOnBlockPunched").getAsBoolean();
		userData.lockBypass = lockDataObj.get("lockBypass").getAsBoolean();
		JsonObject usersTrustedToAllContainers = lockDataObj.getAsJsonObject("usersTrustedToAllContainers");
		for(Map.Entry<String, JsonElement> user : usersTrustedToAllContainers.entrySet()){
			userData.usersTrustedToAllContainers.put(UUID.fromString(user.getKey()), user.getValue().getAsString());
		}

		userData.uuid = UUID.fromString(userDataObj.get("userUUID").getAsString());

		return userData;
	}

	@Override
	public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		JsonObject homeData = new JsonObject();
		JsonObject helperData = new JsonObject();
		JsonObject lockData = new JsonObject();
		JsonObject usersTrustedToAllContainers = new JsonObject();
		JsonObject userData = new JsonObject();


		JsonArray homes = new JsonArray();
		for(Home home : src.homeData){
			homes.add(context.serialize(home));
		}
		homeData.add("homes", homes);
		obj.add("Home Data", homeData);

		helperData.addProperty("isHelper", src.isHelper);
		obj.add("Helper Data", helperData);

		lockData.addProperty("lockOnBlockPlaced", src.lockOnBlockPlaced);
		lockData.addProperty("lockOnBlockPunched", src.lockOnBlockPunched);
		lockData.addProperty("lockBypass", src.lockBypass);

		for(Map.Entry<UUID, String> user : src.usersTrustedToAllContainers.entrySet()){
			usersTrustedToAllContainers.addProperty(user.getKey().toString(), user.getValue());
		}
		lockData.add("usersTrustedToAllContainers", usersTrustedToAllContainers);
		obj.add("Lock Data", lockData);

		userData.addProperty("userUUID", String.valueOf(src.uuid));
		obj.add("User Data", userData);

		return obj;
	}
}
