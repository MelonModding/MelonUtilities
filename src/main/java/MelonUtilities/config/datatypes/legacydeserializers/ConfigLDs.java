package MelonUtilities.config.datatypes.legacydeserializers;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.config.datatypes.data.Warp;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigLDs {
	public static Config legacyDeserialize0(JsonDeserializationContext context, JsonObject obj){
		//New Config
		Config config = new Config();
		config.configVersion = MelonUtilities.mainConfigVersion;

		//Extra Categories
		JsonObject mainConfig = obj.getAsJsonObject("Main Config");
		JsonObject roleConfig = obj.getAsJsonObject("Role Config");
		JsonObject rollbackConfig = obj.getAsJsonObject("Rollback Config");
		JsonObject elevatorConfig = obj.getAsJsonObject("Elevator Config");
		JsonObject sqlLogConfig = obj.getAsJsonObject("SQL Log Config");
		JsonObject warpConfig = obj.getAsJsonObject("Warp Config");
		JsonObject discordIntegrationConfig = obj.getAsJsonObject("Discord Integration Config");


		//Main Options
		config.enableContainerLocking = mainConfig.get("enableContainerLocking").getAsBoolean();
		config.enableRoles = mainConfig.get("enableRoles").getAsBoolean();
		config.enableRollback = mainConfig.get("enableRollback").getAsBoolean();
		config.enableTPA = mainConfig.get("enableTPA").getAsBoolean();
		config.enableHomes = mainConfig.get("enableHomes").getAsBoolean();
		config.enableWarps = mainConfig.get("enableWarps").getAsBoolean();
		config.enableElevators = mainConfig.get("enableElevators").getAsBoolean();
		config.enableKits = mainConfig.get("enableKits").getAsBoolean();
		config.enableRules = mainConfig.get("enableRules").getAsBoolean();
		config.enableSmite = mainConfig.get("enableSmite").getAsBoolean();
		config.enableCrews = mainConfig.get("enableCrews").getAsBoolean();
		config.enableSQLPlayerLogging = mainConfig.get("enableSQLPlayerLogging").getAsBoolean();
		config.enableTXTPlayerLogging = mainConfig.get("enableTXTPlayerLogging").getAsBoolean();
		config.enableDiscordIntegration = mainConfig.get("enableDiscordIntegration").getAsBoolean();

		//Role Options
		if(roleConfig.has("defaultRole")){
			config.defaultRole = roleConfig.get("defaultRole").getAsString();
		}
		config.displayMode = roleConfig.get("displayMode").getAsString();

		//Rollback Options
		config.snapshotsEnabled = rollbackConfig.get("snapshotsEnabled").getAsBoolean();
		config.backupsEnabled = rollbackConfig.get("backupsEnabled").getAsBoolean();
		config.snapshotsImmune = rollbackConfig.get("snapshotsImmune").getAsInt();
		config.backupsImmune = rollbackConfig.get("backupsImmune").getAsInt();
		config.snapshotsLimit = rollbackConfig.get("snapshotsLimit").getAsInt();
		config.backupsLimit = rollbackConfig.get("backupsLimit").getAsInt();
		config.sizeLimit = rollbackConfig.get("sizeLimit").getAsString();
		config.timeBetweenSnapshots = rollbackConfig.get("timeBetweenSnapshots").getAsInt();
		config.timeBetweenBackups = rollbackConfig.get("timeBetweenBackups").getAsInt();
		config.timeBetweenBackupPruning = rollbackConfig.get("timeBetweenBackupPruning").getAsInt();
		config.timeBetweenSnapshotPruning = rollbackConfig.get("timeBetweenSnapshotPruning").getAsInt();
		config.lastSnapshot = rollbackConfig.get("lastSnapshot").getAsDouble();
		config.lastBackup = rollbackConfig.get("lastBackup").getAsDouble();
		config.lastBackupPrune = rollbackConfig.get("lastBackupPrune").getAsDouble();
		config.lastSnapshotPrune = rollbackConfig.get("lastSnapshotPrune").getAsDouble();

		//Elevator Options
		config.allowObstructions = elevatorConfig.get("allowObstructions").getAsBoolean();
		config.elevatorCooldown = elevatorConfig.get("elevatorCooldown").getAsInt();

		//SQL Options
		config.JDBCConnectionUrl = sqlLogConfig.get("JDBCConnectionUrl").getAsString();

		//Discord Options
		config.token = discordIntegrationConfig.get("token").getAsString();
		config.channelID = discordIntegrationConfig.get("channelID").getAsString();
		config.serverPFPURL = discordIntegrationConfig.get("serverPFPURL").getAsString();
		config.serverName = discordIntegrationConfig.get("serverName").getAsString();

		//Warp Options
		JsonArray warps = warpConfig.getAsJsonArray("warps");
		for(JsonElement element : warps){
			config.warpData.add(context.deserialize(element, Warp.class));
		}

		return config;
	}
}
