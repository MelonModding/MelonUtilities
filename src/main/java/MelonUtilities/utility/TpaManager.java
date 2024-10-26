package MelonUtilities.utility;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.ChatPacket;
import net.minecraft.core.util.helper.AES;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.ServerPlayer;

import java.util.HashMap;

public class TpaManager {
    public static HashMap<ServerPlayer, TpaRequest> tpas = new HashMap<>();
	public static int timeout = 15;

	public static class TpaRequest {
        public ServerPlayer player, target;
        public int time = 0;

        public TpaRequest(ServerPlayer player, ServerPlayer target) {
            this.player = player;
            this.target = target;
        }
    }

	static void messagePlayer(ServerPlayer player, String message) {
		MinecraftServer.getInstance().playerList.sendPacketToPlayer(player.username, new ChatPacket(message, AES.keyChain.get(player.username)));
	}
    static void killRequest(TpaRequest tpr, String reason) {
    	messagePlayer(tpr.player, TextFormatting.RED + "> Your tpa request to " + tpr.target.username + " expired! (" + reason + ")");
    	messagePlayer(tpr.target, TextFormatting.RED + "> Your tpa request from " + tpr.player.username + " expired! (" + reason + ")");
    }

	static public void addRequest(ServerPlayer player, ServerPlayer target, boolean reverse) {
        TpaRequest tpr = reverse ? tpas.put(target, new TpaRequest(target, player)) : tpas.put(target, new TpaRequest(player, target));
        if (tpr != null) {
            if (tpr.target == target) return;
            killRequest(tpr, "new request");
		}
		if (!reverse) {
			messagePlayer(target, TextFormatting.ORANGE + "> " + player.username + " would like to teleport to you!");
			messagePlayer(target, TextFormatting.ORANGE + "> Use /tpaccept or /tpadeny");
			messagePlayer(player, TextFormatting.ORANGE + "> You requested to teleport to " + target.username);
		} else {
			messagePlayer(target, TextFormatting.ORANGE + "> " + player.username + " would like you teleport to them!");
			messagePlayer(target, TextFormatting.ORANGE + "> Use /tpaccept or /tpadeny");
			messagePlayer(player, TextFormatting.ORANGE + "> You requested for " + target.username + " to teleport to you");
		}
	}

	static public boolean deny(ServerPlayer player) {
		TpaRequest tpr = tpas.remove(player);
		return tpr != null;
	}

	static public boolean accept(ServerPlayer player) {
		TpaRequest tpr = tpas.remove(player);
		if (tpr == null) return false;
		ServerPlayer target = tpr.target;
		ServerPlayer moved = tpr.player;
		if (target.dimension != moved.dimension) {
			MinecraftServer.getInstance().playerList.sendPlayerToOtherDimension(moved, target.dimension, false);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 100f);
		moved.playerNetServerHandler.teleportAndRotate(target.x, target.y, target.z, target.yRot, target.xRot);
		messagePlayer(player, TextFormatting.ORANGE + "☁ Whoosh! ☁");
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 100f);
		return true;
	}

	static public void tick() {
	    tpas.forEach((player, tpr) -> {
    	    if (tpr.time > timeout * 20) {
                killRequest(tpr, "timeout");
            }
			tpr.time++;
        });
		HashMap<ServerPlayer, TpaRequest> newTpas = new HashMap<>();
		tpas.entrySet()
			.stream()
			.filter(x->x.getValue().time < timeout * 20)
			.forEach(entry -> newTpas.put(entry.getKey(), entry.getValue()));
		tpas = newTpas;
	}
}
