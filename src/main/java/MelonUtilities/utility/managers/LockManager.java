package MelonUtilities.utility.managers;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.Lockable;
import net.minecraft.server.entity.player.PlayerServer;

public class LockManager {

	public static final int UNTRUSTED = 0;
	public static final int COMMUNITY = 1;
	public static final int TRUSTED = 2;
	public static final int FULL = 3;

	public static int determineAuthStatus(Lockable lockable, PlayerServer player){
		int authStatus = UNTRUSTED;
		if(lockable.getIsCommunityContainer())
			authStatus = COMMUNITY;
		if(Data.Users.getOrCreate(lockable.getLockOwner()).usersTrustedToAllContainers.containsKey(player.uuid)
			|| lockable.getTrustedPlayers().contains(player.uuid))
			authStatus = TRUSTED;
		if(lockable.getLockOwner().equals(player.uuid)
			|| Data.Users.getOrCreate(player.uuid).lockBypass)
			authStatus = FULL;
		return authStatus;
	}
}
