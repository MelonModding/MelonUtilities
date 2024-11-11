package MelonUtilities.config.datatypes.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
	public List<Home> homeData = new ArrayList<>();
	public boolean isHelper = false;
	public boolean lockOnBlockPlaced = false;
	public boolean lockOnBlockPunched = false;
	public boolean lockBypass = false;
	public UUID uuid;
	public List<UUID> uuidsTrustedToAllContainers = new ArrayList<>();

	public User(UUID uuid) {
		this.uuid = uuid;
	}
}
