package MelonUtilities.mixins.other;

import MelonUtilities.utility.RollbackManager;
import MelonUtilities.utility.TpaManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin {
	@Inject(at = @At("HEAD"), method = "doTick")
	private void doTick(CallbackInfo ci) {
		TpaManager.tick();
		RollbackManager.tick();
	}
}
