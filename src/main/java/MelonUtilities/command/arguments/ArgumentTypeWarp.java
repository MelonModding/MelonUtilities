package MelonUtilities.command.arguments;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Warp;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeWarp implements ArgumentType<Warp> {
	private static final List<String> EXAMPLES = Arrays.asList("warp", "base", "mobspawner");

	public ArgumentTypeWarp() {
	}

	public static ArgumentType<Warp> warp() {
		return new ArgumentTypeWarp();
	}

	public Warp parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (Warp warp : warps()) {
			if (warp.name.equalsIgnoreCase(string)) {
				return warp;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to find Warp: " + string + " (Warp Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(Warp warp : warps()){
			if (warp.name.startsWith(builder.getRemaining())) {
				builder.suggest(warp.name);
			}
		}

		return builder.buildFuture();
	}

	public Collection<Warp> warps() {
		return new ArrayList<>(Data.MainConfig.config.warpData);
	}



	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
