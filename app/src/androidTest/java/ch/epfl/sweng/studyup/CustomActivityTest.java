package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
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

    @Test
    public void addPictureWithoutException() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        assertTrue(device.findObject(new UiSelector().text(Utils.CAMERA)).exists());
        assertTrue(device.findObject(new UiSelector().text(Utils.GALLERY)).exists());
        assertTrue(device.findObject(new UiSelector().text(Utils.CANCEL)).exists());
        clickButton(Utils.CAMERA);
        clickButton(Utils.ALLOW);
        assertTrue(checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.CAMERA)    == PackageManager.PERMISSION_GRANTED);
    }

    public void clickButton(String textButton) throws UiObjectNotFoundException {
        UiObject button = device.findObject(new UiSelector().text(textButton));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }

}
