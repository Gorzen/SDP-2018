package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AuthTest {
    private static final String TAG = AuthTest.class.getSimpleName();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testGreetingFromResponseInvalid() {
        String invalidResponse = "{\"anykey\":\"anyvalue\"}";
        assert(Authenticator.getGreetingFromResponse(invalidResponse) == null);
    }

    @Test
    public void testGreetingFromResponseError() {
        String invalidResponse = "{\"error\":\"anyvalue\"}";
        assert(Authenticator.getGreetingFromResponse(invalidResponse) == null);
    }


    @Test
    public void testGreetingFromResponsePartiallyValid() {
        String partiallyValidResponse = "{\"Sciper\":\"1234\"}";
        assertThat(Authenticator.getGreetingFromResponse(partiallyValidResponse), is("Your Sciper number is 1234."));
    }

    @Test
    public void testGreetingFromResponseValid() {
        String validResponse = "{\"Sciper\":\"1234\", \"Firstname\":\"Bob\"}";
        assertThat(Authenticator.getGreetingFromResponse(validResponse),
                is("Welcome, Bob.\nYour Sciper number is 1234."));
    }


    @Test
    public void testTokenFromResponseInvalid() {
        String invalidResponse = "{\"anykey\":\"anyvalue\"}";
        assert(Authenticator.getTokenFromResponse(invalidResponse) == null);
    }

    @Test
    public void testTokenFromResponseError() {
        String invalidResponse = "{\"error\":\"anyvalue\"}";
        assert(Authenticator.getTokenFromResponse(invalidResponse) == null);
    }

    @Test
    public void testTokenFromResponseValid() {
        String invalidResponse = "{\"access_token\":\"1234\"}";
        assertThat(Authenticator.getTokenFromResponse(invalidResponse), is("1234"));
    }
}
