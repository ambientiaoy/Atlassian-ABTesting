package ut.fi.ambientia.matchers;

import fi.ambientia.abtesting.model.Identifier;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IdentifierMatcher<T extends Identifier> extends TypeSafeMatcher<Identifier> {

    private final Identifier expected;

    public IdentifierMatcher(Identifier expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(Identifier identifier) {
        return identifier.getIdentifier().equals( expected.getIdentifier() );
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("expected: " + expected.getIdentifier() +
                             "\ngot:      " + "something else");
    }

    public static Matcher<Identifier> matchesWithIdentifier(Identifier experimentIdentifier) {
        return new IdentifierMatcher( experimentIdentifier );
    }

}
