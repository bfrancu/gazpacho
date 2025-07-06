package gazpacho.core.persistence;

import gazpacho.core.persistence.model.*;
import lombok.experimental.UtilityClass;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.Properties;
import java.util.Random;

@UtilityClass
public class PersistenceUtilities {
    private final String H2_TEST_USER = "sa";
    private final String H2_TEST_PASS = "password";
    private final String H2_TEST_URL = "jdbc:h2:mem:test;DATABASE_TO_UPPER=false";
    private final Properties PROPERTIES = new Properties();
    private Connection CONNECTION;

    static {
        PROPERTIES.setProperty("user", H2_TEST_USER);
        PROPERTIES.setProperty("password", H2_TEST_PASS);
        try {
            CONNECTION = DriverManager.getConnection(H2_TEST_URL, PROPERTIES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration createTestDbConfiguration() {
        return new Configuration()
                .addAnnotatedClass(MediaSize.class)
                .addAnnotatedClass(Versioned.class)
                .addAnnotatedClass(MediaDataSource.class)
                .addAnnotatedClass(MediaItem.class)
                .addAnnotatedClass(Profile.class)
                .addAnnotatedClass(MediaItemSubscription.class)
                .addAnnotatedClass(Request.class)
                .addAnnotatedClass(Release.class)
                .addAnnotatedClass(Wish.class);
    }

    public ResultSet executeStatement(String sqlStatement) throws SQLException {
        PreparedStatement statement = CONNECTION.prepareStatement(sqlStatement);
        return statement.executeQuery();
    }

    public void executeUpdate(String sqlStatement) throws SQLException {
        Statement statement = CONNECTION.createStatement();
        statement.executeUpdate(sqlStatement);
    }

    public int randomInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("min must be <= max");
        return new Random().nextInt((max - min) + 1) + min;
    }
}
