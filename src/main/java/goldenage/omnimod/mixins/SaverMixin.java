package goldenage.omnimod.mixins;

import goldenage.omnimod.saver.Saver;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
public class SaverMixin {
	@Inject(method = "saveServerWorld", at = @At("TAIL"))
	public void saveServerWorldInjection(CallbackInfo ci) {
		Saver.getInstance().saveAll();
	}
}
