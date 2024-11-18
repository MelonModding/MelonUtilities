package MelonUtilities.utility;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.utility.managers.RollbackManager;
import com.b100.json.JsonParser;
import com.b100.json.element.JsonObject;
import com.b100.utils.StringUtils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.PacketBlockRegionUpdate;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static MelonUtilities.utility.managers.RollbackManager.*;
import static MelonUtilities.utility.managers.RollbackManager.rollbackChunkFromBackup;

public class MUtil {

	public static float timeOnInit = 0;
	public static final String SECTION_PURPLE = "§a";
	public static final String SECTION_BLUE = "§b";
	public static final String SECTION_BROWN = "§c";
	public static final String SECTION_GREEN = "§d";
	public static final String SECTION_RED = "§e";
	public static final String SECTION_BLACK = "§f";
	public static final String SECTION_ORANGE = "§1";
	public static final String SECTION_MAGENTA = "§2";
	public static final String SECTION_LIGHT_BLUE = "§3";
	public static final String SECTION_YELLOW = "§4";
	public static final String SECTION_LIME = "§5";
	public static final String SECTION_PINK = "§6";
	public static final String SECTION_GREY = "§7";
	public static final String SECTION_GRAY = "§7";
	public static final String SECTION_LIGHT_GREY = "§8";
	public static final String SECTION_LIGHT_GRAY = "§8";
	public static final String SECTION_CYAN = "§9";
	public static final String SECTION_WHITE = "§0";

	public static final HashMap<String, String> colorSectionMap = new HashMap<>();
	static{
		colorSectionMap.put("purple", "§a");
		colorSectionMap.put("blue", "§b");
		colorSectionMap.put("brown", "§c");
		colorSectionMap.put("green", "§d");
		colorSectionMap.put("red", "§e");
		colorSectionMap.put("black", "§f");
		colorSectionMap.put("orange", "§1");
		colorSectionMap.put("magenta", "§2");
		colorSectionMap.put("light_blue", "§3");
		colorSectionMap.put("yellow", "§4");
		colorSectionMap.put("lime", "§5");
		colorSectionMap.put("pink", "§6");
		colorSectionMap.put("grey", "§7");
		colorSectionMap.put("gray", "§7");
		colorSectionMap.put("light_grey", "§8");
		colorSectionMap.put("light_gray", "§8");
		colorSectionMap.put("cyan", "§9");
		colorSectionMap.put("white", "§0");
	}

	public static final HashMap<String, TextFormatting> colorFormattingMap = new HashMap<>();
	static{
		colorFormattingMap.put("purple", TextFormatting.PURPLE);
		colorFormattingMap.put("blue", TextFormatting.BLUE);
		colorFormattingMap.put("brown", TextFormatting.BROWN);
		colorFormattingMap.put("green", TextFormatting.GREEN);
		colorFormattingMap.put("red", TextFormatting.RED);
		colorFormattingMap.put("black", TextFormatting.BLACK);
		colorFormattingMap.put("orange", TextFormatting.ORANGE);
		colorFormattingMap.put("magenta", TextFormatting.MAGENTA);
		colorFormattingMap.put("light_blue", TextFormatting.LIGHT_BLUE);
		colorFormattingMap.put("yellow", TextFormatting.YELLOW);
		colorFormattingMap.put("lime", TextFormatting.LIME);
		colorFormattingMap.put("pink", TextFormatting.PINK);
		colorFormattingMap.put("grey", TextFormatting.GRAY);
		colorFormattingMap.put("gray", TextFormatting.GRAY);
		colorFormattingMap.put("light_grey", TextFormatting.LIGHT_GRAY);
		colorFormattingMap.put("light_gray", TextFormatting.LIGHT_GRAY);
		colorFormattingMap.put("cyan", TextFormatting.CYAN);
		colorFormattingMap.put("white", TextFormatting.WHITE);
	}

