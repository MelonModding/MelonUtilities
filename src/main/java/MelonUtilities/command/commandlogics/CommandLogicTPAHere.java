package MelonUtilities.command.commandlogics;

import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.TpaManager;
import com.mojang.brigadier.Command;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicTPAHere {
	public static int tpaHere(PlayerServer sender, String targetUsername){

		assert sender.world != null;
		PlayerServer target = (PlayerServer) sender.world.getPlayerEntityByName(targetUsername);

		if(target != null){
			TpaManager.addRequest(sender, target, true);
		} else {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, targetUsername + " does not exist!");
		}

		return Command.SINGLE_SUCCESS;
	}
}
