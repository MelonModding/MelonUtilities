package BTAServerUtilities.commands.home;

import BTAServerUtilities.BTAServerUtilities;
import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
import BTAServerUtilities.utility.Home;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.*;
import net.minecraft.server.entity.player.EntityPlayerMP;

import java.util.Objects;

public class DelHomeCommand extends Command {

	public DelHomeCommand() {
		super("delhome");
	}

	public void deleteHome(String name, CommandSender sender){
		for(int i = 0; i < Data.playerData.getOrCreate(sender.getPlayer().username, PlayerData.class).homes.size(); i++) {
			if(Objects.equals(Data.playerData.getOrCreate(sender.getPlayer().username, PlayerData.class).homes.get(i).name, name)){
				Data.playerData.getOrCreate(sender.getPlayer().username, PlayerData.class).homes.remove(i);
				Data.playerData.saveAll();
				return;
			}
		}
	}

	static CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                            "§8< Command Syntax >");
		syntax.append("delhome",                          "§8  > /delhome [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		Home home = HomeCommand.getHome("home", sender);

		if (args.length == 0 && home != null) {
			deleteHome("home", sender);
			sender.sendMessage("§1Deleted Home: <home>");
			return true;
		} else if (args.length == 0){
			sender.sendMessage("§eFailed to Delete Home (Home does not exist!)");
			syntax.printLayerAndSubLayers("home", sender);
			return true;
		} else if (args.length == 1) {
			home = HomeCommand.getHome(args[0], sender);
			if (home != null) {
				deleteHome(args[0], sender);
				sender.sendMessage("§1Deleted Home: <" + args[0] + ">");
				return true;
			}
			sender.sendMessage("§eFailed to Delete Home (Home does not exist!)");
			syntax.printLayerAndSubLayers("home", sender);
			return true;
		}

		sender.sendMessage("§eFailed to Delete Home (Invalid Syntax)");
		syntax.printLayerAndSubLayers("delhome", sender);
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
