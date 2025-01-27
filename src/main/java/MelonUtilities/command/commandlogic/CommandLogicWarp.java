package MelonUtilities.command.commandlogic;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Warp;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackArg;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicWarp {
	public static int warpTP(PlayerServer sender, Warp targetWarp){
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Teleporting to Warp %s", new FeedbackArg(targetWarp));
		MUtil.sendToWarp(sender, targetWarp);
		return Command.SINGLE_SUCCESS;
	}

	public static int warpList(PlayerServer sender){
		if (Data.MainConfig.config.warpData.isEmpty()) {
			sender.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Warps: " + TextFormatting.GRAY + " >");
			sender.sendMessage(TextFormatting.GRAY + "  -No Warps Created-");
			return Command.SINGLE_SUCCESS;
		}
		sender.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Warps: " + TextFormatting.GRAY + " >");
		for (Warp warp : Data.MainConfig.config.warpData) {
			sender.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + warp.toDescriptiveString());
		}
		sender.sendMessage(TextFormatting.GRAY + "<>");
		return Command.SINGLE_SUCCESS;
	}

	public static int warpDelete(PlayerServer sender, Warp targetWarp){
		Data.MainConfig.config.warpData.remove(targetWarp);
		Data.Users.save(sender.uuid);
		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Deleted Warp %s", new FeedbackArg(targetWarp));
		return Command.SINGLE_SUCCESS;
	}

	public static int warpCreate(PlayerServer sender, String name){
		Data.MainConfig.config.warpData.add(new Warp(name, sender.x, sender.y, sender.z, sender.dimension));
		Data.Users.save(sender.uuid);
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Created Warp %s", new FeedbackArg(name));
		return Command.SINGLE_SUCCESS;
	}

	public static int warpRename(PlayerServer sender, Warp targetWarp, String name){
		Data.MainConfig.config.warpData.remove(targetWarp);
		targetWarp.name = name;
		Data.MainConfig.config.warpData.add(targetWarp);
		Data.Users.save(sender.uuid);
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Renamed Warp %s to %s", new FeedbackArg(targetWarp), new FeedbackArg(name));
		return Command.SINGLE_SUCCESS;
	}
}
