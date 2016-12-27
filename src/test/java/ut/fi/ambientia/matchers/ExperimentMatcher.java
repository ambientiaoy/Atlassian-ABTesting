package ut.fi.ambientia.matchers;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ExperimentMatcher extends TypeSafeMatcher<Experiment> {
    private final Experiment expected;
    private final ExperimentIdentifier identifier;

    public ExperimentMatcher(Experiment experiment, ExperimentIdentifier identifier) {
        expected = experiment;
        this.identifier = identifier;
    }

    @Override
    protected boolean matchesSafely(Experiment experiment) {
        return experiment.type().equals( this.expected.type() ) && experiment.isRepresentedBy( identifier );
    }

    @Override
    public void describeTo(Description description) {
        description.appendText( "expected: " + expected.type() + " with identifier " + identifier.getIdentifier() );
    }


    public static Matcher<Experiment> thatIsEqualTo(Experiment experiment, ExperimentIdentifier identifier){
        return new ExperimentMatcher(experiment, identifier);
    }
}
