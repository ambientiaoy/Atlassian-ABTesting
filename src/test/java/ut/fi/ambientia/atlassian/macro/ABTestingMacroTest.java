package ut.fi.ambientia.atlassian.macro;

import fi.ambientia.abtesting.routes.Something;
import fi.ambientia.atlassian.macro.ABTestingMacro;
import fi.ambientia.atlassian.users.MapCurrentUserToUserkey;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ABTestingMacroTest {


    private Something something;
    public static final String AB_TEST_KEY = "ABTESTINGKEY";
    public static final String USER_KEY = "USERKEY";
    private Map<String, String> params;
    private ABTestingMacro abTestingMacro;
    private MapCurrentUserToUserkey mapCurrentUserToUserkey;

    @Before
    public void setUp() throws Exception {
        params = new HashMap<String, String>();
        params.put("AB_TESTING_KEY", AB_TEST_KEY);

        something = mock(Something.class);
        mapCurrentUserToUserkey = mock(MapCurrentUserToUserkey.class);

        abTestingMacro = new ABTestingMacro(something, mapCurrentUserToUserkey);
    }

    @Test
    public void testName() throws Exception {
        when(mapCurrentUserToUserkey.getCurrentUserKey()).thenReturn( USER_KEY );
        when(something.chooseOption(AB_TEST_KEY, USER_KEY)).thenReturn("viewToBeRendered");

        String viewToBeRendered = abTestingMacro.execute(params, "", null);
        //Assert
        assertThat( viewToBeRendered, equalTo("viewToBeRendered") );
    }
}
