package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import MelonUtilities.utility.discord.DiscordChatRelay;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.net.packet.PacketChat;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHandlerServer.class, remap = false)
public class PacketHandlerServerMixinDiscord {
    @Shadow private PlayerServer playerEntity;

    @Inject(
            method = "handleChat",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/net/ChatEmotes;process(Ljava/lang/String;)Ljava/lang/String;", shift = At.Shift.AFTER),
            require = 0
    )
    private void onChatProcessed(PacketChat packet, CallbackInfo ci, @Local String message) {
        // Only process if Discord integration is enabled
        if (!Data.MainConfig.config.enableDiscordIntegration) {
            return;
        }

        if (playerEntity == null) {
            return;
        }

        String username = playerEntity.username;
        if (username == null || username.isEmpty()) {
            return;
        }

        // 'message' is the processed message from ChatEmotes.process
        // Send to Discord
        DiscordChatRelay.sendToDiscord(username, message);
    }

    @Inject(
            method = "handleErrorMessage",
            at = @At("HEAD"),
            require = 0
    )
    private void sendLeaveMessage(String s, Object[] aobj, CallbackInfo ci) {
        // Only process if Discord integration is enabled
        if (!Data.MainConfig.config.enableDiscordIntegration) {
            return;
        }

        if (playerEntity == null) {
            return;
        }

        String username = playerEntity.username;
        if (username == null || username.isEmpty()) {
            return;
        }

        DiscordChatRelay.sendJoinLeaveMessage(username, false);
    }
}
