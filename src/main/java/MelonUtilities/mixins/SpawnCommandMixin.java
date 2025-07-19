package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.commands.CommandSpawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CommandSpawn.class, remap = false)
public class SpawnCommandMixin {

	/**
	 * @author ipiepiepie
	 * @reason override default spawn command, which accessible only for admins.
	 */
	@Inject(
		method = "register",
		at = @At("HEAD"),
		cancellable = true
	)
	private void setSpawn(CommandDispatcher<CommandSource> dispatcher, CallbackInfo ci) {
		if(!Data.MainConfig.config.enableSpawn) return;

		new MelonUtilities.command.commands.CommandSpawn().register(dispatcher);

		ci.cancel();
	}

}
