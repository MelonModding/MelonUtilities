package MelonUtilities;

import MelonUtilities.command.commands.CommandElevator;
import MelonUtilities.command.commands.CommandLock;
import MelonUtilities.command.commands.CommandRole;
import MelonUtilities.command.commands.CommandRollback;
import MelonUtilities.command.commands.CommandMelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.listeners.ChatInputListener;
import MelonUtilities.listeners.LogEventListener;
import MelonUtilities.sqlite.DatabaseManager;
import MelonUtilities.utility.MUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.serverlibe.ServerLibe;


public class MelonUtilities implements ModInitializer {

	public static final String MOD_ID = "melonutilities";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void reloadAndBuildAll() {
		Data.Kits.reload();
		Data.MainConfig.reload();
		Data.Users.reload();
		Data.Roles.reload();

		//CommandKit.buildKitSyntax();
		CommandRollback.buildSyntax();
		CommandRole.buildSyntax();
		CommandLock.buildSyntax();
	}

	public static void registerCommands(){
		CommandManager.registerCommand(new CommandLock());
		CommandManager.registerCommand(new CommandRole());
		CommandManager.registerCommand(new CommandRollback());
		CommandManager.registerCommand(new CommandElevator());
		CommandManager.registerCommand(new CommandMelonUtilities());
	}

	public void initializeCommands(){
		LOGGER.info("Commands initializing!");

		reloadAndBuildAll();

		LOGGER.info("Commands initialized!");
	}

	@Override
	public void onInitialize() {
		LOGGER.info("MelonUtilities initializing!");
		initializeCommands();
		DatabaseManager.onInitilizeTest();
		//Listeners
		ServerLibe.registerListener(new ChatInputListener());
		ServerLibe.registerListener(new LogEventListener());
		LOGGER.info("MelonUtilities initialized!");
	}

	public static void afterServerStart(){
		Data.Users.reload();
		MUtil.timeOnInit = System.currentTimeMillis();

		Config config = Data.MainConfig.config;
		config.lastSnapshot = correctTimeIfZERO(config.lastSnapshot);
		config.lastBackup = correctTimeIfZERO(config.lastBackup);
		config.lastSnapshotPrune = correctTimeIfZERO(config.lastSnapshotPrune);
		config.lastBackupPrune = correctTimeIfZERO(config.lastBackupPrune);
		Data.MainConfig.save();
	}

	public static double correctTimeIfZERO(double d){
		if(d == 0.0d){
			return System.currentTimeMillis();
		}
		return d;
	}
}
