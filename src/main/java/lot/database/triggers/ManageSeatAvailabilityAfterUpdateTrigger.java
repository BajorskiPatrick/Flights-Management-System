package lot.database.triggers;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Database trigger that manages seat availability when a reservation is updated.
 * Makes the old seat available and the new seat unavailable when a reservation's seat is changed.
 */
public class ManageSeatAvailabilityAfterUpdateTrigger implements Trigger {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public ManageSeatAvailabilityAfterUpdateTrigger() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {}

    /**
     * Trigger logic executed when a reservation is updated.
     * Updates seat availability status for both old and new seats.
     *
     * @param conn the database connection
     * @param oldRow the row values before update
     * @param newRow the row values after update
     * @throws SQLException if a database access error occurs
     */
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
