package BTAServerUtilities.commands.home;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
import BTAServerUtilities.utility.Home;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.Dimension;

public class SetHomeCommand extends Command {

	public SetHomeCommand() {
		super("sethome");
	}

	double scale = Math.pow(10, 1);

	public void addHome(String name, double x, double y, double z, int dimID, CommandSender sender){
		Data.playerData.getOrCreate(sender.getPlayer().username, PlayerData.class).homes.add(new Home(name, x, y, z, dimID));
		Data.playerData.saveAll();
	}

	static CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                  "§8< Command Syntax >");
		syntax.append("sethome",                                                 "§8  > /sethome [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {

		int dimID = sender.getPlayer().dimension;
		double x = Math.round(sender.getPlayer().x * scale) / scale;
		double y = Math.round(sender.getPlayer().y * scale) / scale;
		double z = Math.round(sender.getPlayer().z * scale) / scale;

		if (args.length == 0 && HomeCommand.getHome("home", sender) == null) {

			addHome("home", x, y, z, dimID, sender);

			sender.sendMessage("§5Set Home: <home> to:");
			sender.sendMessage("§5[Dimension: " + sender.getPlayer().world.dimension.languageKey + "]");
			sender.sendMessage("§5[x: " + x + " y: " + y + " z: " + z + "]");
			return true;

		} else if (HomeCommand.getHome("home", sender) != null) {
			sender.sendMessage("§eFailed to Set Home (home <home> already exists))");
			syntax.printLayerAndSubLayers("sethome", sender);
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
