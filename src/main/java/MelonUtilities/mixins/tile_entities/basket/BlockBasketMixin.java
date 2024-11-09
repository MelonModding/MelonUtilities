package MelonUtilities.mixins.tile_entities.basket;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.helpers.UUIDHelper;
import net.minecraft.core.block.BlockBasket;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockBasket.class, remap = false)
public class BlockBasketMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockEntity(x, y, z);

		if(iContainer.getIsLocked()){
			if(iContainer.getLockOwner() != null) {
				if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(player.username))
					&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getIsCommunityContainer()
					&& !Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(player.username).toString(), PlayerData.class).lockBypass){
					player.sendMessage(TextFormatting.RED + "Basket is Locked!");
					cir.setReturnValue(false);
					return;
				}
			}
		}
	}
}
