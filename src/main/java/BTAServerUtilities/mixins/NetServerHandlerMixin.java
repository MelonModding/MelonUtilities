package BTAServerUtilities.mixins;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.ConfigData;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.interfaces.TileEntityContainerInterface;
import BTAServerUtilities.utility.BSUtility;
import BTAServerUtilities.utility.UUIDHelper;
import com.llamalad7.mixinextras.sugar.Local;
import BTAServerUtilities.utility.RoleBuilder;
import BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.net.command.*;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet14BlockDig;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.core.net.packet.Packet53BlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.PlayerList;
import net.minecraft.server.net.handler.NetServerHandler;
import net.minecraft.server.world.WorldServer;
import org.apache.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;

@Mixin(value = NetServerHandler.class, remap = false)
public abstract class NetServerHandlerMixin {
	@Shadow
	private EntityPlayerMP playerEntity;

	@Shadow
	public static Logger logger;

	@Shadow
	private MinecraftServer mcServer;

	@Shadow
	private boolean hasMoved;

	@Shadow
	public abstract void sendPacket(Packet packet);

	@Inject(at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/core/net/ChatEmotes;process(Ljava/lang/String;)Ljava/lang/String;"), method = "handleChat", cancellable = true)
	public void handleChat(Packet3Chat packet, CallbackInfo ci, @Local String message) {

		String defaultRoleDisplay;
		String defaultRoleUsername;
		String defaultRoleTextFormatting;

		if(Data.roles.dataHashMap.get(Data.configs.getOrCreate("config", ConfigData.class).defaultRole) == null){
			defaultRoleDisplay = null;
			defaultRoleUsername = null;
			defaultRoleTextFormatting = null;
		} else {
			defaultRoleDisplay = RoleBuilder.buildRoleDisplay(Data.roles.dataHashMap.get(Data.configs.getOrCreate("config", ConfigData.class).defaultRole));
			defaultRoleUsername = RoleBuilder.buildRoleUsername(Data.roles.dataHashMap.get(Data.configs.getOrCreate("config", ConfigData.class).defaultRole), this.playerEntity.getDisplayName());
			defaultRoleTextFormatting = RoleBuilder.buildRoleTextFormat(Data.roles.dataHashMap.get(Data.configs.getOrCreate("config", ConfigData.class).defaultRole));
		}

		StringBuilder roleDisplays = new StringBuilder();
		String roleUsername = "" + TextFormatting.RESET + TextFormatting.WHITE + "<" + this.playerEntity.getDisplayName() + TextFormatting.RESET + "> ";
		String roleTextFormatting = "" + TextFormatting.WHITE;

		ArrayList<RoleData> rolesGranted = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			rolesGranted.add(null);
		}

		boolean hasBeenGrantedRole = false;
		for(RoleData role : Data.roles.dataHashMap.values()){
			if(role.playersGrantedRole.contains(this.playerEntity.username)){
				rolesGranted.add(role.priority, role);
				hasBeenGrantedRole = true;
			}
		}

		String highestPriorityRoleDisplay = "";
		int tempPriority = Integer.MAX_VALUE;
		for(RoleData role : rolesGranted){
			if (role != null && role.priority < tempPriority) {
				tempPriority = role.priority;
				highestPriorityRoleDisplay = RoleBuilder.buildRoleDisplay(role);
				roleUsername = RoleBuilder.buildRoleUsername(role, this.playerEntity.getDisplayName());
				roleTextFormatting = RoleBuilder.buildRoleTextFormat(role);
			}
        }


		for(int i = rolesGranted.size()-1; i >= 0; i--){
			if(rolesGranted.get(i) != null){
				roleDisplays.append(RoleBuilder.buildRoleDisplay(rolesGranted.get(i)));
			}
		}

		if(hasBeenGrantedRole){
			if (Data.configs.getOrCreate("config", ConfigData.class).displayMode.equals("multi")) {
				if(defaultRoleDisplay != null) {
					message = defaultRoleDisplay + roleDisplays + roleUsername + roleTextFormatting + message;
				} else {
					message = roleDisplays + roleUsername + roleTextFormatting + message;
				}
			} else if (Data.configs.getOrCreate("config", ConfigData.class).displayMode.equals("single")) {
                message = highestPriorityRoleDisplay + roleUsername + roleTextFormatting + message;
			}
		} else if(defaultRoleDisplay != null){
			message = defaultRoleDisplay + defaultRoleUsername + defaultRoleTextFormatting + message;
		} else {
			message = roleUsername + message;
		}

		logger.info(message);
		this.mcServer.playerList.sendEncryptedChatToAllPlayers(message);
		ci.cancel();

	}

