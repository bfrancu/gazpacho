package gazpacho.core.persistence.table;


import gazpacho.core.persistence.model.Request;
import gazpacho.core.persistence.query.PersistenceAccessor;
import gazpacho.core.persistence.query.RequestQueries;
import gazpacho.core.persistence.query.RequestQueries_;
import lombok.NonNull;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class RequestTable extends EntityCrudBaseTable<Long, RequestQueries, Request> {

    public RequestTable(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Request> getRequestByUserQuery(@NonNull String userQuery) {
        return sessionFactory.fromTransaction(session -> get(
                (persistenceQueries, key) -> persistenceQueries.getRequestByQuery(key),
                userQuery,
                session
        ));
    }

    public Optional<Request> getRequestById(Long id) {
        return sessionFactory.fromTransaction(session -> get(
                this::retrieve,
                id,
                session
        ));
    }

    private <Key> Optional<Request> get(PersistenceAccessor<Key, RequestQueries, Request> accessor,
                                        Key key,
                                        Session session) {
        try {
            Request persistedObject = accessor.retrieve(queries(session), key);
            LOGGER.info("Object found {}", persistedObject);
            return Optional.of(persistedObject);
        } catch (ObjectNotFoundException e) {
            LOGGER.warn("Object not found {}", e);
            return Optional.empty();
        }
    }

    @Override
    protected Request retrieve(RequestQueries q, Long id) throws ObjectNotFoundException {
        return q.getRequest(id);
    }

    @Override
    protected RequestQueries queries(Session session) {
        return new RequestQueries_(session);
    }
}
