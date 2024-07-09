package BTAServerSolutions.BTAServerUtilities.commands.utility;

import BTAServerSolutions.BTAServerUtilities.commands.kit.KitCommand;
import BTAServerSolutions.BTAServerUtilities.commands.role.RoleCommand;
import BTAServerSolutions.BTAServerUtilities.config.ConfigManager;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class BSUCommand extends Command {

	private final static String COMMAND = "bsu";
	private final static String NAME = "BTAServerUtilities";

	public BSUCommand(){super(COMMAND);}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		}

		if (args[0].equals("reload")) {
			sender.sendMessage("§5Reloading " + NAME + "...");
			ConfigManager.loadAllKits();
			sender.sendMessage("§5Reloaded " + ConfigManager.kitHashMap.size() + " Kit(s)!");
			KitCommand.buildKitSyntax();
			sender.sendMessage("§5Built Kit Syntax!");
			ConfigManager.loadAllRoles();
			sender.sendMessage("§5Reloaded " + ConfigManager.roleHashMap.size() + " Role(s)!");
			RoleCommand.buildRoleSyntax();
			sender.sendMessage("§5Built Role Syntax!");
			ConfigManager.loadAllConfigs();
			sender.sendMessage("§5Reloaded Config!");
			return true;
		}

		sender.sendMessage("§e " + NAME + " Error: (Invalid Syntax)");
        return false;
    }

	@Override
	public boolean opRequired(String[] strings) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {

	}
}