	@Unique
	String commandString = "";

	@Redirect(
		method = "Lnet/minecraft/server/net/handler/NetServerHandler;handleSlashCommand(Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(I)Ljava/lang/String;"),
		remap = false
	)
	private String grabCommandString(String s, int beginIndex){
		commandString = s;
		return s.substring(beginIndex, s.length());
	}

	@Redirect(
		method = "Lnet/minecraft/server/net/handler/NetServerHandler;handleSlashCommand(Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/net/command/PlayerCommandSender;isAdmin()Z"),
		remap = false
	)
	private boolean redirectIsAdmin(PlayerCommandSender sender){

		if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).isHelper) {

			String[] args = commandString.substring(1).split(" ");

			if (args.length != 0) {
				if (args[0].equals("gamemode") || args[0].equals("gm")) {
					if(args[1].equals("spectator") || args[1].equals("4") || args[1].equals("survival") || args[1].equals("0")){
						return true;
					}
				}
				else if (args[0].equals("teleport") || args[0].equals("tp")) {
					return true;
				}
			}
		}
		if(sender.isAdmin()){return true;} else{return false;}
	}

	@Redirect(
		method = "Lnet/minecraft/server/net/handler/NetServerHandler;handleSlashCommand(Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/PlayerList;isOp(Ljava/lang/String;)Z"),
		remap = false
	)
	private boolean redirectIsOp(PlayerList playerList, String s){

		if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(this.playerEntity.username).toString(), PlayerData.class).isHelper) {

			String[] args = commandString.substring(1).split(" ");

			if (args.length != 0) {
				if (args[0].equals("kick")) {
					return true;
				}
				else if (args[0].equals("ban")) {
					return true;
				}
			}
		}
		if(this.playerEntity.isOperator()){return true;} else{return false;}
	}

	@Inject(
		at = @At("HEAD"),
		method = "handleBlockDig",
		cancellable = true)
	private void handleBlockDigInject(Packet14BlockDig packet, CallbackInfo ci){
		WorldServer world = this.mcServer.getDimensionWorld(this.playerEntity.dimension);
		TileEntity container = world.getBlockTileEntity(packet.xPosition, packet.yPosition, packet.zPosition);
		if(container instanceof TileEntityContainerInterface) {
			TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockTileEntity(packet.xPosition, packet.yPosition, packet.zPosition);
			if (iContainer.getLockOwner() != null
				&& !iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(this.playerEntity.username))
				&& !iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(this.playerEntity.username))
				&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(this.playerEntity.username))) {
				ci.cancel();
				sendPacket(new Packet53BlockChange(packet.xPosition, packet.yPosition, packet.zPosition, world));
			}
			if(packet.status == 1 && Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(this.playerEntity.username).toString(), PlayerData.class).lockOnBlockPunched && !iContainer.getIsLocked()){
				if (container instanceof TileEntityChest) {
					TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) BSUtility.getOtherChest(world, (TileEntityChest) container);
					if (iOtherContainer != null) {
						iContainer.setIsLocked(true);
						iOtherContainer.setIsLocked(true);
						iContainer.setLockOwner(this.playerEntity.username);
						iOtherContainer.setLockOwner(this.playerEntity.username);
						this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Double Chest!");
						ci.cancel();
					} else {
						this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Chest!");
					}
				} else if (container instanceof TileEntityBlastFurnace) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Blast Furnace!");
				} else if (container instanceof TileEntityFurnace) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Furnace!");
				} else if (container instanceof TileEntityDispenser) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Dispenser!");
				} else if (container instanceof TileEntityMeshGold) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Golden Mesh!");
				} else if (container instanceof TileEntityTrommel) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Trommel!");
				} else if (container instanceof TileEntityBasket) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Basket!");
				}

				iContainer.setIsLocked(true);
				iContainer.setLockOwner(this.playerEntity.username);
				ci.cancel();
			}

			else if (packet.status == 1
			&& Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(this.playerEntity.username).toString(), PlayerData.class).lockOnBlockPunched
			&& iContainer.getIsLocked()
			&& !iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(this.playerEntity.username)))
			{
				this.playerEntity.sendMessage(TextFormatting.RED + "Failed to Lock Container! (Not Owned By You)");
				ci.cancel();
			}

			else if (packet.status == 1
			&& Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(this.playerEntity.username).toString(), PlayerData.class).lockOnBlockPunched
			&& iContainer.getIsLocked()
			&& iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(this.playerEntity.username)))
			{
				this.playerEntity.sendMessage(TextFormatting.RED + "Failed to Lock Container! (Already Locked)");
				ci.cancel();
			}
		}
	}
}
