package MelonUtilities.utility;

import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;

import java.io.File;
import java.util.*;

public class MelonUtility {

	public static float timeOnInit = 0;

	public static HitResult rayCastFromPlayer(CommandSource source) {
		float f = 1.0f;
		float f1 = source.getSender().xRotO + (source.getSender().xRot - source.getSender().xRotO) * f;
		float f2 = source.getSender().yRotO + (source.getSender().yRot - source.getSender().yRotO) * f;
		double posX = source.getSender().xo + (source.getSender().x - source.getSender().xo) * (double) f;
		float yOff = source.getSender() instanceof PlayerServer ? source.getSender().getHeightOffset() : 0.0f;
		double posY = source.getSender().yo + (source.getSender().y - source.getSender().yo) + (double) yOff;
		double posZ = source.getSender().zo + (source.getSender().z - source.getSender().zo) * (double) f;
		Vec3 vec3 = Vec3.getTempVec3(posX, posY, posZ);
		float f3 = MathHelper.cos(-f2 * 0.01745329f - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.01745329f - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.01745329f);
		float f6 = MathHelper.sin(-f1 * 0.01745329f);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double reachDistance = source.getSender().getGamemode().getBlockReachDistance();
		Vec3 vec3_1 = vec3.add((double) f7 * reachDistance, (double) f8 * reachDistance, (double) f9 * reachDistance);
		return source.getWorld().checkBlockCollisionBetweenPoints(vec3, vec3_1, false);
	}

	public static File getChunkFileFromCoords(CommandSource source, int x, int z){
		return new File(RollbackManager.snapshotsDir, source.getWorld().dimension.id + "/c[x." + x + "-z." + z + "]");
	}

	public static final HashMap<String, String> colorToSectionMap = new HashMap<>();
	static{
		colorToSectionMap.put("purple", "§a");
		colorToSectionMap.put("blue", "§b");
		colorToSectionMap.put("brown", "§c");
		colorToSectionMap.put("green", "§d");
		colorToSectionMap.put("red", "§e");
		colorToSectionMap.put("black", "§f");
		colorToSectionMap.put("orange", "§1");
		colorToSectionMap.put("magenta", "§2");
		colorToSectionMap.put("light_blue", "§3");
		colorToSectionMap.put("yellow", "§4");
		colorToSectionMap.put("lime", "§5");
		colorToSectionMap.put("pink", "§6");
		colorToSectionMap.put("grey", "§7");
		colorToSectionMap.put("gray", "§7");
		colorToSectionMap.put("light_grey", "§8");
		colorToSectionMap.put("light_gray", "§8");
		colorToSectionMap.put("cyan", "§9");
		colorToSectionMap.put("white", "§0");
	}

	public static final HashMap<String, TextFormatting> colorToFormattingMap = new HashMap<>();
	static{
		colorToFormattingMap.put("purple", TextFormatting.PURPLE);
		colorToFormattingMap.put("blue", TextFormatting.BLUE);
		colorToFormattingMap.put("brown", TextFormatting.BROWN);
		colorToFormattingMap.put("green", TextFormatting.GREEN);
		colorToFormattingMap.put("red", TextFormatting.RED);
		colorToFormattingMap.put("black", TextFormatting.BLACK);
		colorToFormattingMap.put("orange", TextFormatting.ORANGE);
		colorToFormattingMap.put("magenta", TextFormatting.MAGENTA);
		colorToFormattingMap.put("light_blue", TextFormatting.LIGHT_BLUE);
		colorToFormattingMap.put("yellow", TextFormatting.YELLOW);
		colorToFormattingMap.put("lime", TextFormatting.LIME);
		colorToFormattingMap.put("pink", TextFormatting.PINK);
		colorToFormattingMap.put("grey", TextFormatting.GRAY);
		colorToFormattingMap.put("gray", TextFormatting.GRAY);
		colorToFormattingMap.put("light_grey", TextFormatting.LIGHT_GRAY);
		colorToFormattingMap.put("light_gray", TextFormatting.LIGHT_GRAY);
		colorToFormattingMap.put("cyan", TextFormatting.CYAN);
		colorToFormattingMap.put("white", TextFormatting.WHITE);
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static List<File> getChunkGridFromCorners(CommandSource source, int x1, int z1, int x2, int z2){
		int temp;
		if (x1 > x2) {
			temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (z1 > z2) {
			temp = z1;
			z1 = z2;
			z2 = temp;
		}

		List<File> chunksInArea = new ArrayList<>();
		for (int x = x1; x <= x2; ++x) {
			for (int z = z1; z <= z2; ++z) {
				chunksInArea.add(getChunkFileFromCoords(source, x, z));
			}
		}
		return chunksInArea;
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
			return (TileEntityChest) world.getBlockEntity(otherChestX, chest.y, otherChestZ);
		}
		//return's null if chest is a single chest
		return  null;
	}
}
