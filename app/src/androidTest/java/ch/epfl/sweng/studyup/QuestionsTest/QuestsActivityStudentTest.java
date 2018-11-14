package ch.epfl.sweng.studyup.QuestionsTest;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class QuestsActivityStudentTest {
    @Rule
    public final ActivityTestRule<QuestsActivityStudent> rule =
            new ActivityTestRule<>(QuestsActivityStudent.class);

    @Test
    public void testOptionNoException() {
        onView(ViewMatchers.withId(R.id.top_navigation_infos)).perform(click());
        onView(withId(R.id.top_navigation_settings)).perform(click());
    }
}