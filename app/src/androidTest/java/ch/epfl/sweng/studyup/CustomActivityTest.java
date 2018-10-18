package ch.epfl.sweng.studyup;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

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


    public void addPictureCameraPermissionRequest() throws Exception {
        assertTrue(shouldDisplayPermissionRequest(Utils.CAMERA));
    }


    public void addPictureGalleryWithoutException() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        UiObject button = device.findObject(new UiSelector().text(Utils.GALLERY));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }

    public boolean shouldDisplayPermissionRequest(String string) throws UiObjectNotFoundException {
        onView(withId(R.id.pic_btn)).perform(click());
        UiObject button = device.findObject(new UiSelector().text(string));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
        return device.findObject(new UiSelector().text(Utils.ALLOW)).exists()
                && (device.findObject(new UiSelector().text(Utils.DENY)).exists());
    }
}
