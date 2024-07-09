package BTAServerSolutions.BTAServerUtilities.config;


import BTAServerSolutions.BTAServerUtilities.BTAServerUtilities;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.server.util.helper.PlayerList.updateList;

public class DataBank<Data> {

	//Creates a Directory specifically for a dataType
	//Allows you to manage that directory with load, save, remove, and get methods

	//configFiles stores the Files (File Object) in your DataBank (you shouldn't need to use this too much.)
	//configData stores the Data (ex: KitData, RoleData) in your DataBank (9/10 use this to grab data from a file)
	// ^ Both are given the same id that you set when creating, and can be accessed using that id ^

	//Using the getOrCreateData method on an unused/empty id will create a file with that id
	//Hence there being no createData method

	String filePath = FabricLoader.getInstance().getConfigDir() + "/" + BTAServerUtilities.MOD_ID + "/";
	Data dataType;
	public final HashMap<String, File> configFiles = new HashMap<>();
	public final HashMap<String, Data> configData = new HashMap<>();


	public DataBank(String configDirName, Data dataType){
		this.filePath = this.filePath + configDirName;
		this.dataType = dataType;

		new File("./config/"  + BTAServerUtilities.MOD_ID + "/" + configDirName).mkdirs();
	}

	private static Set<String> listFilesUsingFilesList(String dir) throws IOException {
		try (Stream<Path> stream = Files.list(Paths.get(dir))) {
			return stream
				.filter(file -> !Files.isDirectory(file))
				.map(Path::getFileName)
				.map(Path::toString)
				.collect(Collectors.toSet());
		}
	}

	private void prepareFile(String id) {
		if (configFiles.get(id) != null) {
			return;
		}
		configFiles.put(id, new File(Paths.get(filePath).toFile(), id + ".json"));
	}

	private void loadData(String id, Class<Data> clazz) {
		prepareFile(id);

		try {
			if (!configFiles.get(id).exists()) {
				saveData(id);
			}
			if (configFiles.get(id).exists()) {
				BufferedReader br = new BufferedReader(new FileReader(configFiles.get(id)));
				configData.put(id, BTAServerUtilities.GSON.fromJson(br, clazz));
				saveData(id);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load Kit: [" + id + "]'s configuration file; reverting to defaults");
			e.printStackTrace();
		}
	}

	public void loadAllData(Class<Data> dataClass){
		try {
			Set<String> files = listFilesUsingFilesList(filePath);
			configData.clear();
			for (String file : files){
				loadData(file.replace(".json", ""), dataClass);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void saveData(String id) {
		RecipeBuilder.isExporting = true;
		prepareFile(id);

		String jsonString = BTAServerUtilities.GSON.toJson(configData.get(id));

		try (FileWriter fileWriter = new FileWriter(configFiles.get(id))) {
			fileWriter.write(jsonString);

		} catch (IOException e) {
			System.err.println("Couldn't save Kit: [" + id + "]'s configuration file");
			e.printStackTrace();
		}
		RecipeBuilder.isExporting = false;
	}

	public void saveAllData(){
		for (String id: configData.keySet()) {
			saveData(id);
			BTAServerUtilities.updateAll();
		}
	}

	public Data getOrCreateData(String id, Class<Data> dataClass) {
		if (configData.get(id) == null){
			{
				configData.put(id, dataType);
				loadData(id, dataClass);

				return configData.get(id);
			}
		}
		return configData.get(id);
	}

	public int removeConfig(String id){
		int error = 2;
		if (!configFiles.containsKey(id)) {
			error = 1;
			return error;
		}
		if(configFiles.get(id).delete()){
			error = 0;
		}
		configFiles.remove(id);
		configData.remove(id);
		return error;
	}
}
