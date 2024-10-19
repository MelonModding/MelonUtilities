package MelonUtilities.mixins.tile_entities.dispenser;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.UUIDHelper;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(value = TileEntityDispenser.class, remap = false)
public class TileEntityDispenserMixin implements TileEntityContainerInterface {
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

	@Inject(at = @At("HEAD"), method = "canInteractWith", cancellable = true)
	public void canInteractWithInject(EntityPlayer entityplayer, CallbackInfoReturnable<Boolean> cir) {
		if(isLocked){
			if(lockOwner != null) {
				if (!lockOwner.equals(UUIDHelper.getUUIDFromName(entityplayer.username))
					&& !trustedPlayers.contains(UUIDHelper.getUUIDFromName(entityplayer.username))
					&& !Data.playerData.getOrCreate(lockOwner.toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(entityplayer.username))
					&& !isCommunityContainer
					&& !Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(entityplayer.username).toString(), PlayerData.class).lockBypass){
					cir.setReturnValue(false);
					return;
				}
			}
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
	public void setLockOwner(String username) {
		lockOwner = UUIDHelper.getUUIDFromName(username);
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
	public void setTrustedPlayers(List<UUID> trustedPlayers) {
		this.trustedPlayers.clear();
		this.trustedPlayers.addAll(trustedPlayers);
	}

	@Override
	public void addTrustedPlayer(String username) {
		trustedPlayers.add(UUIDHelper.getUUIDFromName(username));
	}

	@Override
	public void addTrustedPlayer(UUID uuid) {
		trustedPlayers.add(uuid);
	}

	@Override
	public void removeTrustedPlayer(String username) {
		trustedPlayers.remove(UUIDHelper.getUUIDFromName(username));
	}

	@Override
	public void removeTrustedPlayer(UUID uuid) {
		trustedPlayers.remove(uuid);
	}
}
