package lot.database.triggers;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CheckForSeatAvailabilityTrigger implements Trigger {
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {}

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        String oldSeatNumber = (String) oldRow[3];
        String newSeatNumber = (String) newRow[3];
        if (oldSeatNumber.equals(newSeatNumber)) {
            return;
        }

        String query =
                """
                UPDATE seats
                SET available = ?
                WHERE flightId = ? AND seatNumber = ?
                """;


        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, true);
            ps.setInt(2, (int) oldRow[1]);
            ps.setString(3, (String) oldRow[3]);

            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, false);
            ps.setInt(2, (int) newRow[1]);
            ps.setString(3, (String) newRow[3]);

            ps.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {}

    @Override
    public void remove() throws SQLException {}
}
