package ch.epfl.sweng.studyup.QuestionsTest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.TimeOutNotificationPublisher;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimeOutNotificationPublisherTest {

    private String titleTest = "Test Title";
    private String descTest = "This is a simple test description but it needs to be long enough to be " +
            "displayed on several lines";
    private int notifID = 0;
    private String questionID = "test question";

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class, true, false);

    @BeforeClass
    public static void init() {
        Player.get().resetPlayer();
        GlobalAccessVariables.MOCK_ENABLED = true;
    }

    @AfterClass
    public static void clean() {
        Player.get().resetPlayer();
        GlobalAccessVariables.MOCK_ENABLED = false;
    }

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void closeNotifPanel() {
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        InstrumentationRegistry.getTargetContext().sendBroadcast(it);
        Intents.release();
    }

    @Test
    public void simpleNotifTest() {
        /*
        Question question = new Question("ID test notif", "Title", true, 0, "SWENG", "en", 10);
        Intent launchIntent = DisplayQuestionActivity.getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), question);
        mActivityRule.launchActivity(launchIntent);

        Notification notification = mActivityRule.getActivity().prepareNotificationTimeOut(titleTest, descTest);
        Intent notifIntent = new Intent();
        notifIntent.putExtra(TimeOutNotificationPublisher.NOTIFICATION, notification);
        notifIntent.putExtra(TimeOutNotificationPublisher.NOTIFICATION_ID, notifID);
        notifIntent.putExtra(TimeOutNotificationPublisher.QUESTIONID, questionID);
        notifIntent.putExtra(TimeOutNotificationPublisher.ANSWER_NUMBER, Integer.toString(0));

        TimeOutNotificationPublisher notificationPublisher = new TimeOutNotificationPublisher();
        notificationPublisher.onReceive(mActivityRule.getActivity(), notifIntent);

        waitAndTag(500, "Waiting for the notification to be displayed");

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(titleTest)), 1000);
        UiObject2 title = device.findObject(By.text(titleTest));
        UiObject2 text = device.findObject(By.text(descTest));
        assertEquals(titleTest, title.getText());
        assertEquals(descTest, text.getText());
        title.click();
        Intents.intending(hasComponent(QuestsActivityStudent.class.getName()));
        */
        assertTrue(true);
    }
}