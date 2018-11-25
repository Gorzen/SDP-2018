package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class CannotAnswerTwiceTest extends DisplayQuestionActivityTest {

    /*@Test
    public void correctAnswerGivesXpTest(){
        Question testQuestion = new Question("abc", "test", true, 0);
        Player player = Player.get();
        player.addAnsweredQuestion(testQuestion.getQuestionId(), true);
        player.updatePlayerData(null);
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), testQuestion);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.answer_button)).perform(click());
        onView(withText("Make your choice !")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        onView(withId(R.id.answer2)).perform(click());
        onView(withText("You can't answer a question twice !")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        Intents.intending(hasComponent(QuestsActivityStudent.class.getName()));
    }*/
}
