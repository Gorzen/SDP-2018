package ch.epfl.sweng.studyup.QuestionsTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;

@RunWith(AndroidJUnit4.class)
public class EditQuestionActivityTest {

    @Rule
    ActivityTestRule<QuestsActivityTeacher> mAcivityTestRule =
            new ActivityTestRule<>(QuestsActivityTeacher.class);



}
