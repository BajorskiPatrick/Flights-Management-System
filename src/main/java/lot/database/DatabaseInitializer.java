package lot.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class DatabaseInitializer {
    private static final HikariDataSource dataSource;

    static {
        String dbUrl = "jdbc:h2:./lotdb;MODE=POSTGRESQL;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
        String dbUser = "sa";
        String dbPass = "";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPass);

        dataSource = new HikariDataSource(config);
    }


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

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

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
