package MelonUtilities.listeners;


import MelonUtilities.sqlite.DatabaseManager;
import MelonUtilities.sqlite.log_events.LogEventBreak;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.enums.Priority;
import org.useless.serverlibe.api.event.player.PlayerDigEvent;

public class LogEventListener implements Listener {
	@EventListener(priority = Priority.HIGH)
	public void onBlockBroken(PlayerDigEvent digEvent){
		if(digEvent.status == PlayerDigEvent.DESTROY_BLOCK){
			System.out.println("broke block");
			DatabaseManager.connect((conn) -> LogEventBreak.insert(conn, digEvent.player.uuid.toString(), digEvent.player.world.getBlock(digEvent.x, digEvent.y, digEvent.z).getKey(), digEvent.x, digEvent.y, digEvent.z));
		}
	}
}
