package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static ch.epfl.sweng.studyup.utils.Utils.FB_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.utils.Utils.FB_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.utils.Utils.FB_QUESTION_TRUEFALSE;

@RunWith(AndroidJUnit4.class)
public class QuestsActivityTeacherTest {
    private final String TAG = QuestsActivityTeacher.class.getSimpleName();
    private final String questionUUID = "Temporary fake uuid";
    private Question q;

    @Rule
    public final ActivityTestRule<QuestsActivityTeacher> rule =
            new ActivityTestRule<>(QuestsActivityTeacher.class, true, false);

    @Before
    public void addQuestionThatWillBeDisplayed() {
         q = new Question(questionUUID, "Teacher quests test", true, 0);
        Player.get().setRole(true);
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(500, TAG);
        rule.launchActivity(new Intent());
    }

    @After
    public void deleteQuestion() {
        Firestore.get().deleteQuestion(questionUUID);
    }


    //Test must be changed when changing the function called when clicking on a question
    public void listViewRedirectOnCorrectQuestion() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i=1; i < list.getAdapter().getCount(); ++i) {
                    if(list.getAdapter().getItem(i-1).toString() == questionUUID)
                        list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
                }
            }
        });

        String intentLaunchedTitle = rule.getActivity().getIntent().getStringExtra(FB_QUESTION_TITLE);
        int intentLaunchedAnswer = Integer.parseInt(rule.getActivity().getIntent().getStringExtra(FB_QUESTION_ANSWER));
        boolean intentLaunchedTrueOrFalse = Boolean.parseBoolean(rule.getActivity().getIntent().getStringExtra(FB_QUESTION_TRUEFALSE));

        assert(q.getTitle() == intentLaunchedTitle);
        assert(q.getAnswer() == intentLaunchedAnswer);
        assert(q.isTrueFalse() == intentLaunchedTrueOrFalse);
    }

    @Test
    public void shouldHaveAtLeastOneQuestion() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        assert(1 <= list.getAdapter().getCount());
    }
}