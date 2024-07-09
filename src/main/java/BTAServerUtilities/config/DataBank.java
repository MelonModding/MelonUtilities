package BTAServerUtilities.config;


import BTAServerUtilities.BTAServerUtilities;
import net.fabricmc.loader.api.FabricLoader;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataBank<Data> {

	//Creates a Directory of json files specifically for a dataType
	//Allows you to manage that directory with load, save, remove, and get methods

	//files is a HashMap that stores the Files (File Object) in your DataBank (you shouldn't need to use this too much.)
	//data is a HashMap that stores the Data (ex: KitData, RoleData) in your DataBank (9/10 use this to grab data from a file)
	// ^ Both are given the same id that you set when creating, and can be accessed using that id ^

	//Using the getOrCreateData method on an unused/empty id will create a file with that id
	//Hence there being no createData method

	String filePath = FabricLoader.getInstance().getConfigDir() + "/" + BTAServerUtilities.MOD_ID + "/";
	Data dataType;
	public final HashMap<String, File> files = new HashMap<>();
	public final HashMap<String, Data> data = new HashMap<>();

	public DataBank(String dirName, Data dataType){
		this.filePath = this.filePath + dirName;
		this.dataType = dataType;
		new File("./config/"  + BTAServerUtilities.MOD_ID + "/" + dirName).mkdirs();
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
		if (files.get(id) != null) {
			return;
		}
		files.put(id, new File(Paths.get(filePath).toFile(), id + ".json"));
	}

	private void loadData(String id, Class<Data> clazz) {
		prepareFile(id);

		try {
			if (!files.get(id).exists()) {
				saveData(id);
			}
			if (files.get(id).exists()) {
				BufferedReader br = new BufferedReader(new FileReader(files.get(id)));
				data.put(id, BTAServerUtilities.GSON.fromJson(br, clazz));
				saveData(id);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load [" + id + "]'s data file; reverting to defaults");
			e.printStackTrace();
		}
	}

	public void loadAllData(Class<Data> dataClass){
		try {
			Set<String> files = listFilesUsingFilesList(filePath);
			data.clear();
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

		String jsonString = BTAServerUtilities.GSON.toJson(data.get(id));

		try (FileWriter fileWriter = new FileWriter(files.get(id))) {
			fileWriter.write(jsonString);

		} catch (IOException e) {
			System.err.println("Couldn't save [" + id + "]'s data file");
			e.printStackTrace();
		}
		RecipeBuilder.isExporting = false;
	}

	public void saveAllData(){
		for (String id: data.keySet()) {
			saveData(id);
			BTAServerUtilities.updateAll();
		}
	}

	public Data getOrCreateData(String id, Class<Data> dataClass) {
		if (data.get(id) == null){
			{
				data.put(id, dataType);
				loadData(id, dataClass);

				return data.get(id);
			}
		}
		return data.get(id);
	}

	public int removeConfig(String id){
		int error = 2;
		if (!files.containsKey(id)) {
			error = 1;
			return error;
		}
		if(files.get(id).delete()){
			error = 0;
		}
		files.remove(id);
		data.remove(id);
		return error;
	}
}
