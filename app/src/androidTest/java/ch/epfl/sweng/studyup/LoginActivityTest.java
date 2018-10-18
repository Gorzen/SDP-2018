package ch.epfl.sweng.studyup;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LoginActivityTest {

    private static final String TAG = LoginActivityTest.class.getSimpleName();

    @Test
    public void testLogin() {
        onView(withId(R.id.loginButton)).perform(click());
        Utils.waitAndTag(1000, TAG);
    }
}
