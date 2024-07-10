package BTAServerUtilities.commands.home;

import BTAServerUtilities.BTAServerUtilities;
import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
import BTAServerUtilities.utility.Home;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.ServerCommandHandler;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeCommand extends Command {

	public HomeCommand() {
		super("home");
	}

	public static Home getHome(String name, CommandSender sender){
		for(int i = 0; i < Data.playerData.getOrCreate(sender.getPlayer().username, PlayerData.class).homes.size(); i++){
			if(Objects.equals(Data.playerData.getOrCreate(sender.getPlayer().username, PlayerData.class).homes.get(i).name, name)){
				return Data.playerData.getOrCreate(sender.getPlayer().username, PlayerData.class).homes.get(i);
			}
		}
		return null;
	}

	public static void teleport(double x, double y, double z, EntityPlayer player){
		if (player.world.isClientSide)
			return;
		if (player instanceof EntityPlayerMP){
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			playerMP.playerNetServerHandler.teleport(x, y, z);
		} else if (player instanceof EntityPlayerSP) {
			EntityPlayerSP playerSP = (EntityPlayerSP)player;
			playerSP.setPos(x, y + playerSP.bbHeight, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 100f);
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
				BTAServerUtilities.sendPlayerToOtherDimension((EntityPlayerMP) sender.getPlayer(), home.dimID, (ServerCommandHandler) handler);
			}
			teleport(home.x, home.y, home.z, sender.getPlayer());
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
