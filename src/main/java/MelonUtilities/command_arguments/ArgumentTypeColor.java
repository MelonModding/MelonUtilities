package MelonUtilities.command_arguments;

import MelonUtilities.utility.MUtil;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeColor implements ArgumentType<String> {
	private static final List<String> EXAMPLES = Arrays.asList("red", "green", "blue");

	public ArgumentTypeColor() {
	}

	public static ArgumentType<String> color() {
		return new ArgumentTypeColor();
	}

	public String parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (Map.Entry<String, String> entry : MUtil.colorSectionMap.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(string)) {
				return string;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to Find Color: " + string + " (Color Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(Map.Entry<String, String> entry : MUtil.colorSectionMap.entrySet()){
			if (entry.getKey().startsWith(builder.getRemainingLowerCase())) {
				builder.suggest(entry.getKey());
			}
		}

		return builder.buildFuture();
	}

	public Collection<String> colors() {
		return new ArrayList<>(MUtil.colorSectionMap.keySet());
	}

	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
