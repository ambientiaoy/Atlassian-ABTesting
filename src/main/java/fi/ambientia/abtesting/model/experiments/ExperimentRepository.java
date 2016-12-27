package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;
import java.util.Optional;


public interface ExperimentRepository {
    List<Experiment> experimentsForUser(UserIdentifier userientifier);



}
