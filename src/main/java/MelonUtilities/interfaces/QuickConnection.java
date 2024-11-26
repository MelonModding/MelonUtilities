package MelonUtilities.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface QuickConnection {
	void run(Connection conn) throws SQLException;
}
