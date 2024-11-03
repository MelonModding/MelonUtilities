package MelonUtilities.mixins.other;

import MelonUtilities.MelonUtilities;
import MelonUtilities.utility.RollbackManager;
import MelonUtilities.utility.TpaManager;
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

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Blocks;init()V", shift = At.Shift.BEFORE), method = "startServer")
	private void startServer(CallbackInfoReturnable<Boolean> cir){
		MelonUtilities.onServerStart();
	}
}
