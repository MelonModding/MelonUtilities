package BTAServerUtilities.saver;

import BTAServerUtilities.BTAServerUtilities;
import BTAServerUtilities.mixins.interfaces.Initializable;
import BTAServerUtilities.mixins.interfaces.Saveable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// For some reason the name of the class can't be Saver idk why
public class SaverSingleton implements Initializable {
	private static final Path dataFolder = Paths.get("./BTAServerUtilities_data");

	private static SaverSingleton INSTANCE;
	private final List<Saveable> saveableList;

	private SaverSingleton() {
		saveableList = new ArrayList<>();
	}

	public static SaverSingleton getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SaverSingleton();
		}

		return INSTANCE;
	}

	public void add(Saveable saveable) {
		saveableList.add(saveable);
	}

	public Saveable get(int index) {
		return saveableList.get(index);
	}

	public void remove(int index) {
		saveableList.remove(index);
	}

	public void saveAll() {
		for (Saveable saveable : saveableList) {
			saveable.save();
		}
	}

	public void loadAll() {
		for (Saveable saveable : saveableList) {
			saveable.load();
		}
	}

	@Override
	public void initialize() {
		BTAServerUtilities.LOGGER.info("Initializing SaverSingleton");

		if (Files.notExists(dataFolder)) {
			BTAServerUtilities.LOGGER.info("Creating data folder");

			try {
				Files.createDirectory(dataFolder);
			} catch (IOException e) {
				BTAServerUtilities.LOGGER.error("Failed to create data folder");
				throw new RuntimeException(e);
			}
		}

		HomesSingleton.getInstance().initialize();
		add(HomesSingleton.getInstance());

		BTAServerUtilities.LOGGER.info("SaverSingleton initialized");
	}
}
