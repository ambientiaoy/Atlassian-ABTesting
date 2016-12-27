package ut.fi.ambientia.matchers;

import fi.ambientia.abtesting.model.experiments.FeatureBattleIdentifier;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ExperimentIdentifierMatcher extends TypeSafeMatcher<FeatureBattleIdentifier> {
    private final FeatureBattleIdentifier expected;

    public ExperimentIdentifierMatcher(FeatureBattleIdentifier expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(FeatureBattleIdentifier featureBattleIdentifier) {
        return featureBattleIdentifier.getIdentifier().equals(expected.getIdentifier());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("expected: " + expected +
                             "\ngot:      " + "something else");
    }

    public static Matcher<FeatureBattleIdentifier> matchesWith(FeatureBattleIdentifier featureBattleIdentifier) {
        return new ExperimentIdentifierMatcher(featureBattleIdentifier);
    }
}
