package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.IdResolver;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;

public interface ExperimentRepository {
    List<Experiment> experimentsForUser(UserIdentifier userientifier);

    CreateNewExperiment create(Integer featureBattleId, Experiment.Type experimentType);

    @FunctionalInterface
    public interface CreateNewExperiment {
        IdResolver forPage(String page );

    }

}
