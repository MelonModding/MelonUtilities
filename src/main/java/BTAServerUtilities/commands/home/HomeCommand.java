package BTAServerUtilities.commands.home;

import BTAServerUtilities.BTAServerUtilities;
import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
import BTAServerUtilities.config.custom.classes.Home;
import BTAServerUtilities.utility.UUIDHelper;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.ServerCommandHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;

import java.util.Objects;

public class HomeCommand extends Command {

	public HomeCommand() {
		super("home");
	}

	public static Home getHome(String name, CommandSender sender){
		for(int i = 0; i < Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).homes.size(); i++){
			if(Objects.equals(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).homes.get(i).name, name)){
				return Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).homes.get(i);
			}
		}
		return null;
	}

	public static void teleport(double x, double y, double z, EntityPlayer player){
		if (player.world.isClientSide)
			return;
		if (player instanceof EntityPlayerMP){
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
			playerMP.playerNetServerHandler.teleport(x, y, z);
		} else if (player instanceof EntityPlayerSP) {
			EntityPlayerSP playerSP = (EntityPlayerSP)player;
			playerSP.setPos(x, y + playerSP.bbHeight, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
	}

	static CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                "§8< Command Syntax >");
		syntax.append("home",                                                 "§8  > /home [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		Home home = getHome("home", sender);
		if (args.length == 0 && home != null) {
			sender.sendMessage("§5Teleporting to Home: <home>");
			if (sender.getPlayer().dimension != home.dimID) {
				MinecraftServer.getInstance().playerList.sendPlayerToOtherDimension((EntityPlayerMP) sender.getPlayer(), home.dimID, false);
			}
			teleport(home.x, home.y, home.z, sender.getPlayer());
			return true;
		} else if (args.length == 0){
			sender.sendMessage("§eFailed to Teleport Home (Home does not exist!)");
			syntax.printLayerAndSubLayers("home", sender);
			return true;
		} else if (args.length == 1) {
			home = getHome(args[0], sender);
			if (home != null) {
				sender.sendMessage("§5Teleporting to Home: <" + args[0] + ">");
				if (sender.getPlayer().dimension != home.dimID) {
					MinecraftServer.getInstance().playerList.sendPlayerToOtherDimension((EntityPlayerMP) sender.getPlayer(), home.dimID, false);
				}
				teleport(home.x, home.y, home.z, sender.getPlayer());
				return true;
			}
			sender.sendMessage("§eFailed to Teleport Home (Home does not exist!)");
			syntax.printLayerAndSubLayers("home", sender);
			return true;
		}

		sender.sendMessage("§eFailed to Teleport Home (Invalid Syntax)");
		syntax.printLayerAndSubLayers("home", sender);
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
