package MelonUtilities.mixins;

import net.minecraft.server.entity.EntityTrackerEntryImpl;
import org.spongepowered.asm.mixin.Mixin;

//Player Label Showing Roles. Currently limited by string cap

@Mixin(value = EntityTrackerEntryImpl.class, remap = false)
public abstract class EntityTrackerEntryImplMixin {
	/*@Shadow
	public Entity trackedEntity;

	@Shadow
	public abstract void sendPacketToTrackedPlayersAndTrackedEntity(Packet packet);

	@Redirect(at = @At(ordinal = 2, value = "INVOKE", target = "Lnet/minecraft/server/entity/EntityTrackerEntryImpl;sendPacketToTrackedPlayersAndTrackedEntity(Lnet/minecraft/core/net/packet/Packet;)V"), method = "tick")
	public void tick(EntityTrackerEntryImpl instance, Packet packet) {
		if(trackedEntity instanceof Player){
			this.sendPacketToTrackedPlayersAndTrackedEntity(new PacketEntityNickname(this.trackedEntity.id, RoleBuilder.buildPlayerRoleDisplay((Player) trackedEntity) + ((Mob)this.trackedEntity).nickname, ((Mob)this.trackedEntity).chatColor));
		} else {
			this.sendPacketToTrackedPlayersAndTrackedEntity(packet);
		}
	}*/
}
