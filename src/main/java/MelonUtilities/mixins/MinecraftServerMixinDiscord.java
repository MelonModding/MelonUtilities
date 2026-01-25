package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import MelonUtilities.utility.discord.DiscordChatRelay;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixinDiscord {

    @Inject(
            method = "initiateShutdown",
            at = @At("HEAD"),
            require = 0
    )
    private void sendStopMessage(CallbackInfo ci) {
        // Only process if Discord integration is enabled
        if (!Data.MainConfig.config.enableDiscordIntegration) {
            return;
        }

        DiscordChatRelay.sendServerStoppedMessage();
    }

}
