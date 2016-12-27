package ut.fi.ambientia.matchers;

import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class UserIdentifierMatcher extends TypeSafeMatcher<UserIdentifier> {
    private final UserIdentifier expected;

    public UserIdentifierMatcher(UserIdentifier expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(UserIdentifier userIdentifier) {
        return userIdentifier.getIdentifier().equals( expected.getIdentifier() );
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("expected: " + expected +
                "\ngot:      " + "something else");
    }

    public static Matcher<UserIdentifier> matchesWithUser(UserIdentifier userIdentifier) {
        return new IdentifierMatcher( userIdentifier);
    }
}
