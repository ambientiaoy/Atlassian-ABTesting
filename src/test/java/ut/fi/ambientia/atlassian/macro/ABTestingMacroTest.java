package ut.fi.ambientia.atlassian.macro;

import fi.ambientia.atlassian.macro.ABTestingMacro;
import fi.ambientia.atlassian.users.Users;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class ABTestingMacroTest {


    public static final String AB_TEST_KEY = "ABTESTINGKEY";
    public static final String USER_KEY = "USERKEY";
    private Map<String, String> params;
    private ABTestingMacro abTestingMacro;
    private Users currentUser;

    @Before
    public void setUp() throws Exception {
        params = new HashMap<String, String>();
        params.put("AB_TESTING_KEY", AB_TEST_KEY);

        currentUser = mock(Users.class);

        abTestingMacro = new ABTestingMacro();
    }

    @Test
    public void testName() throws Exception {

        String viewToBeRendered = abTestingMacro.execute(params, "", null);
        //Assert
        assertThat( viewToBeRendered, equalTo("viewToBeRendered") );
    }
}
