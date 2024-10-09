package BTAServerUtilities.utility;

import com.b100.json.JsonParser;
import com.b100.json.element.JsonObject;
import com.b100.utils.StringUtils;
import com.mojang.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDHelper {
	private static final String urlUUID = "https://api.mojang.com/users/profiles/minecraft/";

	private static final JsonParser jsonParser = new JsonParser();
	private static final Map<String, UUID> nameToUUIDMap = new HashMap<>();

	public static boolean isUUID(String usernameOrUUID) {
		return usernameOrUUID.length() == 36 || usernameOrUUID.length() == 32;
	}

	public static void runConversionAction(String username, @Nullable UUIDFunction successAction, @Nullable StringFunction failAction) {
		if (nameToUUIDMap.containsKey(username)) {
			UUID uuid = nameToUUIDMap.get(username);
			if (uuid != null) {
				if (successAction != null) {
					successAction.run(uuid);
				}
			} else {
				if (failAction != null) {
					failAction.run(username);
				}
			}
			return;
		}
		new Thread(() -> {
			try {
				UUID uuid = getUUIDFromName(username);
				if (uuid != null) {
					if (successAction != null) {
						successAction.run(uuid);
					}
					nameToUUIDMap.put(username, uuid);
				} else {
					if (failAction != null) {
						failAction.run(username);
					}
					nameToUUIDMap.put(username, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (failAction != null) {
					failAction.run(username);
				}
			}
		}).start();
	}
	public static @Nullable UUID getUUIDFromName(String username){
		if(nameToUUIDMap.containsKey(username)){
			return nameToUUIDMap.get(username);
		}
		String string;
		try{
			string = StringUtils.getWebsiteContentAsString(urlUUID + username);
		}catch (Exception e) {
			System.err.println("Can't connect to Mojang API.");
			e.printStackTrace();
			return null;
		}
		if(string.isEmpty()) {
			System.err.println("Player "+username+" doesn't exist!");
			return null;
		}
		String uuid;
		try {
			JsonObject contentParsed = jsonParser.parse(string);
			uuid = contentParsed.getString("id");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (uuid == null) return null;
		if (uuid.length() == 32) { // Untrim trimmed UUID
			String s1 = uuid.substring(0, 8);
			String s2 = uuid.substring(8, 12);
			String s3 = uuid.substring(12, 16);
			String s4 = uuid.substring(16, 20);
			String s5 = uuid.substring(20, 32);
			uuid = s1 + "-" + s2 + "-" + s3 + "-" + s4 + "-" + s5;
		}
		UUID returnVal = UUID.fromString(uuid);
		nameToUUIDMap.put(username, returnVal);
		return returnVal;
	}

	public static @Nullable UUID readFromTag(@NotNull CompoundTag tag, @NotNull String keyBase) {
		if (!tag.containsKey(keyBase + "_msb") || !tag.containsKey(keyBase + "_lsb")) return null;
		long msb = tag.getLong(keyBase + "_msb");
		long lsb = tag.getLong(keyBase + "_lsb");
		return new UUID(msb, lsb);
	}

	public static void writeToTag(@NotNull CompoundTag tag, @Nullable UUID uuid, @NotNull String keyBase) {
		if (uuid != null) {
			tag.putLong(keyBase + "_msb", uuid.getMostSignificantBits());
			tag.putLong(keyBase + "_lsb", uuid.getLeastSignificantBits());
		}
	}

	public interface StringFunction {
		void run(String s);
	}
	public interface UUIDFunction {
		void run(UUID s);
	}
}

/*
static {
	ServerPlayer player = server.playerList.getPlayerEntity(nameToBan);
	if (player != null) {
		server.playerList.banPlayer(player.uuid);
		sendNoticeToOps(senderName, "Banning " + nameToBan);
		player.playerNetServerHandler.kickPlayer("Banned by admin");
	} else {
		UUIDHelper.runConversionAction(nameToBan, (uuid) ->
			{server.playerList.banPlayer(uuid);
			sendNoticeToOps(senderName, "Banning " + nameToBan);},
			(username) -> handler.sendCommandFeedback(sender, "Could not retrieve UUID of player '" + username + "'"));
	}
}
*/
