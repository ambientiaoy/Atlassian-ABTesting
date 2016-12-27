package ut.fi.ambientia.abtesting.model;

import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;

public class TestData {
    public static final ExperimentIdentifier EXPERIMENT_IDENTIFIER = new ExperimentIdentifier("EXPERIMENT_IDENTIFIER");
    private static final String USERKEY = "USER KEY";
    public static final UserIdentifier USERIDENTIFIER = new UserIdentifier(USERKEY);
}
