package BTAServerUtilities.commands.utility;

import BTAServerUtilities.BTAServerUtilities;
import BTAServerUtilities.commands.kit.KitCommand;
import BTAServerUtilities.commands.role.RoleCommand;
import BTAServerUtilities.config.datatypes.ConfigData;
import BTAServerUtilities.config.datatypes.KitData;
import BTAServerUtilities.config.datatypes.RoleData;
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

			KitCommand.kits.loadAllData(KitData.class);
			sender.sendMessage("§5Reloaded " + KitCommand.kits.data.size() + " Kit(s)!");

			KitCommand.buildKitSyntax();
			sender.sendMessage("§5Built Kit Syntax!");

			RoleCommand.roles.loadAllData(RoleData.class);
			sender.sendMessage("§5Reloaded " + RoleCommand.roles.data.size() + " Role(s)!");

			RoleCommand.buildRoleSyntax();
			sender.sendMessage("§5Built Role Syntax!");
			BTAServerUtilities.configs.loadAllData(ConfigData.class);
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
