package MelonUtilities.sqlite.log_events;

import MelonUtilities.MelonUtilities;

import java.sql.*;

public class LogEventBreak {

	public static final String tableName = "BreakEvents";

	public static void insert(Connection conn, String playerUUID, String blockKey, int x, int y, int z) throws SQLException {
		String insertSQL = "INSERT INTO " + tableName + "(playerUUID, blockKey, x, y, z) VALUES(?,?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
		preparedStatement.setString(1, playerUUID);
		preparedStatement.setString(2, blockKey);
		preparedStatement.setInt(3, x);
		preparedStatement.setInt(4, y);
		preparedStatement.setInt(5, z);
		preparedStatement.executeUpdate();
	}

	public static void createTable(Connection conn) throws SQLException {
		String createTableSQL =
			"CREATE TABLE  " + tableName + " " +
				"( " +
				"playerUUID varchar(255), " +
				"blockKey varchar(255), " +
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
						resultSet.getString("playerUUID") + ", " +
						resultSet.getString("blockKey") + ", " +
						resultSet.getInt("x") + ", " +
						resultSet.getInt("y") + ", " +
						resultSet.getInt("z")
				);
		}
		System.out.println("-------------------------------");
	}
}
