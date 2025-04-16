package lot.database.triggers;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Database trigger that makes a seat unavailable when a new reservation is created.
 */
public class MakeSeatUnavailableTrigger implements Trigger {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public MakeSeatUnavailableTrigger() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {}

    /**
     * Trigger logic executed when a reservation is created.
     * Marks the associated seat as unavailable.
     *
     * @param conn the database connection
     * @param oldRow null for INSERT operations
     * @param newRow the new row values
     * @throws SQLException if a database access error occurs
     */
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