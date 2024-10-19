package MelonUtilities.commands.utility;

import MelonUtilities.commands.helper.HelperCommand;
import MelonUtilities.commands.kit.KitCommand;
import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

import static net.minecraft.server.util.helper.PlayerList.updateList;

public class MUCommand extends Command {

	private final static String COMMAND = "mu";
	private final static String NAME = "MelonUtilities";

	public MUCommand(){super(COMMAND);}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		}

		if (args[0].equals("reload")) {
			sender.sendMessage(TextFormatting.GREEN + "Reloading " + NAME + "...");

			sender.sendMessage(TextFormatting.ORANGE + "Reloading Player Data...");
			Data.playerData.loadAll(PlayerData.class);
			sender.sendMessage(TextFormatting.LIME + "Reloaded " + Data.playerData.dataHashMap.size() + " Player(s)!");

			sender.sendMessage(TextFormatting.ORANGE + "Building Helper Syntax...");
			HelperCommand.buildHelperSyntax();
			sender.sendMessage(TextFormatting.LIME + "Helper Syntax Built!");

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

		sender.sendMessage(TextFormatting.RED + " " + NAME + " Error: (Invalid Syntax)");
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
