package ut.fi.ambientia.atlassian.macro;

import fi.ambientia.abtesting.routes.Something;
import fi.ambientia.atlassian.macro.ABTestingMacro;
import fi.ambientia.atlassian.users.Users;
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
    private Users currentUser;

    @Before
    public void setUp() throws Exception {
        params = new HashMap<String, String>();
        params.put("AB_TESTING_KEY", AB_TEST_KEY);

        something = mock(Something.class);
        currentUser = mock(Users.class);

        abTestingMacro = new ABTestingMacro(something);
    }

    @Test
    public void testName() throws Exception {
        when(something.chooseOption(AB_TEST_KEY, USER_KEY)).thenReturn("viewToBeRendered");

        String viewToBeRendered = abTestingMacro.execute(params, "", null);
        //Assert
        assertThat( viewToBeRendered, equalTo("viewToBeRendered") );
    }
}
