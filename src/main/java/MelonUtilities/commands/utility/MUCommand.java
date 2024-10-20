package MelonUtilities.commands.utility;

import MelonUtilities.commands.helper.HelperCommand;
import MelonUtilities.commands.kit.KitCommand;
import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.FeedbackHandler;
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
			FeedbackHandler.success(sender, "Reloading " + NAME + "...");

			FeedbackHandler.destructive(sender, "Reloading Player Data...");
			Data.playerData.loadAll(PlayerData.class);
			FeedbackHandler.success(sender, "Reloaded " + Data.playerData.dataHashMap.size() + " Player(s)!");

			FeedbackHandler.destructive(sender, "Building Helper Syntax...");
			HelperCommand.buildHelperSyntax();
			FeedbackHandler.success(sender, "Helper Syntax Built!");

			FeedbackHandler.destructive(sender, "Reloading Kit Data...");
			Data.kits.loadAll(KitData.class);
			FeedbackHandler.success(sender, "Reloaded " + Data.kits.dataHashMap.size() + " Kit(s)!");

			FeedbackHandler.destructive(sender, "Building Kit Syntax...");
			KitCommand.buildKitSyntax();
			FeedbackHandler.success(sender, "Kit Syntax Built!");

			FeedbackHandler.destructive(sender, "Reloading Role Data...");
			Data.roles.loadAll(RoleData.class);
			FeedbackHandler.success(sender, "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");

			FeedbackHandler.destructive(sender, "Building Role Syntax...");
			RoleCommand.buildRoleSyntax();
			FeedbackHandler.success(sender, "Role Syntax Built!");

			FeedbackHandler.destructive(sender, "Reloading General Configs...");
			Data.configs.loadAll(ConfigData.class);
			FeedbackHandler.success(sender, "Reloaded Configs!");

			FeedbackHandler.destructive(sender, "Updating Player List...");
			updateList();
			FeedbackHandler.success(sender, "Updated List!");

			return true;
		}

		FeedbackHandler.error(sender, " " + NAME + " Error: (Invalid Syntax)");
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
