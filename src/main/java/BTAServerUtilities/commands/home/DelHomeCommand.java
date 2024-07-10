package BTAServerUtilities.commands.home;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
import BTAServerUtilities.utility.Home;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

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
		if (args.length == 0){
			deleteHome("home", sender);
			sender.sendMessage("§1Deleted Home: <home>");
			return true;
		}

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
