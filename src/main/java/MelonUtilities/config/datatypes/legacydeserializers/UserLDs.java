package MelonUtilities.config.datatypes.legacydeserializers;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.datatypes.data.Home;
import MelonUtilities.config.datatypes.data.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.UUID;

public class UserLDs {
	public static User legacyDeserialize0(JsonDeserializationContext context, JsonObject obj){
		//New User
		JsonObject userDataObj = obj.getAsJsonObject("User Data");
		User user = new User(UUID.fromString(userDataObj.get("userUUID").getAsString()));
		user.userVersion = MelonUtilities.userConfigVersion;

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
}
