package MelonUtilities.config.datatypes.data;

import java.util.ArrayList;
import java.util.List;

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
	public boolean enableDiscordIntegration = false;

	public String defaultRole = null;
	public String displayMode = "multi";

	public boolean snapshotsEnabled = true;
	public boolean backupsEnabled = true;
	public int snapshotsImmune = 8;
	public int backupsImmune = 4;
	public int snapshotsLimit = 16;
	public int backupsLimit = 8;
	public String sizeLimit = "50GB";
	public int timeBetweenSnapshots = 300; //Seconds
	public int timeBetweenBackups = 12; //Hours
	public int timeBetweenBackupPruning = 96; //Hours
	public int timeBetweenSnapshotPruning = 12; //Hours
	public double lastSnapshot = 0;
	public double lastBackup = 0;
	public double lastBackupPrune = 0;
	public double lastSnapshotPrune = 0;

	public boolean allowObstructions = true;
	public int elevatorCooldown = 6;

	public String JDBCConnectionUrl = "";

	public String token = "SUPER SECRET TOKEN";
	public String channelID = "CHANNEL ID";
	public String serverPFPURL = "https://i.imgur.com/dJUId0O.png";
	public String serverName = "BTA! Server";

	public List<Warp> warpData = new ArrayList<>();
}
