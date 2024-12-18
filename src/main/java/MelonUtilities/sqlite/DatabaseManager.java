package MelonUtilities.sqlite;

import MelonUtilities.MelonUtilities;
import MelonUtilities.interfaces.QuickConnection;
import MelonUtilities.sqlite.log_events.LogEventBreak;
import MelonUtilities.sqlite.log_events.LogEventPlace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {


	public static void onInitilizeTest(){
		connect((conn -> {
			LogEventPlace.createTableIfNotExists(conn);
			LogEventBreak.createTableIfNotExists(conn);

/*			MelonUtilities.LOGGER.info("Inserting data...");
			LogEventPlace.insert(conn, "testUUID9807987839123", "block.thingy:0", 10, 120, 12);
			LogEventPlace.insert(conn, "testUUID1110001110001", "block.biggerthingy:56", 12, 111, 16);
			LogEventBreak.insert(conn, "testUUID9807987839123", "block.thingy:0", 10, 120, 12);
			LogEventBreak.insert(conn, "testUUID1110001110001", "block.biggerthingy:56", 12, 111, 16);
			MelonUtilities.LOGGER.info("Displaying database...");
			LogEventPlace.printAllEvents(conn);
			LogEventBreak.printAllEvents(conn);*/
		}));
	}

	public static void connect(QuickConnection qConn){
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:c:/sqlite/db/chinook.db")) {
			qConn.run(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			MelonUtilities.LOGGER.error("{}: {}", e.getClass().getName(), e.getMessage());
		}
	}

	private static void deleteAllTables(Connection conn) throws SQLException {
		LogEventPlace.deleteTableIfExists(conn);
		LogEventBreak.deleteTableIfExists(conn);
	}

	private static void createAllTables(Connection conn) throws SQLException {
		LogEventPlace.createTableIfNotExists(conn);
		LogEventBreak.createTableIfNotExists(conn);
	}

	public static void clearAllTables(Connection conn) throws SQLException {
		deleteAllTables(conn);
		createAllTables(conn);
	}

	public static void printAllTables(Connection conn) throws SQLException {

	}


}

