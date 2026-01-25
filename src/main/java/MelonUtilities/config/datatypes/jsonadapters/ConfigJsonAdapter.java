package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.config.datatypes.data.Spawn;
import MelonUtilities.config.datatypes.data.Warp;
import MelonUtilities.config.datatypes.legacydeserializers.ConfigLDs;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfigJsonAdapter implements JsonDeserializer<Config>, JsonSerializer<Config> {
	@Override
	public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();

		//New Config
		Config config = new Config();
		config.configVersion = obj.has("configVersion") ? obj.get("configVersion").getAsInt() : 0;

		//Legacy Check
		if(config.configVersion < MelonUtilities.mainConfigVersion){
			return legacyDeserialize(context, obj, config.configVersion);
		}

		//Extra Categories
		JsonObject mainConfig = obj.has("Main Config") ? obj.getAsJsonObject("Main Config") : null;
		JsonObject roleConfig = obj.has("Role Config") ? obj.getAsJsonObject("Role Config") : null;
		JsonObject rollbackConfig = obj.has("Rollback Config") ? obj.getAsJsonObject("Rollback Config") : null;
		JsonObject elevatorConfig = obj.has("Elevator Config") ? obj.getAsJsonObject("Elevator Config") : null;
		JsonObject sqlLogConfig = obj.has("SQL Log Config") ? obj.getAsJsonObject("SQL Log Config") : null;
		JsonObject warpConfig = obj.has("Warp Data") ? obj.getAsJsonObject("Warp Data") : null;
		JsonObject spawnConfig = obj.has("Spawn Data") ? obj.getAsJsonObject("Spawn Data") : null;
		JsonObject discordIntegrationConfig = obj.has("Discord Integration Config") ? obj.getAsJsonObject("Discord Integration Config") : null;


		//Main Options
		if(mainConfig != null){
			config.enableContainerLocking = mainConfig.has("enableContainerLocking") ? mainConfig.get("enableContainerLocking").getAsBoolean() : config.enableContainerLocking;
			config.enableRoles = mainConfig.has("enableRoles") ? mainConfig.get("enableRoles").getAsBoolean() : config.enableRoles;
			config.enableRollback = mainConfig.has("enableRollback") ? mainConfig.get("enableRollback").getAsBoolean() : config.enableRollback;
			config.enableTPA = mainConfig.has("enableTPA") ? mainConfig.get("enableTPA").getAsBoolean() : config.enableTPA;
			config.enableHomes = mainConfig.has("enableHomes") ? mainConfig.get("enableHomes").getAsBoolean() : config.enableHomes;
			config.enableWarps = mainConfig.has("enableWarps") ? mainConfig.get("enableWarps").getAsBoolean() : config.enableWarps;
			config.enableSpawn = mainConfig.has("enableSpawn") ? mainConfig.get("enableSpawn").getAsBoolean() : config.enableSpawn;
			config.enableElevators = mainConfig.has("enableElevators") ? mainConfig.get("enableElevators").getAsBoolean() : config.enableElevators;
			config.enableKits = mainConfig.has("enableKits") ? mainConfig.get("enableKits").getAsBoolean() : config.enableKits;
			config.enableRules = mainConfig.has("enableRules") ? mainConfig.get("enableRules").getAsBoolean() : config.enableRules;
			config.enableSmite = mainConfig.has("enableSmite") ? mainConfig.get("enableSmite").getAsBoolean() : config.enableSmite;
			config.enableCrews = mainConfig.has("enableCrews") ? mainConfig.get("enableCrews").getAsBoolean() : config.enableCrews;
			config.enableSQLPlayerLogging = mainConfig.has("enableSQLPlayerLogging") ? mainConfig.get("enableSQLPlayerLogging").getAsBoolean() : config.enableSQLPlayerLogging;
			config.enableTXTPlayerLogging = mainConfig.has("enableTXTPlayerLogging") ? mainConfig.get("enableTXTPlayerLogging").getAsBoolean() : config.enableTXTPlayerLogging;
			config.enableDiscordIntegration = mainConfig.has("enableDiscordIntegration") ? mainConfig.get("enableDiscordIntegration").getAsBoolean() : config.enableDiscordIntegration;
			config.enableMagnets = mainConfig.has("enableMagnets") ? mainConfig.get("enableMagnets").getAsBoolean() : config.enableMagnets;
		}

		//Role Options
		if(roleConfig != null){
			if(roleConfig.has("defaultRole")){
				config.defaultRole = roleConfig.get("defaultRole").getAsString();
			}
			config.displayMode = roleConfig.has("displayMode") ? roleConfig.get("displayMode").getAsString() : config.displayMode;
		}

		//Rollback Options
		if(rollbackConfig != null){
			config.snapshotsEnabled = rollbackConfig.has("snapshotsEnabled") ? rollbackConfig.get("snapshotsEnabled").getAsBoolean() : config.snapshotsEnabled;
			config.backupsEnabled = rollbackConfig.has("backupsEnabled") ? rollbackConfig.get("backupsEnabled").getAsBoolean() : config.backupsEnabled;
			config.snapshotsImmune = rollbackConfig.has("snapshotsImmune") ? rollbackConfig.get("snapshotsImmune").getAsInt() : config.snapshotsImmune;
			config.backupsImmune = rollbackConfig.has("backupsImmune") ? rollbackConfig.get("backupsImmune").getAsInt() : config.backupsImmune;
			config.snapshotsLimit = rollbackConfig.has("snapshotsLimit") ? rollbackConfig.get("snapshotsLimit").getAsInt() : config.snapshotsLimit;
			config.backupsLimit = rollbackConfig.has("backupsLimit") ? rollbackConfig.get("backupsLimit").getAsInt() : config.backupsLimit;
			config.sizeLimit = rollbackConfig.has("sizeLimit") ? rollbackConfig.get("sizeLimit").getAsString() : config.sizeLimit;
			config.timeBetweenSnapshots = rollbackConfig.has("timeBetweenSnapshots") ? rollbackConfig.get("timeBetweenSnapshots").getAsInt() : config.timeBetweenSnapshots;
			config.timeBetweenBackups = rollbackConfig.has("timeBetweenBackups") ? rollbackConfig.get("timeBetweenBackups").getAsInt() : config.timeBetweenBackups;
			config.timeBetweenBackupPruning = rollbackConfig.has("timeBetweenBackupPruning") ? rollbackConfig.get("timeBetweenBackupPruning").getAsInt() : config.timeBetweenBackupPruning;
			config.timeBetweenSnapshotPruning = rollbackConfig.has("timeBetweenSnapshotPruning") ? rollbackConfig.get("timeBetweenSnapshotPruning").getAsInt() : config.timeBetweenSnapshotPruning;
			config.lastSnapshot = rollbackConfig.has("lastSnapshot") ? rollbackConfig.get("lastSnapshot").getAsDouble() : config.lastSnapshot;
			config.lastBackup = rollbackConfig.has("lastBackup") ? rollbackConfig.get("lastBackup").getAsDouble() : config.lastBackup;
			config.lastBackupPrune = rollbackConfig.has("lastBackupPrune") ? rollbackConfig.get("lastBackupPrune").getAsDouble() : config.lastBackupPrune;
			config.lastSnapshotPrune = rollbackConfig.has("lastSnapshotPrune") ? rollbackConfig.get("lastSnapshotPrune").getAsDouble() : config.lastSnapshotPrune;
		}

		//Elevator Options
		if(elevatorConfig != null){
			config.allowObstructions = elevatorConfig.has("allowObstructions") ? elevatorConfig.get("allowObstructions").getAsBoolean() : config.allowObstructions;
			config.elevatorCooldown = elevatorConfig.has("elevatorCooldown") ? elevatorConfig.get("elevatorCooldown").getAsInt() : config.elevatorCooldown;
		}

		//SQL Options
		if(sqlLogConfig != null){
			config.JDBCConnectionUrl = sqlLogConfig.has("JDBCConnectionUrl") ? sqlLogConfig.get("JDBCConnectionUrl").getAsString() : config.JDBCConnectionUrl;
		}

		//Discord Options
		if(discordIntegrationConfig != null){
			config.token = discordIntegrationConfig.has("token") ? discordIntegrationConfig.get("token").getAsString() : config.token;
			config.channelID = discordIntegrationConfig.has("channelID") ? discordIntegrationConfig.get("channelID").getAsString() : config.channelID;
			config.serverPFPURL = discordIntegrationConfig.has("serverPFPURL") ? discordIntegrationConfig.get("serverPFPURL").getAsString() : config.serverPFPURL;
			config.serverName = discordIntegrationConfig.has("serverName") ? discordIntegrationConfig.get("serverName").getAsString() : config.serverName;
		}

		//Warp Options
		if(warpConfig != null){
			JsonArray warps = warpConfig.getAsJsonArray("warps");
			for(JsonElement element : warps){
				config.warpData.add(context.deserialize(element, Warp.class));
			}
		}

		//Spawn Options
		if(spawnConfig != null){
			config.spawnData = context.deserialize(spawnConfig.getAsJsonObject("spawn"), Spawn.class);
		}

		return config;
	}

	@Override
	public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();

		obj.addProperty("configVersion", MelonUtilities.mainConfigVersion);

		JsonObject mainConfig = new JsonObject();
		JsonObject roleConfig = new JsonObject();
		JsonObject rollbackConfig = new JsonObject();
		JsonObject elevatorConfig = new JsonObject();
		JsonObject sqlLogConfig = new JsonObject();
		JsonObject warpConfig = new JsonObject();
		JsonObject spawnConfig = new JsonObject();
		JsonObject discordIntegrationConfig = new JsonObject();

		mainConfig.addProperty("enableContainerLocking", src.enableContainerLocking);
		mainConfig.addProperty("enableRoles", src.enableRoles);
		mainConfig.addProperty("enableRollback", src.enableRollback);
		mainConfig.addProperty("enableTPA", src.enableTPA);
		mainConfig.addProperty("enableHomes", src.enableHomes);
		mainConfig.addProperty("enableWarps", src.enableWarps);
		mainConfig.addProperty("enableSpawn", src.enableSpawn);
		mainConfig.addProperty("enableElevators", src.enableElevators);
		mainConfig.addProperty("enableKits", src.enableKits);
		mainConfig.addProperty("enableRules", src.enableRules);
		mainConfig.addProperty("enableSmite", src.enableSmite);
		mainConfig.addProperty("enableCrews", src.enableCrews);
		mainConfig.addProperty("enableSQLPlayerLogging", src.enableSQLPlayerLogging);
		mainConfig.addProperty("enableTXTPlayerLogging", src.enableTXTPlayerLogging);
		mainConfig.addProperty("enableDiscordIntegration", src.enableDiscordIntegration);
		mainConfig.addProperty("enableMagnets", src.enableMagnets);
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
		obj.add("Warp Data", warpConfig);

		spawnConfig.add("spawn", context.serialize(src.spawnData));
		obj.add("Spawn Data", spawnConfig);

		return obj;
	}

	private Config legacyDeserialize(JsonDeserializationContext context, JsonObject obj, int configVersion){
		switch(configVersion){
			case 0:
				return ConfigLDs.legacyDeserialize0(context, obj);
		}
		throw new IllegalArgumentException("(MainConfig) legacy deserialize failed: no legacy deserializer present for current version!");
	}
}
