package MelonUtilities.listeners;


import MelonUtilities.MelonUtilities;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.enums.Priority;
import org.useless.serverlibe.api.event.player.PlayerEntityInteractEvent;
import org.useless.serverlibe.api.event.player.PlayerItemUseEvent;
import org.useless.serverlibe.api.event.player.inventory.InventoryClickEvent;
import org.useless.serverlibe.api.event.player.inventory.InventoryCloseEvent;
import org.useless.serverlibe.api.event.player.inventory.InventoryServerOpenEvent;

import java.util.Arrays;

public class DebugInfoListener implements Listener {
	@EventListener(priority = Priority.HIGH)
	public void onItemUsed(PlayerItemUseEvent useEvent){
		useEvent.player.sendMessage(String.format("[%s] item right click", MelonUtilities.MOD_ID));
	}
	@EventListener(priority = Priority.HIGH)
	public void onGuiClose(InventoryCloseEvent closeEvent){
		closeEvent.player.sendMessage(String.format("[%s] GUI closed", MelonUtilities.MOD_ID));
	}
	@EventListener(priority = Priority.HIGH)
	public void onGuiOpen(InventoryServerOpenEvent openEvent){
		openEvent.player.sendMessage(String.format("[%s] GUI " + openEvent.windowTitle + " opened", MelonUtilities.MOD_ID));
	}
	@EventListener(priority = Priority.HIGH)
	public void onGuiClick(InventoryClickEvent clickEvent){
		clickEvent.player.sendMessage(String.format(String.format("[%s] action: %s, args: %s, actionID: %s, Itemstack: %s", MelonUtilities.MOD_ID, clickEvent.action, Arrays.toString(clickEvent.args), clickEvent.actionId, clickEvent.itemStack)));
	}
	@EventListener(priority = Priority.HIGH)
	public void onAttack(PlayerEntityInteractEvent interactEvent){
		interactEvent.player.sendMessage(String.format("[%s] target: %s, mouseButton: %d", MelonUtilities.MOD_ID, interactEvent.targetEntity, interactEvent.mouseButton));
	}
}
