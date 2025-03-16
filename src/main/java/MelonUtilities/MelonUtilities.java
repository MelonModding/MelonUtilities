package MelonUtilities;

import MelonUtilities.command.commands.*;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.listeners.ChatInputListener;
import MelonUtilities.utility.MUtilServer;
import MelonUtilities.utility.discord.DiscordChatRelay;
import MelonUtilities.utility.discord.DiscordClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.client.option.OptionInteger;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.serverlibe.ServerLibe;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class MelonUtilities implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {

	public static final boolean isServer = FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	public static final String MOD_ID = "melonutilities";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int mainConfigVersion = 1;
	public static final int kitConfigVersion = 0;
	public static final int roleConfigVersion = 0;
	public static final int userConfigVersion = 0;

	public static OptionsPage MelonUtilitiesOptions;
	public static OptionBoolean elevatorAllowObstructions;
	public static OptionInteger elevatorCooldown;

	public static void reloadAll() {
		Data.Users.reload();
		if(Data.MainConfig.config.enableKits) Data.Kits.reload();
		if(Data.MainConfig.config.enableRoles) Data.Roles.reload();
	}

	public static void registerServerCommands(){
		if(Data.MainConfig.config.enableContainerLocking) CommandManager.registerCommand(new CommandLock());
		if(Data.MainConfig.config.enableRoles) CommandManager.registerCommand(new CommandRole());
		if(Data.MainConfig.config.enableRollback) CommandManager.registerCommand(new CommandRollback());
		if(Data.MainConfig.config.enableElevators) CommandManager.registerCommand(new CommandElevator());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPA());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPAHere());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPAccept());
		if(Data.MainConfig.config.enableTPA) CommandManager.registerCommand(new CommandTPDeny());
		if(Data.MainConfig.config.enableHomes) CommandManager.registerCommand(new CommandHome());
		if(Data.MainConfig.config.enableWarps) CommandManager.registerCommand(new CommandWarp());
		CommandManager.registerCommand(new CommandMelonUtilities());
	}

	public static void registerClientCommands(){
	}

	public void loadData(){
		LOGGER.info("Loading Utility Data...");
		reloadAll();
		LOGGER.info("Utility Data Loaded!");
	}

	public void registerListeners(){
		LOGGER.info("Registering ServerLibe Listeners...");
		ServerLibe.registerListener(new ChatInputListener());
		LOGGER.info("ServerLibe Listeners Registered!");
	}

	@Override
	public void onInitialize() {
		LOGGER.info("MelonUtilities initializing!");
		Data.MainConfig.reload();
		loadData();
		registerListeners();
		new Thread(() -> {
			if (DiscordClient.init()) {
				DiscordChatRelay.sendServerStartMessage();
			}
		}).start();
		LOGGER.info("MelonUtilities initialized!");
	}

	public static void initOptions(GameSettings settings){
		elevatorAllowObstructions = new OptionBoolean(settings, "melonutilities.category.elevators.allowObstructions", true);
		elevatorCooldown = new OptionInteger(settings, "melonutilities.category.elevators.cooldown", 8);
	}

	public static void afterServerStart(){
		if(isServer){
			Data.Users.reload();
			MUtilServer.timeOnInit = System.currentTimeMillis();

			Config config = Data.MainConfig.config;
			config.lastSnapshot = correctTimeIfZERO(config.lastSnapshot);
			config.lastBackup = correctTimeIfZERO(config.lastBackup);
			config.lastSnapshotPrune = correctTimeIfZERO(config.lastSnapshotPrune);
			config.lastBackupPrune = correctTimeIfZERO(config.lastBackupPrune);
			Data.MainConfig.save();
		}
	}

	public static double correctTimeIfZERO(double d){
		if(d == 0.0d){
			return System.currentTimeMillis();
		}
		return d;
	}

	public static void info(String s) {
		LOGGER.info(s);
	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
/*		MelonUtilitiesOptions =
			new OptionsPage("options.melonutilities.title", new ItemStack(Items.OLIVINE))
				.withComponent(new OptionsCategory("options.melonutilities.category.elevators")
					.withComponent(new BooleanOptionComponent(elevatorAllowObstructions))
					.withComponent(new IntegerOptionComponent(elevatorCooldown)));

		OptionsPages.register(MelonUtilitiesOptions);*/
	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
