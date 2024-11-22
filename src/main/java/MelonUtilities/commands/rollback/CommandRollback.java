package MelonUtilities.commands.rollback;

import MelonUtilities.utility.syntax.SyntaxBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.arguments.ArgumentTypeChunkCoordinates;
import net.minecraft.core.net.command.helpers.Coordinates2D;

@SuppressWarnings("UnusedReturnValue")
public class CommandRollback implements CommandManager.CommandRegistry{
	static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                               TextFormatting.LIGHT_GRAY + "< Command Syntax > ([] = optional, <> = variable, / = or)");
		syntax.append("rollback", "title",                             TextFormatting.LIGHT_GRAY + "  > /rollback [<mode>]");
		syntax.append("rollbackArea", "rollback",                      TextFormatting.LIGHT_GRAY + "    > area <x1> <z1> <x2> <z2>");
		syntax.append("rollbackTakeSnapshot", "rollback",              TextFormatting.LIGHT_GRAY + "    > takesnapshot");
		syntax.append("rollbackTakeBackup", "rollback",                TextFormatting.LIGHT_GRAY + "    > takebackup");
		syntax.append("rollbackPruneSnapshots", "rollback",            TextFormatting.LIGHT_GRAY + "    > prunesnapshots");
		syntax.append("rollbackPruneBackups", "rollback",              TextFormatting.LIGHT_GRAY + "    > prunebackups");
		syntax.append("rollbackToggleAutoBackups", "rollback",         TextFormatting.LIGHT_GRAY + "    > toggleautobackups");
		syntax.append("rollbackToggleAutoSnapshots", "rollback",       TextFormatting.LIGHT_GRAY + "    > toggleautosnapshots");

	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollback(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.executes(context ->
			{
				Player sender = context.getSource().getSender(); if(sender == null){return 0;}
				return RollbackLogic.rollback(sender);
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
							Player sender = source.getSender();
							if (sender == null) return 0;
							Coordinates2D chunk1 = context.getArgument("x1 z1", Coordinates2D.class);
							Coordinates2D chunk2 = context.getArgument("x2 z2", Coordinates2D.class);
							int x1 = chunk1.getX(source);
							int z1 = chunk1.getZ(source);
							int x2 = chunk2.getX(source);
							int z2 = chunk2.getZ(source);
							return RollbackLogic.rollback_area(sender, x1, z1, x2, z2);
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
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return RollbackLogic.rollback_take_snapshot(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackTakeBackup(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("takebackup")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return RollbackLogic.rollback_take_backup(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackPruneSnapshots(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("prunesnapshots")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return RollbackLogic.rollback_prune_snapshots(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackPruneBackups(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("prunebackups")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return RollbackLogic.rollback_prune_backups(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackAutoSnapshots(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("autosnapshots")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return RollbackLogic.rollback_auto_snapshots(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackAutoBackups(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("autobackups")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return RollbackLogic.rollback_auto_backups(sender);
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
