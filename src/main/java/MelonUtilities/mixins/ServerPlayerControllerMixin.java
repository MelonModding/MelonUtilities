package MelonUtilities.mixins;

import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.managers.LockManager;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.world.ServerPlayerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerController.class, remap = false)
public class ServerPlayerControllerMixin {

	@Inject(
		method = "useOrPlaceItemStackOnTile",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/world/ICarriable;tryPlace(Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/Entity;IIILnet/minecraft/core/util/helper/Side;DD)Z"
		),
		cancellable = true
	)
	public void placeItemMixin(Player entityplayer, World world, ItemStack itemstack, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Lockable lockable = (Lockable) world.getTileEntity(x, y, z);

		if (lockable == null || entityplayer.isSneaking()) return;

		if (LockManager.determineAuthStatus(lockable, (PlayerServer) entityplayer) <= LockManager.UNTRUSTED)
			cir.setReturnValue(false);
	}

	@Inject(
		method = "useOrPlaceItemStackOnTile",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/item/ItemStack;useItem(Lnet/minecraft/core/entity/player/Player;Lnet/minecraft/core/world/World;IIILnet/minecraft/core/util/helper/Side;DD)Z"
		),
		cancellable = true
	)
	public void useItemMixin(Player entityplayer, World world, ItemStack itemstack, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Lockable lockable = (Lockable) world.getTileEntity(x, y, z);

		if (lockable == null || entityplayer.isSneaking()) return;

		if (LockManager.determineAuthStatus(lockable, (PlayerServer) entityplayer) <= LockManager.UNTRUSTED)
			cir.setReturnValue(false);
	}



}
