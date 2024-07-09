package BTAServerSolutions.BTAServerUtilities;

import BTAServerSolutions.BTAServerUtilities.commands.kit.KitCommand;
import BTAServerSolutions.BTAServerUtilities.commands.kit.KittenCommand;
import BTAServerSolutions.BTAServerUtilities.commands.misc.WhereAmICommand;
import BTAServerSolutions.BTAServerUtilities.commands.role.RoleCommand;
import BTAServerSolutions.BTAServerUtilities.commands.tpa.TPACommand;
import BTAServerSolutions.BTAServerUtilities.commands.tpa.TPADenyCommand;
import BTAServerSolutions.BTAServerUtilities.commands.tpa.TPAcceptCommand;
import BTAServerSolutions.BTAServerUtilities.commands.utility.BSUCommand;
import BTAServerSolutions.BTAServerUtilities.saver.SaverSingleton;
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
import turniplabs.halplibe.util.TomlConfigHandler;

import java.util.HashMap;


public class BTAServerUtilities implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {

    public static final String MOD_ID = "btaserverutilities";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Gson GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(ItemStack.class, new ItemStackJsonAdapter()).create();


	public static GameRuleBoolean FIRE_TICKS = GameRules.register(new GameRuleBoolean("doFireTick", true));

	public static TomlConfigHandler config;

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

	public void updateConfig() {

	}

    @Override
    public void onInitialize() {
        LOGGER.info("BTAServerUtilities initializing!");
		updateConfig();
		RoleCommand.buildRoleSyntax();
		LOGGER.info("BTAServerUtilities Role Syntax Built!");
		KitCommand.buildKitSyntax();
		LOGGER.info("BTAServerUtilities Kit Syntax Built!");
		SaverSingleton.getInstance().initialize();
		SaverSingleton.getInstance().loadAll();
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		CommandHelper.createCommand(new KitCommand());
		CommandHelper.createCommand(new RoleCommand());
		CommandHelper.createCommand(new WhereAmICommand());
		CommandHelper.createCommand(new KittenCommand());
		//CommandHelper.createCommand(new Smite());
		CommandHelper.createCommand(new TPACommand());
		CommandHelper.createCommand(new TPAcceptCommand());
		CommandHelper.createCommand(new TPADenyCommand());
		CommandHelper.createCommand(new BSUCommand());
	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
