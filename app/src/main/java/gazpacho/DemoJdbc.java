package gazpacho;

import gazpacho.core.persistence.model.*;
import gazpacho.core.persistence.query.ProfileQueries_;
import gazpacho.core.persistence.table.ProfileTable;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Page;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class DemoJdbc {

    private static final String URL = "jdbc:postgresql://localhost:5432/gazpacho";
    private static final String USER = "admin";
    private static final String PASS = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoJdbc.class);
    private static final Integer RESULTS_PER_PAGE = 25;

    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) throws SQLException {
        testHibernateSessionFactory();
    }

    private static void testHibernateSessionFactory() {
        Configuration hibernateConfig = new Configuration()
                .addAnnotatedClass(MediaSize.class)
                .addAnnotatedClass(Versioned.class)
                .addAnnotatedClass(MediaDataSource.class)
                .addAnnotatedClass(MediaItem.class)
                .addAnnotatedClass(Profile.class)
                .addAnnotatedClass(MediaItemSubscription.class)
                .addAnnotatedClass(Request.class)
                .addAnnotatedClass(Wish.class);
        ;/*
        */

        String numb1 = "07541425";
        Profile profile = Profile.builder()
                .phoneNumber(numb1)
                .firstName("Gelu")
                .lastName("Glad")
                .build();

        String numb2 = "07541426";
        Profile profile2 = Profile.builder()
                .phoneNumber(numb2)
                .firstName("Menumorut")
                .build();



        SessionFactory sessionFactory = hibernateConfig.buildSessionFactory();
        ProfileTable profileTable = new ProfileTable(sessionFactory);
        profileTable.persist(profile);
        profileTable.persist(profile2);

        profileTable.getProfileByPhoneNumber(profile.getPhoneNumber()).ifPresent(p -> {
            LOGGER.info("Object found {}", p);
        });

        /*
        profileTable.getProfileByPhoneNumber(profile2.getPhoneNumber()).ifPresent(p -> {
            LOGGER.info(p.toString());
        });
        */

        /*
        HibernateCriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Profile> query = builder.createQuery(Profile.class);
        Root<Profile> profileRoot = query.from(Profile.class);
        Predicate where = builder.like(profileRoot.get(Profile_.phoneNumber), numb1);
        query.select(profileRoot).where(where);

        sessionFactory.inTransaction(session -> {
            session.persist(profile);
            session.persist(profile2);

            Profile foundProfile = session.createSelectionQuery(query).getSingleResult();
            LOGGER.info(foundProfile.toString());
        });



        sessionFactory.inTransaction(session -> {
            Profile foundProfile = session.find(Profile.class, numb2);
            LOGGER.info(foundProfile.toString());
        });

        sessionFactory.inTransaction(session -> {
            Profile foundProfile = ProfileQueries_.getProfile(session, numb1);
            LOGGER.info(foundProfile.toString());
        });

        */

        /*
        sessionFactory.inTransaction(session -> {
            session.enableFetchProfile(Profile_.PROFILE_WITH_REQUESTS_WISHLIST);
            Wish wish = Wish.builder().build();
            Profile foundProfile = new ProfileQueries_(session).getProfile(numb1);
            wish.setProfile(foundProfile);
//            foundProfile.setWishlist(Set.of(wish));

            session.persist(wish);
            LOGGER.info(foundProfile.toString());
        });

        sessionFactory.inTransaction(session -> {
            session.enableFetchProfile(Profile_.PROFILE_WITH_REQUESTS_WISHLIST);
            List<Profile> foundProfiles = new  ProfileQueries_(session)
                    .findProfileByPhoneNumber(numb1, Page.first(RESULTS_PER_PAGE));
            if (!foundProfiles.isEmpty()) {
                var foundProfile = foundProfiles.getFirst();
                LOGGER.info(foundProfile.toString());
                Set<Wish> wishes = foundProfile.getWishlist();
                if (!wishes.isEmpty()) {
                    LOGGER.info(wishes.toString());
                }
            }
        });

        sessionFactory.inTransaction(session -> {
            session.enableFetchProfile(Profile_.PROFILE_WITH_REQUESTS_WISHLIST);
            try {
                Profile foundProfile = ProfileQueries_.getProfile(session, numb1);
                LOGGER.info(foundProfile.toString());
            } catch (ObjectNotFoundException e) {
                LOGGER.error(e.getMessage());
            }
        });
        */
    }

    private static void testJdbConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASS);

        Connection conn = DriverManager.getConnection(URL, props);

        PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO media_items VALUES (1, 'test', 'demo item', 'movie', false)
                """);

        PreparedStatement statement2 = conn.prepareStatement("SELECT * from media_items");
        ResultSet resultSet = statement2.executeQuery();

        while (resultSet.next()) {
            LOGGER.info(resultSet.getString(1));
//            System.out.println(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
    }
}
