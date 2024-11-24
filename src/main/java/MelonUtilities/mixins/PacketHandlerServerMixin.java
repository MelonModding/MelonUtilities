package MelonUtilities.mixins;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import com.llamalad7.mixinextras.sugar.Local;
import MelonUtilities.utility.builders.RoleBuilder;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.*;
import net.minecraft.core.net.packet.PacketBlockUpdate;
import net.minecraft.core.net.packet.PacketChat;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketPlayerAction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import net.minecraft.server.world.WorldServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(value = PacketHandlerServer.class, remap = false)
public abstract class PacketHandlerServerMixin {
	@Shadow
	private PlayerServer playerEntity;

	@Shadow
	private MinecraftServer mcServer;

	@Shadow
	private boolean hasMoved;

	@Shadow
	public abstract void sendPacket(Packet packet);

	@Shadow
	public static Logger LOGGER;

	@Inject(at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/core/net/ChatEmotes;process(Ljava/lang/String;)Ljava/lang/String;"), method = "handleChat", cancellable = true)
	public void handleChat(PacketChat packet, CallbackInfo ci, @Local String message) {

		String defaultRoleDisplay;
		String defaultRoleUsername;
		String defaultRoleTextFormatting;

		if(Data.Roles.roleDataHashMap.get(Data.MainConfig.config.defaultRole) == null){
			defaultRoleDisplay = null;
			defaultRoleUsername = null;
			defaultRoleTextFormatting = null;
		} else {
			defaultRoleDisplay = RoleBuilder.buildRoleDisplay(Data.Roles.roleDataHashMap.get(Data.MainConfig.config.defaultRole));
			defaultRoleUsername = RoleBuilder.buildRoleUsername(Data.Roles.roleDataHashMap.get(Data.MainConfig.config.defaultRole), this.playerEntity.getDisplayName());
			defaultRoleTextFormatting = RoleBuilder.buildRoleTextFormat(Data.Roles.roleDataHashMap.get(Data.MainConfig.config.defaultRole));
		}

		StringBuilder roleDisplays = new StringBuilder();
		String roleUsername = "" + TextFormatting.RESET + TextFormatting.WHITE + "<" + this.playerEntity.getDisplayName() + TextFormatting.RESET + "> ";
		String roleTextFormatting = "" + TextFormatting.WHITE;

		ArrayList<Role> rolesGranted = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			rolesGranted.add(null);
		}

		boolean hasBeenGrantedRole = false;
		for(Role role : Data.Roles.roleDataHashMap.values()){
			if(role.playersGrantedRole.contains(this.playerEntity.uuid)){
				rolesGranted.add(role.priority, role);
				hasBeenGrantedRole = true;
			}
		}

		String highestPriorityRoleDisplay = "";
		int tempPriority = Integer.MAX_VALUE;
		for(Role role : rolesGranted){
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
			if (Data.MainConfig.config.displayMode.equals("multi")) {
				if(defaultRoleDisplay != null) {
					message = defaultRoleDisplay + roleDisplays + roleUsername + roleTextFormatting + message;
				} else {
					message = roleDisplays + roleUsername + roleTextFormatting + message;
				}
			} else if (Data.MainConfig.config.displayMode.equals("single")) {
                message = highestPriorityRoleDisplay + roleUsername + roleTextFormatting + message;
			}
		} else if(defaultRoleDisplay != null){
			message = defaultRoleDisplay + defaultRoleUsername + defaultRoleTextFormatting + message;
		} else {
			message = roleUsername + message;
		}

		MelonUtilities.LOGGER.info(message);
		this.mcServer.playerList.sendEncryptedChatToAllPlayers(message);
		ci.cancel();

	}

	@Unique
	String commandString = "";

	@Redirect(
		method = "handleSlashCommand(Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(I)Ljava/lang/String;"),
		remap = false
	)
	private String grabCommandString(String s, int beginIndex){
		commandString = s;
		return s.substring(beginIndex, s.length());
	}

	//TODO fix/update helper command checks, old methods below:

	/*@Redirect(
		method = "Lnet/minecraft/server/net/handler/PacketHandlerServer;handleSlashCommand(Ljava/lang/String;)V",
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
		method = "Lnet/minecraft/server/net/handler/PacketHandlerServer;handleSlashCommand(Ljava/lang/String;)V",
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
	private void handleBlockDigInject(PacketPlayerAction packet, CallbackInfo ci){
		Player player = this.playerEntity;
		WorldServer world = this.mcServer.getDimensionWorld(player.dimension);
		TileEntity container = world.getBlockEntity(packet.xPosition, packet.yPosition, packet.zPosition);
		if(container instanceof TileEntityContainerInterface) {
			TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockEntity(packet.xPosition, packet.yPosition, packet.zPosition);
			if (iContainer.getLockOwner() != null
				&& !iContainer.getLockOwner().equals(player.uuid)
				&& !iContainer.getTrustedPlayers().contains(player.uuid)
				&& !Data.Users.getOrCreate(iContainer.getLockOwner()).usersTrustedToAllContainers.containsKey(player.uuid)
				&& !Data.Users.getOrCreate(player.uuid).lockBypass){
				ci.cancel();
				sendPacket(new PacketBlockUpdate(packet.xPosition, packet.yPosition, packet.zPosition, world));
			}
			if(packet.action == PacketPlayerAction.ACTION_DIG_CONTINUED && Data.Users.getOrCreate(player.uuid).lockOnBlockPunched && !iContainer.getIsLocked()){
				if (container instanceof TileEntityChest) {
					TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) MUtil.getOtherChest(world, (TileEntityChest) container);
					if (iOtherContainer != null) {
						iContainer.setIsLocked(true);
						iOtherContainer.setIsLocked(true);
						iContainer.setLockOwner(player.uuid);
						iOtherContainer.setLockOwner(player.uuid);
						FeedbackHandler.success(player, "Locked Double Chest!");
						ci.cancel();
					} else {
						FeedbackHandler.success(player, "Locked Chest!");
					}
				} else if (container instanceof TileEntityFurnaceBlast) {
					FeedbackHandler.success(player, "Locked Blast Furnace!");
				} else if (container instanceof TileEntityFurnace) {
					FeedbackHandler.success(player, "Locked Furnace!");
				} else if (container instanceof TileEntityDispenser) {
					FeedbackHandler.success(player, "Locked Dispenser!");
				} else if (container instanceof TileEntityMeshGold) {
					FeedbackHandler.success(player, "Locked Golden Mesh!");
				} else if (container instanceof TileEntityTrommel) {
					FeedbackHandler.success(player, "Locked Trommel!");
				} else if (container instanceof TileEntityBasket) {
					FeedbackHandler.success(player, "Locked Basket!");
				}

				iContainer.setIsLocked(true);
				iContainer.setLockOwner(player.uuid);
				ci.cancel();
			}

			else if (packet.action == PacketPlayerAction.ACTION_DIG_CONTINUED
			&& Data.Users.getOrCreate(player.uuid).lockOnBlockPunched
			&& iContainer.getIsLocked()
			&& !iContainer.getLockOwner().equals(player.uuid))
			{
				FeedbackHandler.error(player, "Failed to Lock Container! (Not Owned By You)");
				ci.cancel();
			}

			else if (packet.action == PacketPlayerAction.ACTION_DIG_CONTINUED
			&& Data.Users.getOrCreate(player.uuid).lockOnBlockPunched
			&& iContainer.getIsLocked()
			&& iContainer.getLockOwner().equals(player.uuid))
			{
				FeedbackHandler.error(player, "Failed to Lock Container! (Already Locked)");
				ci.cancel();
			}
		}
	}
}
