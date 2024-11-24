package MelonUtilities.mixins;

import MelonUtilities.utility.builders.RoleBuilder;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = net.minecraft.core.net.packet.PacketAddPlayer.class, remap = false)
public class PacketAddPlayerMixin {

	@Shadow
	public String nickname;

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/core/entity/player/Player;)V")
	public void inject(Player player, CallbackInfo ci) {
		nickname = RoleBuilder.buildPlayerRoleDisplay(player) + player.getDisplayName();
	}
}
