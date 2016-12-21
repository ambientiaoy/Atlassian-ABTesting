package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AlreadyDecidedBattles {
    public Optional<Experiment> forIdentifier(String s) {
        return Optional.of(new GoodOldWay());
    }
}
