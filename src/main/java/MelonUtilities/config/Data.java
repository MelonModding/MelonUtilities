package MelonUtilities.config;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.custom.classes.Role;
import MelonUtilities.config.custom.jsonadapters.RoleJsonAdapter;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import com.b100.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.util.helper.PlayerList;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Data {

	public static Gson gson = new GsonBuilder().registerTypeAdapter(Role.class, new RoleJsonAdapter()).setPrettyPrinting().create();

	public static class Roles {

		public static final Map<String, Role> roleHashMap = new HashMap<>();
		private static final String roleDir = FabricLoader.getInstance().getConfigDir() + "/" + MelonUtilities.MOD_ID + "/roles";

		public static Role create(String roleID){
			Role role = new Role(roleID);
			roleHashMap.put(roleID, role);
			save(roleID);
			return role;
		}
		public static void delete(String roleID){
			roleHashMap.remove(roleID);
			if(!new File(roleDir, roleID + ".json").delete()){
				MelonUtilities.LOGGER.error("(IO ERROR) Could not delete file for role {}", roleID);
			}
		}
		public static void reload(){
			roleHashMap.clear();
			File dir = new File(roleDir);
			dir.mkdirs();
			File[] files = dir.listFiles();
			if (files != null) {
				for (File child : files) {
					try {
						Role role = gson.fromJson(new JsonReader(new FileReader(child)), Role.class);
						roleHashMap.put(role.roleID, role);
					} catch (FileNotFoundException e) {
						MelonUtilities.LOGGER.error("Could not find file {} while trying to reload roles!", child);
						continue;
					}
				}
			} else {
				MelonUtilities.LOGGER.error("Role config dir does not exist!");
			}
		}
		public static void save(String roleID){
			File file = FileUtils.createNewFile(new File(roleDir, roleID + ".json"));
			try (FileWriter writer = new FileWriter(file)) {
				gson.toJson(get(roleID), Role.class, writer);
				PlayerList.updateList();
			} catch (IOException e) {
				MelonUtilities.LOGGER.error("Role {} failed to save!", roleID);
			}
		}
		private static void load(String roleID){
			roleHashMap.remove(roleID);
			File child = new File(roleDir, roleID + ".json");
			try {
				Role role = gson.fromJson(new JsonReader(new FileReader(child)), Role.class);
				roleHashMap.put(role.roleID, role);
			} catch (FileNotFoundException e) {
				MelonUtilities.LOGGER.error("Could not reload role {}!", child);
			}
		}
		public static Role get(String roleID){
			return roleHashMap.get(roleID);
		}

		public static Role getOrCreate(String roleID) {
			if(!roleHashMap.containsKey(roleID)){
				create(roleID);
			}
			return get(roleID);
		}

	}
	public static class Kits {

	}
	public static class Configs {

	}
	public static class PlayerDatas {

	}


	/*public static DataBank<RoleData> roles = new DataBank<>("roles", new RoleData());*/
	public static DataBank<KitData> kits = new DataBank<>("kits", new KitData());
	public static DataBank<ConfigData> configs = new DataBank<>("configs", new ConfigData());
	public static DataBank<PlayerData> playerData = new DataBank<>("playerData", new PlayerData());
}
