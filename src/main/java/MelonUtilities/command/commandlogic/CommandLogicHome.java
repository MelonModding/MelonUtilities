package MelonUtilities.command.commandlogic;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Home;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackArg;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicHome {
	public static int homeTP(PlayerServer sender, Home targetHome){
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Teleporting to Home %s", new FeedbackArg(targetHome));
		MUtil.sendToHome(sender, targetHome);
		return Command.SINGLE_SUCCESS;
	}

	public static int homeList(PlayerServer sender){
		if (Data.Users.userDataHashMap.get(sender.uuid).homeData.isEmpty()) {
			sender.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Homes: " + TextFormatting.GRAY + " >");
			sender.sendMessage(TextFormatting.GRAY + "  -No Homes Created-");
			return Command.SINGLE_SUCCESS;
		}
		sender.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Homes: " + TextFormatting.GRAY + " >");
		for (Home home : Data.Users.userDataHashMap.get(sender.uuid).homeData) {
			sender.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + home.toDescriptiveString());
		}
		sender.sendMessage(TextFormatting.GRAY + "<>");
		return Command.SINGLE_SUCCESS;
	}

	public static int homeDelete(PlayerServer sender, Home targetHome){
		Data.Users.getOrCreate(sender.uuid).homeData.remove(targetHome);
		Data.Users.save(sender.uuid);
		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Deleted Home %s", new FeedbackArg(targetHome));
		return Command.SINGLE_SUCCESS;
	}

	public static int homeCreate(PlayerServer sender, String name){
		Data.Users.getOrCreate(sender.uuid).homeData.add(new Home(name, sender.x, sender.y, sender.z, sender.dimension));
		Data.Users.save(sender.uuid);
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Created Home %s", new FeedbackArg(name));
		return Command.SINGLE_SUCCESS;
	}

	public static int homeRename(PlayerServer sender, Home targetHome, String name){
		Data.Users.getOrCreate(sender.uuid).homeData.remove(targetHome);
		targetHome.name = name;
		Data.Users.getOrCreate(sender.uuid).homeData.add(targetHome);
		Data.Users.save(sender.uuid);
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Renamed Home %s to %s", new FeedbackArg(targetHome), new FeedbackArg(name));
		return Command.SINGLE_SUCCESS;
	}
}
