package gazpacho.core.persistence.query;

import org.hibernate.ObjectNotFoundException;

import java.util.Optional;

@FunctionalInterface
public interface PersistenceAccessor<Key, Queries, Entity> {
    Entity retrieve(Queries q, Key k) throws ObjectNotFoundException;
}
