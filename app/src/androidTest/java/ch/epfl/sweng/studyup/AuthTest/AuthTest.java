package ch.epfl.sweng.studyup.AuthTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.TestbedActivity;
import ch.epfl.sweng.studyup.auth.Authenticator;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AuthTest {
    private static final String TAG = AuthTest.class.getSimpleName();

    @Rule
    public final ActivityTestRule<TestbedActivity> rule =
            new ActivityTestRule<>(TestbedActivity.class);

    @BeforeClass
    public static void runOnceBeforeClass() {
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void runOnceAfterClass() {
        MOCK_ENABLED = false;
    }

    @Test
    public void testTokenFromResponseInvalid() throws Exception {
        String invalidResponse = "{\"anykey\":\"anyvalue\"}";
        assert (Authenticator.getTokenFromResponse(invalidResponse) == null);
    }

    @Test
    public void testTokenFromResponseError() throws Exception {
        String invalidResponse = "{\"error\":\"anyvalue\"}";
        assert (Authenticator.getTokenFromResponse(invalidResponse) == null);
    }

    @Test
    public void testTokenFromResponseValid() throws Exception {
        String invalidResponse = "{\"access_token\":\"1234\"}";
        assertThat(Authenticator.getTokenFromResponse(invalidResponse), is("1234"));
    }
}
