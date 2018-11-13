package ch.epfl.sweng.studyup;

import java.io.IOException;

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

import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.ViewPagerAdapter;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;
import static ch.epfl.sweng.studyup.utils.Utils.*;
import static ch.epfl.sweng.studyup.utils.Constants.*;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ViewPager viewPager;
    private LinearLayout sliderDotsLayout;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(!isMockEnabled) {
            attemptLoginFromCache();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadInterface();
    }

    private void attemptLoginFromCache() {

        FileCacher<String[]> playerCache = new FileCacher<>(this, PERSIST_LOGIN_FILENAME);

        try {
            if(playerCache.hasCache()) {

                final String[] playerCacheData = playerCache.readCache();

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

                /*
                Auto-login successful.
                Direct user to home activity corresponding to their role.
                 */
                Class homeActivity = Player.get().getRole().equals(Role.STUDENT) ?
                        MainActivity.class : AddQuestionActivity.class;

                startActivity(new Intent(this, homeActivity));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerDataFromCache(final String[] playerCacheData) throws Exception {

        if(playerCacheData.length != 4) {
            throw new Exception("Invalid cache array size.");
        }

        int sciperNum = Integer.parseInt(playerCacheData[0]);
        if (sciperNum < Constants.MIN_SCIPER || sciperNum > Constants.MAX_SCIPER) {
            throw new Exception("Invalid Sciper number: " + sciperNum + ".");
        }

        Player currPlayer = Player.get();
        currPlayer.setSciper(sciperNum);
        currPlayer.setFirstName(playerCacheData[1]);
        currPlayer.setLastName(playerCacheData[2]);
        currPlayer.setRole(Role.valueOf(playerCacheData[3]));
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

            Player currPlayer = Player.get();

            if (checkedRole.getId() == R.id.student) {
                currPlayer.setRole(Role.STUDENT);
            }
            else {
                Player.get().setRole(Role.TEACHER);
            }

            Intent authServerRedirect = new Intent(Intent.ACTION_VIEW);
            authServerRedirect.setData(Uri.parse(AUTH_SERVER_URL));
            startActivity(authServerRedirect);
        }

    }
}