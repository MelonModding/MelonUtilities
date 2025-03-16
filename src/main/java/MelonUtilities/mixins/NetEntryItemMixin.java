package MelonUtilities.mixins;

import net.minecraft.core.net.entity.entries.NetEntryItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = NetEntryItem.class, remap = false)
public class NetEntryItemMixin {
	@Inject(method = "getPacketDelay", at = @At("HEAD"), cancellable = true)
	void getPacketDelay(CallbackInfoReturnable<Integer> cir){
		cir.setReturnValue(6);
		return;
	}
}
