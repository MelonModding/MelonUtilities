package MelonUtilities.mixins.tile_entities.dispenser;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.LockManager;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.PacketSetHeldObject;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = TileEntityDispenser.class, remap = false)
public class TileEntityDispenserMixin implements Lockable {
	@Unique
	private boolean isLocked;

	@Unique
	private boolean isCommunityContainer;

	@Unique
	private UUID lockOwner;

	@Unique
	private final List<UUID> trustedPlayers = new ArrayList<>();

@Inject(at = @At("TAIL"), method = "writeToNBT")
	public void writeToNBTInject(CompoundTag nbttagcompound, CallbackInfo ci){
		nbttagcompound.putBoolean("isLocked", isLocked);
		UUIDHelper.writeToTag(nbttagcompound, lockOwner, "lockOwner");
		nbttagcompound.putBoolean("isCommunityContainer", isCommunityContainer);

		ListTag trustedPlayers = new ListTag();
		for(UUID uuid : this.trustedPlayers){
			CompoundTag compoundTag = new CompoundTag();
			UUIDHelper.writeToTag(compoundTag, uuid, "uuid");
			trustedPlayers.addTag(compoundTag);
		}
		nbttagcompound.putList("trustedPlayers", trustedPlayers);
	}

	@Inject(at = @At("TAIL"), method = "readFromNBT")
	public void readFromNBTInject(CompoundTag nbttagcompound, CallbackInfo ci){
		isLocked = nbttagcompound.getBooleanOrDefault("isLocked", false);
		lockOwner = UUIDHelper.readFromTag(nbttagcompound, "lockOwner");
		isCommunityContainer = nbttagcompound.getBooleanOrDefault("isCommunityContainer", false);

		ListTag tempListTag = nbttagcompound.getList("trustedPlayers");

		for(Tag<?> tag : tempListTag){
			if(tag instanceof CompoundTag){
				CompoundTag compoundTag = (CompoundTag) tag;
				trustedPlayers.add(UUIDHelper.readFromTag(compoundTag, "uuid"));
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "stillValid", cancellable = true)
	public void canInteractWithInject(Player entityplayer, CallbackInfoReturnable<Boolean> cir) {
		if(entityplayer instanceof PlayerServer && LockManager.determineAuthStatus(this, (PlayerServer) entityplayer) <= LockManager.UNTRUSTED){
			cir.setReturnValue(false);
			return;
		}
	}

	@Inject(at = @At("HEAD"), method = "canBeCarried", cancellable = true)
	public void canBeCarriedInject(World world, Entity potentialHolder, CallbackInfoReturnable<Boolean> cir){
		if(potentialHolder instanceof PlayerServer && LockManager.determineAuthStatus(this, (PlayerServer) potentialHolder) <= LockManager.COMMUNITY){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, (PlayerServer) potentialHolder, "Failed to Pickup Container! (Not Authorized)");
			((PlayerServer) potentialHolder).playerNetServerHandler.sendPacket(new PacketSetHeldObject(potentialHolder.id, ((PlayerServer) potentialHolder).getHeldObject()));
			cir.setReturnValue(false);
			return;
		}
	}

	@Override
	public boolean getIsLocked() {
		return isLocked;
	}

	@Override
	public boolean getIsCommunityContainer() {
		return isCommunityContainer;
	}

	@Override
	public void setIsLocked(boolean flag) {
		isLocked = flag;
	}

	@Override
	public void setIsCommunityContainer(boolean flag) {
		isCommunityContainer = flag;
	}

	@Override
	public UUID getLockOwner() {
		return lockOwner;
	}

	@Override
	public void setLockOwner(UUID owner) {
		lockOwner = owner;
	}

	@Override
	public List<UUID> getTrustedPlayers() {
		return trustedPlayers;
	}

	@Override
	public Map<UUID, Boolean> getAllTrustedPlayers() {
		Map<UUID, Boolean> tempTrustedPlayers = new HashMap<>(Collections.emptyMap());
		for(UUID uuid : trustedPlayers){
			tempTrustedPlayers.put(uuid, false);
		}
		for(Map.Entry<UUID, String> entry : Data.Users.getOrCreate(lockOwner).usersTrustedToAllContainers.entrySet()){
			tempTrustedPlayers.put(entry.getKey(), true);
		}
		return tempTrustedPlayers;
	}

	@Override
	public void setTrustedPlayers(List<UUID> trustedPlayers) {
		this.trustedPlayers.clear();
		this.trustedPlayers.addAll(trustedPlayers);
	}

	@Override
	public void addTrustedPlayer(UUID uuid) {
		trustedPlayers.add(uuid);
	}

	@Override
	public void removeTrustedPlayer(UUID uuid) {
		trustedPlayers.remove(uuid);
	}
}
