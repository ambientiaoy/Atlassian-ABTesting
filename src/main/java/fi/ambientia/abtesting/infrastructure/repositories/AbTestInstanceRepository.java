package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import fi.ambientia.abtesting.model.write.FeatureBattle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AbTestInstanceRepository {

    @Autowired
    public AbTestInstanceRepository(@ComponentImport ActiveObjects ao) {

    }

    public void createNew(FeatureBattle abTestInstance) {

    }
}
