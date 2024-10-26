package MelonUtilities.mixins.other;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.interfaces.BlockEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.UUIDHelper;
import com.llamalad7.mixinextras.sugar.Local;
import MelonUtilities.utility.RoleBuilder;
import MelonUtilities.config.datatypes.RoleData;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.net.command.*;
import net.minecraft.core.net.packet.BlockUpdatePacket;
import net.minecraft.core.net.packet.ChatPacket;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PlayerActionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.ServerPlayer;
import net.minecraft.server.net.handler.ServerPacketHandler;
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

@Mixin(value = ServerPacketHandler.class, remap = false)
public abstract class ServerPacketHandlerMixin {
	@Shadow
	private ServerPlayer playerEntity;

	@Shadow
	public static Logger LOGGER;

	@Shadow
	private MinecraftServer mcServer;

	@Shadow
	private boolean hasMoved;

	@Shadow
	public abstract void sendPacket(Packet packet);

	@Inject(at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/core/net/ChatEmotes;process(Ljava/lang/String;)Ljava/lang/String;"), method = "handleChat", cancellable = true)
	public void handleChat(ChatPacket packet, CallbackInfo ci, @Local String message) {

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

		LOGGER.info(message);
		this.mcServer.playerList.sendEncryptedChatToAllPlayers(message);
		ci.cancel();

	}

	@Unique
	String commandString = "";

	@Redirect(
		method = "Lnet/minecraft/server/net/handler/ServerPacketHandler;handleSlashCommand(Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(I)Ljava/lang/String;"),
		remap = false
	)
	private String grabCommandString(String s, int beginIndex){
		commandString = s;
		return s.substring(beginIndex, s.length());
	}

	//TODO fix/update helper command checks, old methods below:

	/*@Redirect(
		method = "Lnet/minecraft/server/net/handler/ServerPacketHandler;handleSlashCommand(Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/net/command/PlayerCommandSource;isAdmin()Z"),
		remap = false
	)

	private boolean redirectIsAdmin(PlayerCommandSource source){

		if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(source.getSender().username).toString(), PlayerData.class).isHelper) {

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
		if(source.hasAdmin()){return true;} else{return false;}
	}*/

	/*@Redirect(
		method = "Lnet/minecraft/server/net/handler/ServerPacketHandler;handleSlashCommand(Ljava/lang/String;)V",
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
	}*/

	@Inject(
		at = @At("HEAD"),
		method = "handleBlockDig",
		cancellable = true)
	private void handleBlockDigInject(PlayerActionPacket packet, CallbackInfo ci){
		WorldServer world = this.mcServer.getDimensionWorld(this.playerEntity.dimension);
		BlockEntity container = world.getBlockEntity(packet.xPosition, packet.yPosition, packet.zPosition);
		if(container instanceof BlockEntityContainerInterface) {
			BlockEntityContainerInterface iContainer = (BlockEntityContainerInterface) world.getBlockEntity(packet.xPosition, packet.yPosition, packet.zPosition);
			if (iContainer.getLockOwner() != null
				&& !iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(this.playerEntity.username))
				&& !iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(this.playerEntity.username))
				&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(this.playerEntity.username))
				&& !Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(this.playerEntity.username).toString(), PlayerData.class).lockBypass){
				ci.cancel();
				sendPacket(new BlockUpdatePacket(packet.xPosition, packet.yPosition, packet.zPosition, world));
			}
			if(packet.action == PlayerActionPacket.ACTION_DIG_CONTINUED && Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(this.playerEntity.username).toString(), PlayerData.class).lockOnBlockPunched && !iContainer.getIsLocked()){
				if (container instanceof ChestBlockEntity) {
					BlockEntityContainerInterface iOtherContainer = (BlockEntityContainerInterface) MUtil.getOtherChest(world, (ChestBlockEntity) container);
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
				} else if (container instanceof BlastFurnaceBlockEntity) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Blast Furnace!");
				} else if (container instanceof FurnaceBlockEntity) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Furnace!");
				} else if (container instanceof DispenserBlockEntity) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Dispenser!");
				} else if (container instanceof GoldMeshBlockEntity) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Golden Mesh!");
				} else if (container instanceof TrommelBlockEntity) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Trommel!");
				} else if (container instanceof BasketBlockEntity) {
					this.playerEntity.sendMessage(TextFormatting.LIME + "Locked Basket!");
				}

				iContainer.setIsLocked(true);
				iContainer.setLockOwner(this.playerEntity.username);
				ci.cancel();
			}

			else if (packet.action == PlayerActionPacket.ACTION_DIG_CONTINUED
			&& Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(this.playerEntity.username).toString(), PlayerData.class).lockOnBlockPunched
			&& iContainer.getIsLocked()
			&& !iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(this.playerEntity.username)))
			{
				this.playerEntity.sendMessage(TextFormatting.RED + "Failed to Lock Container! (Not Owned By You)");
				ci.cancel();
			}

			else if (packet.action == PlayerActionPacket.ACTION_DIG_CONTINUED
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
