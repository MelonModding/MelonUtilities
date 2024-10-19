package MelonUtilities.mixins.gamerules;

import MelonUtilities.MelonUtilities;
import net.minecraft.core.block.BlockFire;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockFire.class, remap = false)
public abstract class FireTickMixin {

	@Inject(at = @At("HEAD"), cancellable = true, method = "setBurnResult(Lnet/minecraft/core/world/World;III)V")
	private void checkFireTickRule(World world, int x, int y, int z, CallbackInfo ci) {
		if(!(Boolean)world.getGameRuleValue(MelonUtilities.FIRE_TICKS)) {
			ci.cancel();
		}
	}
	@Inject(at = @At("HEAD"), cancellable = true, method = "canNeighborCatchFire(Lnet/minecraft/core/world/World;III)Z")
	private void checkFireTickRuleForSpread(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if(!(Boolean)world.getGameRuleValue(MelonUtilities.FIRE_TICKS)) {
			cir.setReturnValue(false);
		}
	}


}
