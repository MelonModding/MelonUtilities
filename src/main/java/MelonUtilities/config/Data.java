package MelonUtilities.config;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.config.datatypes.data.Kit;
import MelonUtilities.config.datatypes.data.User;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.config.datatypes.jsonadapters.ConfigJsonAdapter;
import MelonUtilities.config.datatypes.jsonadapters.KitJsonAdapter;
import MelonUtilities.config.datatypes.jsonadapters.RoleJsonAdapter;
import MelonUtilities.config.datatypes.jsonadapters.UserJsonAdapter;
import com.b100.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.data.registry.recipe.adapter.ItemStackJsonAdapter;
import net.minecraft.core.item.ItemStack;
import net.minecraft.server.util.helper.PlayerList;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Data {

	public static Gson gson = new GsonBuilder()
		.registerTypeAdapter(Role.class, new RoleJsonAdapter())
		.registerTypeAdapter(Config.class, new ConfigJsonAdapter())
		.registerTypeAdapter(Kit.class, new KitJsonAdapter())
		.registerTypeAdapter(ItemStack.class, new ItemStackJsonAdapter())
		.registerTypeAdapter(User.class, new UserJsonAdapter())
		.setPrettyPrinting().create();

	public static class Roles {

		public static final Map<String, Role> roleDataHashMap = new HashMap<>();
		private static final String roleDir = FabricLoader.getInstance().getConfigDir() + "/" + MelonUtilities.MOD_ID + "/roles";

		public static Role create(String roleID){
			Role role = new Role(roleID);
			roleDataHashMap.put(roleID, role);
			save(roleID);
			return role;
		}
		public static void delete(String roleID){
			roleDataHashMap.remove(roleID);
			if(!new File(roleDir, roleID + ".json").delete()){
				MelonUtilities.LOGGER.error("(IO ERROR) Could not delete file for role {}", roleID);
			}
		}
		public static void reload(){
			roleDataHashMap.clear();
			File dir = new File(roleDir);
			dir.mkdirs();
			File[] files = dir.listFiles();
			if (files != null) {
				for (File child : files) {
					try {
						RoleJsonAdapter.fileName = child.getName().replace(".json", "");
						Role role = gson.fromJson(new JsonReader(new FileReader(child)), Role.class);
						roleDataHashMap.put(role.roleID, role);
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
		public static void saveAll(){
			for(Role role : roleDataHashMap.values()){
				role.save();
			}
		}
		private static void load(String roleID){
			roleDataHashMap.remove(roleID);
			File child = new File(roleDir, roleID + ".json");
			try {
				RoleJsonAdapter.fileName = child.getName().replace(".json", "");
				Role role = gson.fromJson(new JsonReader(new FileReader(child)), Role.class);
				roleDataHashMap.put(role.roleID, role);
			} catch (FileNotFoundException e) {
				MelonUtilities.LOGGER.error("Could not reload role {}!", child);
			}
		}
		public static Role get(String roleID){
			return roleDataHashMap.get(roleID);
		}

		public static Role getOrCreate(String roleID) {
			if(!roleDataHashMap.containsKey(roleID)){
				create(roleID);
			}
			return get(roleID);
		}

	}
	public static class Kits {

		public static final Map<String, Kit> kitDataHashMap = new HashMap<>();
		private static final String kitDir = FabricLoader.getInstance().getConfigDir() + "/" + MelonUtilities.MOD_ID + "/kits";

		public static Kit create(String kitID){
			Kit kit = new Kit(kitID);
			kitDataHashMap.put(kitID, kit);
			save(kitID);
			return kit;
		}
		public static void delete(String kitID){
			kitDataHashMap.remove(kitID);
			if(!new File(kitDir, kitID + ".json").delete()){
				MelonUtilities.LOGGER.error("(IO ERROR) Could not delete file for kit {}", kitID);
			}
		}
		public static void reload(){
			kitDataHashMap.clear();
			File dir = new File(kitDir);
			dir.mkdirs();
			File[] files = dir.listFiles();
			if (files != null) {
				for (File child : files) {
					try {
						Kit kit = gson.fromJson(new JsonReader(new FileReader(child)), Kit.class);
						kitDataHashMap.put(kit.kitID, kit);
					} catch (FileNotFoundException e) {
						MelonUtilities.LOGGER.error("Could not find file {} while trying to reload kits!", child);
						continue;
					}
				}
			} else {
				MelonUtilities.LOGGER.error("Kit config dir does not exist!");
			}
		}
		public static void save(String kitID){
			File file = FileUtils.createNewFile(new File(kitDir, kitID + ".json"));
			try (FileWriter writer = new FileWriter(file)) {
				gson.toJson(get(kitID), Kit.class, writer);
				PlayerList.updateList();
			} catch (IOException e) {
				MelonUtilities.LOGGER.error("Kit {} failed to save!", kitID);
			}
		}
		private static void load(String kitID){
			kitDataHashMap.remove(kitID);
			File child = new File(kitDir, kitID + ".json");
			try {
				Kit kit = gson.fromJson(new JsonReader(new FileReader(child)), Kit.class);
				kitDataHashMap.put(kit.kitID, kit);
			} catch (FileNotFoundException e) {
				MelonUtilities.LOGGER.error("Could not reload kit {}!", child);
			}
		}
		public static Kit get(String kitID){
			return kitDataHashMap.get(kitID);
		}

		public static Kit getOrCreate(String kitID) {
			if(!kitDataHashMap.containsKey(kitID)){
				create(kitID);
			}
			return get(kitID);
		}

	}
	public static class MainConfig {
		public static Config config = new Config();
		private static final String configDir = FabricLoader.getInstance().getConfigDir() + "/" + MelonUtilities.MOD_ID;

		public static void save(){
			File file = FileUtils.createNewFile(new File(configDir, "config.json"));
			try (FileWriter writer = new FileWriter(file)) {
				gson.toJson(config, Config.class, writer);
				PlayerList.updateList();
			} catch (IOException e) {
				MelonUtilities.LOGGER.error("Config failed to save!");
			}
		}
		public static void reload(){
			File configFile = new File(configDir, "config.json");
			try {
				config = gson.fromJson(new JsonReader(new FileReader(configFile)), Config.class);
			} catch (FileNotFoundException e) {
				MelonUtilities.LOGGER.error("Could not reload Config!");
			}
		}
	}
	public static class Users {

		public static final Map<UUID, User> userDataHashMap = new HashMap<>();
		private static final String userDir = FabricLoader.getInstance().getConfigDir() + "/" + MelonUtilities.MOD_ID + "/users";

		public static User create(UUID uuid){
			User user = new User(uuid);
			userDataHashMap.put(uuid, user);
			save(uuid);
			return user;
		}
		public static void delete(UUID uuid){
			userDataHashMap.remove(uuid);
			if(!new File(userDir, uuid + ".json").delete()){
				MelonUtilities.LOGGER.error("Could not delete file for User {}", uuid);
			}
		}
		public static void reload(){
			userDataHashMap.clear();
			File dir = new File(userDir);
			dir.mkdirs();
			File[] files = dir.listFiles();
			if (files != null) {
				for (File child : files) {
					try {
						User user = gson.fromJson(new JsonReader(new FileReader(child)), User.class);
						userDataHashMap.put(user.uuid, user);
					} catch (FileNotFoundException e) {
						MelonUtilities.LOGGER.error("Could not find file {} while trying to reload Users!", child);
						continue;
					}
				}
			} else {
				MelonUtilities.LOGGER.error("User config dir does not exist!");
			}
		}
		public static void save(UUID uuid){
			File file = FileUtils.createNewFile(new File(userDir, uuid + ".json"));
			try (FileWriter writer = new FileWriter(file)) {
				gson.toJson(getOrCreate(uuid), User.class, writer);
				PlayerList.updateList();
			} catch (IOException e) {
				MelonUtilities.LOGGER.error("User {} failed to save!", uuid);
			}
		}
		private static void load(UUID uuid){
			userDataHashMap.remove(uuid);
			File child = new File(userDir, uuid + ".json");
			try {
				User user = gson.fromJson(new JsonReader(new FileReader(child)), User.class);
				userDataHashMap.put(user.uuid, user);
			} catch (FileNotFoundException e) {
				MelonUtilities.LOGGER.error("Could not reload User {}!", child);
			}
		}
		public static User getOrCreate(UUID uuid){
			if(!userDataHashMap.containsKey(uuid)){
				create(uuid);
			}
			return get(uuid);

		}

		public static User get(UUID uuid) {
			return userDataHashMap.get(uuid);
		}
	}
}
