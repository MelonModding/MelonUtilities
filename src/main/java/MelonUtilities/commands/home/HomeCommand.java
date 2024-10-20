package MelonUtilities.commands.home;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.SyntaxBuilder;
import MelonUtilities.config.custom.classes.Home;
import MelonUtilities.utility.UUIDHelper;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.*;
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
		if (player.world.isClientSide) return;

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

	static SyntaxBuilder syntax = new SyntaxBuilder();

	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		syntax.append("home",                                                 TextFormatting.LIGHT_GRAY + "  > /home [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		Home home = getHome("home", sender);
		if (args.length == 0 && home != null) {
			FeedbackHandler.success(sender, "Teleporting to Home: <home>");

			return sendHome(sender, home);
		}

		if (args.length == 0) {
			FeedbackHandler.error(sender, "Failed to Teleport Home (Home does not exist!)");

			syntax.printLayerAndSubLayers("home", sender);
			return true;
		}

		if (args.length == 1) {
			home = getHome(args[0], sender);
			if (home == null) {
				FeedbackHandler.error(sender, "Failed to Teleport Home (Home does not exist!)");
				syntax.printLayerAndSubLayers("home", sender);
				return true;
			}

			FeedbackHandler.success(sender, "Teleporting to Home: <" + args[0] + ">");
			return sendHome(sender, home);
		}

		FeedbackHandler.error(sender, "Failed to Teleport Home (Invalid Syntax)");
		syntax.printLayerAndSubLayers("home", sender);
		return true;
	}

	private boolean sendHome(CommandSender sender, Home home) {
		if (sender.getPlayer().dimension != home.dimID) {
			MinecraftServer mc = MinecraftServer.getInstance();
			EntityPlayerMP player = (EntityPlayerMP) sender.getPlayer();
			mc.playerList.sendPlayerToOtherDimension(player, home.dimID, false);
		}
		teleport(home.x, home.y, home.z, sender.getPlayer());
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
