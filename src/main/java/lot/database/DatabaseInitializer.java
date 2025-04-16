package lot.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.TestOnly;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * Initializes and manages the database connection pool and schema.
 * Uses HikariCP for connection pooling and handles database schema initialization.
 */
public class DatabaseInitializer {
    private static DataSource dataSource;

    static {
        configureDefault();
    }

    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public DatabaseInitializer() {}

    private static void configureDefault() {
        String dbUrl = "jdbc:h2:./lotdb;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
        String dbUser = "sa";
        String dbPass = "";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPass);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Sets dataSource field to provided DataSource object
     *
     * @param ds DataSource object to set dataSource field to
     */
    @TestOnly
    public static void overrideDataSource(DataSource ds) {
        dataSource = ds;
    }

    /**
     * Resets dataSource field to its default value
     */
    @TestOnly
    public static void resetToDefault() {
        configureDefault();
    }

    /**
     * Initializes the database by executing schema and data scripts.
     * Only loads initial data if new tables were created during schema initialization.
     *
     * @throws SQLException if a database access error occurs
     * @throws IOException if there's an error reading schema or data files
     */
    public static void initialize() throws SQLException, IOException {
        Connection conn = DatabaseInitializer.getConnection();
        Statement stmt = conn.createStatement();

        InputStream schema = DatabaseInitializer.class.getResourceAsStream("/db/schema.sql");
        String schemaQuery = new String(schema.readAllBytes(), StandardCharsets.UTF_8);

        int countBefore = countTables(conn);
        stmt.execute(schemaQuery);
        schema.close();
        int countAfter = countTables(conn);

        if (countAfter > countBefore) {
            InputStream data = DatabaseInitializer.class.getResourceAsStream("/db/data.sql");
            String dataQuery = new String(data.readAllBytes(), StandardCharsets.UTF_8);
            stmt.execute(dataQuery);
            data.close();
        }

        stmt.close();
        conn.close();
    }

    /**
     * Gets a database connection from the connection pool.
     *
     * @return a Connection object from the pool
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Counts the number of tables in the database.
     *
     * @param conn the database connection to use
     * @return the number of tables in the database
     * @throws SQLException if a database access error occurs
     */
    private static int countTables(Connection conn) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, "%", null)) {
            int count = 0;
            while (rs.next()) {
                count++;
            }
            return count;
        }
    }
}
