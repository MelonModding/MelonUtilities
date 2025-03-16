package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import MelonUtilities.utility.MUtil;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixinElevator extends Mob {

	@Unique
	protected int elevatorBlockX;
	@Unique
	protected int elevatorBlockY;
	@Unique
	protected int elevatorBlockZ;
	@Unique
	protected boolean stoodOnElevator;
	@Unique
	protected int gracePeriod = 0;
	@Unique
	protected double py = 0;
	@Unique
	protected int cooldown = 0;
	@Unique
	protected Player thisAs = (Player)(Object)this;

	public PlayerMixinElevator(World world) {
		super(world);
	}

	@Inject(method= "onLivingUpdate()V", at = @At("TAIL"))
	private void elevatorTick(CallbackInfo ci){
		cooldown--;
		double dy = this.y-py;
		py = this.y;

		List<AABB> cubes = this.world.getCubes(this, this.bb.getInsetBoundingBox(this.xd, -1.0, 0.0));
		if (!cubes.isEmpty()){
			AABB cube = cubes.get(0);
			if (cube != null){

				int blockX = (int) cube.minX;
				int blockY = (int) cube.minY;
				int blockZ = (int) cube.minZ;
				Block<?> blockUnderFeet = world.getBlock(blockX, blockY, blockZ);

				if(blockUnderFeet == Blocks.BLOCK_STEEL) {
					gracePeriod = 0;
					stoodOnElevator = true;
					elevatorBlockX = blockX;
					elevatorBlockY = blockY;
					elevatorBlockZ = blockZ;
				}

				else if (blockUnderFeet != null || world.getBlockId(blockX, blockY, blockZ) == 0) {
					if(gracePeriod < 80){
						gracePeriod++;
					}
					if(gracePeriod == 80) {
						stoodOnElevator = false;
						cooldown += 1;
					}
				}

				if(isSneaking() && cooldown <= 0 && blockUnderFeet == Blocks.BLOCK_STEEL && stoodOnElevator){
					if (MUtil.sneakOnElevator(world, blockX, blockY, blockZ, thisAs))
						cooldown = Data.MainConfig.config.elevatorCooldown;
					return;
				}
			}
		}


		if(dy > 0.109 && cooldown <= 0 && stoodOnElevator && Math.abs(this.x - (elevatorBlockX+0.5f)) < 0.6f && Math.abs(this.z - (elevatorBlockZ+0.5f)) < 0.6f && this.y - elevatorBlockY > 0){
			if (MUtil.jumpOnElevator(world, elevatorBlockX, elevatorBlockY, elevatorBlockZ, thisAs)) {
				// reset y velocity and cooldown if we teleported
				cooldown = Data.MainConfig.config.elevatorCooldown;
				this.yd = 0;
			}
		}
	}

	@Inject(method = "jump",at=@At("HEAD"),cancellable = true)
	public void jump(CallbackInfo ci) {
		// prevent a jump if we just used an elevator
		// this is ONLY called clientside, theirs no good way to prevent it in multiplayer
		if (cooldown > 0) {
			ci.cancel();
		}
	}
}
