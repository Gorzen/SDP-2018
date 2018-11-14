package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kosalgeek.android.caching.FileCacher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import ch.epfl.sweng.studyup.questions.AddQuestionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ROLE_S;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ROLE_T;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

@RunWith(AndroidJUnit4.class)
public class PersistLoginTest {
    @Rule
    public final ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class);
    FileCacher<String[]> persistLoginData;
    Intent toLogin = new Intent();

    @Before
    public void setup() {
        init();
        MOCK_ENABLED = true;
        toLogin.setClass(rule.getActivity(), LoginActivity.class);
        persistLoginData = new FileCacher<>(rule.getActivity().getApplicationContext(), PERSIST_LOGIN_FILENAME);
        rule.launchActivity(new Intent());
        MOCK_ENABLED = false;
        try {
            persistLoginData.clearCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void clear() {
        release();
        try {
            persistLoginData.clearCache();
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
    public void cacheWithInvalidInfoShouldNotRedirect1() {
        String[] invalid = new String[4];
        invalid[0] = "90";
        invalid[1] = INITIAL_FIRSTNAME;
        invalid[2] = INITIAL_LASTNAME;
        invalid[3] = FB_ROLE_S;

        try {
            persistLoginData.writeCache(invalid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rule.finishActivity();
        rule.launchActivity(new Intent());

        onView(withId(R.id.student)).perform(click());
    }

    @Test
    public void cacheWithInvalidInfoShouldNotRedirect2() {
        String[] invalid = new String[3];
        invalid[0] = "222222";
        invalid[1] = INITIAL_FIRSTNAME;
        invalid[2] = INITIAL_LASTNAME;

        try {
            persistLoginData.writeCache(invalid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rule.finishActivity();
        rule.launchActivity(new Intent());

        onView(withId(R.id.student)).perform(click());
    }

    @Test
    public void cacheWithInvalidInfoShouldNotRedirect3() {
        String[] invalid = new String[4];
        invalid[0] = "notANumber";
        invalid[1] = INITIAL_FIRSTNAME;
        invalid[2] = INITIAL_LASTNAME;
        invalid[3] = FB_ROLE_S;

        try {
            persistLoginData.writeCache(invalid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rule.finishActivity();
        rule.launchActivity(new Intent());

        onView(withId(R.id.student)).perform(click());
    }

    @Test
    public void cacheWithInvalidInfoShouldNotRedirect4() {
        String[] invalid = new String[4];
        invalid[0] = "100000";
        invalid[1] = INITIAL_FIRSTNAME;
        invalid[2] = INITIAL_LASTNAME;
        invalid[3] = "notStudentNorTeacher";

        try {
            persistLoginData.writeCache(invalid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rule.finishActivity();
        rule.launchActivity(new Intent());

        onView(withId(R.id.student)).perform(click());
    }

    @Test
    public void validCacheShouldRedirectStudent() {
        String[] invalid = new String[4];
        invalid[0] = "100000";
        invalid[1] = INITIAL_FIRSTNAME;
        invalid[2] = INITIAL_LASTNAME;
        invalid[3] = FB_ROLE_S;

        try {
            persistLoginData.writeCache(invalid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rule.finishActivity();
        rule.launchActivity(new Intent());

        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void validCacheShouldRedirectTeacher() {
        String[] invalid = new String[4];
        invalid[0] = "100000";
        invalid[1] = INITIAL_FIRSTNAME;
        invalid[2] = INITIAL_LASTNAME;
        invalid[3] = FB_ROLE_T;

        try {
            persistLoginData.writeCache(invalid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rule.finishActivity();
        rule.launchActivity(new Intent());

        intended(hasComponent(AddQuestionActivity.class.getName()));
    }
}