/*
private static final Logger log = LoggerFactory.getLogger(LogManager.class);

public static String getHeader(){
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	return "(" + sdf.format(System.currentTimeMillis()) + ")";
}

public static void logEventPlace(UUID uuid, ItemStack itemStack, int x, int y, int z){
	if(itemStack == null){
		return;
	}
	Data.Users.log(uuid, getHeader() + " placed [" + MUtil.itemStackToString(itemStack, false) + "] at [" + x + " " + y + " " + z + "]");
}

public static void logEventBreak(UUID uuid, Block<?> block, int x, int y, int z){
	Data.Users.log(uuid, getHeader() + " broke [" + block.getKey() + "] at [" + x + " " + y + " " + z + "]");
}

public static void logEventLogin(UUID uuid){
	Data.Users.log(uuid, getHeader() + " logged in");
}

public static void logEventDisconnect(UUID uuid){
	Data.Users.log(uuid, getHeader() + " disconnected");
}

public static void logEventOpenContainer(UUID uuid, Block<?> block, int x, int y, int z){
	Data.Users.log(uuid, getHeader() + " opened [" + block.getKey() + "] at [" + x + " " + y + " " + z + "]");
}

public static void logEventCloseContainer(InventoryCloseEvent closeEvent){
	Player player = closeEvent.player;
	HitResult hitResult = MUtil.rayCastFromPlayer(player);

	if (hitResult != null && hitResult.hitType == HitResult.HitType.TILE) {
		int containerX = MathHelper.floor(hitResult.x);
		int containerY = MathHelper.floor(hitResult.y);
		int containerZ = MathHelper.floor(hitResult.z);
		if
		(
			player.world.getBlock(containerX, containerY, containerZ) == Blocks.FURNACE_BLAST_IDLE ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.FURNACE_BLAST_ACTIVE ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.CHEST_LEGACY ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.CHEST_LEGACY_PAINTED ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.CHEST_PLANKS_OAK ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.CHEST_PLANKS_OAK_PAINTED ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.DISPENSER_COBBLE_STONE ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.FURNACE_STONE_IDLE ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.FURNACE_STONE_ACTIVE ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.TROMMEL_IDLE ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.TROMMEL_ACTIVE ||
				player.world.getBlock(containerX, containerY, containerZ) == Blocks.ACTIVATOR_COBBLE_NETHERRACK
		) {
			Data.Users.log(player.uuid, getHeader() + " closed container");
		}
	}
}

public static void logEventInteractContainer(InventoryClickEvent clickEvent){
	Player player = clickEvent.player;
	HitResult hitResult = MUtil.rayCastFromPlayer(player);
	StringBuilder logBuilder = new StringBuilder();
	logBuilder.append(getHeader());

	switch (clickEvent.action) {

		case CLICK_LEFT:
			if(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer() instanceof ContainerInventory) {
				return;
			}

			if (hitResult != null && hitResult.hitType == HitResult.HitType.TILE) {
				int containerX = MathHelper.floor(hitResult.x);
				int containerY = MathHelper.floor(hitResult.y);
				int containerZ = MathHelper.floor(hitResult.z);

				if (clickEvent.itemStack != null && player.inventory.getHeldItemStack() == null){
					logBuilder
						.append(" took [")
						.append(MUtil.itemStackToString(clickEvent.itemStack, true))
						.append("] from slot [")
						.append(clickEvent.args[0])
						.append("] of [")
						.append(player.world.getBlock(containerX, containerY, containerZ).getKey())
						.append(" (").append(containerX).append(" ").append(containerY).append(" ").append(containerZ)
						.append(")]");
				}
				if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack == null) {
					logBuilder
						.append(" put [")
						.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
						.append("] into slot [")
						.append(clickEvent.args[0])
						.append("] of [")
						.append(player.world.getBlock(containerX, containerY, containerZ).getKey())
						.append(" (").append(containerX).append(" ").append(containerY).append(" ").append(containerZ)
						.append(")]");
				}
				if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack != null) {
					logBuilder
						.append(" swapped [")
						.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
						.append("] with [")
						.append(MUtil.itemStackToString(clickEvent.itemStack, true))
						.append("] in slot [")
						.append(clickEvent.args[0])
						.append("] of [")
						.append(player.world.getBlock(containerX, containerY, containerZ).getKey())
						.append(" (").append(containerX).append(" ").append(containerY).append(" ").append(containerZ)
						.append(")]");
				}
				Data.Users.log(player.uuid, logBuilder.toString());
				return;
			}

			if (clickEvent.itemStack != null && player.inventory.getHeldItemStack() == null) {
				logBuilder
					.append(" took [")
					.append(MUtil.itemStackToString(clickEvent.itemStack, true))
					.append("] from slot [")
					.append(clickEvent.args[0])
					.append("] of [")
					.append(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer().getName())
					.append(" (").append(player.x).append(" ").append(player.y).append(" ").append(player.z)
					.append(")] (HitResult not container, resorted to backup syntax)");
			}
			if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack == null) {
				logBuilder
					.append(" put [")
					.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
					.append("] into slot [")
					.append(clickEvent.args[0])
					.append("] of [")
					.append(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer().getName())
					.append(" (").append(player.x).append(" ").append(player.y).append(" ").append(player.z)
					.append(")] (HitResult not container, resorted to backup syntax)");
			}
			if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack != null) {
				logBuilder
					.append(" swapped [")
					.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
					.append("] with [")
					.append(MUtil.itemStackToString(clickEvent.itemStack, true))
					.append("] in slot [")
					.append(clickEvent.args[0])
					.append("] of [")
					.append(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer().getName())
					.append(" (").append(player.x).append(" ").append(player.y).append(" ").append(player.z)
					.append(")] (HitResult not container, resorted to backup syntax)");
			}
			Data.Users.log(player.uuid, logBuilder.toString());
			return;

		case CLICK_RIGHT:
			if(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer() instanceof ContainerInventory) {
				return;
			}
			if (hitResult != null && hitResult.hitType == HitResult.HitType.TILE) {
				int containerX = MathHelper.floor(hitResult.x);
				int containerY = MathHelper.floor(hitResult.y);
				int containerZ = MathHelper.floor(hitResult.z);

				if (clickEvent.itemStack != null && player.inventory.getHeldItemStack() == null){
					logBuilder
						.append(" took half of [")
						.append(MUtil.itemStackToString(clickEvent.itemStack, true))
						.append("] from slot [")
						.append(clickEvent.args[0])
						.append("] of [")
						.append(player.world.getBlock(containerX, containerY, containerZ).getKey())
						.append(" (").append(containerX).append(" ").append(containerY).append(" ").append(containerZ)
						.append(")]");
				}
				if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack == null) {
					logBuilder
						.append(" put one [")
						.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
						.append("] into slot [")
						.append(clickEvent.args[0])
						.append("] of [")
						.append(player.world.getBlock(containerX, containerY, containerZ).getKey())
						.append(" (").append(containerX).append(" ").append(containerY).append(" ").append(containerZ)
						.append(")]");
				}
				if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack != null) {
					logBuilder
						.append(" swapped [")
						.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
						.append("] with [")
						.append(MUtil.itemStackToString(clickEvent.itemStack, true))
						.append("] in slot [")
						.append(clickEvent.args[0])
						.append("] of [")
						.append(player.world.getBlock(containerX, containerY, containerZ).getKey())
						.append(" (").append(containerX).append(" ").append(containerY).append(" ").append(containerZ)
						.append(")]");
				}
				Data.Users.log(player.uuid, logBuilder.toString());
				return;
			}

			if (clickEvent.itemStack != null && player.inventory.getHeldItemStack() == null) {
				logBuilder
					.append(" took half of [")
					.append(MUtil.itemStackToString(clickEvent.itemStack, true))
					.append("] from slot [")
					.append(clickEvent.args[0])
					.append("] of [")
					.append(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer().getName())
					.append(" (").append(player.x).append(" ").append(player.y).append(" ").append(player.z)
					.append(")] (HitResult not container, resorted to backup syntax)");
			}
			if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack == null) {
				logBuilder
					.append(" put one [")
					.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
					.append("] into slot [")
					.append(clickEvent.args[0])
					.append("] of [")
					.append(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer().getName())
					.append(" (").append(player.x).append(" ").append(player.y).append(" ").append(player.z)
					.append(")] (HitResult not container, resorted to backup syntax)");
			}
			if (player.inventory.getHeldItemStack() != null && clickEvent.itemStack != null) {
				logBuilder
					.append(" swapped [")
					.append(MUtil.itemStackToString(player.inventory.getHeldItemStack(), true))
					.append("] with [")
					.append(MUtil.itemStackToString(clickEvent.itemStack, true))
					.append("] in slot [")
					.append(clickEvent.args[0])
					.append("] of [")
					.append(player.craftingInventory.slots.get(clickEvent.args[0]).getContainer().getName())
					.append(" (").append(player.x).append(" ").append(player.y).append(" ").append(player.z)
					.append(")] (HitResult not container, resorted to backup syntax)");
			}
			Data.Users.log(player.uuid, logBuilder.toString());
			return;

		case HOTBAR_ITEM_SWAP:
			return;
		case DROP:
			return;
		case LOCK:
			return;
		case PICKUP_SIMILAR:
			return;
		case MOVE_ALL:
			return;
		case MOVE_SIMILAR:
			return;
		case MOVE_SINGLE_ITEM:
			return;
		case MOVE_STACK:
			return;
	}


}

public static void logEventHit(UUID uuid){

}

public static void logEventUseItem(UUID uuid){

}

public static void logEventPosition(UUID uuid){

}

public static void logEventChat(UUID uuid){

}*/
