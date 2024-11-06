package MelonUtilities.commands.rules;

import MelonUtilities.MelonUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesCommand extends Command {
	public static final File rulesFile = new File("./config/" + MelonUtilities.MOD_ID + "/rules.json");
	public static final Map<String /*Category*/, List<String> /*lines*/> rulesData = new HashMap<>();
	public RulesCommand() {
		super("rules");
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSource commandSource, String[] strings) {
		if (strings.length == 0) {
			for (Map.Entry<String, List<String>> entry : rulesData.entrySet()) {
				commandSource.sendMessage(entry.getKey());
				int i = 1;
				for (String s : entry.getValue()) {
					commandSource.sendMessage(String.format("  - §3§nRule #%s§r: %s", i, s));
					commandSource.sendMessage("");
					i++;
				}
			}
			return true;
		}

		if (strings.length == 1 && strings[0].equals("reload")) {
			try {
				reload();
			} catch (Exception e) {
				MinecraftServer.getInstance().playerList.sendChatMessageToAllOps(String.format("Exception while loading rules! %s", e));
				MelonUtilities.LOGGER.error("Exception while loading rules!", e);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean opRequired(String[] strings) {
		if (strings.length == 1 && strings[0].equals("reload")) {
			return true;
		}
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSource commandSource) {
		commandSource.sendMessage("/rules");
		if (commandSource.isAdmin()) {
			commandSource.sendMessage("/rules reload");
		}
	}

	public static void reload() throws IOException {
		JsonObject rulesObject = JsonParser.parseReader(new JsonReader(Files.newBufferedReader(rulesFile.toPath()))).getAsJsonObject();
		rulesData.clear();
		int i = 0;
		for (Map.Entry<String, JsonElement> category : rulesObject.asMap().entrySet()) {
			List<String> rules = new ArrayList<>();
			for (JsonElement element : category.getValue().getAsJsonArray()) {
				rules.add(element.getAsString());
				i++;
			}
			rulesData.put(category.getKey(), rules);
		}
		MinecraftServer.getInstance().playerList.sendChatMessageToAllOps("Loaded all " + i + " rules");
	}

	static {
		try {
			reload(); // Initial rule load
		} catch (IOException e) {
			MelonUtilities.LOGGER.error("Exception while loading rules!", e);
		}
	}
}
