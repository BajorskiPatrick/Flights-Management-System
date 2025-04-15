package lot.database.triggers;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MakeSeatAvailableTrigger implements Trigger {
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {}

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        String query =
                """
                UPDATE seats
                SET available = true
                WHERE flightId = ? AND seatNumber = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, (int) oldRow[1]);
            ps.setString(2, (String) oldRow[3]);

            ps.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {}

    @Override
    public void remove() throws SQLException {}
}
