package MelonUtilities.mixins;

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
            at = @At("HEAD")
    )
    public void sendStopMessage(CallbackInfo ci) {
        DiscordChatRelay.sendServerStoppedMessage();
    }

}
