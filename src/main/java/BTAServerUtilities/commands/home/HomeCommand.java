package BTAServerUtilities.commands.home;

import BTAServerUtilities.commands.home.utility.HomesSingleton;
import BTAServerUtilities.utility.Position;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class HomeCommand extends Command {
	public HomeCommand() {
		super("home");
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] strings) {
		String homeName = "default";

		if (strings.length > 0) {
			homeName = strings[0];
		}

		EntityPlayerMP player = (EntityPlayerMP) commandSender.getPlayer();

		Position position = HomesSingleton.getInstance().getPlayerHome(player.username, homeName);

		if (position == null) {
			commandSender.sendMessage(TextFormatting.RED + "Home does not exist!");
			return true;
		}

		commandSender.sendMessage("Teleporting...");

		if (player.dimension != position.dimension) {
			player.mcServer.playerList.sendPlayerToOtherDimension(player, position.dimension);
		}

		player.playerNetServerHandler.teleport(position.x, position.y, position.z);

		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		commandSender.sendMessage("/home <home>");
	}
}
