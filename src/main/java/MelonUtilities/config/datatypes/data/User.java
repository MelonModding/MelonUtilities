package MelonUtilities.config.datatypes.data;

import java.util.*;

public class User {
	public List<Home> homeData = new ArrayList<>();
	public boolean isHelper = false;
	public boolean lockOnBlockPlaced = false;
	public boolean lockOnBlockPunched = false;
	public boolean lockBypass = false;
	public UUID uuid;
	public Map<UUID, String> usersTrustedToAllContainers = new HashMap<>();

	public User(UUID uuid) {
		this.uuid = uuid;
	}
}
