package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Config;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfigJsonAdapter implements JsonDeserializer<Config>, JsonSerializer<Config> {
	@Override
	public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		JsonObject roleConfig = obj.getAsJsonObject("Role Config");
		JsonObject rollbackConfig = obj.getAsJsonObject("Rollback Config");

		Config config = new Config();

		if(roleConfig.has("defaultRole")){
			config.defaultRole = roleConfig.get("defaultRole").getAsString();
		}
		config.displayMode = roleConfig.get("displayMode").getAsString();

		config.snapshotsEnabled = rollbackConfig.get("snapshotsEnabled").getAsBoolean();
		config.timeBetweenSnapshots = rollbackConfig.get("timeBetweenSnapshots").getAsInt();
		config.lastSnapshot = rollbackConfig.get("lastSnapshot").getAsDouble();
		config.backupsEnabled = rollbackConfig.get("backupsEnabled").getAsBoolean();
		config.timeBetweenBackups = rollbackConfig.get("timeBetweenBackups").getAsInt();
		config.lastBackup = rollbackConfig.get("lastBackup").getAsDouble();
		config.timeBetweenBackupPruning = rollbackConfig.get("timeBetweenBackupPruning").getAsInt();
		config.lastBackupPrune = rollbackConfig.get("lastBackupPrune").getAsDouble();
		config.timeBetweenSnapshotPruning = rollbackConfig.get("timeBetweenSnapshotPruning").getAsInt();
		config.lastSnapshotPrune = rollbackConfig.get("lastSnapshotPrune").getAsDouble();

		return config;
	}

	@Override
	public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		JsonObject roleConfig = new JsonObject();
		JsonObject rollbackConfig = new JsonObject();

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

		return obj;
	}
}
