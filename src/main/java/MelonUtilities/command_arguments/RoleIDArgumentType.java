package MelonUtilities.command_arguments;

import MelonUtilities.config.Data;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class RoleIDArgumentType implements ArgumentType<String>{
	private static final List<String> EXAMPLES = Arrays.asList("starter", "pvp1", "saplings");

	public RoleIDArgumentType() {
	}

	public static ArgumentType<String> roleID() {
		return new RoleIDArgumentType();
	}

	public String parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (String roleID : roleIDs()) {
			if (roleID.equalsIgnoreCase(string)) {
				return string;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to Delete Role: " + string + " (Role Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(String roleID : roleIDs()){
			if (roleID.startsWith(builder.getRemainingLowerCase())) {
				builder.suggest(roleID);
			}
		}

		return builder.buildFuture();
	}

	public Collection<String> roleIDs() {
		return new ArrayList<>(Data.roles.fileHashMap.keySet());
	}

	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
