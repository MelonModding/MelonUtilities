package MelonUtilities.config.datatypes.data;

public class Config {

	public boolean enableContainerLocking = true;
	public boolean enableRoles = true;
	public boolean enableRollback = true;
	public boolean enableTPA = true;
	public boolean enableWarps = true;
	public boolean enableHomes = true;
	public boolean enableElevators = true;
	public boolean enableKits = true;
	public boolean enableRules = true;
	public boolean enableSmite = true;
	public boolean enableCrews = true;
	public boolean enableSQLPlayerLogging = true;
	public boolean enableTXTPlayerLogging = true;

	public String defaultRole = null;
	public String displayMode = "multi";

	public boolean snapshotsEnabled = true;
	public boolean backupsEnabled = true;
	public int timeBetweenSnapshots = 120;
	public int timeBetweenBackups = 12;
	public int timeBetweenBackupPruning = 96;
	public int timeBetweenSnapshotPruning = 12;
	public double lastSnapshot = 0;
	public double lastBackup = 0;
	public double lastBackupPrune = 0;
	public double lastSnapshotPrune = 0;

	public boolean allowObstructions = true;
	public int elevatorCooldown = 6;

	public String JDBCConnectionUrl = "";
}
