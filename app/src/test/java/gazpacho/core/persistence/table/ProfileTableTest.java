package gazpacho.core.persistence.table;

import gazpacho.core.persistence.model.*;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProfileTableTest {
    private static Configuration CONFIGURATION;
    private static final String TABLE_NAME = "UserProfiles";
    private static final String PHONE_NUMBER = "07541425";

    @BeforeAll
    public static void setUp() {
        CONFIGURATION = PersistenceUtilities.createTestDbConfiguration();
    }

    @Test
    void getProfileByPhoneNumber_returnsValue_whenPresent() throws SQLException {
        ProfileTable table = getTable();
        PersistenceUtilities.executeUpdate(
                String.format("INSERT INTO %s VALUES('2025-02-22 09:33:08.141314', 'John', 'Doe', '%s')", TABLE_NAME, PHONE_NUMBER)
        );

        assertTrue(table.getProfileByPhoneNumber(PHONE_NUMBER).isPresent());
    }

    @Test
    void remove_removesPersistedInstance() throws SQLException {
        ProfileTable table = getTable();
        PersistenceUtilities.executeUpdate(
                String.format("INSERT INTO %s VALUES('2025-02-22 09:33:08.141314', 'John', 'Doe', '%s')", TABLE_NAME, PHONE_NUMBER)
        );

        assertTrue(table.getProfileByPhoneNumber(PHONE_NUMBER).isPresent());
        table.remove(PHONE_NUMBER);

        ResultSet resultSet = PersistenceUtilities.executeStatement(
                String.format("SELECT * FROM %s WHERE phone_number like '%s'", TABLE_NAME, PHONE_NUMBER)
        );

        assertFalse(resultSet.next());
    }

    @Test
    void getProfileByPhoneNumber_returnsEmpty_whenAbsent() {
        ProfileTable table = getTable();
        assertFalse(table.getProfileByPhoneNumber(PHONE_NUMBER).isPresent());
    }

    @Test
    void exists_returnsTrue_whenPresent() throws SQLException {
        ProfileTable table = getTable();
        PersistenceUtilities.executeUpdate(
                String.format("INSERT INTO %s VALUES('2025-02-22 09:33:08.141314', 'John', 'Doe', '%s')", TABLE_NAME, PHONE_NUMBER)
        );
        assertTrue(table.exists(PHONE_NUMBER));

    }

    @Test
    void exists_returnsFalse_whenAbsent() {
        ProfileTable table = getTable();
        assertFalse(table.exists(PHONE_NUMBER));
    }

    @Test
    void persist_storesInstance() throws SQLException {
        ProfileTable table = getTable();
        table.persist(buildProfile(PHONE_NUMBER));
        ResultSet resultSet = PersistenceUtilities.executeStatement(
                String.format("SELECT * FROM %s", TABLE_NAME)
        );
        assertTrue(resultSet.next());
    }

    @Test
    void update_updatesStoredInstance() throws SQLException {
        ProfileTable table = getTable();
        String changedLastName = "Walker";
        table.persist(buildProfile(PHONE_NUMBER));
        table.update(PHONE_NUMBER, profile -> profile.setLastName(changedLastName));

        ResultSet resultSet = PersistenceUtilities.executeStatement(
                String.format("SELECT * FROM %s WHERE phone_number like '%s'", TABLE_NAME, PHONE_NUMBER)
        );

        if (resultSet.next()) {
            assertEquals(changedLastName, resultSet.getString("last_name"));
        }
    }

    private ProfileTable getTable() {
        return new ProfileTable(CONFIGURATION.buildSessionFactory());
    }

    private Profile buildProfile(String phoneNumber) {
        return Profile.builder()
                .phoneNumber(phoneNumber)
                .firstName("John")
                .lastName("Doe")
                .build();
    }
}