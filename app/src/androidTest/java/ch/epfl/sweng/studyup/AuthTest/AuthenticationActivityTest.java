package ch.epfl.sweng.studyup.AuthTest;

import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.auth.AuthenticationActivity;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.HOME_ACTIVITY;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

// Tests for the functionality of handling of responses can be found in AuthTest.
// Tests in this class are to test that the authentication activity handles the
// different possible types of data it receives from intents.
@RunWith(AndroidJUnit4.class)
public class AuthenticationActivityTest {

    @Rule
    public ActivityTestRule<AuthenticationActivity> rule =
            new ActivityTestRule<>(AuthenticationActivity.class, true, false);

    @BeforeClass
    public static void setupMock() {
        MOCK_ENABLED = true;
        HOME_ACTIVITY = HomeActivity.class;
    }

    @AfterClass
    public static void breakdownMock() {
        MOCK_ENABLED = false;
    }

    @Before
    public void init() {
        Intents.init();
    }

    @After
    public void release() {
        Intents.release();
    }

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

    @Test
    public void testBasicRunAuthentication() throws Exception {
        Intent i = new Intent();
        Uri uriWithoutCode = Uri.parse("studyup://login?code=anyvalue");
        i.setData(uriWithoutCode);

        rule.launchActivity(i);
        assert (Player.get().getFirstName() == INITIAL_FIRSTNAME);
    }

    @Test
    public void continueButtonStudentTest() {
        Player.get().setRole(Constants.Role.student);
        useContinueButton();
        intended(hasComponent(HomeActivity.class.getName()));
    }

    @Test
    public void continueButtonTeacherTest() {
        Player.get().setRole(Constants.Role.teacher);
        useContinueButton();
        intended(hasComponent(QuestsActivityTeacher.class.getName()));

    }
    private void useContinueButton() {
        Uri uriWithoutCode = Uri.parse("");
        Intent intentWithoutCode = new Intent();
        intentWithoutCode.setData(uriWithoutCode);

        rule.launchActivity(intentWithoutCode);
        UiDevice phone = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        phone.pressBack();
        onView(withId(R.id.continueToMainButton)).perform(click());
    }
}
