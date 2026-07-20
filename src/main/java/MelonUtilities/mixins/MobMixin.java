package MelonUtilities.mixins;

import MelonUtilities.MelonUtilities;
import MelonUtilities.utility.discord.DiscordChatRelay;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Mob.class, remap = false)
public abstract class MobMixin extends Entity {
    @Shadow public abstract boolean sendsDeathMessage(Entity entity);

    @Shadow public abstract String getDeathMessageKey(Entity entity);

    @Shadow public boolean isMultiplayerEntity;

    @Shadow public abstract String getDisplayName();

    @Shadow public abstract int getMaxHealth();

    public MobMixin(World world) {
        super(world);
    }



    @Inject(
            method = "onDeath",
            at = @At("RETURN")
    )
    void processDeathMessage(Entity entityKilledBy, CallbackInfo ci) {
        if((Mob)((Object)this) instanceof Player && !world.isClientSide && sendsDeathMessage(entityKilledBy)) {
            try {
                String victimName = Entity.getNameFromEntity((Mob)(Object)this, true);
                String[] args = entityKilledBy == null
                    ? new String[]{victimName}
                    : new String[]{victimName, Entity.getNameFromEntity(entityKilledBy, true)};
                String message = I18n.getInstance()
                    .translateKeyAndFormat(getDeathMessageKey(entityKilledBy), (Object[]) args)
                    .replaceAll("§.", "");
                DiscordChatRelay.sendDeathMessage(message);
            } catch (Exception e) {
                MelonUtilities.LOGGER.error("Failed to build/relay death message for Discord", e);
            }
        }
    }

	@Inject(
		method = "canSpawnHere",
		at = @At("RETURN")
	)
	void preventBedrockSpawning(CallbackInfoReturnable<Boolean> cir){
		int blockX = MathHelper.floor(x);
		int blockY = MathHelper.floor(bb.minY);
		int blockZ = MathHelper.floor(z);
		if(world.getBlock(blockX, blockY, blockZ).id() == Blocks.BEDROCK.id()){
			cir.setReturnValue(false);
			return;
		}
	}
}
