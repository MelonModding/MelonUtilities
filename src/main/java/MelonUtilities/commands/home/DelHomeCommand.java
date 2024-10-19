package MelonUtilities.commands.home;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.utility.SyntaxBuilder;
import MelonUtilities.config.custom.classes.Home;
import MelonUtilities.utility.UUIDHelper;
import net.minecraft.core.net.command.*;

import java.util.Objects;

public class DelHomeCommand extends Command {

	public DelHomeCommand() {
		super("delhome");
	}

	public void deleteHome(String name, CommandSender sender){
		for(int i = 0; i < Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).homes.size(); i++) {
			if(Objects.equals(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).homes.get(i).name, name)){
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).homes.remove(i);
				Data.playerData.saveAll();
				return;
			}
		}
	}

	static SyntaxBuilder syntax = new SyntaxBuilder();

	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                            TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		syntax.append("delhome",                          TextFormatting.LIGHT_GRAY + "  > /delhome [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		Home home = HomeCommand.getHome("home", sender);

		if (args.length == 0 && home != null) {
			deleteHome("home", sender);
			sender.sendMessage(TextFormatting.ORANGE + "Deleted Home: <home>");
			return true;
		} else if (args.length == 0){
			sender.sendMessage(TextFormatting.RED + "Failed to Delete Home (Home does not exist!)");
			syntax.printLayerAndSubLayers("home", sender);
			return true;
		} else if (args.length == 1) {
			home = HomeCommand.getHome(args[0], sender);
			if (home != null) {
				deleteHome(args[0], sender);
				sender.sendMessage(TextFormatting.ORANGE + "Deleted Home: <" + args[0] + ">");
				return true;
			}
			sender.sendMessage(TextFormatting.RED + "Failed to Delete Home (Home does not exist!)");
			syntax.printLayerAndSubLayers("home", sender);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Delete Home (Invalid Syntax)");
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
