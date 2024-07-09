package BTAServerUtilities.commands.home;

import BTAServerUtilities.config.DataBank;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.config.datatypes.RoleData;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
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

	CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	public void buildRoleSyntax(){
		syntax.clear();
		syntax.append("title",                                                  "§8< Command Syntax >");
		syntax.append("home",                                                 "§8  > /home [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {


		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		syntax.printAllLines(sender);
	}
}

/*if (player.dimension != position.dimension) {
			player.mcServer.playerList.sendPlayerToOtherDimension(player, position.dimension);
		}

		player.playerNetServerHandler.teleport(position.x, position.y, position.z);*/
