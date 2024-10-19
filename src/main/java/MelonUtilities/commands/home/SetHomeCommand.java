package MelonUtilities.commands.home;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.utility.SyntaxBuilder;
import MelonUtilities.config.custom.classes.Home;
import MelonUtilities.utility.UUIDHelper;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

public class SetHomeCommand extends Command {

	public SetHomeCommand() {
		super("sethome");
	}

	double scale = Math.pow(10, 1);

	public void addHome(String name, double x, double y, double z, int dimID, CommandSender sender){
		Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).homes.add(new Home(name, x, y, z, dimID));
		Data.playerData.saveAll();
	}

	static SyntaxBuilder syntax = new SyntaxBuilder();

	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                  TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		syntax.append("sethome",                                                 TextFormatting.LIGHT_GRAY + "  > /sethome [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {

		int dimID = sender.getPlayer().dimension;
		double x = Math.round(sender.getPlayer().x * scale) / scale;
		double y = Math.round(sender.getPlayer().y * scale) / scale;
		double z = Math.round(sender.getPlayer().z * scale) / scale;

		Home home = HomeCommand.getHome("home", sender);

		if (args.length == 0 && home == null) {

			addHome("home", x, y, z, dimID, sender);
			sender.sendMessage(TextFormatting.LIME + "Set Home: <home> to:");
			sender.sendMessage(TextFormatting.LIME + "[Dimension: " + sender.getPlayer().world.dimension.getTranslatedName() + "]");
			sender.sendMessage(TextFormatting.LIME + "[x: " + x + " y: " + y + " z: " + z + "]");
			return true;

		} else if (args.length == 0) {
			sender.sendMessage(TextFormatting.RED + "Failed to Set Home (Home already exists!))");
			syntax.printLayerAndSubLayers("sethome", sender);
			return true;
		} else if (args.length == 1) {
			home = HomeCommand.getHome(args[0], sender);
			if(home == null){
				addHome(args[0], x, y, z, dimID, sender);
				sender.sendMessage(TextFormatting.LIME + "Set Home: <" + args[0] + "> to:");
				sender.sendMessage(TextFormatting.LIME + "[Dimension: " + sender.getPlayer().world.dimension.getTranslatedName() + "]");
				sender.sendMessage(TextFormatting.LIME + "[x: " + x + " y: " + y + " z: " + z + "]");
				return true;
			}
			sender.sendMessage(TextFormatting.RED + "Failed to Set Home (Invalid Syntax)");
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
