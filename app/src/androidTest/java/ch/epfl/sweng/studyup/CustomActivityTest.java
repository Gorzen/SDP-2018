package ch.epfl.sweng.studyup;


import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.constraint.Constraints.TAG;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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

    public static void assertPermissionRequestIsVisible(UiDevice device, String string) {
        UiObject allowButton = device.findObject(new UiSelector().text(string));
        if (!allowButton.exists()) {
            throw new AssertionError("View with text <" + string + "> not found!");
        }
    }

    /*  public static void denyCurrentPermission(UiDevice device) throws UiObjectNotFoundException {
          UiObject denyButton = device.findObject(new UiSelector().text(TEXT_DENY));
          denyButton.click();
      }
  */
    @Test
    public void a_shouldDisplayPermissionRequestDialogAtStartup() throws Exception {
        onView(withId(R.id.pic_btn)).perform(click());
        Utils.waitAndTag(1000, TAG);
        AlertDialog dialog = mActivity.getDialog(); // I create getLastDialog method in MyActivity class. Its return last created AlertDialog
        if (dialog.isShowing()) {
            try {
                performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        assertPermissionRequestIsVisible(device, "Allow");
        //  assertPermissionRequestIsVisible(device, "DENY");

        // cleanup for the next test
        // denyCurrentPermission(device);
    }


}