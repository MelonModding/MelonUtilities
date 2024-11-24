package MelonUtilities.mixins;

import MelonUtilities.MelonUtilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.net.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CommandManager.class, remap = false)
public class CommandManagerMixin {
	@Inject(at = @At("HEAD"), method = "init")
	public void initInject(CallbackInfo ci) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER){
			MelonUtilities.registerCommands();
		}
	}
}
