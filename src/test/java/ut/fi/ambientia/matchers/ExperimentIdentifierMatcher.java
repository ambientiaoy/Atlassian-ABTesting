package ut.fi.ambientia.matchers;

import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ExperimentIdentifierMatcher extends TypeSafeMatcher<ExperimentIdentifier> {
    private final ExperimentIdentifier expected;

    public ExperimentIdentifierMatcher(ExperimentIdentifier expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(ExperimentIdentifier experimentIdentifier) {
        return experimentIdentifier.getIdentifier().equals(expected.getIdentifier());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("expected: " + expected +
                             "\ngot:      " + "something else");
    }

    public static Matcher<ExperimentIdentifier> matchesWith(ExperimentIdentifier experimentIdentifier) {
        return new ExperimentIdentifierMatcher( experimentIdentifier );
    }
}
