package BTAServerUtilities.interfaces;

import java.util.List;
import java.util.UUID;

public interface TileEntityContainerInterface {

	boolean getIsLocked();

	boolean getIsCommunityContainer();

	void setIsLocked(boolean flag);

	void setIsCommunityContainer(boolean flag);

	UUID getLockOwner();

	void setLockOwner(String owner);

	void setLockOwner(UUID owner);

	List<UUID> getTrustedPlayers();

	void setTrustedPlayers(List<UUID> trustedPlayers);

	void addTrustedPlayer(String username);

	void addTrustedPlayer(UUID uuid);

	void removeTrustedPlayer(String username);

	void removeTrustedPlayer(UUID uuid);

}
