package gazpacho.core.persistence.query;

import gazpacho.core.persistence.model.Request;
import gazpacho.core.persistence.model.Request_;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.processing.CheckHQL;
import org.hibernate.annotations.processing.Find;

@CheckHQL
public interface RequestQueries {
    EntityManager entityManager();

    @Find(enabledFetchProfiles = {Request_.PROFILE_WITH_DATASOURCE_ITEM_PROFILE})
    Request getRequestByQuery(String query);

    @Find(enabledFetchProfiles = {Request_.PROFILE_WITH_DATASOURCE_ITEM_PROFILE})
    Request getRequest(Long id);
}
