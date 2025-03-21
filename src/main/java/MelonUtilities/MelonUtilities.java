package MelonUtilities;

import MelonUtilities.command.commands.*;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.listeners.ChatInputListener;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.discord.DiscordChatRelay;
import MelonUtilities.utility.discord.DiscordClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.DyeColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.serverlibe.ServerLibe;
import turniplabs.halplibe.helper.RecipeBuilder;
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

	public static void afterServerStart(){
		if(isServer){
			Data.Users.reload();
			MUtil.timeOnInit = System.currentTimeMillis();

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

	}

	@Override
	public void onRecipesReady() {
		if(Data.MainConfig.config.enableMagnets){
			ItemStack magnet = new ItemStack(Items.AMMO_FIREBALL, 1, 1);
			magnet.setCustomName(TextFormatting.RESET + "Magnet");

			RecipeBuilder.Shaped("minecraft")
				.setShape(
					"R L",
					"S S",
					"SSS")
				.addInput('R', Items.DUST_REDSTONE)
				.addInput('L', Items.DYE, DyeColor.BLUE.itemMeta)
				.addInput('S', Items.INGOT_STEEL)
				.create("magnet", magnet);
		}
	}

	@Override
	public void initNamespaces() {

	}
}
