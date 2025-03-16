package MelonUtilities.utility;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.interfaces.PlayerMagnetInterface;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class MUtilCore {

	public static @Nullable Player closestPlayerToEntity(World world, Entity entity){
		Player closestPlayer = null;
		float closestDistance = Float.MAX_VALUE;
		for(Player player : world.players){
			float distanceTo = player.distanceTo(entity);
			if(distanceTo < closestDistance){
				closestPlayer = player;
				closestDistance = distanceTo;
			}
		}
		return closestPlayer;
	}

	public static @Nullable Player closestPlayerWithMagnetToItem(World world, Entity entity){
		Player closestPlayer = null;
		float closestDistance = Float.MAX_VALUE;
		for(Player player : world.players){
			if(!(PlayerMagnetInterface.class.cast(player).hasMagnet())){
				continue;
			}
			float distanceTo = player.distanceTo(entity);
			if(distanceTo < closestDistance){
				closestPlayer = player;
				closestDistance = distanceTo;
			}
		}
		return closestPlayer;
	}

	public static String hmsConversion(long systemTimeMillis) {

		Duration duration = Duration.ofMillis(systemTimeMillis);

		long h = duration.toHours();
		long m = duration.toMinutes() % 60;
		long s = duration.getSeconds() % 60;

		return String.format("%02d:%02d:%02d [h:m:s]", h, m, s);
	}

	//TODO simplify
	public static String formatHexString(String dirtyHex){
		StringBuilder output = new StringBuilder();
		output.append("§<");
		char[] charArray = dirtyHex.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (i < 6 && Character.isDigit(c)) {
				output.append(c);
			} else {
				break;
			}
		}
		output.append(">");
		return output.toString();
	}

	// returns true if we teleported
	public static boolean jumpOnElevator(World world, int x, int y, int z, Player player){
		for(int y2 = y+1; y2 < 255; y2++){
			if(world.getBlock(x, y2, z) == Blocks.BLOCK_STEEL && !Blocks.solid[world.getBlockId(x, y2+1, z)] && !Blocks.solid[world.getBlockId(x, y2+2, z)]){
				teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !Data.MainConfig.config.allowObstructions) {
				break;
			}
		}
		return false;
	}

	// returns true if we teleported
	public static boolean sneakOnElevator(World world, int x, int y, int z, Player player){
		for(int y2 = y-1; y2 > 0; y2--){
			if(world.getBlock(x, y2, z) == Blocks.BLOCK_STEEL && !Blocks.solid[world.getBlockId(x, y2+1, z)] && !Blocks.solid[world.getBlockId(x, y2+2, z)]){
				teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !Data.MainConfig.config.allowObstructions) {
				break;
			}
		}
		return false;
	}

	public static void teleport(double x, double y, double z, @NotNull Player player, @NotNull Dimension dimension){

		assert player.world != null;
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
		if (MelonUtilities.isServer){
			if(player.dimension != dimension.id){
				MinecraftServer mc = MinecraftServer.getInstance();
				mc.playerList.sendPlayerToOtherDimension((PlayerServer) player, dimension.id, DyeColor.WHITE, false);
			}
			((PlayerServer) player).playerNetServerHandler.teleport(x, y + 0.2, z);
		} else {
			if(player.dimension != dimension.id){
				MUtilClient.teleportToDimension(dimension);
			}
			player.setPos(x, y + player.bbHeight + 0.2, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);

	}

	public static void teleport(double x, double y, double z, @NotNull Player player){
		assert player.world != null;
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
		if (MelonUtilities.isServer){
			((PlayerServer) player).playerNetServerHandler.teleport(x, y + 0.2, z);
		} else {
			player.setPos(x, y + player.bbHeight + 0.2, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
	}
}
