package MelonUtilities.config.custom.jsonadapters;

import MelonUtilities.config.custom.classes.Role;
import com.google.gson.*;

import java.lang.reflect.Type;

public class RoleJsonAdapter implements JsonDeserializer<Role>, JsonSerializer<Role> {
	@Override
	public Role deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		JsonObject display = obj.getAsJsonObject("Display:");
		JsonObject displayBorder = obj.getAsJsonObject("Display Border:");
		JsonObject username = obj.getAsJsonObject("Username:");
		JsonObject usernameBorder = obj.getAsJsonObject("Username Border:");
		JsonObject text = obj.getAsJsonObject("Text:");
		JsonObject generalValues = obj.getAsJsonObject("General Values:");

		Role role = new Role(generalValues.get("roleID").getAsString());

		role.displayColor = display.get("displayColor").getAsString();
		role.displayName = display.get("displayName").getAsString();
		role.isDisplayUnderlined = display.get("isDisplayUnderlined").getAsBoolean();
		role.isDisplayBold = display.get("isDisplayBold").getAsBoolean();
		role.isDisplayItalics = display.get("isDisplayItalics").getAsBoolean();

		role.displayBorderColor = displayBorder.get("displayBorderColor").getAsString();
		role.isDisplayBorderNone = displayBorder.get("isDisplayBorderNone").getAsBoolean();
		role.isDisplayBorderBracket = displayBorder.get("isDisplayBorderBracket").getAsBoolean();
		role.isDisplayBorderCurly = displayBorder.get("isDisplayBorderCurly").getAsBoolean();
		role.isDisplayBorderCaret = displayBorder.get("isDisplayBorderCaret").getAsBoolean();
		role.isDisplayBorderCustom = displayBorder.get("isDisplayBorderCustom").getAsBoolean();
		role.customDisplayBorderPrefix = displayBorder.get("customDisplayBorderPrefix").getAsString();
		role.customDisplayBorderSuffix = displayBorder.get("customDisplayBorderSuffix").getAsString();

		role.usernameColor = username.get("usernameColor").getAsString();
		role.isUsernameUnderlined = username.get("isUsernameUnderlined").getAsBoolean();
		role.isUsernameBold = username.get("isUsernameBold").getAsBoolean();
		role.isUsernameItalics = username.get("isUsernameItalics").getAsBoolean();

		role.usernameBorderColor = usernameBorder.get("usernameBorderColor").getAsString();
		role.isUsernameBorderNone = usernameBorder.get("isUsernameBorderNone").getAsBoolean();
		role.isUsernameBorderBracket = usernameBorder.get("isUsernameBorderBracket").getAsBoolean();
		role.isUsernameBorderCurly = usernameBorder.get("isUsernameBorderCurly").getAsBoolean();
		role.isUsernameBorderCaret = usernameBorder.get("isUsernameBorderCaret").getAsBoolean();
		role.isUsernameBorderCustom = usernameBorder.get("isUsernameBorderCustom").getAsBoolean();
		role.customUsernameBorderPrefix = usernameBorder.get("customUsernameBorderPrefix").getAsString();
		role.customUsernameBorderSuffix = usernameBorder.get("customUsernameBorderSuffix").getAsString();

		role.textColor = text.get("textColor").getAsString();
		role.isTextUnderlined = text.get("isTextUnderlined").getAsBoolean();
		role.isTextBold = text.get("isTextBold").getAsBoolean();
		role.isTextItalics = text.get("isTextItalics").getAsBoolean();

		role.priority = generalValues.get("priority").getAsInt();

		JsonArray playersGrantedRole = generalValues.getAsJsonArray("playersGrantedRole");
		for(JsonElement element : playersGrantedRole){
			role.playersGrantedRole.add(element.getAsString());
		}

		return role;
	}

	@Override
	public JsonElement serialize(Role src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		JsonObject display = new JsonObject();
		JsonObject displayBorder = new JsonObject();
		JsonObject username = new JsonObject();
		JsonObject usernameBorder = new JsonObject();
		JsonObject text = new JsonObject();
		JsonObject generalValues = new JsonObject();

		display.addProperty("displayColor", src.displayColor);
		display.addProperty("displayName", src.displayName);
		display.addProperty("isDisplayUnderlined", src.isDisplayUnderlined);
		display.addProperty("isDisplayBold", src.isDisplayBold);
		display.addProperty("isDisplayItalics", src.isDisplayItalics);

		obj.add("Display:", display);

		displayBorder.addProperty("displayBorderColor", src.displayBorderColor);
		displayBorder.addProperty("isDisplayBorderNone", src.isDisplayBorderNone);
		displayBorder.addProperty("isDisplayBorderBracket", src.isDisplayBorderBracket);
		displayBorder.addProperty("isDisplayBorderCurly", src.isDisplayBorderCurly);
		displayBorder.addProperty("isDisplayBorderCaret", src.isDisplayBorderCaret);
		displayBorder.addProperty("isDisplayBorderCustom", src.isDisplayBorderCustom);
		displayBorder.addProperty("customDisplayBorderPrefix", src.customDisplayBorderPrefix);
		displayBorder.addProperty("customDisplayBorderSuffix", src.customDisplayBorderSuffix);

		obj.add("Display Border:", displayBorder);

		username.addProperty("usernameColor", src.usernameColor);
		username.addProperty("isUsernameUnderlined", src.isUsernameUnderlined);
		username.addProperty("isUsernameBold", src.isUsernameBold);
		username.addProperty("isUsernameItalics", src.isUsernameItalics);

		obj.add("Username:", username);

		usernameBorder.addProperty("usernameBorderColor", src.usernameBorderColor);
		usernameBorder.addProperty("isUsernameBorderNone", src.isUsernameBorderNone);
		usernameBorder.addProperty("isUsernameBorderBracket", src.isUsernameBorderBracket);
		usernameBorder.addProperty("isUsernameBorderCurly", src.isUsernameBorderCurly);
		usernameBorder.addProperty("isUsernameBorderCaret", src.isUsernameBorderCaret);
		usernameBorder.addProperty("isUsernameBorderCustom", src.isUsernameBorderCustom);
		usernameBorder.addProperty("customUsernameBorderPrefix", src.customUsernameBorderPrefix);
		usernameBorder.addProperty("customUsernameBorderSuffix", src.customUsernameBorderSuffix);

		obj.add("Username Border:", usernameBorder);

		text.addProperty("textColor", src.textColor);
		text.addProperty("isTextUnderlined", src.isTextUnderlined);
		text.addProperty("isTextBold", src.isTextBold);
		text.addProperty("isTextItalics", src.isTextItalics);

		obj.add("Text:", text);

		generalValues.addProperty("roleID", src.roleID);
		generalValues.addProperty("priority", src.priority);
		JsonArray playersGrantedRole = new JsonArray();
		for(String player : src.playersGrantedRole){
			playersGrantedRole.add(player);
		}
		generalValues.add("playersGrantedRole", playersGrantedRole);

		obj.add("General Values:", generalValues);

		return obj;
	}
}
