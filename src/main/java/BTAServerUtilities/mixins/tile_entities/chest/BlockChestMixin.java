package BTAServerUtilities.mixins.tile_entities.chest;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.interfaces.TileEntityContainerInterface;
import BTAServerUtilities.utility.BSUtility;
import BTAServerUtilities.utility.UUIDHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockChest.class, remap = false)
public class BlockChestMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, EntityPlayer player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockTileEntity(x, y, z);

		if(iContainer.getIsLocked()){
			if(iContainer.getLockOwner() != null) {
				if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(player.username))
					&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getIsCommunityContainer()
					&& !Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(player.username).toString(), PlayerData.class).lockBypass){
					player.sendMessage(TextFormatting.RED + "Chest is Locked!");
					cir.setReturnValue(false);
					return;
				}
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "onBlockPlaced", cancellable = true)
	public void onBlockPlacedInject(World world, int x, int y, int z, Side placeSide, EntityLiving entity, double sideHeight, CallbackInfo ci, @Local(name = "type") BlockChest.Type type) {
		TileEntityChest existingChest = BSUtility.getOtherChest(world, (TileEntityChest) world.getBlockTileEntity(x, y, z));
		TileEntityChest placedChest = (TileEntityChest) world.getBlockTileEntity(x, y, z);

		TileEntityContainerInterface existingIContainer = (TileEntityContainerInterface) existingChest;
		TileEntityContainerInterface placedIContainer = (TileEntityContainerInterface) placedChest;

		if(existingIContainer != null) {
			placedIContainer.setLockOwner(existingIContainer.getLockOwner());
			placedIContainer.setIsLocked(existingIContainer.getIsLocked());
			placedIContainer.setTrustedPlayers(existingIContainer.getTrustedPlayers());
		}
	}
}
