package BTAServerUtilities.commands.role.subcommands;

import BTAServerUtilities.commands.role.RoleCommand;
import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class EditRoleDisplaySubcommand {

	public static boolean displayName(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Name (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayName", sender);
			return true;
		}

		sender.sendMessage(TextFormatting.LIME + "Set Display Name for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).displayName = args[4];
		Data.roles.saveAll();
		return true;
	}

	public static boolean displayColor(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Color (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayColor", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).displayColor = args[4];
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Display Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	public static boolean displayUnderline(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayUnderline", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayUnderlined = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Display Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayUnderlined = false;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Display Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Underline (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean displayBold(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBold", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayBold = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Display Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayBold = false;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Display Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Bold (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean displayItalics(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayItalics", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayItalics = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Display Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayItalics = false;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Display Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Italics (Invalid Boolean)");
		sender.sendMessage(TextFormatting.LIGHT_GRAY + "(Tip: Use true/false)");
		return true;
	}

	public static boolean displayBorder(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Border (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorder", sender);
			return true;
		}

		switch(args[4]){
			case "color":
				return displayBorderColor(sender, args);
			case "none":
				return displayBorderNone(sender, args);
			case "bracket":
				return displayBorderBracket(sender, args);
			case "caret":
				return displayBorderCaret(sender, args);
			case "curly":
				return displayBorderCurly(sender, args);
			case "custom":
				return displayBorderCustom(sender, args);
		}
		sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("displayBorder", sender);
		return true;
	}

	private static boolean displayBorderColor(CommandSender sender, String[] args){

		if(args.length == 5){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Border Color (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderColor", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).displayBorderColor = args[5];
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Border Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	private static boolean displayBorderNone(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Border to None for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderBracket(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Border to [Bracket] for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCaret(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Border to <Caret> for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCurly(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		Data.roles.saveAll();
		sender.sendMessage(TextFormatting.LIME + "Set Border to {Curly} for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCustom(CommandSender sender, String[] args){

		if(args.length == 5){
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isDisplayBorderNone = false;
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Border to ?Custom? for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
			return true;
		}
		if(args[5].equals("suffix")){
			if(args.length == 6){
				sender.sendMessage(TextFormatting.RED + "Failed to Edit Custom Suffix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("displayBorderCustomAffix", sender);
				return true;
			}
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			role.customDisplayBorderSuffix = args[6];
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Custom Display Border Suffix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		} else if(args[5].equals("prefix")){
			if(args.length == 6){
				sender.sendMessage(TextFormatting.RED + "Failed to Edit Custom Prefix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("displayBorderCustomAffix", sender);
				return true;
			}
			Data.roles.loadAll(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			role.customDisplayBorderPrefix = args[6];
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Custom Display Border Prefix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Custom Display Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("displayBorderCustom", sender);
		return true;
	}
}
