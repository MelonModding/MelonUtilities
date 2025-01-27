package MelonUtilities.mixins;

import MelonUtilities.utility.discord.DiscordChatRelay;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, remap = false)
public class WorldMixin {
    @Inject(
            method = "wakeUpAllPlayers",
            at = @At("RETURN")
    )
    public void sendServerSleepMessage(CallbackInfo ci) {
        DiscordChatRelay.sendServerSleepMessage();
    }
}
