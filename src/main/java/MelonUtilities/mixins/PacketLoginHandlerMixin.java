package MelonUtilities.mixins;

import net.minecraft.core.net.NetworkManager;
import net.minecraft.core.net.packet.PacketLogin;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
	value = {PacketHandlerLogin.class},
	remap = false
)
public class PacketLoginHandlerMixin {
	@Shadow
	public NetworkManager netManager;

	public PacketLoginHandlerMixin() {
	}

	@Inject(
		method = {"doLogin"},
		at = {@At("HEAD")}
	)
	public void doLogin(PacketLogin loginPacket, CallbackInfo ci) {
		//this.netManager.addToSendQueue(new Packet3Chat(TextFormatting.RED + "MelonCommands: /kit does not save Flag data"));
		//this.netManager.addToSendQueue(new Packet3Chat(TextFormatting.RED + "            (item names were given custom support)"));
	}
}
