package BTAServerUtilities.mixins;

import BTAServerUtilities.utility.UUIDHelper;
import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetLoginHandler.class, remap = false)
public class NetLoginHandlerMixin {
	@Inject(at = @At("TAIL"), method = "doLogin")
	public void doLoginMixin(Packet1Login packet1login, CallbackInfo ci){
		UUIDHelper.getUUIDFromName(packet1login.username);
	}
}
