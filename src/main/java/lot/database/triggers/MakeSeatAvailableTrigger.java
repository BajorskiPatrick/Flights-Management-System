package lot.database.triggers;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Database trigger that makes a seat available when a reservation is deleted.
 */
public class MakeSeatAvailableTrigger implements Trigger {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public MakeSeatAvailableTrigger() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {}

    /**
     * Trigger logic executed when a reservation is deleted.
     * Marks the associated seat as available.
     *
     * @param conn the database connection
     * @param oldRow the row values before deletion
     * @param newRow null for DELETE operations
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SQLException {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() throws SQLException {}
}
