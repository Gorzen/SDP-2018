package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)

public class BackButtonTest extends DisplayQuestionActivityTest{
    @Test
    public void backButtonTest() {
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), sampleQuestion);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.back_button)).perform(ViewActions.click());
        assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
