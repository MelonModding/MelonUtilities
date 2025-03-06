package MelonUtilities.config.datatypes.legacydeserializers;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.datatypes.data.Role;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.util.helper.UUIDHelper;

import java.util.UUID;

public class RoleLDs {
	public static Role legacyDeserialize0(JsonObject obj){

		JsonObject generalValues = obj.getAsJsonObject("General Values");

		Role role = new Role(generalValues.get("roleID").getAsString());
		role.roleVersion = MelonUtilities.roleConfigVersion;

		JsonObject display = obj.getAsJsonObject("Display");
		JsonObject displayBorder = obj.getAsJsonObject("Display Border");
		JsonObject username = obj.getAsJsonObject("Username");
		JsonObject usernameBorder = obj.getAsJsonObject("Username Border");
		JsonObject text = obj.getAsJsonObject("Text");


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
}
