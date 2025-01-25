package MelonUtilities.mixins;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = net.minecraft.core.net.packet.PacketAddPlayer.class, remap = false)
public class PacketAddPlayerMixin {

	//TODO fix bta src to allow for the display of long playertags/labels
/*
	@Shadow
	public String nickname;

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/core/entity/player/Player;)V")
	public void inject(Player player, CallbackInfo ci) {
		nickname = RoleBuilder.buildPlayerRoleDisplay(player) + player.getDisplayName();
	}*/
}
