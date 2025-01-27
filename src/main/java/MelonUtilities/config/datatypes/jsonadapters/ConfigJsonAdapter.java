package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.config.datatypes.data.Warp;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfigJsonAdapter implements JsonDeserializer<Config>, JsonSerializer<Config> {
	@Override
	public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		JsonObject mainConfig = obj.getAsJsonObject("Main Config");
		JsonObject roleConfig = obj.getAsJsonObject("Role Config");
		JsonObject rollbackConfig = obj.getAsJsonObject("Rollback Config");
		JsonObject elevatorConfig = obj.getAsJsonObject("Elevator Config");
		JsonObject sqlLogConfig = obj.getAsJsonObject("SQL Log Config");
		JsonObject warpConfig = obj.getAsJsonObject("Warp Config");
		JsonObject discordIntegrationConfig = obj.getAsJsonObject("Discord Integration Config");

		Config config = new Config();

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

		if(roleConfig.has("defaultRole")){
			config.defaultRole = roleConfig.get("defaultRole").getAsString();
		}
		config.displayMode = roleConfig.get("displayMode").getAsString();

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

		config.allowObstructions = elevatorConfig.get("allowObstructions").getAsBoolean();
		config.elevatorCooldown = elevatorConfig.get("elevatorCooldown").getAsInt();

		config.JDBCConnectionUrl = sqlLogConfig.get("JDBCConnectionUrl").getAsString();

		config.token = discordIntegrationConfig.get("token").getAsString();
		config.channelID = discordIntegrationConfig.get("channelID").getAsString();
		config.serverPFPURL = discordIntegrationConfig.get("serverPFPURL").getAsString();
		config.serverName = discordIntegrationConfig.get("serverName").getAsString();

		JsonArray warps = warpConfig.getAsJsonArray("warps");
		for(JsonElement element : warps){
			config.warpData.add(context.deserialize(element, Warp.class));
		}

		return config;
	}

	@Override
	public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		JsonObject mainConfig = new JsonObject();
		JsonObject roleConfig = new JsonObject();
		JsonObject rollbackConfig = new JsonObject();
		JsonObject elevatorConfig = new JsonObject();
		JsonObject sqlLogConfig = new JsonObject();
		JsonObject warpConfig = new JsonObject();
		JsonObject discordIntegrationConfig = new JsonObject();

		mainConfig.addProperty("enableContainerLocking", src.enableContainerLocking);
		mainConfig.addProperty("enableRoles", src.enableRoles);
		mainConfig.addProperty("enableRollback", src.enableRollback);
		mainConfig.addProperty("enableTPA", src.enableTPA);
		mainConfig.addProperty("enableHomes", src.enableHomes);
		mainConfig.addProperty("enableWarps", src.enableWarps);
		mainConfig.addProperty("enableElevators", src.enableElevators);
		mainConfig.addProperty("enableKits", src.enableKits);
		mainConfig.addProperty("enableRules", src.enableRules);
		mainConfig.addProperty("enableSmite", src.enableSmite);
		mainConfig.addProperty("enableCrews", src.enableCrews);
		mainConfig.addProperty("enableSQLPlayerLogging", src.enableSQLPlayerLogging);
		mainConfig.addProperty("enableTXTPlayerLogging", src.enableTXTPlayerLogging);
		mainConfig.addProperty("enableDiscordIntegration", src.enableDiscordIntegration);
		obj.add("Main Config", mainConfig);

		roleConfig.addProperty("defaultRole", src.defaultRole);
		roleConfig.addProperty("displayMode", src.displayMode);
		obj.add("Role Config", roleConfig);

		rollbackConfig.addProperty("snapshotsEnabled", src.snapshotsEnabled);
		rollbackConfig.addProperty("backupsEnabled", src.backupsEnabled);
		rollbackConfig.addProperty("snapshotsImmune", src.snapshotsImmune);
		rollbackConfig.addProperty("backupsImmune", src.backupsImmune);
		rollbackConfig.addProperty("snapshotsLimit", src.snapshotsLimit);
		rollbackConfig.addProperty("backupsLimit", src.backupsLimit);
		rollbackConfig.addProperty("sizeLimit", src.sizeLimit);
		rollbackConfig.addProperty("timeBetweenSnapshots", src.timeBetweenSnapshots);
		rollbackConfig.addProperty("timeBetweenBackups", src.timeBetweenBackups);
		rollbackConfig.addProperty("timeBetweenBackupPruning", src.timeBetweenBackupPruning);
		rollbackConfig.addProperty("timeBetweenSnapshotPruning", src.timeBetweenSnapshotPruning);
		rollbackConfig.addProperty("lastSnapshot", src.lastSnapshot);
		rollbackConfig.addProperty("lastBackup", src.lastBackup);
		rollbackConfig.addProperty("lastBackupPrune", src.lastBackupPrune);
		rollbackConfig.addProperty("lastSnapshotPrune", src.lastSnapshotPrune);
		obj.add("Rollback Config", rollbackConfig);

		elevatorConfig.addProperty("allowObstructions", src.allowObstructions);
		elevatorConfig.addProperty("elevatorCooldown", src.elevatorCooldown);
		obj.add("Elevator Config", elevatorConfig);

		sqlLogConfig.addProperty("JDBCConnectionUrl", src.JDBCConnectionUrl);
		obj.add("SQL Log Config", sqlLogConfig);

		discordIntegrationConfig.addProperty("token", src.token);
		discordIntegrationConfig.addProperty("channelID", src.channelID);
		discordIntegrationConfig.addProperty("serverPFPURL", src.serverPFPURL);
		discordIntegrationConfig.addProperty("serverName", src.serverName);
		obj.add("Discord Integration Config", discordIntegrationConfig);

		JsonArray warps = new JsonArray();
		for(Warp warp : src.warpData){
			warps.add(context.serialize(warp));
		}
		warpConfig.add("warps", warps);
		obj.add("Warp Config", warpConfig);

		return obj;
	}
}
