package MelonUtilities.command.commandlogics;

import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.TpaManager;
import com.mojang.brigadier.Command;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicTPA {
	public static int tpa(PlayerServer sender, String targetUsername) {

		assert sender.world != null;
		PlayerServer target = (PlayerServer) sender.world.getPlayerEntityByName(targetUsername);

		if (target != null) {
			TpaManager.addRequest(sender, target, false);
		} else {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, targetUsername + " does not exist!");
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int tpaccept(PlayerServer sender){
		if (!TpaManager.acceptRequest(sender)) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "No pending request found to accept!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int tpdeny(PlayerServer sender){
		if (!TpaManager.denyRequest(sender)) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "No pending request found to deny!");
		}
		return Command.SINGLE_SUCCESS;
	}
}
