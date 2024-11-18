package MelonUtilities.mixins.other;

import MelonUtilities.commands.lock.CommandLock;
import MelonUtilities.commands.role.CommandRole;
import MelonUtilities.commands.rollback.CommandRollback;
import MelonUtilities.commands.utility.CommandMelonUtilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.net.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CommandManager.class, remap = false)
public class CommandManagerMixin {
	@Inject(at = @At("HEAD"), method = "init")
	public void initInject(CallbackInfo ci) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER){
			// Crew
			// Helper
			// Home
			//TODO CommandManager.registerCommand(new SetHomeCommand());
			//TODO CommandManager.registerCommand(new HomeCommand());
			//TODO CommandManager.registerCommand(new DelHomeCommand());

			// Kit
			//TODO CommandManager.registerCommand(new KitCommand());
			//TODO CommandManager.registerCommand(new KittenCommand());

			// Lock
			CommandManager.registerCommand(new CommandLock());

			// Misc
			//TODO CommandManager.registerCommand(new WhereAmICommand());

			// Role
			CommandManager.registerCommand(new CommandRole());

			// Rollback
			CommandManager.registerCommand(new CommandRollback());

			// Tpa
			//TODO CommandManager.registerCommand(new TPACommand());
			//TODO CommandManager.registerCommand(new TPAcceptCommand());
			//TODO CommandManager.registerCommand(new TPADenyCommand());

			// Utility
			//TODO CommandManager.registerCommand(new HelperCommand());
			CommandManager.registerCommand(new CommandMelonUtilities());

			// Warp

			// Rules
			//TODO CommandManager.registerCommand(new RulesCommand());

			// Anything Else
		}
	}
}
