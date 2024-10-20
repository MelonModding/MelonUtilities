package MelonUtilities.mixins.other;

import MelonUtilities.utility.RoleBuilder;
import net.minecraft.core.net.packet.Packet138PlayerList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.util.helper.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PlayerList.class, remap = false)
public class PlayerListMixin {

	//TODO Redirect player.getDisplayName(); with a method that returns RoleBuilder.buildPlayerRoleDisplay(player) + player.getDisplayName();

	/*@Inject(at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/server/entity/player/EntityPlayerMP;getDisplayName()Ljava/lang/String;"), method = "updateList", cancellable = true)*/
	/**
	 * @author MelonMojito
	 * @reason adding Roles to PlayerList
	 */
	@Overwrite
	public static void updateList() {
		MinecraftServer server = MinecraftServer.getInstance();
		int playerCount = server.playerList.playerEntities.size();
		String[] players = new String[playerCount];
		String[] scores = new String[playerCount];
		for (int i = 0; i < playerCount; ++i) {
			EntityPlayerMP player = server.playerList.playerEntities.get(i);
			players[i] = RoleBuilder.buildPlayerRoleDisplay(player) + player.getDisplayName();
			scores[i] = String.valueOf(player.getScore());
		}
		server.playerList.sendPacketToAllPlayers(new Packet138PlayerList(playerCount, players, scores));
	}
}
