package ch.epfl.sweng.studyup.TeacherTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.teacher.DisplayCourseStatsActivity;

@RunWith(AndroidJUnit4.class)
public class DisplayCourseStatsActivityTest {

    @Rule
    public final ActivityTestRule<DisplayCourseStatsActivity> mActivityRule =
            new ActivityTestRule<>(DisplayCourseStatsActivity.class);
}
