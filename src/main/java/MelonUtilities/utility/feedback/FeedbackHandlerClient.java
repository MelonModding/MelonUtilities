package MelonUtilities.utility.feedback;

import MelonUtilities.MelonUtilities;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FeedbackHandlerClient {

    public static void sendTranslatedFeedback(@NotNull FeedbackType feedbackType, @NotNull Player player, @NotNull String formatKey, FeedbackArg... args) {
        player.sendMessage(translateKeyAndFormat(feedbackType, formatKey, args));
        MelonUtilities.LOGGER.info(String.format("Sent command feedback: [%s] to player: [username: %s, uuid: %s]", translateKeyAndFormatRaw(formatKey, args), player.username, player.uuid));
        playFeedbackSound(player, feedbackType);
    }

	public static void sendFeedback(@NotNull FeedbackType feedbackType, @NotNull Player player, @NotNull String message, FeedbackArg... args) {
		player.sendMessage(format(feedbackType, message, args));
		MelonUtilities.LOGGER.info(String.format("Sent command feedback: [%s] to player: [username: %s, uuid: %s]", formatRaw(message, args), player.username, player.uuid));
		playFeedbackSound(player, feedbackType);
	}

	public static void sendFeedback(@NotNull Player player, @NotNull TextFormatting color, @NotNull String message, FeedbackArg... args) {
		player.sendMessage(format(color, message, args));
		MelonUtilities.LOGGER.info(String.format("Sent command feedback: [%s] to player: [username: %s, uuid: %s]", formatRaw(message, args), player.username, player.uuid));
	}

    public static @NotNull String translateKeyAndFormat(@NotNull FeedbackType feedbackType, @NotNull String formatKey, FeedbackArg... args){
        ArrayList<String> argStrings = new ArrayList<>();
        for(FeedbackArg arg : args){
            String argString = (
				arg.getBorderColor() + arg.getBorderOpener() +
                arg.getArgSpecial() +  arg.getArgColor() + arg.getAllArgs() +
                TextFormatting.RESET +
				arg.getBorderColor() + arg.getBorderCloser() +
                feedbackType.getColor()
            );
            argStrings.add(argString);
        }
        return feedbackType.getColor() + I18n.getInstance().translateKeyAndFormat(formatKey, argStrings.toArray());
    }

	public static @NotNull String format(@NotNull FeedbackType feedbackType, @NotNull String message, FeedbackArg... args){
		ArrayList<String> argStrings = new ArrayList<>();
		for(FeedbackArg arg : args){
			String argString = (
				arg.getBorderColor() + arg.getBorderOpener() +
				arg.getArgSpecial() +  arg.getArgColor() + arg.getAllArgs() +
				TextFormatting.RESET +
				arg.getBorderColor() + arg.getBorderCloser() +
				feedbackType.getColor()
			);
			argStrings.add(argString);
		}
		return feedbackType.getColor() + String.format(message, argStrings.toArray());
	}

	public static @NotNull String format(@NotNull TextFormatting color, @NotNull String message, FeedbackArg... args){
		ArrayList<String> argStrings = new ArrayList<>();
		for(FeedbackArg arg : args){
			String argString = (
				arg.getBorderColor() + arg.getBorderOpener() +
					arg.getArgSpecial() +  arg.getArgColor() + arg.getAllArgs() +
					TextFormatting.RESET +
					arg.getBorderColor() + arg.getBorderCloser() +
					color
			);
			argStrings.add(argString);
		}
		return color + String.format(message, argStrings.toArray());
	}

    private static @NotNull String translateKeyAndFormatRaw(@NotNull String formatKey, FeedbackArg... args){
        ArrayList<String> argStrings = new ArrayList<>();
        for(FeedbackArg arg : args){
            String argString = (
                arg.getBorderOpener() +
				arg.getAllArgs() +
                arg.getBorderCloser()
            );
            argStrings.add(argString);
        }
        return I18n.getInstance().translateKeyAndFormat(formatKey, argStrings.toArray());
    }

	private static @NotNull String formatRaw(@NotNull String message, FeedbackArg... args){
		ArrayList<String> argStrings = new ArrayList<>();
		for(FeedbackArg arg : args){
			String argString = (
				arg.getBorderOpener() +
				arg.getAllArgs() +
				arg.getBorderCloser()
			);
			argStrings.add(argString);
		}
		return String.format(message, argStrings.toArray());
	}

    public static void playFeedbackSound(@NotNull Player player, @NotNull FeedbackType feedbackType){
        if(feedbackType.getSoundPath().equals("NO_SOUND_PATH")){return;}
		assert player.world != null;
		player.world.playSoundAtEntity(player, player, feedbackType.getSoundPath(), 1f, 1f);
    }
}
