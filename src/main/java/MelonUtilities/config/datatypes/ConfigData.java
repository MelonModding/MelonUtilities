package MelonUtilities.config.datatypes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigData {

		@SerializedName(value = "Default Role: ")
		@Expose public String defaultRole = null;

		@SerializedName(value = "Display Mode: ")
		@Expose public String displayMode = "multi";

		@SerializedName(value = "Time Between Snapshots (in seconds): \nThe longer the time, the more ram/memory is used. \nThe shorter the time, the more chance for save corruption/other unintended effects. \n 30-90 seconds works best")
		@Expose public int timeBetweenSnapshots = 40;

		@SerializedName(value = "Time Between Backups (in hours): ")
		@Expose public int timeBetweenBackups = 12;
}
