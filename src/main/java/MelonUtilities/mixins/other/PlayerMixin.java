package MelonUtilities.mixins.other;

import MelonUtilities.interfaces.PlayerCustomInputFunctionInterface;
import net.minecraft.core.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Player.class, remap = false)
public class PlayerMixin implements PlayerCustomInputFunctionInterface {

	@Unique
	@Nullable
	private CustomInput customInput;

	@Override
	@Nullable
	public CustomInput melonutilities$getCustomInputFunction() {
		return customInput;
	}

	@Override
	public void melonutilities$setCustomInputFunction(@Nullable CustomInput customInput) {
		this.customInput = customInput;
	}
}
