package BTAServerSolutions.BTAServerUtilities.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoleData {

	//Role Display Formatting

		//Display

		@SerializedName(value = "Role Display Color:")
		@Expose public String displayColor = "white";

		@SerializedName(value = "Role Display Name:")
		@Expose public String displayName = "DefaultRoleName";

		@SerializedName(value = "Role Display Underlined:")
		@Expose public boolean isDisplayUnderlined = false;

		@SerializedName(value = "Role Display Bold:")
		@Expose public boolean isDisplayBold = false;

		@SerializedName(value = "Role Display Italics:")
		@Expose public boolean isDisplayItalics = false;

		//Display Border

		@SerializedName(value = "Display Border Color:")
		@Expose public String displayBorderColor = "white";

		@SerializedName(value = "No Display Border:")
		@Expose public boolean isDisplayBorderNone = false;

		@SerializedName(value = "Bracket Display Border:")
		@Expose public boolean isDisplayBorderBracket = true;

		@SerializedName(value = "Curly Bracket Display Border:")
		@Expose public boolean isDisplayBorderCurly = false;

		@SerializedName(value = "Caret Display Border:")
		@Expose public boolean isDisplayBorderCaret = false;

		@SerializedName(value = "Custom Display Border:")
		@Expose public boolean isDisplayBorderCustom = false;

		@SerializedName(value = "Custom Display Border Prefix:")
		@Expose public String customDisplayBorderPrefix = "";

		@SerializedName(value = "Custom Display Border Suffix:")
		@Expose public String customDisplayBorderSuffix = "";

	//Role Username Formatting

		//Username

		@SerializedName(value = "Role Username Color:")
		@Expose public String usernameColor = "white";

		@SerializedName(value = "Role Username Underlined:")
		@Expose public boolean isUsernameUnderlined = false;

		@SerializedName(value = "Role Username Bold:")
		@Expose public boolean isUsernameBold = false;

		@SerializedName(value = "Role Username Italics:")
		@Expose public boolean isUsernameItalics = false;

		//Username Border

		@SerializedName(value = "Username Border Color:")
		@Expose public String usernameBorderColor = "white";

		@SerializedName(value = "No Username Border:")
		@Expose public boolean isUsernameBorderNone = false;

		@SerializedName(value = "Bracket Username Border:")
		@Expose public boolean isUsernameBorderBracket = false;

		@SerializedName(value = "Curly Bracket Username Border:")
		@Expose public boolean isUsernameBorderCurly = false;

		@SerializedName(value = "Caret Username Border:")
		@Expose public boolean isUsernameBorderCaret = true;

		@SerializedName(value = "Custom Username Border:")
		@Expose public boolean isUsernameBorderCustom = false;

		@SerializedName(value = "Custom Username Border Prefix:")
		@Expose public String customUsernameBorderPrefix = "";

		@SerializedName(value = "Custom Username Border Suffix:")
		@Expose public String customUsernameBorderSuffix = "";

	//Role Text Formatting

		//Text

		@SerializedName(value = "Role Text Color:")
		@Expose public String textColor = "§0";

		@SerializedName(value = "Role Text Underlined:")
		@Expose public boolean isTextUnderlined = false;

		@SerializedName(value = "Role Text Bold:")
		@Expose public boolean isTextBold = false;

		@SerializedName(value = "Role Text Italics:")
		@Expose public boolean isTextItalics = false;

	//Players Granted Role

		@SerializedName(value = "Players Granted Role:")
		@Expose public List<String> playersGrantedRole = new ArrayList<>();

	//Role Priority

		@SerializedName(value = "Role Priority: (Highest - 0..1..2.. - Lowest)")
		@Expose public int priority = 0;

}
