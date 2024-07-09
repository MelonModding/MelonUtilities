package BTAServerSolutions.BTAServerUtilities.homes;

import BTAServerSolutions.BTAServerUtilities.homes.commands.CommandHome;
import BTAServerSolutions.BTAServerUtilities.interfaces.Initializable;
import BTAServerSolutions.BTAServerUtilities.interfaces.Saveable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import BTAServerSolutions.BTAServerUtilities.BTAServerUtilities;
import BTAServerSolutions.BTAServerUtilities.homes.commands.CommandDelHome;
import BTAServerSolutions.BTAServerUtilities.homes.commands.CommandSetHome;
import BTAServerSolutions.BTAServerUtilities.homes.misc.PlayerHomes;
import BTAServerSolutions.BTAServerUtilities.misc.Position;
import turniplabs.halplibe.helper.CommandHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomesSingleton implements Initializable, Saveable {
	private static final Path filePath = Paths.get("./BTAServerUtilities_data/homes.json");

	private static HomesSingleton INSTANCE;
	private final Map<String, PlayerHomes> playerHomeMap;

	private HomesSingleton() {
		playerHomeMap = new HashMap<>();
	}

	public static HomesSingleton getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HomesSingleton();
		}

		return INSTANCE;
	}

	public Position getPlayerHome(String playerName, String homeName) {
		if (!playerHomeMap.containsKey(playerName)) {
			return null;
		}

		if (!playerHomeMap.get(playerName).getAllHomes().containsKey(homeName)) {
			return null;
		}

		return playerHomeMap.get(playerName).getAllHomes().get(homeName);
	}

	public boolean addPlayerHome(String playerName, String homeName, Position position) {
		if (!playerHomeMap.containsKey(playerName)) {
			playerHomeMap.put(playerName, new PlayerHomes(3));
		}

		return playerHomeMap.get(playerName).addHome(homeName, position);
	}

	public boolean removePlayerHome(String playerName, String homeName) {
		if (!playerHomeMap.containsKey(playerName)) {
			return false;
		}

		if (!playerHomeMap.get(playerName).getAllHomes().containsKey(homeName)) {
			return false;
		}

		return playerHomeMap.get(playerName).removeHome(homeName);
	}

	@Override
	public void initialize() {
		BTAServerUtilities.LOGGER.info("Initializing HomesSingleton");

		if (Files.notExists(filePath)) {
			BTAServerUtilities.LOGGER.info("Creating homes file");

			try {
				Files.createFile(filePath);
			} catch (IOException e) {
				BTAServerUtilities.LOGGER.error("Failed to create homes file");
				throw new RuntimeException(e);
			}
		}

		BTAServerUtilities.LOGGER.info("Creating home commands");

		CommandHelper.Server.createCommand(new CommandHome());
		CommandHelper.Server.createCommand(new CommandSetHome());
		CommandHelper.Server.createCommand(new CommandDelHome());

		BTAServerUtilities.LOGGER.info("Home commands created");

		BTAServerUtilities.LOGGER.info("HomeSingleton initialized");
	}

	@Override
	public void save() {
		PrintWriter printWriter;

		try {
			printWriter = new PrintWriter(new File(filePath.toUri()));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		JsonObject jsonObject = new JsonObject();

		for (String key : playerHomeMap.keySet()) {
			PlayerHomes playerHomes = playerHomeMap.get(key);

			System.out.println("Player name: " + key);

			jsonObject.add(key, playerHomes.toJson());
		}

		printWriter.write(jsonObject.toString());

		printWriter.close();
	}

	@Override
	public void load() {
		try {
			Gson gson = new Gson();

			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()));

			LinkedTreeMap<?, ?> json = gson.fromJson(bufferedReader, LinkedTreeMap.class);

			if (json == null) {
				return;
			}

			for (Object obj : json.keySet()) {
				String playerName = (String) obj;

				ArrayList<?> homes = (ArrayList<?>) json.get(playerName);

				for (Object homeObj : homes) {
					LinkedTreeMap<?, ?> values = (LinkedTreeMap<?, ?>) homeObj;

					String homeName = (String) values.get("name");

					LinkedTreeMap<?, ?> positionValues = (LinkedTreeMap<?, ?>) values.get("position");

					int x = (int) (double) positionValues.get("x");
					int y = (int) (double) positionValues.get("y");
					int z = (int) (double) positionValues.get("z");
					int dimension = (int) (double) positionValues.get("dimension");

					Position position = new Position(x, y, z, dimension);

					addPlayerHome(playerName, homeName, position);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
