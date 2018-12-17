package ch.epfl.sweng.studyup.HomeActivityTests;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.Course.*;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_SCIPER;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;

@RunWith(AndroidJUnit4.class)
public class StatsInHomeActivityTest {

    @Rule
    public final ActivityTestRule<HomeActivity> mActivityRule =
            new ActivityTestRule<>(HomeActivity.class, true, false);

    @Before
    public void initPlayerAndMock() {
        Player.get().setSciperNum(INITIAL_SCIPER);
        Player.get().resetPlayer();
        MOCK_ENABLED = true;
    }

    @After
    public void cleanPlayerAndMock() {
        Player.get().resetPlayer();
        MOCK_ENABLED = false;
        QuestionParser.writeQuestions(new ArrayList<Question>(), mActivityRule.getActivity());
    }

    @Test
    public void testRankDisplay() {
        Player.get().addExperience((int) (Math.pow(2, 31) - 1), null);
        waitAndTag(2000, "Waiting for the server to be notificated.");
        mActivityRule.launchActivity(new Intent());
        waitAndTag(2000, "Waiting for the rank to be displayed.");
        onView(withId(R.id.rankNumberTextview)).check(matches(withText("#1")));
    }

    @Test
    public void testFavoriteCourseDisplay() {
        Player.get().addAnsweredQuestion("1", true, 1);
        Player.get().addAnsweredQuestion("2", true, 1);
        Player.get().addAnsweredQuestion("3", true, 1);
        Player.get().addAnsweredQuestion("4", true, 1);
        mActivityRule.launchActivity(new Intent());

        List<Question> questionsToWrite = new ArrayList<>();
        questionsToWrite.add(new Question("1", "first question", true, 0, Analyse.name(), "en"));
        questionsToWrite.add(new Question("2", "second question", true, 0, Analyse.name(), "en"));
        questionsToWrite.add(new Question("3", "third question", true, 0, SWENG.name(), "en"));
        questionsToWrite.add(new Question("4", "fourth question", true, 0, FunProg.name(), "en"));
        QuestionParser.writeQuestions(questionsToWrite, mActivityRule.getActivity());

        mActivityRule.finishActivity();
        mActivityRule.launchActivity(new Intent());

        waitAndTag(250, "Waiting for display to load.");
        onView(withId(R.id.favoriteCourseTextview)).check(matches(withText(Analyse.name())));
    }

    @Test
    public void testRatio() {
        Player.get().addAnsweredQuestion("1", true, 1);
        Player.get().addAnsweredQuestion("2", false, 1);
        Player.get().addAnsweredQuestion("3", true, 1);
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.ratioPercentageTextview)).check(matches(withText("66.7 %")));
        onView(withId(R.id.answeredNumberTextview)).check(matches(withText("3")));
    }
}