package gazpacho.core.persistence.model.mapper;

public interface PersistenceModelMapper<T, U> {

    /**
     * Maps a domain model to a persistence model.
     *
     * @param domainModel the domain model to map
     * @return the mapped persistence model
     */
    U toPersistenceModel(T domainModel);

    /**
     * Maps a persistence model to a domain model.
     *
     * @param persistenceModel the persistence model to map
     * @return the mapped domain model
     */
    T fromPersistenceModel(U persistenceModel);
}
