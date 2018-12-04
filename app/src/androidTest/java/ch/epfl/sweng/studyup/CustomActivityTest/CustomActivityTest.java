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
import android.widget.EditText;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("HardCodedStringLiteral")
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
    public void A_changeUserName() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText) mActivityRule.getActivity().findViewById(R.id.edit_username)).setText("Wir Sind Helden Too Long Not Should Be Displayed");
            }
        });
        waitAndTag(300, "Waiting for main thread to set the text.");
        onView(withId(R.id.valid_btn)).perform(click());
    }


    @Test
    public void Z_checkDisplayAndAccessToGallery() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        assertTrue(device.findObject(new UiSelector().text(mActivityRule.getActivity().getApplicationContext().getString(R.string.camera))).exists());
        assertTrue(device.findObject(new UiSelector().text(mActivityRule.getActivity().getApplicationContext().getString(R.string.gallery))).exists());
        assertTrue(device.findObject(new UiSelector().text(mActivityRule.getActivity().getApplicationContext().getString(R.string.cancel))).exists());
        clickButton(mActivityRule.getActivity().getApplicationContext().getString(R.string.gallery));
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