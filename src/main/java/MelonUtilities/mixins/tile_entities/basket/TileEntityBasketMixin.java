package MelonUtilities.mixins.tile_entities.basket;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(value = TileEntityBasket.class, remap = false)
public class TileEntityBasketMixin implements TileEntityContainerInterface {
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

	@Inject(at = @At("HEAD"), method = "givePlayerAllItems", cancellable = true)
	public void givePlayerAllItemsInject(World world, Player entityplayer, CallbackInfo ci) {
		if(isLocked){
			if(lockOwner != null) {
				if (!lockOwner.equals(entityplayer.uuid)
					&& !trustedPlayers.contains(entityplayer.uuid)
					&& !Data.Users.getOrCreate(lockOwner).usersTrustedToAllContainers.containsKey(entityplayer.uuid)
					&& !isCommunityContainer
					&& !Data.Users.getOrCreate(entityplayer.uuid).lockBypass){
					ci.cancel();
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
	public void addTrustedPlayer(UUID uuid) {
		trustedPlayers.add(uuid);
	}

	@Override
	public void removeTrustedPlayer(UUID uuid) {
		trustedPlayers.remove(uuid);
	}
}
