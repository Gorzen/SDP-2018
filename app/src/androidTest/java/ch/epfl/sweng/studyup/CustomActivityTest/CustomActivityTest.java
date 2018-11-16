package ch.epfl.sweng.studyup.CustomActivityTest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.CAMERA;
import static ch.epfl.sweng.studyup.utils.Constants.CANCEL;
import static ch.epfl.sweng.studyup.utils.Constants.GALLERY;
import static ch.epfl.sweng.studyup.utils.Constants.JUSTONCE;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomActivityTest {
    private static final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public final ActivityTestRule<CustomActivity> mActivityRule =
            new ActivityTestRule<>(CustomActivity.class);

    @BeforeClass
    public static void enableMock() {
        MOCK_ENABLED = true;
        Player.get().resetPlayer();
    }

    @AfterClass
    public static void disableMock() {
        MOCK_ENABLED = false;
    }

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
        onView(ViewMatchers.withId(R.id.edit_username)).perform(clearText())
                .perform(typeText("Wir Sind Helden Too Long Not Should Be displayed"));
        onView(withId(R.id.valid_btn)).perform(click());
        onView(withId(R.id.usernameText)).check(matches(withText("Wir Sind Helden")));
    }

    /*
    @Test
    public void email_check() {
        Player.get().setFirstName(INITIAL_FIRSTNAME);
        Player.get().setLastName(INITIAL_LASTNAME);
        ViewInteraction a = onView(withId(R.id.user_email));
        System.out.print(a);
        a.check(matches(withText("jean-louis.reymond@epfl.ch")));
    }*/

    @Test
    public void Z_checkDisplayAndAccessToGallery() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        assertTrue(device.findObject(new UiSelector().text(CAMERA)).exists());
        assertTrue(device.findObject(new UiSelector().text(GALLERY)).exists());
        assertTrue(device.findObject(new UiSelector().text(CANCEL)).exists());
        clickButton(GALLERY);
        Utils.waitAndTag(1000, "CustomTest");
        device.pressBack();
        Utils.waitAndTag(1000, "CustomTest");
        clickButton(JUSTONCE);
    }

    public void clickButton(String textButton) throws UiObjectNotFoundException {
        UiObject button = device.findObject(new UiSelector().text(textButton));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }

    @Test
    public void setImageCircularAndUploadTest(){
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(mActivityRule.getActivity().getApplicationContext().getResources(), R.drawable.potion);
                mActivityRule.getActivity().setImageCircularAndUpload(bitmap);
            }
        });
    }
}