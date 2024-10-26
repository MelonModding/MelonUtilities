package MelonUtilities.commands.utility;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.util.Iterables;

public class HelpCommandExample implements CommandManager.CommandRegistry {
	private static final SimpleCommandExceptionType FAILURE = new SimpleCommandExceptionType(() -> {
		return I18n.getInstance().translateKey("command.commands.help.exception_failure");
	});

	public HelpCommandExample() {
	}

	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)LiteralArgumentBuilder.literal("help").executes((c) -> {
			CommandSource source = (CommandSource)c.getSource();
			Map<CommandNode<CommandSource>, String> map = commandDispatcher.getSmartUsage(commandDispatcher.getRoot(), source);
			if (!source.messageMayBeMultiline()) {
				source.sendMessage("Cannot display help on single-line command source");
				return 0;
			} else {
				Iterator var4 = map.values().iterator();

				while(var4.hasNext()) {
					String string = (String)var4.next();
					source.sendMessage("/" + string);
				}

				return map.size();
			}
		})).then(RequiredArgumentBuilder.argument("command", StringArgumentType.greedyString()).executes((commandContext) -> {
			ParseResults<CommandSource> parseResults = commandDispatcher.parse(StringArgumentType.getString(commandContext, "command"), (CommandSource)commandContext.getSource());
			if (parseResults.getContext().getNodes().isEmpty()) {
				throw FAILURE.create();
			} else {
				Map<CommandNode<CommandSource>, String> map = commandDispatcher.getSmartUsage(((ParsedCommandNode)Iterables.getLast(parseResults.getContext().getNodes())).getNode(), (CommandSource)commandContext.getSource());
				Iterator var4 = map.values().iterator();

				while(var4.hasNext()) {
					String string = (String)var4.next();
					((CommandSource)commandContext.getSource()).sendMessage("/" + parseResults.getReader().getString() + " " + string);
				}

				return map.size();
			}
		})));
	}
}
