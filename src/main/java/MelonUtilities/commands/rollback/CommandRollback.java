package MelonUtilities.commands.rollback;

import MelonUtilities.utility.syntax.SyntaxBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.arguments.ArgumentTypeChunkCoordinates;
import net.minecraft.core.net.command.helpers.Coordinates2D;

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
		builder.executes(
			RollbackLogic::rollback
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackArea(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("area")
			.then(RequiredArgumentBuilder.<CommandSource, Coordinates2D>argument("x1 z1", ArgumentTypeChunkCoordinates.chunkCoordinates())
				.then(RequiredArgumentBuilder.<CommandSource, Coordinates2D>argument("x2 z2", ArgumentTypeChunkCoordinates.chunkCoordinates())
					.executes(
						RollbackLogic::rollbackArea
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackTakeSnapshot(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("takesnapshot")
			.executes(
				RollbackLogic::rollbackTakeSnapshot
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackTakeBackup(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("takebackup")
			.executes(
				RollbackLogic::rollbackTakeBackup
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackPruneSnapshots(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("prunesnapshots")
			.executes(
				RollbackLogic::rollbackPruneSnapshots
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackPruneBackups(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("prunebackups")
			.executes(
				RollbackLogic::rollbackPruneBackups
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackToggleAutoSnapshots(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("toggleautosnapshots")
			.executes(
				RollbackLogic::rollbackToggleAutoSnapshots
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> rollbackToggleAutoBackups(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("toggleautobackups")
			.executes(
				RollbackLogic::rollbackToggleAutoBackups
			)
		);
		return builder;
	}


	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("rollback");

		rollback(builder);
		rollbackArea(builder);
		rollbackTakeSnapshot(builder);
		rollbackTakeBackup(builder);
		rollbackPruneSnapshots(builder);
		rollbackPruneBackups(builder);
		rollbackToggleAutoSnapshots(builder);
		rollbackToggleAutoBackups(builder);

		dispatcher.register(builder);
	}
}
