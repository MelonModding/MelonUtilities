package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Config;
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

		Config config = new Config();

		config.enableContainerLocking = mainConfig.get("enableContainerLocking").getAsBoolean();
		config.enableRoles = mainConfig.get("enableRoles").getAsBoolean();
		config.enableRollback = mainConfig.get("enableRollback").getAsBoolean();
		config.enableTPA = mainConfig.get("enableTPA").getAsBoolean();
		config.enableWarps = mainConfig.get("enableWarps").getAsBoolean();
		config.enableHomes = mainConfig.get("enableHomes").getAsBoolean();
		config.enableElevators = mainConfig.get("enableElevators").getAsBoolean();
		config.enableKits = mainConfig.get("enableKits").getAsBoolean();
		config.enableRules = mainConfig.get("enableRules").getAsBoolean();
		config.enableSmite = mainConfig.get("enableSmite").getAsBoolean();
		config.enableCrews = mainConfig.get("enableCrews").getAsBoolean();
		config.enableSQLPlayerLogging = mainConfig.get("enableSQLPlayerLogging").getAsBoolean();
		config.enableTXTPlayerLogging = mainConfig.get("enableTXTPlayerLogging").getAsBoolean();

		if(roleConfig.has("defaultRole")){
			config.defaultRole = roleConfig.get("defaultRole").getAsString();
		}
		config.displayMode = roleConfig.get("displayMode").getAsString();

		config.snapshotsEnabled = rollbackConfig.get("snapshotsEnabled").getAsBoolean();
		config.backupsEnabled = rollbackConfig.get("backupsEnabled").getAsBoolean();
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


		mainConfig.addProperty("enableContainerLocking", src.enableContainerLocking);
		mainConfig.addProperty("enableRoles", src.enableRoles);
		mainConfig.addProperty("enableRollback", src.enableRollback);
		mainConfig.addProperty("enableTPA", src.enableTPA);
		mainConfig.addProperty("enableWarps", src.enableWarps);
		mainConfig.addProperty("enableHomes", src.enableHomes);
		mainConfig.addProperty("enableElevators", src.enableElevators);
		mainConfig.addProperty("enableKits", src.enableKits);
		mainConfig.addProperty("enableRules", src.enableRules);
		mainConfig.addProperty("enableSmite", src.enableSmite);
		mainConfig.addProperty("enableCrews", src.enableCrews);
		mainConfig.addProperty("enableSQLPlayerLogging", src.enableSQLPlayerLogging);
		mainConfig.addProperty("enableTXTPlayerLogging", src.enableTXTPlayerLogging);
		obj.add("Main Config", mainConfig);

		roleConfig.addProperty("defaultRole", src.defaultRole);
		roleConfig.addProperty("displayMode", src.displayMode);
		obj.add("Role Config", roleConfig);

		rollbackConfig.addProperty("snapshotsEnabled", src.snapshotsEnabled);
		rollbackConfig.addProperty("timeBetweenSnapshots", src.timeBetweenSnapshots);
		rollbackConfig.addProperty("lastSnapshot", src.lastSnapshot);
		rollbackConfig.addProperty("backupsEnabled", src.backupsEnabled);
		rollbackConfig.addProperty("timeBetweenBackups", src.timeBetweenBackups);
		rollbackConfig.addProperty("lastBackup", src.lastBackup);
		rollbackConfig.addProperty("timeBetweenBackupPruning", src.timeBetweenBackupPruning);
		rollbackConfig.addProperty("lastBackupPrune", src.lastBackupPrune);
		rollbackConfig.addProperty("timeBetweenSnapshotPruning", src.timeBetweenSnapshotPruning);
		rollbackConfig.addProperty("lastSnapshotPrune", src.lastSnapshotPrune);
		obj.add("Rollback Config", rollbackConfig);

		elevatorConfig.addProperty("allowObstructions", src.allowObstructions);
		elevatorConfig.addProperty("elevatorCooldown", src.elevatorCooldown);
		obj.add("Elevator Config", elevatorConfig);

		sqlLogConfig.addProperty("JDBCConnectionUrl", src.JDBCConnectionUrl);
		obj.add("SQL Log Config", sqlLogConfig);

		return obj;
	}
}
