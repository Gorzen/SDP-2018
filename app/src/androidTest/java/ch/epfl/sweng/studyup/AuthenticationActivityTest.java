package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.auth.AuthenticationActivity;

// Tests for the functionality of handling of responses can be found in AuthTest.
// Tests in this class are to test that the authentication activity handles the
// different possible types of data it receives from intents.
@RunWith(AndroidJUnit4.class)
public class AuthenticationActivityTest {

    @Rule
    public ActivityTestRule<AuthenticationActivity> rule =
            new ActivityTestRule<>(AuthenticationActivity.class, true, false);

    @Test
    public void testIntentWithError() {

        Uri uriWithoutCode = Uri.parse("studyup://login?error=anyvalue");
        Intent intentWithoutCode = new Intent();
        intentWithoutCode.setData(uriWithoutCode);

        rule.launchActivity(intentWithoutCode);
    }

    @Test
    public void testIntentWithValidCode() {

        Uri uriWithoutCode = Uri.parse("studyup://login?code=anyvalue");
        Intent intentWithoutCode = new Intent();
        intentWithoutCode.setData(uriWithoutCode);

        rule.launchActivity(intentWithoutCode);
    }
}
