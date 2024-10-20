package MelonUtilities.config.datatypes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigData {


		@SerializedName(value = "Default Role: ")
		@Expose public String defaultRole = null;

		@SerializedName(value = "Display Mode: ")
		@Expose public String displayMode = "multi";

		@SerializedName(value = "Time Between Snapshots (in seconds): ")
		@Expose public int timeBetweenSnapshots = 120;
		public double lastSnapshot = 0;

		@SerializedName(value = "Time Between Backups (in hours): ")
		@Expose public int timeBetweenBackups = 12;
		public double lastBackup = 0;

		@SerializedName(value = "Time Between Pruning Backups (in hours): ")
		@Expose public int timeBetweenBackupPruning = 96;
		public double lastBackupPrune = 0;

		@SerializedName(value = "Time Between Pruning Snapshots (in hours): ")
		@Expose public int timeBetweenSnapshotPruning = 1;
		public double lastSnapshotPrune = 0;
}
