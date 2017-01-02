package fi.ambientia.abtesting.model.experiments.user_experiment;

import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.TargetedForUserPredicate;
import fi.ambientia.abtesting.model.user.UserIdentifier;

public class UserExperimentResult implements TargetedForUserPredicate{

    private final UserIdentifier userIdentifier;
    private final Experiment experiment;

    public UserExperimentResult(UserIdentifier userIdentifier, Experiment experiment) {
        this.userIdentifier = userIdentifier;
        this.experiment = experiment;
    }

    @Override
    public boolean forUser(Identifier user) {
        return userIdentifier.getIdentifier().equals(user.getIdentifier());
    }

    public Experiment getExperiment() {
        return experiment;
    }
}
