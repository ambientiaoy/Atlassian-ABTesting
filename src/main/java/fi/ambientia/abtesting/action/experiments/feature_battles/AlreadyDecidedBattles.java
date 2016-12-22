package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
public class AlreadyDecidedBattles {
    public Optional<Experiment> forIdentifier(String s) {
        int i = new Random().nextInt(100);
        return i < 50 ? Optional.of(new GoodOldWay() ) :
                Optional.of(new NewAndShiny());
    }
}
