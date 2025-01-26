package MelonUtilities.sqlite.log_events;

import net.minecraft.server.entity.player.PlayerServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogEventPosition {

	public static final String tableName = "PositionEvents";

	public static void insert(Connection conn, PlayerServer player) throws SQLException {
		String insertSQL = "INSERT INTO " + tableName + "(time, playerUUID, x, y, z, xRot, yRot, isSneaking, dimension) VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
		preparedStatement.setLong(1, System.currentTimeMillis());
		preparedStatement.setString(2, player.uuid.toString());
		preparedStatement.setDouble(3, player.x);
		preparedStatement.setDouble(4, player.y);
		preparedStatement.setDouble(5, player.z);
		preparedStatement.setDouble(6, player.xRot);
		preparedStatement.setDouble(7, player.yRot);
		preparedStatement.setBoolean(8, player.isSneaking());
		preparedStatement.setInt(9, player.dimension);
		preparedStatement.executeUpdate();
	}

	public static void createTable(Connection conn) {

	}

	public static void deleteTable(Connection conn) {

	}

	public static void printAllEvents(Connection conn) {

	}
}
