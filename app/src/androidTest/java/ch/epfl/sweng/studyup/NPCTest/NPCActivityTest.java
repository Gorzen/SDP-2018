package ch.epfl.sweng.studyup.NPCTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.npc.NPCActivity;


@RunWith(AndroidJUnit4.class)
public class NPCActivityTest {

    @Rule
    public final ActivityTestRule<NPCActivity> mActivityRule2 =
            new ActivityTestRule<>(NPCActivity.class);

    @Test
    public void ifWithinRangeOfNPCTest() {

    }

}
