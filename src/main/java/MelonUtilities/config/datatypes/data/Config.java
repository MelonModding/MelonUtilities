package MelonUtilities.config.datatypes.data;

public class Config {

	public String defaultRole = null;
	public String displayMode = "multi";

	public boolean snapshotsEnabled = true;
	public int timeBetweenSnapshots = 120;
	public double lastSnapshot = 0;
	public boolean backupsEnabled = true;
	public int timeBetweenBackups = 12;
	public double lastBackup = 0;
	public int timeBetweenBackupPruning = 96;
	public double lastBackupPrune = 0;
	public int timeBetweenSnapshotPruning = 12;
	public double lastSnapshotPrune = 0;

	public boolean allowObstructions = true;
	public int elevatorCooldown = 6;

	public String JDBCConnectionUrl = "";
}
