package MelonUtilities.commands.role.subcommands;

import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.FeedbackHandler;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class EditRoleTextSubcommand {
	public static boolean textColor(CommandSource source, String[] args){
		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).textColor = args[4];
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Text Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		source.getWorld().playSoundAtEntity(null, source.getSender(), "note.pling", 1f, 2f);
		return true;
	}

	public static boolean textUnderline(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Text Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textUnderline", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Text Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Text Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Text Underline (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean textBold(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Text Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textBold", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextBold = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Text Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextBold = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Text Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Text Bold (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean textItalics(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Text Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textItalics", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Text Italics (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}
}
