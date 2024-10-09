package BTAServerUtilities.commands.lock;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.config.datatypes.RoleData;
import BTAServerUtilities.interfaces.TileEntityContainerInterface;
import BTAServerUtilities.utility.BSUtility;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
import BTAServerUtilities.utility.UUIDHelper;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class LockCommand extends Command {

	private final static String COMMAND = "lock";

	public LockCommand() {
		super(COMMAND, "l");
	}

	public static CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	public static void buildLockSyntax(){
		syntax.clear();
		syntax.append("title",                                                   "§8< Command Syntax >");
		syntax.append("lock", "title",                                     "§8  > /lock [<mode>]");
		syntax.append("lockTrust", "lock",                                 "§8    > trust <player>");
		syntax.append("lockTrust", "lock",                                 "§8    > trustall <player>");
		syntax.append("lockUntrust", "lock",                               "§8    > untrust <player>");
		syntax.append("lockUntrust", "lock",                               "§8    > untrustall <player>");

	}



	private boolean trust(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {

			HitResult rayCastResult = BSUtility.rayCastFromPlayer(sender);

			if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
				sender.sendMessage("§eFailed to Trust Player to Container! (Not Looking at Container)");
				return true;
			}

			TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

			if(container instanceof TileEntityContainerInterface){
				TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
				if(iContainer.getIsLocked()){

					if(!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))){
						sender.sendMessage("§eFailed to Trust Player to Container! (Not Owned By You)");
						return true;
					}

					if(iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(args[1]))){
						sender.sendMessage("§eFailed to Trust Player to Container! (Player already Trusted)");
						return true;
					}

					if(container instanceof TileEntityChest){
						TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) BSUtility.getOtherChest(sender.getWorld(), (TileEntityChest) container);
						if(iOtherContainer != null) {
							iContainer.addTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
							iOtherContainer.addTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
							sender.sendMessage("§5Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Double Chest!");
							return true;
						}
						iContainer.addTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
						sender.sendMessage("§5Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Chest!");
						return true;
					}

				}
			}
		}
		return true;
	}

	private boolean trustAll(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {
			if(!Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(args[1]))){
				Data.playerData.loadAll(PlayerData.class);
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.add(UUIDHelper.getUUIDFromName(args[1]));
				Data.playerData.saveAll();
				sender.sendMessage("§5Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to all Containers!");
				return true;
			}
			sender.sendMessage("§eFailed to Trust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " to all Containers!");
			sender.sendMessage("§e(Player is Already Trusted)");
			return true;
		}
		sender.sendMessage("§eFailed to Trust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " to all Containers!");
		sender.sendMessage("§e(Player Doesn't Exist)");
		return true;
	}

	private boolean untrust(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {

			HitResult rayCastResult = BSUtility.rayCastFromPlayer(sender);

			if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
				sender.sendMessage("§eFailed to Untrust Player from Container! (Not Looking at Container)");
				return true;
			}

			TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

			if(container instanceof TileEntityContainerInterface){
				TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
				if(iContainer.getIsLocked()){

					if(!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))){
						sender.sendMessage("§eFailed to Untrust Player from Container! (Not Owned By You)");
						return true;
					}

					if(!iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(args[1]))){
						sender.sendMessage("§eFailed to Untrust Player from Container! (Player not Trusted)");
						return true;
					}

					if(container instanceof TileEntityChest){
						TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) BSUtility.getOtherChest(sender.getWorld(), (TileEntityChest) container);
						if(iOtherContainer != null) {
							iContainer.removeTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
							iOtherContainer.removeTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
							sender.sendMessage(TextFormatting.ORANGE + "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Double Chest!");
							return true;
						}
						iContainer.removeTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
						sender.sendMessage(TextFormatting.ORANGE + "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Chest!");
						return true;
					}

				}
			}
		}
		return true;
	}

	private boolean untrustAll(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {
			if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(args[1]))){
				Data.playerData.loadAll(PlayerData.class);
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.remove(UUIDHelper.getUUIDFromName(args[1]));
				Data.playerData.saveAll();
				sender.sendMessage(TextFormatting.ORANGE + "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from all Containers!");
				return true;
			}
			sender.sendMessage("§eFailed to Untrust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " from all Containers!");
			sender.sendMessage("§e(Player isn't Trusted)");
			return true;
		}
		sender.sendMessage("§eFailed to Untrust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " from all Containers!");
		sender.sendMessage("§e(Player Doesn't Exist)");
		return true;
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {

		if (args.length == 0) {
			HitResult rayCastResult = BSUtility.rayCastFromPlayer(sender);
			if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
				sender.sendMessage("§eFailed to Lock Container! (Not Looking at Container)");
				return true;
			}

			TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

			if(container instanceof TileEntityContainerInterface){
				TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
				if(!iContainer.getIsLocked()){

					if(container instanceof TileEntityChest){
						TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) BSUtility.getOtherChest(sender.getWorld(), (TileEntityChest) container);
						if(iOtherContainer != null) {
							iContainer.setIsLocked(true);
							iOtherContainer.setIsLocked(true);
							iContainer.setLockOwner(sender.getPlayer().username);
							iOtherContainer.setLockOwner(sender.getPlayer().username);
							sender.sendMessage("§5Locked Double Chest!");
							return true;
						}
						sender.sendMessage("§5Locked Chest!");
						iContainer.setIsLocked(true);
						iContainer.setLockOwner(sender.getPlayer().username);
						return true;
					}


				} else if(iContainer.getIsLocked() && !iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))){
					sender.sendMessage("§eFailed to Lock Container! (Not Owned By You)");
					return true;
				}
				sender.sendMessage("§eFailed to Lock Container! (Already Locked)");
				return true;
			}
		}

		switch(args[0]){
			case "trust":
				return trust(handler, sender, args);
			case "trustall":
				return trustAll(handler, sender, args);
			case "untrust":
				return untrust(handler, sender, args);
			case "untrustall":
				return untrustAll(handler, sender, args);
			case "help":
				return false;
		}

		sender.sendMessage("§eLock Command Failed (Invalid Syntax)");
		syntax.printAllLines(sender);
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
