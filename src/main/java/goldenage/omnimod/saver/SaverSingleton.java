package goldenage.omnimod.saver;

import goldenage.omnimod.OmniMod;
import goldenage.omnimod.homes.Homes;
import goldenage.omnimod.interfaces.Initializable;
import goldenage.omnimod.interfaces.Saveable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// For some reason the name of the class can't be Saver idk why
public class SaverSingleton implements Initializable {
	private static final Path dataFolder = Paths.get("./omnimod_data");

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
		OmniMod.LOGGER.info("Initializing SaverSingleton");

		if (Files.notExists(dataFolder)) {
			OmniMod.LOGGER.info("Creating data folder");

			try {
				Files.createDirectory(dataFolder);
			} catch (IOException e) {
				OmniMod.LOGGER.error("Failed to create data folder");
				throw new RuntimeException(e);
			}
		}



		OmniMod.LOGGER.info("SaverSingleton initialized");
	}
}
