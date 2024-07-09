package BTAServerUtilities.commands.role;

import BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.CommandSender;

@SuppressWarnings("SameReturnValue")
public class EditRoleDisplaySubCommand {

	public static boolean displayName(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Display Name (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayName", sender);
			return true;
		}

		sender.sendMessage("§5Set Display Name for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).displayName = args[4];
		RoleCommand.roles.saveAllData();
		return true;
	}

	public static boolean displayColor(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Display Color (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayColor", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).displayColor = args[4];
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Display Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	public static boolean displayUnderline(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Display Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayUnderline", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayUnderlined = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Display Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayUnderlined = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Display Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Display Underline (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean displayBold(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Display Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBold", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayBold = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Display Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayBold = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Display Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Display Bold (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean displayItalics(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Display Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayItalics", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayItalics = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Display Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isDisplayItalics = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Display Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Display Italics (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean displayBorder(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Display Border (Invalid Syntax)");
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
		sender.sendMessage("§eFailed to Edit Display Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("displayBorder", sender);
		return true;
	}

	private static boolean displayBorderColor(CommandSender sender, String[] args){

		if(args.length == 5){
			sender.sendMessage("§eFailed to Edit Display Border Color (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderColor", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).displayBorderColor = args[5];
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Border Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	private static boolean displayBorderNone(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Border to None for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderBracket(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Border to [Bracket] for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCaret(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Border to <Caret> for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCurly(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Display Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("displayBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCurly = true;
		RoleCommand.getRoleFromArg(args[1]).isDisplayBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Border to {Curly} for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean displayBorderCustom(CommandSender sender, String[] args){

		if(args.length == 5){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isDisplayBorderNone = false;
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Border to ?Custom? for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
			return true;
		}
		if(args[5].equals("suffix")){
			if(args.length == 6){
				sender.sendMessage("§eFailed to Edit Custom Suffix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("displayBorderCustomAffix", sender);
				return true;
			}
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			role.customDisplayBorderSuffix = args[6];
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Custom Display Border Suffix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		} else if(args[5].equals("prefix")){
			if(args.length == 6){
				sender.sendMessage("§eFailed to Edit Custom Prefix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("displayBorderCustomAffix", sender);
				return true;
			}
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isDisplayBorderBracket = false;
			role.isDisplayBorderCaret = false;
			role.isDisplayBorderCurly = false;
			role.isDisplayBorderCustom = true;
			role.customDisplayBorderPrefix = args[6];
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Custom Display Border Prefix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Custom Display Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("displayBorderCustom", sender);
		return true;
	}
}
