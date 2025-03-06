package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.datatypes.data.Home;
import MelonUtilities.config.datatypes.data.User;
import MelonUtilities.config.datatypes.legacydeserializers.UserLDs;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class UserJsonAdapter implements JsonDeserializer<User>, JsonSerializer<User> {
	@Override
	public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();

		//New User
		JsonObject userDataObj = obj.getAsJsonObject("User Data");
		User user = new User(UUID.fromString(userDataObj.get("userUUID").getAsString()));

		//Legacy Check
		user.userVersion = obj.has("userVersion") ? obj.get("userVersion").getAsInt() : 0;
		if(user.userVersion < MelonUtilities.userConfigVersion){
			return legacyDeserialize(context, obj, user.userVersion);
		}

		//Extra Categories
		JsonObject homeDataObj = obj.getAsJsonObject("Home Data");
		JsonObject helperDataObj = obj.getAsJsonObject("Helper Data");
		JsonObject lockDataObj = obj.getAsJsonObject("Lock Data");


		//Home Data
		JsonArray homes = homeDataObj.getAsJsonArray("homes");
		for(JsonElement element : homes){
			user.homeData.add(context.deserialize(element, Home.class));
		}

		//Helper Data
		user.isHelper = helperDataObj.get("isHelper").getAsBoolean();

		//Lock Data
		user.lockOnBlockPlaced = lockDataObj.get("lockOnBlockPlaced").getAsBoolean();
		user.lockOnBlockPunched = lockDataObj.get("lockOnBlockPunched").getAsBoolean();
		user.lockBypass = lockDataObj.get("lockBypass").getAsBoolean();
		JsonObject usersTrustedToAllContainers = lockDataObj.getAsJsonObject("usersTrustedToAllContainers");
		for(Map.Entry<String, JsonElement> entry : usersTrustedToAllContainers.entrySet()){
			user.usersTrustedToAllContainers.put(UUID.fromString(entry.getKey()), entry.getValue().getAsString());
		}

		//User Data
		user.uuid = UUID.fromString(userDataObj.get("userUUID").getAsString());

		return user;
	}

	@Override
	public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		JsonObject homeData = new JsonObject();
		JsonObject helperData = new JsonObject();
		JsonObject lockData = new JsonObject();
		JsonObject usersTrustedToAllContainers = new JsonObject();
		JsonObject user = new JsonObject();


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

		for(Map.Entry<UUID, String> entry : src.usersTrustedToAllContainers.entrySet()){
			usersTrustedToAllContainers.addProperty(entry.getKey().toString(), entry.getValue());
		}
		lockData.add("usersTrustedToAllContainers", usersTrustedToAllContainers);
		obj.add("Lock Data", lockData);

		user.addProperty("userUUID", String.valueOf(src.uuid));
		obj.add("User Data", user);

		return obj;
	}

	private User legacyDeserialize(JsonDeserializationContext context, JsonObject obj, int userVersion){
		switch(userVersion){
			case 0:
				return UserLDs.legacyDeserialize0(context, obj);
		}
		throw new IllegalArgumentException("(User) legacy deserialize failed: no legacy deserializer present for current version!");
	}

}
