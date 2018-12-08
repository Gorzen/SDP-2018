package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.studyup.auth.AuthenticationActivity;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;
import ch.epfl.sweng.studyup.utils.adapters.ViewPagerAdapter;

import static ch.epfl.sweng.studyup.utils.Constants.AUTH_SERVER_URL;
import static ch.epfl.sweng.studyup.utils.Constants.COLOR_SETTINGS_KEYWORD;
import static ch.epfl.sweng.studyup.utils.Constants.LANG_SETTINGS_KEYWORD;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_RED;
import static ch.epfl.sweng.studyup.utils.Constants.TIME_TO_WAIT_FOR_AUTO_LOGIN;
import static ch.epfl.sweng.studyup.utils.Constants.USER_PREFS;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.HOME_ACTIVITY;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.Utils.setLocale;
import static ch.epfl.sweng.studyup.utils.Utils.setupColor;

public class LoginActivity extends RefreshContext {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ViewPager viewPager;
    private LinearLayout sliderDotsLayout;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Firestore.get().getCoursesSchedule(this, Role.student);

        // Language
        String lang = getSharedPreferences(USER_PREFS, MODE_PRIVATE)
                .getString(LANG_SETTINGS_KEYWORD, Locale.getDefault().getLanguage());
        setLocale(lang, this);

        // Color
        String col = getSharedPreferences(USER_PREFS, MODE_PRIVATE)
                .getString(COLOR_SETTINGS_KEYWORD, SETTINGS_COLOR_RED);
        setupColor(col);

        if(!MOCK_ENABLED) {
            try {
                if(attemptLoginFromCache()) return;
            } catch (Exception e) {
                Log.e(TAG, "Unable to load from cache: " + e.getMessage());
            }
        }

        // TestFairy.begin(this, "2d95d8f0a9d7e4244bbd87321bcc5a12b56ccb2c");
      
        loadInterface(lang);
    }


    private boolean attemptLoginFromCache() throws Exception {

        FileCacher<List<String>> loginPersistenceCache = new FileCacher<>(this, PERSIST_LOGIN_FILENAME);

        try {
            if (loginPersistenceCache.hasCache()) {

                final List<String> playerCacheData = loginPersistenceCache.readCache();
                try {
                    loadPlayerDataFromCache(playerCacheData);
                } catch (Exception e) {
                    /*
                    Something went wrong when loading player data from cache.
                    Cannot auto-login, return to onCreate(), user must maunally log in.
                     */
                    Log.e(TAG, e.toString());
                    return false;
                }

                AuthenticationActivity.syncPlayerData();
                Utils.waitAndTag(TIME_TO_WAIT_FOR_AUTO_LOGIN, TAG);
                /*
                Auto-login successful.
                Direct user to home activity corresponding to their role.
                 */

                HOME_ACTIVITY = Player.get().isStudent() ?
                        HomeActivity.class : QuestsActivityTeacher.class;

                startActivity(new Intent(this, HOME_ACTIVITY));
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    public void loadPlayerDataFromCache(List<String> playerCacheData) throws Exception {

        String sciperNum = playerCacheData.get(0);
        String firstName = playerCacheData.get(1);
        String lastName = playerCacheData.get(2);
        Role role = Role.valueOf(playerCacheData.get(3));

        if (Integer.parseInt(sciperNum) < Constants.MIN_SCIPER ||
                Integer.parseInt(sciperNum) > Constants.MAX_SCIPER) {

            throw new Exception("Invalid Sciper number: " + sciperNum + ".");
        }

        Player currPlayer = Player.get();
        currPlayer.setSciperNum(sciperNum);
        currPlayer.setFirstName(firstName);
        currPlayer.setLastName(lastName);
        currPlayer.setRole(role);

    }

    private void loadInterface(String lang) {

        viewPager = findViewById(R.id.viewPager);
        sliderDotsLayout = findViewById(R.id.SliderDots);

        Integer[] images = {R.drawable.login_slide1,
                        R.drawable.login_slide2,
                        R.drawable.login_slide3,
                        R.drawable.login_slide4 };
        if(lang.equals("fr")) {
            images[0] = R.drawable.login_slide1_fr;
            images[1] = R.drawable.login_slide2_fr;
            images[2] = R.drawable.login_slide3_fr;
            images[3] = R.drawable.login_slide4_fr;
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, images);
        viewPager.setAdapter(viewPagerAdapter);

        final int dotsNumber = viewPagerAdapter.getCount();
        dots = new ImageView[dotsNumber];
        for (int i = 0; i < dotsNumber; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_non_active_24dp));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsLayout.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_active_24dp));

        //dots change colors in function of the current Page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsNumber; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_non_active_24dp));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_active_24dp));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onLoginButtonClick(View view) {

        RadioGroup roles = findViewById(R.id.StudentOrTeacherButtons);
        RadioButton checkedRole = findViewById(roles.getCheckedRadioButtonId());
        if (checkedRole == null) {
            Toast.makeText(this, R.string.text_when_no_role_selected, Toast.LENGTH_SHORT).show();
        } else {

            Intent authServerRedirect = new Intent(Intent.ACTION_VIEW);

            authServerRedirect.setData(Uri.parse(AUTH_SERVER_URL));

            Role loginRole = checkedRole.getId() == R.id.student ? Role.student : Role.teacher;
            HOME_ACTIVITY = loginRole.equals(Role.student) ?
                    HomeActivity.class : QuestsActivityTeacher.class;
            Player.get().setRole(loginRole);

            startActivity(authServerRedirect);
        }

    }
}
