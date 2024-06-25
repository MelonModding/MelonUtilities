package goldenage.omnimod.saver;

import goldenage.omnimod.interfaces.Saveable;

import java.util.ArrayList;
import java.util.List;

public class Saver {
	private static Saver INSTANCE;
	private final List<Saveable> saveableList;

	private Saver() {
		saveableList = new ArrayList<>();
	}

	public static void add(Saveable saveable) {
		if (INSTANCE == null) {
			INSTANCE = new Saver();
		}

		INSTANCE.saveableList.add(saveable);
	}

	public static void saveAll() {
		if (INSTANCE == null) {
			INSTANCE = new Saver();
		}

		for (Saveable saveable : INSTANCE.saveableList) {
			saveable.save();
		}
	}

	public static void loadAll() {
		if (INSTANCE == null) {
			INSTANCE = new Saver();
		}

		for (Saveable saveable : INSTANCE.saveableList) {
			saveable.load();
		}
	}
}
