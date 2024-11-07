package MelonUtilities.mixins.other;

import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.commands.utility.HelpCommand;
import MelonUtilities.commands.utility.MUCommand;
import MelonUtilities.commands.utility.ReloadCommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.net.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
			//TODO CommandManager.registerCommand(new LockCommand());

			// Misc
			//TODO CommandManager.registerCommand(new WhereAmICommand());

			// Role
			CommandManager.registerCommand(new RoleCommand());

			// Rollback
			//TODO CommandManager.registerCommand(new RollbackCommand());

			// Tpa
			//TODO CommandManager.registerCommand(new TPACommand());
			//TODO CommandManager.registerCommand(new TPAcceptCommand());
			//TODO CommandManager.registerCommand(new TPADenyCommand());

			// Utility
			//TODO CommandManager.registerCommand(new HelperCommand());
			CommandManager.registerCommand(new MUCommand());
			CommandManager.registerCommand(new ReloadCommand());

			// Warp

			// Rules
			//TODO CommandManager.registerCommand(new RulesCommand());

			// Anything Else
		}
	}
}
