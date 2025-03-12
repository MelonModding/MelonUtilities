package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeUsername;
import MelonUtilities.command.commandlogic.CommandLogicTPA;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandTPA implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> tpa(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderRequired.<CommandSource, String>argument("target", ArgumentTypeUsername.string())
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					String targetUsername = context.getArgument("target", String.class);
					return CommandLogicTPA.tpa(sender, targetUsername);
				}
			)
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("tpa");

		tpa(builder);

		dispatcher.register(builder);
	}
}
