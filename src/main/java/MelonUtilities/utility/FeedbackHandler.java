package MelonUtilities.utility;

import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class FeedbackHandler {
	private static void info(@NotNull CommandSender sender, @NotNull String msg, @NotNull TextFormatting color, @NotNull String soundPath) {
		sender.sendMessage(color + msg);
//		TODO: Implement sound for feedback
//		EntityPlayer player = sender.getPlayer();
//		if (player == null) return;
//		player.world.playSoundAtEntity(null, player, "note.harp", 1f, 1f);
	}

	public static void success(@NotNull CommandSender sender, @NotNull String msg) {
		info(sender, msg, TextFormatting.LIME, "note.harp");
	}

	public static void error(@NotNull CommandSender sender, @NotNull String msg) {
		info(sender, msg, TextFormatting.RED, "note.bd");
	}

	public static void destructive(@NotNull CommandSender sender, @NotNull String msg) {
		info(sender, msg, TextFormatting.YELLOW, "note.snare");
	}
}
