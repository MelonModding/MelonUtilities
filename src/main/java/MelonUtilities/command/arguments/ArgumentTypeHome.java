package MelonUtilities.command.arguments;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Home;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.server.net.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeHome implements ArgumentType<String> {
	private static final List<String> EXAMPLES = Arrays.asList("home", "base", "mobspawner");

	public ArgumentTypeHome() {
	}

	public static ArgumentType<String> home() {
		return new ArgumentTypeHome();
	}

	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.readString();
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

		if(context.getSource() instanceof ServerCommandSource){
			Player sender = ((ServerCommandSource) context.getSource()).getSender();
			for(Home home : homes(sender)){
				if (home.name.startsWith(builder.getRemaining())) {
					builder.suggest(home.name);
				}
			}
		}

		return builder.buildFuture();
	}

	public Collection<Home> homes(Player sender) {
		return new ArrayList<>(Data.Users.userDataHashMap.get(sender.uuid).homeData);
	}



	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
