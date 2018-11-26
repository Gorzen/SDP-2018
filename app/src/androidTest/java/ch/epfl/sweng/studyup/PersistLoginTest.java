package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kosalgeek.android.caching.FileCacher;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.questions.AddOrEditQuestionActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

@RunWith(AndroidJUnit4.class)
public class PersistLoginTest {

    @Rule
    public final ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, false);

    FileCacher<List<String>> loginDataCacher;

    @Before
    public void setupTest() {
        init();
        MOCK_ENABLED = true;
        rule.launchActivity(new Intent());
        Utils.waitAndTag(100, "Wait to activity to launch.");
        loginDataCacher = new FileCacher<>(rule.getActivity().getApplicationContext(), PERSIST_LOGIN_FILENAME);

        try {
            loginDataCacher.clearCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void clear() {
        release();
        MOCK_ENABLED = false;
        try {
            loginDataCacher.clearCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void emptyCacheShouldNotRedirect() {

        rule.finishActivity();
        rule.launchActivity(new Intent());

        onView(withId(R.id.student)).perform(click());
    }

    @Test
    public void invalidCacheShouldNotRedirect() throws Exception {

        List<String> invalidCacheData = new ArrayList<>();
        invalidCacheData.add(0, "0");
        invalidCacheData.add(1, INITIAL_FIRSTNAME);
        invalidCacheData.add(2, INITIAL_LASTNAME);
        invalidCacheData.add(3, Role.student.name());

        loginDataCacher.writeCache(invalidCacheData);

        rule.finishActivity();
        MOCK_ENABLED = false;
        rule.launchActivity(new Intent());

        onView(withId(R.id.student)).perform(click());
    }


    @Test
    public void validCacheShouldRedirectStudent() throws Exception {

        List<String> studentCacheData = new ArrayList<>();
        studentCacheData.add(0, INITIAL_SCIPER);
        studentCacheData.add(1, INITIAL_FIRSTNAME);
        studentCacheData.add(2, INITIAL_LASTNAME);
        studentCacheData.add(3, Role.student.name());

        loginDataCacher.writeCache(studentCacheData);

        rule.finishActivity();
        MOCK_ENABLED = false;
        rule.launchActivity(new Intent());

        intended(hasComponent(HomeActivity.class.getName()));
    }

    @Test
    public void validCacheShouldRedirectTeacher() throws Exception {

        List<String> teacherCacheData = new ArrayList<>();
        teacherCacheData.add(0, INITIAL_SCIPER);
        teacherCacheData.add(1, INITIAL_FIRSTNAME);
        teacherCacheData.add(2, INITIAL_LASTNAME);
        teacherCacheData.add(3, Role.teacher.name());

        loginDataCacher.writeCache(teacherCacheData);

        rule.finishActivity();
        MOCK_ENABLED = false;
        rule.launchActivity(new Intent());

        intended(hasComponent(AddOrEditQuestionActivity.class.getName()));
    }
}
