package BTAServerSolutions.BTAServerUtilities.commands.role;

import BTAServerSolutions.BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.CommandSender;

@SuppressWarnings("SameReturnValue")
public class EditRoleTextSubCommand {
	public static boolean textColor(CommandSender sender, String[] args){
		RoleCommand.roles.loadAllData(RoleData.class);
		RoleCommand.getRoleFromArg(args[1]).textColor = args[4];
		RoleCommand.roles.saveAllData();
		sender.sendMessage("§5Set Text Color for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
		return true;
	}

	public static boolean textUnderline(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Text Underline (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textUnderline", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Text Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Text Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Text Underline (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean textBold(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Text Bold (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textBold", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextBold = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Text Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextBold = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Text Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Text Bold (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}

	public static boolean textItalics(CommandSender sender, String[] args){

		if(args.length == 4){
			sender.sendMessage("§eFailed to Edit Text Italics (Invalid Syntax)");
			RoleCommand.syntax.printLayerAndSubLayers("textItalics", sender);
			return true;
		}

		if(args[4].equalsIgnoreCase("true")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = true;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			RoleCommand.roles.loadAllData(RoleData.class);
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = false;
			RoleCommand.roles.saveAllData();
			sender.sendMessage("§5Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Text Italics (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}
}
