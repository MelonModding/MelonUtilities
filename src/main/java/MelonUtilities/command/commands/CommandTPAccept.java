package MelonUtilities.command.commands;

import MelonUtilities.command.commandlogic.CommandLogicTPA;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandTPAccept implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> tpaccept(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.executes(context ->
			{
				PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
				return CommandLogicTPA.tpaccept(sender);
			}
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal("tpaccept");

		tpaccept(builder);

		dispatcher.register(builder);
	}
}

