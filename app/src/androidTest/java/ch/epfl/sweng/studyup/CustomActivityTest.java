package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomActivityTest {
    private static final String TAG = CustomActivityTest.class.getSimpleName();
    private static final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    @Rule
    public final ActivityTestRule<CustomActivity> mActivityRule =
            new ActivityTestRule<>(CustomActivity.class);
    private CustomActivity mActivity;

    @Before
    public void init() {
        mActivity = mActivityRule.getActivity();

    }

    @Test
    public void A_changeUserName() {
        onView(withId(R.id.edit_username)).perform(clearText()).perform(typeText("Wir Sind Helden"));
        onView(withId(R.id.valid_btn)).perform(click());
        onView(withId(R.id.view_username)).check(matches(withText("Wir Sind Helden")));
    }

    @Test
    public void B_checkAccessAndDisplayCamera() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        clickButton(Utils.CAMERA);
        assertTrue(device.findObject(new UiSelector().text(Utils.DENY)).exists());
        assertTrue(device.findObject(new UiSelector().text(Utils.ALLOW)).exists());
        clickButton(Utils.DENY);
    }

    @Test
    public void Z_checkDisplayAndAccessToGallery() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        assertTrue(device.findObject(new UiSelector().text(Utils.CAMERA)).exists());
        assertTrue(device.findObject(new UiSelector().text(Utils.GALLERY)).exists());
        assertTrue(device.findObject(new UiSelector().text(Utils.CANCEL)).exists());
        clickButton(Utils.GALLERY);
        clickButton("JUST ONCE");
    }

    private void clickButton(String textButton) throws UiObjectNotFoundException {
        UiObject button = device.findObject(new UiSelector().text(textButton));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }

}
