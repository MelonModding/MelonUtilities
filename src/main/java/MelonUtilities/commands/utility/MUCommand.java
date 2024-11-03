package MelonUtilities.commands.utility;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.FeedbackHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.IntegerCoordinatesArgumentType;

import static net.minecraft.server.util.helper.PlayerList.updateList;

public class MUCommand implements CommandManager.CommandRegistry{

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		System.out.println("REGISTER BEING RUN");
		CommandNode<CommandSource> command = dispatcher
			.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) LiteralArgumentBuilder
				.<CommandSource>literal("melonutilities")
				.requires(CommandSource::hasAdmin))
				.then(RequiredArgumentBuilder.argument("reload", IntegerCoordinatesArgumentType.intCoordinates())
					.executes((c) -> {
			CommandSource source = (CommandSource) c.getSource();
			FeedbackHandler.success(source, "Reloading MelonUtilities...");

			FeedbackHandler.destructive(source, "Reloading Player Data...");
			Data.playerData.loadAll(PlayerData.class);
			FeedbackHandler.success(source, "Reloaded " + Data.playerData.dataHashMap.size() + " Player(s)!");

			//TODO FeedbackHandler.destructive(source, "Building Helper Syntax...");
			//TODO HelperCommand.buildHelperSyntax();
			//TODO FeedbackHandler.success(source, "Helper Syntax Built!");

			FeedbackHandler.destructive(source, "Reloading Kit Data...");
			Data.kits.loadAll(KitData.class);
			FeedbackHandler.success(source, "Reloaded " + Data.kits.dataHashMap.size() + " Kit(s)!");

			//TODO FeedbackHandler.destructive(source, "Building Kit Syntax...");
			//TODO KitCommand.buildKitSyntax();
			//TODO FeedbackHandler.success(source, "Kit Syntax Built!");

			FeedbackHandler.destructive(source, "Reloading Role Data...");
			Data.roles.loadAll(RoleData.class);
			FeedbackHandler.success(source, "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");

			//TODO FeedbackHandler.destructive(source, "Building Role Syntax...");
			//TODO RoleCommand.buildRoleSyntax();
			//TODO FeedbackHandler.success(source, "Role Syntax Built!");

			//TODO FeedbackHandler.destructive(source, "Building Rollback Syntax...");
			//TODO RollbackCommand.buildSyntax();
			//TODO FeedbackHandler.success(source, "Rollback Syntax Built!");

			FeedbackHandler.destructive(source, "Reloading General Configs...");
			Data.configs.loadAll(ConfigData.class);
			FeedbackHandler.success(source, "Reloaded Configs!");

			FeedbackHandler.destructive(source, "Updating Player List...");
			updateList();
			FeedbackHandler.success(source, "Updated List!");
			return 1;
				})));
	}
}
