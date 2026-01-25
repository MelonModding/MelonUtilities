package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import MelonUtilities.utility.discord.DiscordChatRelay;
import net.minecraft.core.net.packet.PacketLogin;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PacketHandlerLogin.class, remap = false)
public class PacketHandlerLoginMixinDiscord {
    @Inject(
            method = "doLogin",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/net/PlayerList;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            require = 0
    )
    private void sendJoinMessage(PacketLogin packet1login, CallbackInfo ci, PlayerServer player) {
        // Only process if Discord integration is enabled
        if (!Data.MainConfig.config.enableDiscordIntegration) {
            return;
        }

        if (player == null) {
            return;
        }

        String username = player.username;
        if (username == null || username.isEmpty()) {
            return;
        }

        DiscordChatRelay.sendJoinLeaveMessage(username, true);
    }
}
