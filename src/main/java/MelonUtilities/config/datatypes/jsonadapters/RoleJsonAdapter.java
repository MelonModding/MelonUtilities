package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Role;
import com.google.gson.*;
import net.minecraft.core.util.helper.UUIDHelper;

import java.lang.reflect.Type;
import java.util.UUID;

public class RoleJsonAdapter implements JsonDeserializer<Role>, JsonSerializer<Role> {

	public static String fileName;

	@Override
	public Role deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		if(obj.has("Role Display Color:")){
			return legacyDeserialize(obj);
		}
		JsonObject display = obj.getAsJsonObject("Display");
		JsonObject displayBorder = obj.getAsJsonObject("Display Border");
		JsonObject username = obj.getAsJsonObject("Username");
		JsonObject usernameBorder = obj.getAsJsonObject("Username Border");
		JsonObject text = obj.getAsJsonObject("Text");
		JsonObject generalValues = obj.getAsJsonObject("General Values");

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
			if(!UUIDHelper.isUUID(element.getAsString())){
				UUIDHelper.runConversionAction(element.getAsString(), uUID -> role.playersGrantedRole.add(uUID), null);
				continue;
			}
			role.playersGrantedRole.add(UUID.fromString(element.getAsString()));
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

		obj.add("Display", display);

		displayBorder.addProperty("displayBorderColor", src.displayBorderColor);
		displayBorder.addProperty("isDisplayBorderNone", src.isDisplayBorderNone);
		displayBorder.addProperty("isDisplayBorderBracket", src.isDisplayBorderBracket);
		displayBorder.addProperty("isDisplayBorderCurly", src.isDisplayBorderCurly);
		displayBorder.addProperty("isDisplayBorderCaret", src.isDisplayBorderCaret);
		displayBorder.addProperty("isDisplayBorderCustom", src.isDisplayBorderCustom);
		displayBorder.addProperty("customDisplayBorderPrefix", src.customDisplayBorderPrefix);
		displayBorder.addProperty("customDisplayBorderSuffix", src.customDisplayBorderSuffix);

		obj.add("Display Border", displayBorder);

		username.addProperty("usernameColor", src.usernameColor);
		username.addProperty("isUsernameUnderlined", src.isUsernameUnderlined);
		username.addProperty("isUsernameBold", src.isUsernameBold);
		username.addProperty("isUsernameItalics", src.isUsernameItalics);

		obj.add("Username", username);

		usernameBorder.addProperty("usernameBorderColor", src.usernameBorderColor);
		usernameBorder.addProperty("isUsernameBorderNone", src.isUsernameBorderNone);
		usernameBorder.addProperty("isUsernameBorderBracket", src.isUsernameBorderBracket);
		usernameBorder.addProperty("isUsernameBorderCurly", src.isUsernameBorderCurly);
		usernameBorder.addProperty("isUsernameBorderCaret", src.isUsernameBorderCaret);
		usernameBorder.addProperty("isUsernameBorderCustom", src.isUsernameBorderCustom);
		usernameBorder.addProperty("customUsernameBorderPrefix", src.customUsernameBorderPrefix);
		usernameBorder.addProperty("customUsernameBorderSuffix", src.customUsernameBorderSuffix);

		obj.add("Username Border", usernameBorder);

		text.addProperty("textColor", src.textColor);
		text.addProperty("isTextUnderlined", src.isTextUnderlined);
		text.addProperty("isTextBold", src.isTextBold);
		text.addProperty("isTextItalics", src.isTextItalics);

		obj.add("Text", text);

		generalValues.addProperty("roleID", src.roleID);
		generalValues.addProperty("priority", src.priority);
		JsonArray playersGrantedRole = new JsonArray();
		for(UUID uuid : src.playersGrantedRole){
			playersGrantedRole.add(String.valueOf(uuid));
		}
		generalValues.add("playersGrantedRole", playersGrantedRole);

		obj.add("General Values", generalValues);

		return obj;
	}

	private Role legacyDeserialize(JsonObject obj){

		Role role = new Role(fileName);

		role.displayColor = obj.get("Role Display Color:").getAsString();
		role.displayName = obj.get("Role Display Name:").getAsString();
		role.isDisplayUnderlined = obj.get("Role Display Underlined:").getAsBoolean();
		role.isDisplayBold = obj.get("Role Display Bold:").getAsBoolean();
		role.isDisplayItalics = obj.get("Role Display Italics:").getAsBoolean();

		role.displayBorderColor = obj.get("Display Border Color:").getAsString();
		role.isDisplayBorderNone = obj.get("No Display Border:").getAsBoolean();
		role.isDisplayBorderBracket = obj.get("Bracket Display Border:").getAsBoolean();
		role.isDisplayBorderCurly = obj.get("Curly Bracket Display Border:").getAsBoolean();
		role.isDisplayBorderCaret = obj.get("Caret Display Border:").getAsBoolean();
		role.isDisplayBorderCustom = obj.get("Custom Display Border:").getAsBoolean();
		role.customDisplayBorderPrefix = obj.get("Custom Display Border Prefix:").getAsString();
		role.customDisplayBorderSuffix = obj.get("Custom Display Border Suffix:").getAsString();

		role.usernameColor = obj.get("Role Username Color:").getAsString();
		role.isUsernameUnderlined = obj.get("Role Username Underlined:").getAsBoolean();
		role.isUsernameBold = obj.get("Role Username Bold:").getAsBoolean();
		role.isUsernameItalics = obj.get("Role Username Italics:").getAsBoolean();

		role.usernameBorderColor = obj.get("Username Border Color:").getAsString();
		role.isUsernameBorderNone = obj.get("No Username Border:").getAsBoolean();
		role.isUsernameBorderBracket = obj.get("Bracket Username Border:").getAsBoolean();
		role.isUsernameBorderCurly = obj.get("Curly Bracket Username Border:").getAsBoolean();
		role.isUsernameBorderCaret = obj.get("Caret Username Border:").getAsBoolean();
		role.isUsernameBorderCustom = obj.get("Custom Username Border:").getAsBoolean();
		role.customUsernameBorderPrefix = obj.get("Custom Username Border Prefix:").getAsString();
		role.customUsernameBorderSuffix = obj.get("Custom Username Border Suffix:").getAsString();

		role.textColor = obj.get("Role Text Color:").getAsString();
		role.isTextUnderlined = obj.get("Role Text Underlined:").getAsBoolean();
		role.isTextBold = obj.get("Role Text Bold:").getAsBoolean();
		role.isTextItalics = obj.get("Role Text Italics:").getAsBoolean();

		role.priority = obj.get("Role Priority: (Highest - 0..1..2.. - Lowest)").getAsInt();

		JsonArray playersGrantedRole = obj.getAsJsonArray("Players Granted Role:");
		for(JsonElement element : playersGrantedRole){
			UUIDHelper.runConversionAction(element.getAsString(), uUID -> role.playersGrantedRole.add(uUID), null);
		}

		return role;
	}

}
