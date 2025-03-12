package MelonUtilities.command.commands;

import MelonUtilities.command.commandlogic.CommandLogicTPA;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandTPDeny implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> tpdeny(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.executes(context ->
			{
				PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
				return CommandLogicTPA.tpdeny(sender);
			}
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("tpdeny");

		tpdeny(builder);

		dispatcher.register(builder);
	}
}
