package MelonUtilities;

import MelonUtilities.command.commands.*;
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

	public static void reloadAll() {
		if(Data.MainConfig.config.enableKits) Data.Kits.reload();
		if(Data.MainConfig.config.enableRoles) Data.Roles.reload();
		Data.Users.reload();
		Data.MainConfig.reload();
	}

	public static void registerCommands(){
		if(Data.MainConfig.config.enableContainerLocking) CommandManager.registerCommand(new CommandLock());
		if(Data.MainConfig.config.enableRoles) CommandManager.registerCommand(new CommandRole());
		if(Data.MainConfig.config.enableRollback) CommandManager.registerCommand(new CommandRollback());
		if(Data.MainConfig.config.enableElevators) CommandManager.registerCommand(new CommandElevator());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPA());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPAHere());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPAccept());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPDeny());
		if(Data.MainConfig.config.enableSQLPlayerLogging) CommandManager.registerCommand(new CommandLogger());
		CommandManager.registerCommand(new CommandMelonUtilities());
	}

	public void loadData(){
		LOGGER.info("Loading Utility Data...");
		reloadAll();
		LOGGER.info("Utility Data Loaded!");
	}

	public void registerListeners(){
		LOGGER.info("Registering ServerLibe Listeners...");
		ServerLibe.registerListener(new ChatInputListener());
		ServerLibe.registerListener(new LogEventListener());
		LOGGER.info("ServerLibe Listeners Registered!");
	}

	@Override
	public void onInitialize() {
		LOGGER.info("MelonUtilities initializing!");
		loadData();
		DatabaseManager.onInitilizeTest();
		registerListeners();
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
