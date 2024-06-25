package goldenage.omnimod;

import goldenage.omnimod.saver.SaverSingleton;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class OmniMod implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "omnimod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("OmniMod initializing");

		SaverSingleton.getInstance().initialize();
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
