package MelonUtilities.commands.examples;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeIntegerCoordinates;
import net.minecraft.core.net.command.helpers.IntegerCoordinates;
import net.minecraft.core.world.chunk.ChunkCoordinates;

public class SetSpawnCommand implements CommandManager.CommandRegistry{

	public void register(CommandDispatcher<CommandSource> dispatcher) {
		CommandNode<CommandSource> command =
			dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("setworldspawn")
			.requires(CommandSource::hasAdmin)
			.then(RequiredArgumentBuilder.<CommandSource, IntegerCoordinates>argument("position", ArgumentTypeIntegerCoordinates.intCoordinates())
				.executes(c -> {
					CommandSource source = c.getSource();
					IntegerCoordinates coordinates = c.getArgument("position", IntegerCoordinates.class);

					int x = coordinates.getX(source);
					int y = coordinates.getY(source, true);
					int z = coordinates.getZ(source);

					source.getWorld().setSpawnPoint(new ChunkCoordinates(x, y, z));
					source.sendTranslatableMessage("command.commands.setspawn.success", x, y, z);

					return Command.SINGLE_SUCCESS;
				})));
		dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("setspawn")
			.requires(CommandSource::hasAdmin)
			.redirect(command));
	}
}
