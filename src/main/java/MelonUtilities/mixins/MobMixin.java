package MelonUtilities.mixins;

import MelonUtilities.utility.discord.DiscordChatRelay;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class, remap = false)
public abstract class MobMixin {
    @Shadow public abstract String getDeathMessage(Entity entity);

    @Shadow public boolean isMultiplayerEntity;

    @Shadow public abstract String getDisplayName();

    @Shadow public abstract int getMaxHealth();

    @Inject(
            method = "onDeath",
            at = @At("RETURN")
    )
    void processDeathMessage(Entity entity, CallbackInfo ci) {
        if((Mob)((Object)this) instanceof Player) {
            String message = getDeathMessage(entity).replaceAll("§.", "");
            DiscordChatRelay.sendDeathMessage(message);
        }
    }
}
