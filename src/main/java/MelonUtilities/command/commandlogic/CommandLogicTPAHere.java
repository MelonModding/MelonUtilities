package MelonUtilities.command.commandlogic;

import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.TpaManager;
import com.mojang.brigadier.Command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.world.WorldServer;

public class CommandLogicTPAHere {
	public static int tpaHere(PlayerServer sender, String targetUsername){

		PlayerServer target = null;
		for(WorldServer dimension : MinecraftServer.getInstance().dimensionWorlds.values()){
			if(dimension.getPlayerEntityByName(targetUsername) != null){
				target = (PlayerServer) dimension.getPlayerEntityByName(targetUsername);
			}
		}

		if(target != null){
			TpaManager.addRequest(sender, target, true);
		} else {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, targetUsername + " does not exist!");
		}

		return Command.SINGLE_SUCCESS;
	}
}
