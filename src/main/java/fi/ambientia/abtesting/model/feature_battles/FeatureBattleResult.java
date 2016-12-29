package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.UserIdentifier;

public class FeatureBattleResult {
    private final UserIdentifier userIdentifier;
    private final Experiment experiment;

    public FeatureBattleResult(UserIdentifier userIdentifier, Experiment experiment) {
        this.userIdentifier = userIdentifier;
        this.experiment = experiment;
    }

    public boolean forUser(Identifier user) {
        return user.equals( userIdentifier );
    }

    public boolean forExperimentType( Experiment.Type type){
        return experiment.type().equals( type );
    }

    public Experiment getExperiment() {
        return experiment ;
    }
}
