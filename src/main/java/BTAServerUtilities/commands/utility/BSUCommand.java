package BTAServerUtilities.commands.utility;

import BTAServerUtilities.commands.kit.KitCommand;
import BTAServerUtilities.commands.role.RoleCommand;
import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.ConfigData;
import BTAServerUtilities.config.datatypes.KitData;
import BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

import static net.minecraft.server.util.helper.PlayerList.updateList;

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

			sender.sendMessage(TextFormatting.GREEN + "Reloading " + NAME + "...");

			sender.sendMessage(TextFormatting.ORANGE + "Reloading Kit Data...");
			Data.kits.loadAll(KitData.class);
			sender.sendMessage(TextFormatting.LIME + "Reloaded " + Data.kits.dataHashMap.size() + " Kit(s)!");

			sender.sendMessage(TextFormatting.ORANGE + "Building Kit Syntax...");
			KitCommand.buildKitSyntax();
			sender.sendMessage(TextFormatting.LIME + "Kit Syntax Built!");

			sender.sendMessage(TextFormatting.ORANGE + "Reloading Role Data...");
			Data.roles.loadAll(RoleData.class);
			sender.sendMessage(TextFormatting.LIME + "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");

			sender.sendMessage(TextFormatting.ORANGE + "Building Role Syntax...");
			RoleCommand.buildRoleSyntax();
			sender.sendMessage(TextFormatting.LIME + "Role Syntax Built!");

			sender.sendMessage(TextFormatting.ORANGE + "Reloading General Configs...");
			Data.configs.loadAll(ConfigData.class);
			sender.sendMessage(TextFormatting.LIME + "Reloaded Configs!");

			sender.sendMessage(TextFormatting.ORANGE + "Updating Player List...");
			updateList();
			sender.sendMessage(TextFormatting.LIME + "Updated List!");

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
