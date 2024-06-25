package goldenage.omnimod.saver;

import goldenage.omnimod.interfaces.Saveable;

import java.util.ArrayList;
import java.util.List;

public class Saver {
	private static Saver INSTANCE;
	private final List<Saveable> saveableList;

	public static Saver getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Saver();
		}

		return INSTANCE;
	}

	private Saver() {
		saveableList = new ArrayList<>();
	}

	public void add(Saveable saveable) {
		saveableList.add(saveable);
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
}
