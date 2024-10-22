package MelonUtilities;

import MelonUtilities.commands.helper.HelperCommand;
import MelonUtilities.commands.home.DelHomeCommand;
import MelonUtilities.commands.home.HomeCommand;
import MelonUtilities.commands.home.SetHomeCommand;
import MelonUtilities.commands.kit.KitCommand;
import MelonUtilities.commands.kit.KittenCommand;
import MelonUtilities.commands.lock.LockCommand;
import MelonUtilities.commands.misc.WhereAmICommand;
import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.commands.rollback.RollbackCommand;
import MelonUtilities.commands.tpa.TPACommand;
import MelonUtilities.commands.tpa.TPADenyCommand;
import MelonUtilities.commands.tpa.TPAcceptCommand;
import MelonUtilities.commands.utility.MUCommand;
import MelonUtilities.config.*;
import MelonUtilities.config.custom.classes.Crew;
import MelonUtilities.config.custom.jsonadapters.CrewJsonAdapter;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.config.custom.classes.Home;
import MelonUtilities.config.custom.jsonadapters.HomeJsonAdapter;
import MelonUtilities.utility.RollbackManager;
import MelonUtilities.utility.MUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.data.registry.recipe.adapter.ItemStackJsonAdapter;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.CommandHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.HashMap;

import static net.minecraft.server.util.helper.PlayerList.updateList;


public class MelonUtilities implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {

    public static final String MOD_ID = "melonutilities";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Gson GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(ItemStack.class, new ItemStackJsonAdapter()).registerTypeAdapter(Home.class, new HomeJsonAdapter()).registerTypeAdapter(Crew.class, new CrewJsonAdapter()).create();

	public static GameRuleBoolean FIRE_TICKS = GameRules.register(new GameRuleBoolean("doFireTick", true));

	public static final HashMap<String, String> colorMap = new HashMap<>();
	static{
		colorMap.put("purple", "§a");
		colorMap.put("blue", "§b");
		colorMap.put("brown", "§c");
		colorMap.put("green", "§d");
		colorMap.put("red", "§e");
		colorMap.put("black", "§f");
		colorMap.put("orange", "§1");
		colorMap.put("magenta", "§2");
		colorMap.put("light_blue", "§3");
		colorMap.put("yellow", "§4");
		colorMap.put("lime", "§5");
		colorMap.put("pink", "§6");
		colorMap.put("grey", "§7");
		colorMap.put("gray", "§7");
		colorMap.put("light_grey", "§8");
		colorMap.put("light_gray", "§8");
		colorMap.put("cyan", "§9");
		colorMap.put("white", "§0");
	}

	public static void updateAll() {
		// Crew
		// Helper
		HelperCommand.buildHelperSyntax();
		// Home
		// Kit
		Data.kits.loadAll(KitData.class);
		KitCommand.buildKitSyntax();
		// Lock
		// Misc
		// Role
		Data.roles.loadAll(RoleData.class);
		RoleCommand.buildRoleSyntax();
		// Tpa
		// Utility
		// Warp
		// Anything Else
		Data.configs.loadAll(ConfigData.class);
		Data.playerData.loadAll(PlayerData.class);
		updateList();

	}

	public void updateRoles(){
		Data.configs.loadAll(ConfigData.class);
		Data.roles.loadAll(RoleData.class);
		RoleCommand.buildRoleSyntax();
		updateList();
	}

	public void updateKits(){
		Data.configs.loadAll(ConfigData.class);
		Data.kits.loadAll(KitData.class);
		KitCommand.buildKitSyntax();
	}

    @Override
    public void onInitialize() {
		if (HalpLibe.isClient) return;
		LOGGER.info("MelonUtilities initializing!");
		// Crew
		// Helper
		HelperCommand.buildHelperSyntax();
		// Home
		SetHomeCommand.buildSyntax();
		DelHomeCommand.buildSyntax();
		HomeCommand.buildSyntax();
		// Kit
		Data.kits.loadAll(KitData.class);
		KitCommand.buildKitSyntax();
		// Lock
		LockCommand.buildLockSyntax();
		// Misc
		// Role
		Data.roles.loadAll(RoleData.class);
		RoleCommand.buildRoleSyntax();
		// Rollback
		RollbackCommand.buildSyntax();
		RollbackManager.onInit();
		// Tpa
		// Utility
		// Warp
		// ServerLibe

		// In order for methods inside your listeners to be recognized by ServerLibe you must
		// register them into ServerLibe like such
		//ServerLibe.registerListener(new GuiTestListener()); // Example Listener
		//ServerLibe.registerListener(new DebugInfoListener()); // Prints out debug info to chat on a number of events, disable by default because it's annoying

		// Anything Else
		Data.playerData.loadAll(PlayerData.class);
		MUtil.timeOnInit = System.currentTimeMillis();

		Data.configs.loadAll(ConfigData.class);


		LOGGER.info("MelonUtilities initialized!");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		if (HalpLibe.isClient) return;
		// Crew
		// Helper
		// Home
		CommandHelper.createCommand(new SetHomeCommand());
		CommandHelper.createCommand(new HomeCommand());
		CommandHelper.createCommand(new DelHomeCommand());
		// Kit
		CommandHelper.createCommand(new KitCommand());
		CommandHelper.createCommand(new KittenCommand());
		// Lock
		CommandHelper.createCommand(new LockCommand());
		// Misc
		CommandHelper.createCommand(new WhereAmICommand());
		// Role
		CommandHelper.createCommand(new RoleCommand());
		// Rollback
		CommandHelper.createCommand(new RollbackCommand());
		// Tpa
		CommandHelper.createCommand(new TPACommand());
		CommandHelper.createCommand(new TPAcceptCommand());
		CommandHelper.createCommand(new TPADenyCommand());
		// Utility
		CommandHelper.createCommand(new HelperCommand());
		CommandHelper.createCommand(new MUCommand());
		// Warp
		// Anything Else

		Data.configs.loadAll(ConfigData.class);
		ConfigData config = Data.configs.getOrCreate("config", ConfigData.class);
		config.lastSnapshot = correctTimeIfZERO(Data.configs.getOrCreate("config", ConfigData.class).lastSnapshot);
		config.lastBackup = correctTimeIfZERO(config.lastBackup);
		config.lastSnapshotPrune = correctTimeIfZERO(config.lastSnapshotPrune);
		config.lastBackupPrune = correctTimeIfZERO(config.lastBackupPrune);
		Data.configs.saveAll();

	}

	private double correctTimeIfZERO(double d){
		if(d == 0.0d){
			return System.currentTimeMillis();
		}
		return d;
	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
