package ch.epfl.sweng.studyup;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.ImageButton;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_LASTNAME;
import static org.junit.Assert.assertTrue;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomActivityTest {
    private static final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public final ActivityTestRule<CustomActivity> mActivityRule =
            new ActivityTestRule<>(CustomActivity.class);

    @Before
    public void initiateIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void A_changeUserName() {
        onView(withId(R.id.edit_username)).perform(clearText()).perform(typeText("Wir Sind Helden Too Long Not Should Be displayed"));
        onView(withId(R.id.valid_btn)).perform(click());
        onView(withId(R.id.usernameText)).check(matches(withText("Wir Sind Helden")));
    }

    @Test
    public void email_check() {
        Player.get().setFirstName(INITIAL_FIRSTNAME);
        Player.get().setLastName(INITIAL_LASTNAME);
        ViewInteraction a = onView(withId(R.id.user_email));
        System.out.print(a);
        a.check(matches(withText("jean-louis.reymond@epfl.ch")));
    }

    @Test
    public void Z_checkDisplayAndAccessToGallery() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        assertTrue(device.findObject(new UiSelector().text(Utils.CAMERA)).exists());
        assertTrue(device.findObject(new UiSelector().text(Utils.GALLERY)).exists());
        assertTrue(device.findObject(new UiSelector().text(Utils.CANCEL)).exists());
        clickButton(Utils.GALLERY);
        clickButton(Utils.JUSTONCE);
    }

    public void clickButton(String textButton) throws UiObjectNotFoundException {
        UiObject button = device.findObject(new UiSelector().text(textButton));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }

    @Test
    public void testValidButtonChangeOnClick() {
        assert (mActivityRule.getActivity().valid_button.getBackground().getAlpha() == R.drawable.ic_check_black_24dp);
        onView(withId(R.id.valid_btn)).perform(click());
        assert (mActivityRule.getActivity().valid_button.getBackground().getAlpha() == R.drawable.ic_check_done_24dp);
    }

}