	public static String breakDownHex(String dirtyHex){
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < dirtyHex.length(); i++){
			char c = dirtyHex.charAt(i);
			if(Character.isDigit(c) || Character.isLetter(c)){
				hex.append(c);
			}
		}
		return String.valueOf(hex);
	}

	public static boolean canInteractWithLock(boolean isLocked, boolean isCommunityContainer, UUID lockOwner, List<UUID> trustedPlayers, Player player){
		if(!isLocked) {
			return true;
		}
		if(lockOwner == null) {
			return true;
		}

		return lockOwner.equals(player.uuid)
			|| trustedPlayers.contains(player.uuid)
			|| Data.Users.getOrCreate(lockOwner).usersTrustedToAllContainers.containsKey(player.uuid)
			|| isCommunityContainer
			|| Data.Users.getOrCreate(player.uuid).lockBypass;
	}

	private static final String url = "https://sessionserver.mojang.com/session/minecraft/profile/";
	private static final JsonParser jsonParser = new JsonParser();
	private static final Map<UUID, String> UUIDtoNameMap = new HashMap<>();

	public static @Nullable String getNameFromUUID(UUID uuid){
		if(UUIDtoNameMap.containsKey(uuid)){
			return UUIDtoNameMap.get(uuid);
		}

		String string;
		try{
			string = StringUtils.getWebsiteContentAsString(url + uuid);
		}catch (Exception e) {
			System.err.println("Can't connect to Mojang API.");
			e.printStackTrace();
			return null;
		}
		if(string.isEmpty()) {
			System.err.println("UUID [" + uuid + "] doesn't exist!");
			return null;
		}
		String username;
		try {
			JsonObject contentParsed = jsonParser.parse(string);
			username = contentParsed.getString("name");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (uuid == null) return null;

		UUIDtoNameMap.put(uuid, username);
		return username;
	}

	public static HitResult rayCastFromPlayer(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		float f = 1.0f;
		float f1 = sender.xRotO + (sender.xRot - sender.xRotO) * f;
		float f2 = sender.yRotO + (sender.yRot - sender.yRotO) * f;
		double posX = sender.xo + (sender.x - sender.xo) * (double) f;
		float yOff = sender instanceof PlayerServer ? sender.getHeightOffset() : 0.0f;
		double posY = sender.yo + (sender.y - sender.yo) + (double) yOff;
		double posZ = sender.zo + (sender.z - sender.zo) * (double) f;
		Vec3 vec3 = Vec3.getTempVec3(posX, posY, posZ);
		float f3 = MathHelper.cos(-f2 * 0.01745329f - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.01745329f - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.01745329f);
		float f6 = MathHelper.sin(-f1 * 0.01745329f);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double reachDistance = sender.getGamemode().getBlockReachDistance();
		Vec3 vec3_1 = vec3.add((double) f7 * reachDistance, (double) f8 * reachDistance, (double) f9 * reachDistance);
		return source.getWorld().checkBlockCollisionBetweenPoints(vec3, vec3_1, false);
	}

	public static File getChunkFileFromCoords(CommandSource source, int x, int z){
		return new File(RollbackManager.snapshotsDir, source.getWorld().dimension.id + "/c[x." + x + "-z." + z + "]");
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
		BlockLogicChest.Type type = BlockLogicChest.getTypeFromMeta(meta);
		if (type != BlockLogicChest.Type.SINGLE) {
			Direction direction = BlockLogicChest.getDirectionFromMeta(meta);
			int otherChestX = chest.x;
			int otherChestZ = chest.z;
			if (direction == Direction.NORTH) {
				if (type == BlockLogicChest.Type.LEFT) {
					--otherChestX;
				}
				if (type == BlockLogicChest.Type.RIGHT) {
					++otherChestX;
				}
			}
			if (direction == Direction.EAST) {
				if (type == BlockLogicChest.Type.LEFT) {
					--otherChestZ;
				}
				if (type == BlockLogicChest.Type.RIGHT) {
					++otherChestZ;
				}
			}
			if (direction == Direction.SOUTH) {
				if (type == BlockLogicChest.Type.LEFT) {
					++otherChestX;
				}
				if (type == BlockLogicChest.Type.RIGHT) {
					--otherChestX;
				}
			}
			if (direction == Direction.WEST) {
				if (type == BlockLogicChest.Type.LEFT) {
					++otherChestZ;
				}
				if (type == BlockLogicChest.Type.RIGHT) {
					--otherChestZ;
				}
			}
			return (TileEntityChest) world.getBlockEntity(otherChestX, chest.y, otherChestZ);
		}
		//return's null if chest is a single chest
		return  null;
	}

	public static int[] parseCoordsFromChunkDir(File chunkDir) {
		int xIndex = chunkDir.getName().indexOf('x');
		int zIndex = chunkDir.getName().indexOf('z');

		StringBuilder xStringBuilder = new StringBuilder();
		StringBuilder zStringBuilder = new StringBuilder();

		String s = chunkDir.getName();

		boolean firstMinus = true;
		for (int i = xIndex + 2; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c) || (c == '-' && firstMinus)) {
				xStringBuilder.append(c);
				firstMinus = false;
			} else {
				break;
			}
		}

		firstMinus = true;
		for (int i = zIndex + 2; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c) || (c == '-' && firstMinus)) {
				zStringBuilder.append(c);
				firstMinus = false;
			} else {
				break;
			}
		}

		String xString = xStringBuilder.toString();
		String zString = zStringBuilder.toString();

		int x = Integer.parseInt(xString);
		int z = Integer.parseInt(zString);
		return new int[]{x, z};
	}

	public static HashMap<Long, File> getSortedCaptures(CommandSource source, File chunkDir){
		int[] chunkCoords = parseCoordsFromChunkDir(chunkDir);

		HashMap<Long, File> capturesHashmap = new HashMap<>();

		//Add Snapshots To Hashmap
		File[] snapshots = chunkDir.listFiles();
		if(snapshots != null) {
			for (File snapshot : snapshots) {
				if (snapshot.isFile()) {
					capturesHashmap.putIfAbsent(Long.parseLong(snapshot.getName().split(" ")[0]), snapshot);
				}
			}
		}

		//Add Backups To Hashmap
		File[] backups = backupsDir.listFiles();
		if(backups != null){
			for (File backup : backups) {
				if (backup.isDirectory()) {
					capturesHashmap.putIfAbsent(Long.parseLong(backup.getName().split(" ")[0]), getRegionFileFromCoords(new File(backup.getPath(), String.valueOf(source.getWorld().dimension.id)), chunkCoords[0], chunkCoords[1]));
				}
			}
		}

		//Return Sorted Hashmap of all Captures (Both Backups and Snapshots)
		return MUtil.sortByKey(capturesHashmap);
	}

	public HashMap<Long, File> getSortedBackups(CommandSource source, File chunkDir){
		int[] chunkCoords = parseCoordsFromChunkDir(chunkDir);

		HashMap<Long, File> backupsHashmap = new HashMap<>();

		//Add Backups To Hashmap
		File[] backups = backupsDir.listFiles();
		if(backups != null){
			for (File backup : backups) {
				if (backup.isDirectory()) {
					backupsHashmap.putIfAbsent(Long.parseLong(backup.getName().split(" ")[0]), getRegionFileFromCoords(new File(backup.getPath(), String.valueOf(source.getWorld().dimension.id)), chunkCoords[0], chunkCoords[1]));
				}
			}
		}

		return MUtil.sortByKey(backupsHashmap);
	}

	public HashMap<Long, File> getSortedSnapshots(File chunkDir){

		HashMap<Long, File> snapshotsHashmap = new HashMap<>();

		File[] snapshots = chunkDir.listFiles();
		if(snapshots != null) {
			for (File snapshot : snapshots) {
				if (snapshot.isFile()) {
					snapshotsHashmap.putIfAbsent(Long.parseLong(snapshot.getName().split(" ")[0]), snapshot);
				}
			}
		}

		return MUtil.sortByKey(snapshotsHashmap);
	}

	public static void rollbackChunkArea(CommandSource source, List<File> chunkGrid, Map.Entry<Long, File> primaryCapture){
		for(File chunkDir : chunkGrid) {
			int[] chunkCoords = parseCoordsFromChunkDir(chunkDir);

			HashMap<Long, File> captures = getSortedCaptures(source, chunkDir);


			long timeOfPrimaryCapture = primaryCapture.getKey();
			long lowestDifference = Long.MAX_VALUE;
			Map.Entry<Long, File> oldestClosestCapture = null;

			for(Map.Entry<Long, File> capture : captures.entrySet()){
				long timeOfCapture = capture.getKey();
				if(timeOfCapture <= timeOfPrimaryCapture){
					long difference = Math.abs(timeOfPrimaryCapture - timeOfCapture);
					if(difference < lowestDifference){
						lowestDifference = difference;
						oldestClosestCapture = capture;
					}

				}
			}

			if(oldestClosestCapture == null) {
				((PlayerServer) source.getSender()).usePersonalCraftingInventory();
				return;
			}

			if (oldestClosestCapture.getValue().getName().contains(".dat")) {
				for (Entity entity : source.getWorld().loadedEntityList) {
					if (entity.chunkCoordX == chunkCoords[0] && entity.chunkCoordZ == chunkCoords[1]) {
						if (!(entity instanceof Player)) {
							entity.remove();
						}
					}
				}
				try {
					Chunk chunk = source.getWorld().getChunkFromChunkCoords(chunkCoords[0], chunkCoords[1]);
					CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(oldestClosestCapture.getValue().toPath()));
					rollbackChunk(chunk, tag);
					MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new PacketBlockRegionUpdate(chunkCoords[0] * 16, 0, chunkCoords[1] * 16, 16, 256, 16, source.getWorld()), source.getWorld().dimension.id);
				} catch (IOException e) {
					MelonUtilities.LOGGER.error("IOException occurred trying to read compressed data from Chunk File: {}", oldestClosestCapture.getValue());
				}
			}

			if (oldestClosestCapture.getValue().getName().contains(".mcr")) {
				for (Entity entity : source.getWorld().loadedEntityList) {
					if (entity.chunkCoordX == chunkCoords[0] && entity.chunkCoordZ == chunkCoords[1]) {
						if (!(entity instanceof Player)) {
							entity.remove();
						}
					}
				}
				File backupDir = oldestClosestCapture.getValue().getParentFile().getParentFile().getParentFile();
				rollbackChunkFromBackup(source.getWorld().getChunkFromChunkCoords(chunkCoords[0], chunkCoords[1]), backupDir);
				MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new PacketBlockRegionUpdate(chunkCoords[0] * 16, 0, chunkCoords[1] * 16, 16, 256, 16, source.getWorld()), source.getWorld().dimension.id);
			}
		}
	}
}
