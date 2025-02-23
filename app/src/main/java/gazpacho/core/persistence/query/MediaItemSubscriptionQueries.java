package gazpacho.core.persistence.query;

import gazpacho.core.persistence.model.MediaItemSubscription;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.processing.CheckHQL;
import org.hibernate.annotations.processing.Find;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.query.Order;
import org.hibernate.query.Page;

import java.util.stream.Stream;

@CheckHQL
public interface MediaItemSubscriptionQueries {
    EntityManager entityManager();

    @HQL("from MediaItemSubscription")
    Stream<MediaItemSubscription> scanTable(Page page, Order<MediaItemSubscription> order);

    @Find
    MediaItemSubscription getSubscription(Long id);
}
