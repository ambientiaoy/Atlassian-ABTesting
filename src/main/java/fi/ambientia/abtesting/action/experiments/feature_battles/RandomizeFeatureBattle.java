package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.function.Function;

@Component
public class RandomizeFeatureBattle {



    public GetExperimentForUser getExperiment(ExperimentIdentifier experimentIdentifier) {
        // TODO AkS: find this from DB?
        Random random = new Random();
        int res = random.nextInt(100);

        return (user) -> res < 50 ? new NewAndShiny() : new GoodOldWay();
    }

    public interface GetExperimentForUser extends Function<UserIdentifier, Experiment>{

        default Experiment forUser(UserIdentifier user) {
            return this.apply(user);
        }
    }
}
