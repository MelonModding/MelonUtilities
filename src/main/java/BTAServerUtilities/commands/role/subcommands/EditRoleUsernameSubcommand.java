package BTAServerUtilities.commands.role.subcommands;

import BTAServerUtilities.commands.role.RoleCommand;
import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class EditRoleUsernameSubcommand {

	public static boolean usernameColor(CommandSender sender, String[] args){
		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).usernameColor = args[4];
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Username Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	public static boolean usernameUnderline(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameUnderline", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameUnderlined = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Username Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameUnderlined = false;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Username Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Underline (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameBold(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBold", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameBold = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Username Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameBold = false;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Username Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Bold (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameItalics(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameItalics", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameItalics = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Username Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameItalics = false;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Username Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Italics (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameBorder(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Border (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorder", sender);
			return true;
		}

		switch(args[4]){
			case "color":
				return usernameBorderColor(sender, args);
			case "none":
				return usernameBorderNone(sender, args);
			case "bracket":
				return usernameBorderBracket(sender, args);
			case "caret":
				return usernameBorderCaret(sender, args);
			case "curly":
				return usernameBorderCurly(sender, args);
			case "custom":
				return usernameBorderCustom(sender, args);
		}
		sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("usernameBorder", sender);
		return true;
	}

	private static boolean usernameBorderColor(CommandSender sender, String[] args){

		if(args.length == 5){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Border Color (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderColor", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).usernameBorderColor = args[5];
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Username Border Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	private static boolean usernameBorderNone(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Username Border to None for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderBracket(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Username Border to [Bracket] for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCaret(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Username Border to <Caret> for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCurly(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Username Border to {Curly} for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCustom(CommandSender sender, String[] args){
		if(args.length == 5){
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isUsernameBorderNone = false;
			role.isUsernameBorderBracket = false;
			role.isUsernameBorderCaret = false;
			role.isUsernameBorderCurly = false;
			role.isUsernameBorderCustom = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Username Border to ?Custom? for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
			return true;
		}
		if(args[5].equals("suffix")){
			if(args.length == 6){
				sender.sendMessage(TextFormatting.RED + "Failed to Edit Custom Suffix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustomAffix", sender);
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
			sender.sendMessage(TextFormatting.LIME + "Set Custom Username Border Suffix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		} else if(args[5].equals("prefix")){
			if(args.length == 6){
				sender.sendMessage(TextFormatting.RED + "Failed to Edit Custom Prefix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustomAffix", sender);
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
			sender.sendMessage(TextFormatting.LIME + "Set Custom Username Border Prefix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Custom Username Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustom", sender);
		return true;
	}
}
