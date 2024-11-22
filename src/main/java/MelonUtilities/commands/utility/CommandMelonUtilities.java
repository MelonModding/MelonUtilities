package MelonUtilities.commands.utility;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

public class CommandMelonUtilities implements CommandManager.CommandRegistry{
	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register((LiteralArgumentBuilder.<CommandSource>literal("melonutilities").requires(CommandSource::hasAdmin))
			.then(LiteralArgumentBuilder.<CommandSource>literal("reload")
				.executes(context ->
					{
						Player sender = context.getSource().getSender();
						return MelonUtilitiesLogic.melonutilities_reload(sender);
					}
				)
			)
		);
	}
}
