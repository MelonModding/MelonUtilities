package MelonUtilities.commands.role.subcommands;

import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.FeedbackHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class EditRoleTextSubcommand {
	public static boolean textColor(CommandSender sender, String[] args){
		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).textColor = args[4];
		Data.roles.saveAll();
		FeedbackHandler.success(sender, "Set Text Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		sender.getWorld().playSoundAtEntity(null, sender.getPlayer(), "note.pling", 1f, 2f);
		return true;
	}

	public static boolean textUnderline(CommandSender sender, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(sender, "Failed to Edit Text Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textUnderline", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = true;
			Data.roles.saveAll();
			FeedbackHandler.success(sender, "Set Text Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = false;
			Data.roles.saveAll();
			FeedbackHandler.success(sender, "Set Text Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(sender, "Failed to Edit Text Underline (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean textBold(CommandSender sender, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(sender, "Failed to Edit Text Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textBold", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextBold = true;
			Data.roles.saveAll();
			FeedbackHandler.success(sender, "Set Text Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextBold = false;
			Data.roles.saveAll();
			FeedbackHandler.success(sender, "Set Text Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(sender, "Failed to Edit Text Bold (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean textItalics(CommandSender sender, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(sender, "Failed to Edit Text Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textItalics", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = true;
			Data.roles.saveAll();
			FeedbackHandler.success(sender, "Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = false;
			Data.roles.saveAll();
			FeedbackHandler.success(sender, "Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(sender, "Failed to Edit Text Italics (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}
}
