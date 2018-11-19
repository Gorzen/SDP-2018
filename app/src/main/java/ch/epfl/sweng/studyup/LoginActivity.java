package ch.epfl.sweng.studyup;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.testfairy.TestFairy;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;
import ch.epfl.sweng.studyup.utils.ViewPagerAdapter;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.Utils.setLocale;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ViewPager viewPager;
    private LinearLayout sliderDotsLayout;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Language
        String lang = getSharedPreferences(USER_PREFS, MODE_PRIVATE)
                .getString("lang", Locale.getDefault().getLanguage());
        setLocale(lang, this);

        if(!MOCK_ENABLED) {
            try {
                attemptLoginFromCache();
            }
            catch (Exception e) {
                Log.e(TAG, "Unable to load from cache: " + e.getMessage());
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TestFairy.begin(this, "2d95d8f0a9d7e4244bbd87321bcc5a12b56ccb2c");

        loadInterface();
    }

    private void attemptLoginFromCache() throws Exception {

        FileCacher<List<String>> loginPersistenceCache = new FileCacher<>(this, PERSIST_LOGIN_FILENAME);

        try {
            if(loginPersistenceCache.hasCache()) {

                final List<String> playerCacheData = loginPersistenceCache.readCache();
                try {
                    loadPlayerDataFromCache(playerCacheData);
                }
                catch (Exception e) {
                    /*
                    Something went wrong when loading player data from cache.
                    Cannot auto-login, return to onCreate(), user must maunally log in.
                     */
                    Log.e(TAG, e.toString());
                    return;
                }

                Firestore.get().syncPlayerData();
                Utils.waitAndTag(TIME_TO_WAIT_FOR_AUTO_LOGIN, TAG);
                /*
                Auto-login successful.
                Direct user to home activity corresponding to their role.
                 */
                Class homeActivity = Player.get().getRole().equals(Role.student) ?
                        MainActivity.class : AddQuestionActivity.class;
                startActivity(new Intent(this, homeActivity));
            }

        } catch (IOException e) {
            e.printStackTrace();
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

    private void loadInterface() {

        viewPager = findViewById(R.id.viewPager);
        sliderDotsLayout = findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        final int dotsNumber = viewPagerAdapter.getCount();
        dots = new ImageView[dotsNumber];
        for(int i = 0; i<dotsNumber; i++){
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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i<dotsNumber; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_non_active_24dp));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_active_24dp));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void onLoginButtonClick(View view) {

        RadioGroup roles = findViewById(R.id.StudentOrTeacherButtons);
        RadioButton checkedRole = findViewById(roles.getCheckedRadioButtonId());
        if(checkedRole == null) {
            Toast.makeText(this, R.string.text_when_no_role_selected, Toast.LENGTH_SHORT).show();
        }
        else {

            Intent authServerRedirect = new Intent(Intent.ACTION_VIEW);

            authServerRedirect.setData(Uri.parse(AUTH_SERVER_URL));

            Role loginRole = checkedRole.getId() == R.id.student ? Role.student : Role.teacher;
            Player.get().setRole(loginRole);

            startActivity(authServerRedirect);
        }

    }
}
