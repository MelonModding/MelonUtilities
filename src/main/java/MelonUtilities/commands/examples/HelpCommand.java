package MelonUtilities.commands.examples;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;

import java.util.Map;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.util.Iterables;

public class HelpCommand implements CommandManager.CommandRegistry{
	private static final SimpleCommandExceptionType FAILURE = new SimpleCommandExceptionType(() -> I18n.getInstance().translateKey("command.commands.help.exception_failure"));

	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("help")
			.executes(c -> {
				CommandSource source = c.getSource();
				Map<CommandNode<CommandSource>, String> map = commandDispatcher.getSmartUsage(commandDispatcher.getRoot(), source);
				if (!source.messageMayBeMultiline()) {
					source.sendMessage("Cannot display help on single-line command source");
					return 0;
				}
				for (String string : map.values()) {
					source.sendMessage("/" + string);
				}
				return map.size();
				}
			)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("command", StringArgumentType.greedyString())
				.executes(commandContext -> {
					ParseResults<CommandSource> parseResults = commandDispatcher.parse(StringArgumentType.getString(commandContext, "command"), commandContext.getSource());
					if (parseResults.getContext().getNodes().isEmpty()) {
						throw FAILURE.create();
					}
					Map<CommandNode<CommandSource>, String> map = commandDispatcher.getSmartUsage(Iterables.getLast(parseResults.getContext().getNodes()).getNode(), commandContext.getSource());
					for (String string : map.values()) {
						commandContext.getSource().sendMessage("/" + parseResults.getReader().getString() + " " + string);
					}
					return map.size();
					}
				)
			)
		);
	}
}
