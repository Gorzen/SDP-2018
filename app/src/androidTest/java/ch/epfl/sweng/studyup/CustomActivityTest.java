package ch.epfl.sweng.studyup;


import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static android.support.constraint.Constraints.TAG;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomActivityTest {
    private static final String TAG = CustomActivityTest.class.getSimpleName();
    private CustomActivity mActivity;
    private static final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public final ActivityTestRule<CustomActivity> mActivityRule =
            new ActivityTestRule<>(CustomActivity.class);

    @Before
    public void init() {
        mActivity = mActivityRule.getActivity();

    }

    @Test
    public void a_addPictureCameraPermissionRequest() throws Exception {
        assertTrue(shouldDisplayPermissionRequest(Utils.CAMERA));
    }

  /*  @Test
    public void b_addPictureGalleryPermissionRequest() throws Exception {
        assertTrue(shouldDisplayPermissionRequest("Gallery"));
    }*/

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
