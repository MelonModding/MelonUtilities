package MelonUtilities.command.commands;

import MelonUtilities.command.commandlogic.CommandLogicMelonUtilities;
import MelonUtilities.sqlite.DatabaseManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandMelonUtilities implements CommandManager.CommandRegistry{
	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register((LiteralArgumentBuilder.<CommandSource>literal("melonutilities").requires(CommandSource::hasAdmin))
			.then(LiteralArgumentBuilder.<CommandSource>literal("reload")
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender();
						return CommandLogicMelonUtilities.melonutilitiesReload(sender);
					}
				)
			)
			.then(LiteralArgumentBuilder.<CommandSource>literal("print")
				.executes(context ->
					{
						DatabaseManager.connect((conn) -> {

						});
						return 1;
					}
				)
			)
		);
	}
}
