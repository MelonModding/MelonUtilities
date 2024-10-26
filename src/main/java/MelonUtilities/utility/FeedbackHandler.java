package MelonUtilities.utility;

import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class FeedbackHandler {
	private static void info(@NotNull CommandSource source, @NotNull String msg, @NotNull TextFormatting color, @NotNull String soundPath) {
		source.sendMessage(color + msg);
//		TODO: Implement sound for feedback
//		Player player = source.getSender();
//		if (player == null) return;
//		player.world.playSoundAtEntity(null, player, "note.harp", 1f, 1f);
	}

	public static void success(@NotNull CommandSource source, @NotNull String msg) {
		info(source, msg, TextFormatting.LIME, "note.harp");
	}

	public static void error(@NotNull CommandSource source, @NotNull String msg) {
		info(source, msg, TextFormatting.RED, "note.bd");
	}

	public static void destructive(@NotNull CommandSource source, @NotNull String msg) {
		info(source, msg, TextFormatting.ORANGE, "note.snare");
	}

	public static void syntax(@NotNull CommandSource source, @NotNull String msg) {
		info(source, msg, TextFormatting.LIGHT_GRAY, "");
	}
}
