package MelonUtilities.mixins;

import MelonUtilities.MelonUtilities;
import MelonUtilities.utility.managers.RollbackManager;
import MelonUtilities.utility.managers.TpaManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin {
	@Inject(at = @At("HEAD"), method = "doTick")
	private void doTick(CallbackInfo ci) {
		TpaManager.tick();
		RollbackManager.tick();
	}

	@Inject(at = @At("TAIL"), method = "startServer")
	private void startServerInject(CallbackInfoReturnable<Boolean> cir){
		MelonUtilities.afterServerStart();
	}

}
