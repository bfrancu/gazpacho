package gazpacho.core.persistence.table;

import gazpacho.core.persistence.model.MediaItemSubscription;
import gazpacho.core.persistence.model.MediaItemSubscription_;
import gazpacho.core.persistence.query.MediaItemSubscriptionQueries;
import gazpacho.core.persistence.query.MediaItemSubscriptionQueries_;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Order;
import org.hibernate.query.Page;

import java.util.Optional;
import java.util.stream.Stream;


public class MediaItemSubscriptionTable extends EntityCrudBaseTable<Long, MediaItemSubscriptionQueries, MediaItemSubscription> {

    public MediaItemSubscriptionTable(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    Optional<MediaItemSubscription> getSubscriptionById(Long id) {
        return sessionFactory.fromTransaction(session -> {
            session.enableFetchProfile(MediaItemSubscription_.PROFILE_WITH_ITEM_SUBSCRIBERS);
            return get(id, session);
        });
    }

    Stream<MediaItemSubscription> scan(Page page) {
        return scan(page, Order.asc(MediaItemSubscription_.lastScanned));
    }

    Stream<MediaItemSubscription> scan(Page page, Order<MediaItemSubscription> order) {
        return sessionFactory.fromTransaction(session -> {
            session.enableFetchProfile(MediaItemSubscription_.PROFILE_WITH_ITEM_SUBSCRIBERS);
            return queries(session).scanTable(page, order);
        });
    }

    @Override
    protected MediaItemSubscription retrieve(MediaItemSubscriptionQueries q, Long id) throws ObjectNotFoundException {
        return q.getSubscription(id);
    }

    @Override
    protected MediaItemSubscriptionQueries queries(Session session) {
        return new MediaItemSubscriptionQueries_(session);
    }
}
