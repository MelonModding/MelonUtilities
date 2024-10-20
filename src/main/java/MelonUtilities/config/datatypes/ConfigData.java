package MelonUtilities.config.datatypes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigData {


		@SerializedName(value = "Default Role: ")
		@Expose public String defaultRole = null;

		@SerializedName(value = "Display Mode: ")
		@Expose public String displayMode = "multi";

		@SerializedName(value = "Time Between Snapshots (in seconds): Longer = more ram/memory is used. Shorter = more chance for save corruption/other unintended effects. 30-240 seconds works best")
		@Expose public int timeBetweenSnapshots = 120;
		public float lastSnapshot = 0;

		@SerializedName(value = "Time Between Backups (in hours): ")
		@Expose public int timeBetweenBackups = 12;
		public float lastBackup = 0;

		@SerializedName(value = "Time Between Pruning Backups (in hours): ")
		@Expose public int timeBetweenBackupPruning = 96;
		public float lastBackupPrune = 0;

		@SerializedName(value = "Time Between Pruning Snapshots (in hours): ")
		@Expose public int timeBetweenSnapshotPruning = 1;
		public float lastSnapshotPrune = 0;
}
