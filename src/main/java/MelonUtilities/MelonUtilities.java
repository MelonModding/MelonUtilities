package MelonUtilities;

import MelonUtilities.commands.lock.CommandLock;
import MelonUtilities.commands.role.CommandRole;
import MelonUtilities.config.*;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.config.datatypes.data.Crew;
import MelonUtilities.config.datatypes.data.Home;
import MelonUtilities.config.datatypes.jsonadapters.CrewJsonAdapter;
import MelonUtilities.config.datatypes.jsonadapters.HomeJsonAdapter;
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

import static net.minecraft.server.util.helper.PlayerList.updateList;


public class MelonUtilities implements ModInitializer {

	public static final String MOD_ID = "melonutilities";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Gson GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(ItemStack.class, new ItemStackJsonAdapter()).registerTypeAdapter(Home.class, new HomeJsonAdapter()).registerTypeAdapter(Crew.class, new CrewJsonAdapter()).create();

	public static GameRuleBoolean FIRE_TICKS = GameRules.register(new GameRuleBoolean("doFireTick", true));


	public static void updateAll() {
		// Crew
		// Helper
		//TODO HelperCommand.buildHelperSyntax();

		// Home

		// Kit
		//TODO Data.kits.loadAll(KitData.class);
		//TODO KitCommand.buildKitSyntax();

		// Lock

		// Misc

		// Role
		Data.Roles.reload();
		CommandRole.buildRoleSyntax();

		// Tpa

		// Utility

		// Warp

		// Anything Else
		Data.MainConfig.reload();
		Data.Users.reload();
		updateList();

	}

	public void updateRoles(){
		Data.MainConfig.reload();
		Data.Roles.reload();
		CommandRole.buildRoleSyntax();
		updateList();
	}

	public void updateKits(){
		//TODO Data.configs.loadAll(ConfigData.class);
		//TODO Data.kits.loadAll(KitData.class);
		//TODO KitCommand.buildKitSyntax();
	}

	@Override
	public void onInitialize() {
		LOGGER.info("MelonUtilities initializing!");
		// Crew
		// Helper
		//TODO HelperCommand.buildHelperSyntax();

		// Home
		//TODO SetHomeCommand.buildSyntax();
		//TODO DelHomeCommand.buildSyntax();
		//TODO HomeCommand.buildSyntax();

		// Kit 352246
		//TODO Data.kits.loadAll(KitData.class);
		//TODO KitCommand.buildKitSyntax();

		// Lock
		CommandLock.buildLockSyntax();

		// Misc
		// Role
		Data.Roles.reload();
		CommandRole.buildRoleSyntax();

		// Rollback
		//TODO RollbackCommand.buildSyntax();
		//TODO RollbackManager.onInit();

		// Tpa
		// Utility
		// Warp
		// ServerLibe

		// In order for methods inside your listeners to be recognized by ServerLibe you must
		// register them into ServerLibe like such
		//ServerLibe.registerListener(new GuiTestListener()); // Example Listener
		//ServerLibe.registerListener(new DebugInfoListener()); // Prints out debug info to chat on a number of events, disable by default because it's annoying

		// Anything Else


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
