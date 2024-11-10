package MelonUtilities.commands.role.subcommands;

import MelonUtilities.commands.role.RoleCommandOld;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.feedback.FeedbackHandler;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class EditRoleDisplaySubcommandOld {

	public static boolean displayName(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Display Name (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayName", source);
			return true;
		}


		FeedbackHandler.success(source, "Set Display Name for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		Data.roles.loadAll(RoleData.class);
		RoleCommandOld.getRoleFromArg(args[1]).displayName = args[4];
		Data.roles.saveAll();
		return true;
	}

	public static boolean displayColor(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Display Color (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayColor", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommandOld.getRoleFromArg(args[1]).displayColor = args[4];
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Display Color for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	public static boolean displayUnderline(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Display Underline (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayUnderline", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommandOld.getRoleFromArg(args[1]).isDisplayUnderlined = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Display Underline for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommandOld.getRoleFromArg(args[1]).isDisplayUnderlined = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Display Underline for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Display Underline (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean displayBold(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Display Bold (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayBold", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommandOld.getRoleFromArg(args[1]).isDisplayBold = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Display Bold for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommandOld.getRoleFromArg(args[1]).isDisplayBold = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Display Bold for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Display Bold (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean displayItalics(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Display Italics (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayItalics", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommandOld.getRoleFromArg(args[1]).isDisplayItalics = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Display Italics for role: " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommandOld.getRoleFromArg(args[1]).isDisplayItalics = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Display Italics for role: " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Display Italics (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean displayBorder(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Display Border (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayBorder", source);
			return true;
		}

		switch(args[4]){
			case "color":
				return displayBorderColor(source, args);
			case "none":
				return displayBorderNone(source, args);
			case "bracket":
				return displayBorderBracket(source, args);
			case "caret":
				return displayBorderCaret(source, args);
			case "curly":
				return displayBorderCurly(source, args);
			case "custom":
				return displayBorderCustom(source, args);
		}
		FeedbackHandler.error(source, "Failed to Edit Display Border (Invalid Syntax)");
		RoleCommandOld.syntax.printLayerAndSubLayers("displayBorder", source);
		return true;
	}

	private static boolean displayBorderColor(CommandSource source, String[] args){

		if(args.length == 5){
			FeedbackHandler.error(source, "Failed to Edit Display Border Color (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderColor", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommandOld.getRoleFromArg(args[1]).displayBorderColor = args[5];
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Border Color for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	private static boolean displayBorderNone(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderNone = true;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Border to None for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderBracket(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderBracket = true;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Border to [Bracket] for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCaret(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCaret = true;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Border to <Caret> for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCurly(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCurly = true;
		RoleCommandOld.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Border to {Curly} for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCustom(CommandSource source, String[] args){

		if(args.length == 5){
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommandOld.getRoleFromArg(args[1]);
			role.isDisplayBorderNone = false;
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Border to ?Custom? for role " + RoleCommandOld.getRoleFromArg(args[1]).displayName);
			return true;
		}
		if(args[5].equals("suffix")){
			if(args.length == 6){
				FeedbackHandler.error(source, "Failed to Edit Custom Suffix (Invalid Syntax)");
				RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderCustomAffix", source);
				return true;
			}
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommandOld.getRoleFromArg(args[1]);
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			role.customDisplayBorderSuffix = args[6];
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Custom Display Border Suffix for Role: " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		} else if(args[5].equals("prefix")){
			if(args.length == 6){
				FeedbackHandler.error(source, "Failed to Edit Custom Prefix (Invalid Syntax)");
				RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderCustomAffix", source);
				return true;
			}
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommandOld.getRoleFromArg(args[1]);
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			role.customDisplayBorderPrefix = args[6];
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Custom Display Border Prefix for Role: " + RoleCommandOld.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Custom Display Border (Invalid Syntax)");
		RoleCommandOld.syntax.printLayerAndSubLayers("displayBorderCustom", source);
		return true;
	}
}
