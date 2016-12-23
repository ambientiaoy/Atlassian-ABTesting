package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.function.Function;

public class RandomizeFeatureBattle {
    public GetExperimentForUser getExperiment(ExperimentIdentifier experimentIdentifier) {
        return null;
    }

    public interface GetExperimentForUser extends Function<UserIdentifier, Experiment>{

        default Experiment forUser(UserIdentifier user) {
            return this.apply(user);
        }
    }
}
