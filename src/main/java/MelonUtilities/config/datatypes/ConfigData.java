package MelonUtilities.config.datatypes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigData {

	//Default Role

		@SerializedName(value = "Default Role: ")
		@Expose public String defaultRole = null;

	//Display Mode

		@SerializedName(value = "Display Mode: ")
		@Expose public String displayMode = "multi";

}
