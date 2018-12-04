package ch.epfl.sweng.studyup.NPCTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.npc.NPCActivity;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NPCActivityTest {

    @Rule
    public final ActivityTestRule<HomeActivity> mActivityRule2 =
            new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void backButtonTest() {
        mActivityRule2.getActivity().startActivity(new Intent(mActivityRule2.getActivity().getApplicationContext(), NPCActivity.class)
                .putExtra(Constants.NPC_ACTIVITY_INTENT_NAME, Constants.allNPCs.get(0).getName()));
        onView(withId(R.id.back_button)).perform(click());
    }

}
