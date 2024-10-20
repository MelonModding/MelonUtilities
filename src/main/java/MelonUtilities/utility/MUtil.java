package MelonUtilities.utility;

import net.minecraft.core.HitResult;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.*;

public class MUtil {

	public static HitResult rayCastFromPlayer(CommandSender sender) {
		float f = 1.0f;
		float f1 = sender.getPlayer().xRotO + (sender.getPlayer().xRot - sender.getPlayer().xRotO) * f;
		float f2 = sender.getPlayer().yRotO + (sender.getPlayer().yRot - sender.getPlayer().yRotO) * f;
		double posX = sender.getPlayer().xo + (sender.getPlayer().x - sender.getPlayer().xo) * (double) f;
		float yOff = sender.getPlayer() instanceof EntityPlayerMP ? sender.getPlayer().getHeightOffset() : 0.0f;
		double posY = sender.getPlayer().yo + (sender.getPlayer().y - sender.getPlayer().yo) + (double) yOff;
		double posZ = sender.getPlayer().zo + (sender.getPlayer().z - sender.getPlayer().zo) * (double) f;
		Vec3d vec3d = Vec3d.createVector(posX, posY, posZ);
		float f3 = MathHelper.cos(-f2 * 0.01745329f - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.01745329f - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.01745329f);
		float f6 = MathHelper.sin(-f1 * 0.01745329f);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double reachDistance = sender.getPlayer().getGamemode().getBlockReachDistance();
		Vec3d vec3d1 = vec3d.addVector((double) f7 * reachDistance, (double) f8 * reachDistance, (double) f9 * reachDistance);
		return sender.getWorld().checkBlockCollisionBetweenPoints(vec3d, vec3d1, false);
	}

	public static HashMap<Long, File> sortByKey(HashMap<Long, File> hm) {

		List<Map.Entry<Long, File> > list
			= new LinkedList<>(
			hm.entrySet());

		list.sort(Map.Entry.comparingByKey());
		Collections.reverse(list);

		HashMap<Long, File> result
			= new LinkedHashMap<>();
		for (Map.Entry<Long, File> me : list) {
			result.put(me.getKey(), me.getValue());
		}

		return result;
	}

	public static TileEntityChest getOtherChest(World world, TileEntityChest chest){
		int meta = world.getBlockMetadata(chest.x, chest.y, chest.z);
		BlockChest.Type type = BlockChest.getTypeFromMeta(meta);
		if (type != BlockChest.Type.SINGLE) {
			Direction direction = BlockChest.getDirectionFromMeta(meta);
			int otherChestX = chest.x;
			int otherChestZ = chest.z;
			if (direction == Direction.NORTH) {
				if (type == BlockChest.Type.LEFT) {
					--otherChestX;
				}
				if (type == BlockChest.Type.RIGHT) {
					++otherChestX;
				}
			}
			if (direction == Direction.EAST) {
				if (type == BlockChest.Type.LEFT) {
					--otherChestZ;
				}
				if (type == BlockChest.Type.RIGHT) {
					++otherChestZ;
				}
			}
			if (direction == Direction.SOUTH) {
				if (type == BlockChest.Type.LEFT) {
					++otherChestX;
				}
				if (type == BlockChest.Type.RIGHT) {
					--otherChestX;
				}
			}
			if (direction == Direction.WEST) {
				if (type == BlockChest.Type.LEFT) {
					++otherChestZ;
				}
				if (type == BlockChest.Type.RIGHT) {
					--otherChestZ;
				}
			}
			return (TileEntityChest) world.getBlockTileEntity(otherChestX, chest.y, otherChestZ);
		}
		//return's null if chest is a single chest
		return  null;
	}
}
