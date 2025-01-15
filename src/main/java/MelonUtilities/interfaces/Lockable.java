package MelonUtilities.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Lockable {

	boolean getIsLocked();

	boolean getIsCommunityContainer();

	void setIsLocked(boolean flag);

	void setIsCommunityContainer(boolean flag);

	UUID getLockOwner();

	void setLockOwner(UUID owner);

	List<UUID> getTrustedPlayers();

	//UUID of player, Boolean true if trusted to all containers
	Map<UUID, Boolean> getAllTrustedPlayers();

	void setTrustedPlayers(List<UUID> trustedPlayers);

	void addTrustedPlayer(UUID uuid);

	void removeTrustedPlayer(UUID uuid);

}
