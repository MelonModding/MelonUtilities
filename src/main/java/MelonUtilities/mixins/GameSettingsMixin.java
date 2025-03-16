package MelonUtilities.mixins;

import MelonUtilities.MelonUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.client.option.OptionInteger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(value = GameSettings.class, remap = false)
public abstract class GameSettingsMixin {

	@Unique
	public OptionBoolean elevatorAllowObstructions;

	@Unique
	public OptionInteger elevatorCooldown;

	@Inject(method = "<init>", at = @At(value = "NEW", target = "(Ljava/io/File;Ljava/lang/String;)Ljava/io/File;"))
	public void addOptions(Minecraft minecraft, File file, CallbackInfo ci){
		MelonUtilities.initOptions((GameSettings) (Object)this);
		this.elevatorAllowObstructions = MelonUtilities.elevatorAllowObstructions;
		this.elevatorCooldown = MelonUtilities.elevatorCooldown;

	}
}
