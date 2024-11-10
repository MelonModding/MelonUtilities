package MelonUtilities.config.custom.classes;

import MelonUtilities.config.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Role {

	//Display
	public String displayColor = "white";
	public String displayName = "DefaultRoleName";
	public boolean isDisplayUnderlined = false;
	public boolean isDisplayBold = false;
	public boolean isDisplayItalics = false;

	//Display Border
	public String displayBorderColor = "white";
	public boolean isDisplayBorderNone = false;
	public boolean isDisplayBorderBracket = true;
	public boolean isDisplayBorderCurly = false;
	public boolean isDisplayBorderCaret = false;
	public boolean isDisplayBorderCustom = false;
	public String customDisplayBorderPrefix = "";
	public String customDisplayBorderSuffix = "";

	//Username
	public String usernameColor = "white";
	public boolean isUsernameUnderlined = false;
	public boolean isUsernameBold = false;
	public boolean isUsernameItalics = false;

	//Username Border
	public String usernameBorderColor = "white";
	public boolean isUsernameBorderNone = false;
	public boolean isUsernameBorderBracket = false;
	public boolean isUsernameBorderCurly = false;
	public boolean isUsernameBorderCaret = true;
	public boolean isUsernameBorderCustom = false;
	public String customUsernameBorderPrefix = "";
	public String customUsernameBorderSuffix = "";

	//Text
	public String textColor = "§0";
	public boolean isTextUnderlined = false;
	public boolean isTextBold = false;
	public boolean isTextItalics = false;

	//Role General Values
	public int priority = 0;
	public List<String> playersGrantedRole = new ArrayList<>();

	public final String roleID;

	public Role(String roleID) {
		this.roleID = roleID;
	}

	public void save(){
		Data.Roles.save(roleID);
	}

	public void delete(){
		Data.Roles.delete(roleID);
	}

}
