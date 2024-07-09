package BTAServerSolutions.BTAServerUtilities.commands.role;

import BTAServerSolutions.BTAServerUtilities.config.ConfigManager;
import net.minecraft.core.net.command.CommandSender;

@SuppressWarnings("SameReturnValue")
public class EditRoleText {
	public static boolean textColor(CommandSender sender, String[] args){
		ConfigManager.loadAllRoles();
		RoleCommand.getRoleFromArg(args[1]).textColor = args[4];
		ConfigManager.saveAllRoles();
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
			ConfigManager.loadAllRoles();
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = true;
			ConfigManager.saveAllRoles();
			sender.sendMessage("§5Set Text Underline for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			ConfigManager.loadAllRoles();
			RoleCommand.getRoleFromArg(args[1]).isTextUnderlined = false;
			ConfigManager.saveAllRoles();
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
			ConfigManager.loadAllRoles();
			RoleCommand.getRoleFromArg(args[1]).isTextBold = true;
			ConfigManager.saveAllRoles();
			sender.sendMessage("§5Set Text Bold for role " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			ConfigManager.loadAllRoles();
			RoleCommand.getRoleFromArg(args[1]).isTextBold = false;
			ConfigManager.saveAllRoles();
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
			ConfigManager.loadAllRoles();
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = true;
			ConfigManager.saveAllRoles();
			sender.sendMessage("§5Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		} else if(args[4].equalsIgnoreCase("false")){
			ConfigManager.loadAllRoles();
			RoleCommand.getRoleFromArg(args[1]).isTextItalics = false;
			ConfigManager.saveAllRoles();
			sender.sendMessage("§5Set Text Italics for role: " + RoleCommand.getRoleFromArg(args[1]).displayName + " to: " + args[4]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Text Italics (Invalid Boolean)");
		sender.sendMessage("§8(Tip: Use true/false)");
		return true;
	}
}
