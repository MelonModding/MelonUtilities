package BTAServerUtilities.mixins.gamerules;

import net.minecraft.core.net.NetworkManager;
import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
	value = {NetLoginHandler.class},
	remap = false
)
public class NetLoginHandlerMixin {
	@Shadow
	public NetworkManager netManager;

	public NetLoginHandlerMixin() {
	}

	@Inject(
		method = {"doLogin"},
		at = {@At("HEAD")}
	)
	public void doLogin(Packet1Login packet1login, CallbackInfo ci) {
		//this.netManager.addToSendQueue(new Packet3Chat(TextFormatting.RED + "MelonCommands: /kit does not save Flag data"));
		//this.netManager.addToSendQueue(new Packet3Chat(TextFormatting.RED + "            (item names were given custom support)"));
	}
}
