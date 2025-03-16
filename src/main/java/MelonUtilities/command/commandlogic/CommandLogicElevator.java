package MelonUtilities.command.commandlogic;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.utility.feedback.FeedbackHandlerClient;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.core.entity.player.Player;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicElevator {
	public static int elevatorAllowobstructions(Player sender){
		if(MelonUtilities.isServer){
			if(Data.MainConfig.config.allowObstructions){
				Data.MainConfig.config.allowObstructions = false;
				Data.MainConfig.save();
				FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, (PlayerServer) sender, "Allow Obstructions Disabled");
			} else {
				Data.MainConfig.config.allowObstructions = true;
				Data.MainConfig.save();
				FeedbackHandlerServer.sendFeedback(FeedbackType.success, (PlayerServer) sender, "Allow Obstructions Enabled!");
			}
		} else {
			if(Data.MainConfig.config.allowObstructions){
				Data.MainConfig.config.allowObstructions = false;
				Data.MainConfig.save();
				FeedbackHandlerClient.sendFeedback(FeedbackType.destructive, sender, "Allow Obstructions Disabled");
			} else {
				Data.MainConfig.config.allowObstructions = true;
				Data.MainConfig.save();
				FeedbackHandlerClient.sendFeedback(FeedbackType.success, sender, "Allow Obstructions Enabled!");
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int elevatorCooldown(Player sender, int cooldownValue) {
		if(MelonUtilities.isServer){
			Data.MainConfig.config.elevatorCooldown = cooldownValue;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, (PlayerServer) sender, "Elevator Cooldown Set to %" + cooldownValue);
		} else {
			Data.MainConfig.config.elevatorCooldown = cooldownValue;
			Data.MainConfig.save();
			FeedbackHandlerClient.sendFeedback(FeedbackType.success, sender, "Elevator Cooldown Set to %" + cooldownValue);
		}
		return Command.SINGLE_SUCCESS;
	}
}
