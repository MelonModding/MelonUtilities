package MelonUtilities.sqlite.log_events;

import MelonUtilities.MelonUtilities;

import java.sql.*;

public class LogEventPlace {

	public static final String tableName = "PlaceEvents";

	public static void insert(Connection conn, String playerUUID, String itemKey, int x, int y, int z) throws SQLException {
		String insertSQL = "INSERT INTO " + tableName + "(time, playerUUID, itemKey, x, y, z) VALUES(?,?,?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
		preparedStatement.setLong(1, System.currentTimeMillis());
		preparedStatement.setString(2, playerUUID);
		preparedStatement.setString(3, itemKey);
		preparedStatement.setInt(4, x);
		preparedStatement.setInt(5, y);
		preparedStatement.setInt(6, z);
		preparedStatement.executeUpdate();
	}

	public static void createTable(Connection conn) throws SQLException {
		String createTableSQL =
			"CREATE TABLE  " + tableName + " " +
				"( " +
				"time long, " +
				"playerUUID varchar(255), " +
				"itemKey varchar(255), " +
				"x integer, " +
				"y integer, " +
				"z integer " +
				"); ";
		Statement statement = conn.createStatement();
		statement.execute(createTableSQL);
	}

	public static void deleteTable(Connection conn) throws SQLException {
		String deleteTableSQL = "DROP TABLE " + tableName;
		Statement statement = conn.createStatement();
		statement.execute(deleteTableSQL);
	}

	public static void printAllEvents(Connection conn) throws SQLException {
		String selectSQL = "SELECT * from " + tableName;
		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery(selectSQL);

		System.out.println("------ " + tableName + " ------");
		while (resultSet.next()){
			MelonUtilities.LOGGER.info
				(
					tableName + ": " +
						resultSet.getLong("time") + ", " +
						resultSet.getString("playerUUID") + ", " +
						resultSet.getString("itemKey") + ", " +
						resultSet.getInt("x") + ", " +
						resultSet.getInt("y") + ", " +
						resultSet.getInt("z")
				);
		}
		System.out.println("-------------------------------");
	}
}
