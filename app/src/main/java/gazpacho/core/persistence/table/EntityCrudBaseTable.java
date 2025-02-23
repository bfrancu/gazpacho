package gazpacho.core.persistence.table;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class EntityCrudBaseTable<Key, Queries, Entity> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(EntityCrudBaseTable.class);

    protected final SessionFactory sessionFactory;

    public boolean exists(@NonNull Key key) {
        return sessionFactory.fromTransaction(session -> get(key, session).isPresent());
    }

    public void persist(@NonNull Entity entity) {
        sessionFactory.inTransaction(session -> session.persist(entity));
    }

    public void remove(@NonNull Key key) {
        update(key, ((entity, session) -> session.remove(entity)));
    }

    public void update(@NonNull Key key, Consumer<Entity> updater) {
        update(key, ((entity, session) -> {
            updater.accept(entity);
            session.persist(entity);
        }));
    }

    protected void update(Key key, BiConsumer<Entity, Session> updater) {
        sessionFactory.inTransaction(session -> {
            Optional<Entity> persistedEntity = get(key, session);
            persistedEntity.ifPresent(entity -> updater.accept(entity, session));
        });
    }

    protected Optional<Entity> get(Key key, Session session) {
        try {
            Entity persistedEntity = retrieve(queries(session), key);
            LOGGER.info("Object found {}", persistedEntity);
            return Optional.of(persistedEntity);
        } catch (ObjectNotFoundException e) {
            LOGGER.warn("Object not found {}", e);
            return Optional.empty();
        }
    }

    protected abstract Entity retrieve(Queries q, Key k) throws ObjectNotFoundException;
    protected abstract Queries queries(Session session);
}
