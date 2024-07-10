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

	//[fileHashMap] is a HashMap that stores the Files (File Object) in your DataBank (you shouldn't need to use this too much.)
	//[dataHashMap] is a HashMap that stores the Data (ex: KitData, RoleData) in your DataBank (9/10 use this to grab data from a file)
	// ^ Both are given the same id that you set when creating, and can be accessed using that id ^

	//Using the getOrCreateData method on an unused/empty id will create a file with that id
	//Hence there being no createData method

	String filePath = FabricLoader.getInstance().getConfigDir() + "/" + BTAServerUtilities.MOD_ID + "/";
	Data dataType;
	public final HashMap<String, File> fileHashMap = new HashMap<>();
	public final HashMap<String, Data> dataHashMap = new HashMap<>();

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
		if (fileHashMap.get(id) != null) {
			return;
		}
		fileHashMap.put(id, new File(Paths.get(filePath).toFile(), id + ".json"));
	}

	private void load(String id, Class<Data> dataClass) {
		prepareFile(id);

		try {
			if (!fileHashMap.get(id).exists()) {
				save(id);
			}
			if (fileHashMap.get(id).exists()) {
				BufferedReader br = new BufferedReader(new FileReader(fileHashMap.get(id)));
				dataHashMap.put(id, BTAServerUtilities.GSON.fromJson(br, dataClass));
				save(id);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load [" + id + "]'s data file; reverting to defaults");
			e.printStackTrace();
		}
	}

	public void loadAll(Class<Data> dataClass){
		try {
			Set<String> files = listFilesUsingFilesList(filePath);
			dataHashMap.clear();
			for (String file : files){
				load(file.replace(".json", ""), dataClass);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void save(String id) {
		RecipeBuilder.isExporting = true;
		prepareFile(id);

		String jsonString = BTAServerUtilities.GSON.toJson(dataHashMap.get(id));

		try (FileWriter fileWriter = new FileWriter(fileHashMap.get(id))) {
			fileWriter.write(jsonString);

		} catch (IOException e) {
			System.err.println("Couldn't save [" + id + "]'s data file");
			e.printStackTrace();
		}
		RecipeBuilder.isExporting = false;
	}

	public void saveAll(){
		for (String id: dataHashMap.keySet()) {
			save(id);
			BTAServerUtilities.updateAll();
		}
	}

	public Data getOrCreate(String id, Class<Data> dataClass) {
		if (dataHashMap.get(id) == null){
			{
				dataHashMap.put(id, dataType);
				load(id, dataClass);

				return dataHashMap.get(id);
			}
		}
		return dataHashMap.get(id);
	}

	public int remove(String id){
		int error = 2;
		if (!fileHashMap.containsKey(id)) {
			error = 1;
			return error;
		}
		if(fileHashMap.get(id).delete()){
			error = 0;
		}
		fileHashMap.remove(id);
		dataHashMap.remove(id);
		return error;
	}
}
