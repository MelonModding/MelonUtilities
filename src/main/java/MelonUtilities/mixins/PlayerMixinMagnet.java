package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.PlayerMagnetInterface;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixinMagnet implements PlayerMagnetInterface {

	@Shadow
	public ContainerInventory inventory;

	@Shadow
	public abstract ItemStack getHeldItem();

	@Unique
	boolean hasMagnet = false;

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci){
		if (Data.MainConfig.config.enableMagnets) {
			ItemStack headItem = this.inventory.armorInventory[IArmorItem.PIECE_HEAD];
			ItemStack heldItem = this.getHeldItem();

			hasMagnet =
				//item on head check
				headItem != null &&
				headItem.getItem().equals(Items.AMMO_FIREBALL) &&
				headItem.getMetadata() == 1 ||
				//held item check
				heldItem != null &&
				heldItem.getItem().equals(Items.AMMO_FIREBALL) &&
				heldItem.getMetadata() == 1;
		}
	}

	@Override
	public boolean hasMagnet() {
		return hasMagnet;
	}
}
