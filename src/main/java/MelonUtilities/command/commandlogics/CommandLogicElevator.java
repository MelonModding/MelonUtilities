package MelonUtilities.command.commandlogics;

import MelonUtilities.config.Data;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicElevator {
	public static int elevatorAllowobstructions(PlayerServer sender){
		if(Data.MainConfig.config.allowObstructions){
			Data.MainConfig.config.allowObstructions = false;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Allow Obstructions Disabled");
		} else {
			Data.MainConfig.config.allowObstructions = true;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Allow Obstructions Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int elevatorCooldown(PlayerServer sender, int cooldownValue) {
		Data.MainConfig.config.elevatorCooldown = cooldownValue;
		Data.MainConfig.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Elevator Cooldown Set to %" + cooldownValue);
		return Command.SINGLE_SUCCESS;
	}
}
