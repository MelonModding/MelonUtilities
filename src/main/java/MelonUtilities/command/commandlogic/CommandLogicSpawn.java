package MelonUtilities.command.commandlogic;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Spawn;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackArg;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicSpawn {
	public static int spawnTP(PlayerServer sender){
		if (Data.MainConfig.config.spawnData == null){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Spawn isn't set");
		} else {
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Teleporting to Spawn");
			MUtil.sendToSpawn(sender);
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int spawnReset(PlayerServer sender){
		if (Data.MainConfig.config.spawnData == null){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Spawn isn't set");
		} else {
			Data.MainConfig.config.spawnData = null;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Deleted Spawn");
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int spawnSet(PlayerServer sender){
		Spawn spawn = new Spawn(sender.x, sender.y, sender.z, sender.dimension);
		Data.MainConfig.config.spawnData = spawn;
		Data.MainConfig.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Spawn to %s", new FeedbackArg(spawn));
		return Command.SINGLE_SUCCESS;
	}
}
