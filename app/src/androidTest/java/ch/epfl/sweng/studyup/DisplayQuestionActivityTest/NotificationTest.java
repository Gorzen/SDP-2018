package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;

import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static junit.framework.TestCase.assertEquals;

public class NotificationTest extends DisplayQuestionActivityTest {

    @Test
    public void displayNotifCorrectly() {
        long duration = 500;
        Question questionTest = new Question("fakeIDNotifTest", "This is a title",
                true, 0, "SWENG", "en");
        Intent launchIntent = DisplayQuestionActivity.getIntentForDisplayQuestion(mActivityRule.getActivity(), questionTest);
        mActivityRule.launchActivity(launchIntent);

        waitAndTag(1000, "Wait for the notification to be displayed from DisplayQuestionActivity");

        String titleString = mActivityRule.getActivity().getString(R.string.time_out_title);
        String desc = mActivityRule.getActivity().getString(R.string.time_out_notification);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(titleString)), 1000);
        UiObject2 title = device.findObject(By.text(titleString));
        UiObject2 text = device.findObject(By.text(desc));
        assertEquals(titleString, title.getText());
        assertEquals(desc, text.getText());
    }
}
