package lot.database.triggers;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MakeSeatUnavailableTrigger implements Trigger {
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {}

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        String query =
                """
                UPDATE seats
                SET available = false
                WHERE flightId = ? AND seatNumber = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, (int) newRow[1]);
            ps.setString(2, (String) newRow[3]);

            ps.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {}

    @Override
    public void remove() throws SQLException {}
}