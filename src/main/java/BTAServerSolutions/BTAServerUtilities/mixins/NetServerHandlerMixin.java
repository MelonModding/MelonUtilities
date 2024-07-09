package BTAServerSolutions.BTAServerUtilities.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import BTAServerSolutions.BTAServerUtilities.utility.RoleBuilder;
import BTAServerSolutions.BTAServerUtilities.config.ConfigManager;
import BTAServerSolutions.BTAServerUtilities.config.RoleData;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetServerHandler;
import org.apache.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(value = NetServerHandler.class, remap = false)
public class NetServerHandlerMixin {
	@Shadow
	private EntityPlayerMP playerEntity;

	@Shadow
	public static Logger logger;

	@Shadow
	private MinecraftServer mcServer;

	@Inject(at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/core/net/ChatEmotes;process(Ljava/lang/String;)Ljava/lang/String;"), method = "handleChat", cancellable = true)
	public void handleChat(Packet3Chat packet, CallbackInfo ci, @Local String message) {

		String defaultRoleDisplay;
		String defaultRoleUsername;
		String defaultRoleTextFormatting;

		if(ConfigManager.roleHashMap.get(ConfigManager.getConfig("config").defaultRole) == null){
			defaultRoleDisplay = null;
			defaultRoleUsername = null;
			defaultRoleTextFormatting = null;
		} else {
			defaultRoleDisplay = RoleBuilder.buildRoleDisplay(ConfigManager.roleHashMap.get(ConfigManager.getConfig("config").defaultRole));
			defaultRoleUsername = RoleBuilder.buildRoleUsername(ConfigManager.roleHashMap.get(ConfigManager.getConfig("config").defaultRole), this.playerEntity.getDisplayName());
			defaultRoleTextFormatting = RoleBuilder.buildRoleTextFormat(ConfigManager.roleHashMap.get(ConfigManager.getConfig("config").defaultRole));
		}

		StringBuilder roleDisplays = new StringBuilder();
		String roleUsername = "" + TextFormatting.RESET + TextFormatting.WHITE + "<" + this.playerEntity.getDisplayName() + TextFormatting.RESET + "> ";
		String roleTextFormatting = "" + TextFormatting.WHITE;

		ArrayList<RoleData> rolesGranted = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			rolesGranted.add(null);
		}

		boolean hasBeenGrantedRole = false;
		for(RoleData role : ConfigManager.roleHashMap.values()){
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
			if (ConfigManager.getConfig("config").displayMode.equals("multi")) {
				if(defaultRoleDisplay != null) {
					message = defaultRoleDisplay + roleDisplays + roleUsername + roleTextFormatting + message;
				} else {
					message = roleDisplays + roleUsername + roleTextFormatting + message;
				}
			} else if (ConfigManager.getConfig("config").displayMode.equals("single")) {
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
}
