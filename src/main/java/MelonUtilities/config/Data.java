package MelonUtilities.config;

import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.config.datatypes.RoleData;

public class Data {
	public static DataBank<RoleData> roles = new DataBank<>("roles", new RoleData());
	public static DataBank<KitData> kits = new DataBank<>("kits", new KitData());
	public static DataBank<ConfigData> configs = new DataBank<>("configs", new ConfigData());
	public static DataBank<PlayerData> playerData = new DataBank<>("playerData", new PlayerData());
}
