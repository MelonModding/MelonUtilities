package MelonUtilities.utility.feedback;

import MelonUtilities.MelonUtilities;
import MelonUtilities.utility.MUtil;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.packet.PacketPlaySoundEffectDirect;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.sound.SoundTypes;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;

public class FeedbackHandler {

	static FeedbackType success = new FeedbackType("success", "lime", "note.harp");
	static FeedbackType error = new FeedbackType("error", "red", "note.bd");
	static FeedbackType destructive = new FeedbackType("destructive", "orange", "note.snare");
	static FeedbackType syntax = new FeedbackType("syntax", "light_grey");

	// Logic that actually starts/ends the Variable (only visible in code)
	// Using the ender at the end of a string is optional, but you have to in the middle of a string
	private static final char variableStarter = '%';
	private static final char variableEnder = '%';
	// Visual cover for the Variable (shows up in game)
	private static final char variableOpener = '[';
	private static final char variableCloser = ']';

	private static @NotNull String buildFeedback(@NotNull FeedbackType feedbackType, @NotNull String msg) {
		StringBuilder tempmsg = new StringBuilder();
		for (int i = 0; i < msg.length(); i++){
			char c = msg.charAt(i);
			if(c != variableStarter){
				tempmsg.append(c);
			} else{
				tempmsg.append(MUtil.SECTION_GRAY);
				tempmsg.append(variableOpener);
				tempmsg.append(MUtil.SECTION_LIGHT_GRAY);
				for(int j = i+1; j < msg.length(); j++){
					i = j;
					c = msg.charAt(j);
					if(c == variableEnder){
						tempmsg.append(MUtil.SECTION_GRAY);
						tempmsg.append(variableCloser);
						tempmsg.append(feedbackType.getColorSection());
						break;
					} else {
						tempmsg.append(c);
						if(msg.length()-1 == j){
							tempmsg.append(MUtil.SECTION_GRAY);
							tempmsg.append(variableCloser);
						}
					}
				}
			}
		}
		return String.valueOf(tempmsg);
	}

	private static void info(@NotNull CommandContext<CommandSource> context, @NotNull FeedbackType feedbackType, @NotNull String msg, boolean log) {

		CommandSource source = context.getSource();
		msg = buildFeedback(feedbackType, msg);

		source.sendMessage(feedbackType.getColorFormat() + msg);

		if(log){
			MelonUtilities.LOGGER.info("{} used command '{}' ", source.getName(), context.getInput());
		}

		if(!feedbackType.getSoundPath().equals("NO_SOUND")){
			Player player = source.getSender();
			if (player == null) return;
			if(player instanceof PlayerServer){
				((PlayerServer) player).playerNetServerHandler.sendPacket(new PacketPlaySoundEffectDirect(SoundTypes.getSoundId(feedbackType.getSoundPath()), SoundCategory.GUI_SOUNDS, player.x, player.y, player.z, 1f, 1f));
			}
		}
	}

	private static void info(@NotNull Player player, @NotNull FeedbackType feedbackType, @NotNull String msg) {

		msg = buildFeedback(feedbackType, msg);

		player.sendMessage(feedbackType.getColorFormat() + msg);

		if(!feedbackType.getSoundPath().equals("NO_SOUND")){
			if(player instanceof PlayerServer){
				((PlayerServer) player).playerNetServerHandler.sendPacket(new PacketPlaySoundEffectDirect(SoundTypes.getSoundId(feedbackType.getSoundPath()), SoundCategory.GUI_SOUNDS, player.x, player.y, player.z, 1f, 1f));
			}
		}
	}

	public static void success(@NotNull Player player,  @NotNull String msg) {
		info(player, success, msg);
	}

	public static void success(@NotNull CommandContext<CommandSource> context, @NotNull String msg, boolean log) {
		info(context, success, msg, log);
	}

	public static void success(@NotNull CommandContext<CommandSource> context, @NotNull String msg) {
		info(context, success, msg, false);
	}

	public static void error(@NotNull Player player,  @NotNull String msg) {
		info(player, error, msg);
	}

	public static void error(@NotNull CommandContext<CommandSource> context, @NotNull String msg, boolean log) {
		info(context, error, msg, log);
	}

	public static void error(@NotNull CommandContext<CommandSource> context, @NotNull String msg) {
		info(context, error, msg, false);
	}

	public static void destructive(@NotNull Player player,  @NotNull String msg) {
		info(player, destructive, msg);
	}

	public static void destructive(@NotNull CommandContext<CommandSource> context, @NotNull String msg, boolean log) {
		info(context, destructive, msg, log);
	}

	public static void destructive(@NotNull CommandContext<CommandSource> context, @NotNull String msg) {
		info(context, destructive, msg, false);
	}

	public static void syntax(@NotNull Player player,  @NotNull String msg) {
		info(player, syntax, msg);
	}

	public static void syntax(@NotNull CommandContext<CommandSource> context, @NotNull String msg, boolean log) {
		info(context, syntax, msg, log);
	}

	public static void syntax(@NotNull CommandContext<CommandSource> context, @NotNull String msg) {
		info(context, syntax, msg, false);
	}
}
