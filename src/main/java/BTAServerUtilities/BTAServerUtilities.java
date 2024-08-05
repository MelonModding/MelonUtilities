package BTAServerUtilities;

import BTAServerUtilities.commands.home.DelHomeCommand;
import BTAServerUtilities.commands.home.HomeCommand;
import BTAServerUtilities.commands.home.SetHomeCommand;
import BTAServerUtilities.commands.kit.KitCommand;
import BTAServerUtilities.commands.kit.KittenCommand;
import BTAServerUtilities.commands.misc.WhereAmICommand;
import BTAServerUtilities.commands.role.RoleCommand;
import BTAServerUtilities.commands.tpa.TPACommand;
import BTAServerUtilities.commands.tpa.TPADenyCommand;
import BTAServerUtilities.commands.tpa.TPAcceptCommand;
import BTAServerUtilities.commands.utility.BSUCommand;
import BTAServerUtilities.config.*;
import BTAServerUtilities.config.custom.classes.Crew;
import BTAServerUtilities.config.custom.jsonadapters.CrewJsonAdapter;
import BTAServerUtilities.config.datatypes.ConfigData;
import BTAServerUtilities.config.datatypes.KitData;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.config.datatypes.RoleData;
import BTAServerUtilities.config.custom.classes.Home;
import BTAServerUtilities.config.custom.jsonadapters.HomeJsonAdapter;
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
import turniplabs.halplibe.helper.CommandHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.HashMap;

import static net.minecraft.server.util.helper.PlayerList.updateList;


public class BTAServerUtilities implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {

    public static final String MOD_ID = "btaserverutilities";
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
		Data.configs.loadAll(ConfigData.class);
		Data.kits.loadAll(KitData.class);
		KitCommand.buildKitSyntax();
		Data.roles.loadAll(RoleData.class);
		RoleCommand.buildRoleSyntax();
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
        LOGGER.info("BTAServerUtilities initializing!");
		Data.configs.loadAll(ConfigData.class);
		Data.kits.loadAll(KitData.class);
		KitCommand.buildKitSyntax();
		Data.roles.loadAll(RoleData.class);
		RoleCommand.buildRoleSyntax();
		Data.playerData.loadAll(PlayerData.class);
		SetHomeCommand.buildSyntax();
		DelHomeCommand.buildSyntax();
		HomeCommand.buildSyntax();
		LOGGER.info("BTAServerUtilities initialized!");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

		// Kit
		CommandHelper.createCommand(new KitCommand());
		CommandHelper.createCommand(new KittenCommand());
		// Role
		CommandHelper.createCommand(new RoleCommand());
		// Home
		CommandHelper.createCommand(new SetHomeCommand());
		CommandHelper.createCommand(new HomeCommand());
		CommandHelper.createCommand(new DelHomeCommand());
		// Whereami
		CommandHelper.createCommand(new WhereAmICommand());
		// Tpa
		CommandHelper.createCommand(new TPACommand());
		CommandHelper.createCommand(new TPAcceptCommand());
		CommandHelper.createCommand(new TPADenyCommand());
		// Misc/utility
		CommandHelper.createCommand(new BSUCommand());

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
