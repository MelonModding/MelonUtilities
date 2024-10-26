package MelonUtilities.commands.role.subcommands;

import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.FeedbackHandler;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class EditRoleUsernameSubcommand {

	public static boolean usernameColor(CommandSource source, String[] args){
		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).usernameColor = args[4];
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Username Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	public static boolean usernameUnderline(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Username Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameUnderline", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameUnderlined = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Username Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameUnderlined = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Username Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Username Underline (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameBold(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Username Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBold", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameBold = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Username Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameBold = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Username Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Username Bold (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameItalics(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Username Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameItalics", source);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameItalics = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Username Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameItalics = false;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Username Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Username Italics (Invalid Boolean)");
		source.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameBorder(CommandSource source, String[] args){

		if(args.length == 4){
			FeedbackHandler.error(source, "Failed to Edit Username Border (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorder", source);
			return true;
		}

		switch(args[4]){
			case "color":
				return usernameBorderColor(source, args);
			case "none":
				return usernameBorderNone(source, args);
			case "bracket":
				return usernameBorderBracket(source, args);
			case "caret":
				return usernameBorderCaret(source, args);
			case "curly":
				return usernameBorderCurly(source, args);
			case "custom":
				return usernameBorderCustom(source, args);
		}
		FeedbackHandler.error(source, "Failed to Edit Username Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("usernameBorder", source);
		return true;
	}

	private static boolean usernameBorderColor(CommandSource source, String[] args){

		if(args.length == 5){
			FeedbackHandler.error(source, "Failed to Edit Username Border Color (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderColor", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).usernameBorderColor = args[5];
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Username Border Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	private static boolean usernameBorderNone(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Username Border to None for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderBracket(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Username Border to [Bracket] for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCaret(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Username Border to <Caret> for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCurly(CommandSource source, String[] args){

		if(args.length == 6){
			FeedbackHandler.error(source, "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", source);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(source, "Set Username Border to {Curly} for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCustom(CommandSource source, String[] args){
		if(args.length == 5){
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isUsernameBorderNone = false;
			role.isUsernameBorderBracket = false;
			role.isUsernameBorderCaret = false;
			role.isUsernameBorderCurly = false;
			role.isUsernameBorderCustom = true;
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Username Border to ?Custom? for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
			return true;
		}
		if(args[5].equals("suffix")){
			if(args.length == 6){
				FeedbackHandler.error(source, "Failed to Edit Custom Suffix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustomAffix", source);
				return true;
			}
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isUsernameBorderBracket = false;
			role.isUsernameBorderCaret = false;
			role.isUsernameBorderCurly = false;
			role.isUsernameBorderCustom = true;
			role.customUsernameBorderSuffix = args[6];
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Custom Username Border Suffix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		} else if(args[5].equals("prefix")){
			if(args.length == 6){
				FeedbackHandler.error(source, "Failed to Edit Custom Prefix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustomAffix", source);
				return true;
			}
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isUsernameBorderBracket = false;
			role.isUsernameBorderCaret = false;
			role.isUsernameBorderCurly = false;
			role.isUsernameBorderCustom = true;
			role.customUsernameBorderPrefix = args[6];
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Set Custom Username Border Prefix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		}

		FeedbackHandler.error(source, "Failed to Edit Custom Username Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustom", source);
		return true;
	}
}
