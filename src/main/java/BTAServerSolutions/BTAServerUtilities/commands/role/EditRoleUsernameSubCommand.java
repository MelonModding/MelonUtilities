package BTAServerSolutions.BTAServerUtilities.commands.role;

import BTAServerSolutions.BTAServerUtilities.config.datatypes.ConfigData;
import BTAServerSolutions.BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.CommandSender;

@SuppressWarnings("SameReturnValue")
public class EditRoleUsernameSubCommand {

	public static boolean usernameColor(CommandSender sender, String[] args){
		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).usernameColor = args[4];
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Username Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	public static boolean usernameUnderline(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Username Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameUnderline", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameUnderlined = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Username Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameUnderlined = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Username Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Username Underline (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameBold(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Username Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBold", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameBold = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Username Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameBold = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Username Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Username Bold (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameItalics(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Username Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameItalics", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameItalics = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Username Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isUsernameItalics = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Username Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Username Italics (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean usernameBorder(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Username Border (Invalid Syntax)");
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
		sender.sendMessage("§eFailed to Edit Username Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("usernameBorder", sender);
		return true;
	}

	private static boolean usernameBorderColor(CommandSender sender, String[] args){

		if(args.length == 5){
			sender.sendMessage("§eFailed to Edit Username Border Color (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderColor", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).usernameBorderColor = args[5];
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Username Border Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	private static boolean usernameBorderNone(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Username Border to None for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderBracket(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Username Border to [Bracket] for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCaret(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Username Border to <Caret> for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCurly(CommandSender sender, String[] args){

		if(args.length == 6){
			sender.sendMessage("§eFailed to Edit Username Border Type (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("usernameBorderType", sender);
			return true;
		}

		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderNone = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderBracket = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCaret = false;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCurly = true;
		RoleCommand.getRoleFromArg(args[1]).isUsernameBorderCustom = false;
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Username Border to {Curly} for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
		return true;
	}

	private static boolean usernameBorderCustom(CommandSender sender, String[] args){
		if(args.length == 5){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isUsernameBorderNone = false;
			role.isUsernameBorderBracket = false;
			role.isUsernameBorderCaret = false;
			role.isUsernameBorderCurly = false;
			role.isUsernameBorderCustom = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Username Border to ?Custom? for role " + RoleCommand.getRoleFromArg(args[1]).displayName);
			return true;
		}
		if(args[5].equals("suffix")){
			if(args.length == 6){
				sender.sendMessage("§eFailed to Edit Custom Suffix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustomAffix", sender);
				return true;
			}
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isUsernameBorderBracket = false;
			role.isUsernameBorderCaret = false;
			role.isUsernameBorderCurly = false;
			role.isUsernameBorderCustom = true;
			role.customUsernameBorderSuffix = args[6];
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Custom Username Border Suffix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		} else if(args[5].equals("prefix")){
			if(args.length == 6){
				sender.sendMessage("§eFailed to Edit Custom Prefix (Invalid Syntax)");
				RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustomAffix", sender);
				return true;
			}
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleData role = RoleCommand.getRoleFromArg(args[1]);
			role.isUsernameBorderBracket = false;
			role.isUsernameBorderCaret = false;
			role.isUsernameBorderCurly = false;
			role.isUsernameBorderCustom = true;
			role.customUsernameBorderPrefix = args[6];
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Custom Username Border Prefix for Role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to " + args[6]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Custom Username Border (Invalid Syntax)");
		RoleCommand.syntax.printLayerAndSubLayers("usernameBorderCustom", sender);
		return true;
	}
}
