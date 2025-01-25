package MelonUtilities.utility;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.RollbackManager;
import com.b100.json.JsonParser;
import com.b100.json.element.JsonObject;
import com.b100.utils.StringUtils;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.Duration;
import java.util.*;

public class MUtil {

	public static float timeOnInit = 0;

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

	public static Pair<UUID, String> getProfileFromUsername(String username) throws NullPointerException {
		UUID uuid;
		String usernameOrDisplayName;

		Player target = MinecraftServer.getInstance().playerList.getPlayerEntity(username);

		if(target != null){
			uuid = target.uuid;
			usernameOrDisplayName = target.getDisplayName();
		} else {
			uuid = UUIDHelper.getUUIDFromName(username);
			if(uuid == null){
				throw new NullPointerException();
			}
			usernameOrDisplayName = username;
		}
		return Pair.of(uuid, usernameOrDisplayName);
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

	private static final String url = "https://sessionserver.mojang.com/session/minecraft/profile/";
	private static final JsonParser jsonParser = new JsonParser();
	private static final Map<UUID, String> UUIDtoNameMap = new HashMap<>();

	public static void runUUIDConversionAction(UUID uuid, @Nullable UsernameFunction successAction, @Nullable UUIDFunction failAction) {
		if (UUIDtoNameMap.containsKey(uuid)) {
			String username = UUIDtoNameMap.get(uuid);
			if (username != null) {
				if (successAction != null) {
					successAction.run(username);
				}
			} else {
				if (failAction != null) {
					failAction.run(uuid);
				}
			}
			return;
		}
		new Thread(() -> {
			try {
				String username = getNameFromUUID(uuid);
				if (username != null) {
					if (successAction != null) {
						successAction.run(username);
					}
					UUIDtoNameMap.put(uuid, username);
				} else {
					if (failAction != null) {
						failAction.run(uuid);
					}
					UUIDtoNameMap.put(uuid, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (failAction != null) {
					failAction.run(uuid);
				}
			}
		}).start();
	}

	public interface UsernameFunction {
		void run(String username);
	}

	public interface UUIDFunction {
		void run(UUID uuid);
	}

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

	public static HitResult rayCastFromPlayer(PlayerServer sender) {
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
		return sender.world.checkBlockCollisionBetweenPoints(vec3, vec3_1, false);
	}

	public static File getChunkFileFromCoords(World world, int x, int z){
		return new File(RollbackManager.snapshotsDir, world.dimension.id + "/c[x." + x + "-z." + z + "]");
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

	public static List<File> getChunkGridFromCorners(World world, int x1, int z1, int x2, int z2){
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
				chunksInArea.add(getChunkFileFromCoords(world, x, z));
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

	public static void sendContainerLockInfo(PlayerServer player, Lockable lockable, String containerName) {
		if (!lockable.getIsLocked()) {
			player.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + containerName + ": " + TextFormatting.RED + "Not Locked " + TextFormatting.GRAY + ">");
			FeedbackHandlerServer.playFeedbackSound(player, FeedbackType.error);
			return;
		}

		new Thread(() -> {
			String owner = getNameFromUUID(lockable.getLockOwner());
			Map<String, Boolean> trustedPlayers = new HashMap<>();
			for(Map.Entry<UUID, Boolean> entry : lockable.getAllTrustedPlayers().entrySet()){
				trustedPlayers.put(getNameFromUUID(entry.getKey()), entry.getValue());
			}
			containerLockInfoLogic(player, lockable, containerName, owner, trustedPlayers);
		}).start();
	}

	private static void containerLockInfoLogic(PlayerServer player, Lockable lockable, String containerName, String owner, Map<String, Boolean> trustedPlayers){
		player.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + containerName + ": " + TextFormatting.GRAY + ">" + TextFormatting.ORANGE + " * " + TextFormatting.GRAY + "=" + TextFormatting.LIGHT_GRAY + " In " + owner + "'s TrustAll List");
		player.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + "Owner: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + owner + TextFormatting.GRAY + "]");
		player.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + "Community Container: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + lockable.getIsCommunityContainer() + TextFormatting.GRAY + "]");
		player.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + "Trusted Players: ");
		for(Map.Entry<String, Boolean> entry : trustedPlayers.entrySet()){
			if(entry.getValue()){
				player.sendMessage(TextFormatting.GRAY + "    > [" + TextFormatting.LIGHT_GRAY + entry.getKey() + TextFormatting.GRAY + "]" + TextFormatting.ORANGE + "*");
			} else {
				player.sendMessage(TextFormatting.GRAY + "    > [" + TextFormatting.LIGHT_GRAY + entry.getKey() + TextFormatting.GRAY + "]");
			}
		}
		player.sendMessage(TextFormatting.GRAY + "<>");
		FeedbackHandlerServer.playFeedbackSound(player, FeedbackType.destructive);
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
			return (TileEntityChest) world.getTileEntity(otherChestX, chest.y, otherChestZ);
		}
		//return's null if chest is a single chest
		return  null;
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

	public static void teleport(double x, double y, double z, Player player){
		if (player.world.isClientSide)
			return;
		if (player instanceof PlayerServer){
			PlayerServer playerServer = (PlayerServer)player;
			playerServer.playerNetServerHandler.teleport(x, y, z);
		} else if (player instanceof PlayerLocal) {
			PlayerLocal playerLocal = (PlayerLocal)player;
			playerLocal.setPos(x, y + playerLocal.bbHeight, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 100f);
	}

	public static String hmsConversion(long systemTimeMillis) {

		Duration duration = Duration.ofMillis(systemTimeMillis);

		long h = duration.toHours();
		long m = duration.toMinutes() % 60;
		long s = duration.getSeconds() % 60;

		return String.format("%02d:%02d:%02d [h:m:s]", h, m, s);
	}

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
}
