package MelonUtilities.listeners;

import MelonUtilities.interfaces.PlayerCustomInputFunctionInterface;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.event.player.PlayerChatEvent;

public class ChatInputListener implements Listener {
	@EventListener
	public void chatInputListener(PlayerChatEvent chatEvent){
		PlayerCustomInputFunctionInterface player = (PlayerCustomInputFunctionInterface) chatEvent.player;
		if(player.melonutilities$getCustomInputFunction() != null){
			PlayerCustomInputFunctionInterface.CustomInput customInput = player.melonutilities$getCustomInputFunction();
			customInput.apply(chatEvent.originalMessage);
			player.melonutilities$setCustomInputFunction(null);
			chatEvent.setCancelled(true);
		}
	}
}
