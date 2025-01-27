package MelonUtilities.command.commands;

import MelonUtilities.command.commandlogic.CommandLogicRollback;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeChunkCoordinates;
import net.minecraft.core.net.command.helpers.Coordinates2D;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandRollback implements CommandManager.CommandRegistry{
	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollback(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.requires(CommandSource::hasAdmin).executes(context ->
			{
				PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
				return CommandLogicRollback.rollback(sender);
			}
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackArea(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("area")
			.then(RequiredArgumentBuilder.<CommandSource, Coordinates2D>argument("x1 z1", ArgumentTypeChunkCoordinates.chunkCoordinates())
				.then(RequiredArgumentBuilder.<CommandSource, Coordinates2D>argument("x2 z2", ArgumentTypeChunkCoordinates.chunkCoordinates())
					.executes(context ->
						{
							CommandSource source = context.getSource();
							PlayerServer sender = (PlayerServer) context.getSource().getSender();
							if (sender == null) return 0;
							Coordinates2D chunk1 = context.getArgument("x1 z1", Coordinates2D.class);
							Coordinates2D chunk2 = context.getArgument("x2 z2", Coordinates2D.class);
							int x1 = chunk1.getX(source);
							int z1 = chunk1.getZ(source);
							int x2 = chunk2.getX(source);
							int z2 = chunk2.getZ(source);
							return CommandLogicRollback.rollbackArea(sender, x1, z1, x2, z2);
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackTakeSnapshot(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("takesnapshot")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRollback.rollbackTakeSnapshot(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackTakeBackup(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("takebackup")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRollback.rollbackTakeBackup(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackPruneSnapshots(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("prunesnapshots")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRollback.rollbackPruneSnapshots(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackPruneBackups(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("prunebackups")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRollback.rollbackPruneBackups(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackAutoSnapshots(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("autosnapshots")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRollback.rollbackAutoSnapshots(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackAutoBackups(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("autobackups")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRollback.rollbackAutoBackups(sender);
				}
			)
		);
		return builder;
	}


	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal("rollback");

		rollback(builder);
		rollbackArea(builder);
		rollbackTakeSnapshot(builder);
		rollbackTakeBackup(builder);
		rollbackPruneSnapshots(builder);
		rollbackPruneBackups(builder);
		rollbackAutoSnapshots(builder);
		rollbackAutoBackups(builder);

		dispatcher.register(builder);
	}
}
