package fi.ambientia.atlassian.routes.arguments;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.codehaus.jackson.annotate.JsonCreator;

public class FeatureBattleWinCommand {

    private final Experiment.Type experimentType;
    private final UserIdentifier userIdentifier;

    @JsonCreator
    public FeatureBattleWinCommand(Experiment.Type experimentType, String userKey) {
        this.experimentType = experimentType;
        this.userIdentifier = new UserIdentifier(userKey);
    }

    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public Experiment.Type getType() {
        return experimentType;
    }
}
