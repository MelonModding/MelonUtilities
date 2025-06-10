package MelonUtilities.command.commands;

import MelonUtilities.command.commandlogic.CommandLogicSpawn;
import MelonUtilities.config.Data;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandSpawn implements CommandManager.CommandRegistry{
	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> spawn(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.literal("")).executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}

					return CommandLogicSpawn.spawnTP(sender);
				}
			);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> spawnSet(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("set").requires(CommandSource::hasAdmin)
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}

					return CommandLogicSpawn.spawnSet(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> spawnReset(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("reset").requires(CommandSource::hasAdmin)
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}

					return CommandLogicSpawn.spawnReset(sender);
				}
			)
		);
		return builder;
	}



	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.<CommandSource>literal("spawn");

		spawn(builder);
		spawnSet(builder);
		spawnReset(builder);

		dispatcher.register(builder);
	}
}
