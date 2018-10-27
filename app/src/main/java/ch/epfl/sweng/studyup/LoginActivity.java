package ch.epfl.sweng.studyup;

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

import java.io.IOException;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.utils.ViewPagerAdapter;

import static ch.epfl.sweng.studyup.utils.Utils.FB_ROLES_T;
import static ch.epfl.sweng.studyup.utils.Utils.PERSIST_LOGIN_FILENAME;
import static ch.epfl.sweng.studyup.utils.Utils.TIME_TO_WAIT_FOR_LOGIN;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    ViewPager viewPager;
    LinearLayout sliderDotsLayout;
    private ImageView[] dots;


    // Display message from intent set by authentication activity upon failed login
    public void displayFailedLoginMessage(Intent intent) {
        String failureMessage = intent.getStringExtra(getString(R.string.post_login_message_value));
        if (failureMessage != null) {
            Toast.makeText(LoginActivity.this, failureMessage, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Function that, given the data of the player (that is present in the cache), will update the
     * info of the player with those data
     *
     * @param array data of the user that was logged in
     * @return true iff the update has been a success
     */
    public boolean updatePlayerInfoFromArrayCached(final String[] array) {
        if(array.length != 4) return false;

        try {
            Player.get().setSciper(Integer.parseInt(array[0]));
        } catch (NumberFormatException e) {
            Log.d(TAG, "The format of the sciper is not valid: "+array[0]);
            return false;
        }
        Player.get().setFirstName(array[1]);
        Player.get().setLastName(array[2]);
        if(array[3].equals(FB_ROLES_T)) {
            Player.get().setRole(true);
        } else {
            Player.get().setRole(false);
        }

        return true;
    }

    /**
     * Function used to persist the login of the user once he/she has already logged in. It checks
     * if there's data for persisting login in the cache. If that data is valid, it will
     * automatically log the user in.
     */
    private void checkLoggedUser() {
        FileCacher<String[]> persistLogin = new FileCacher<>(this, PERSIST_LOGIN_FILENAME);
        try {
            if(persistLogin.hasCache()) {
                final String[] loggedUserData = persistLogin.readCache();
                if(!updatePlayerInfoFromArrayCached(loggedUserData)) return;
                Firestore.get().getAndSetUserData(Player.get().getSciper(), Player.get().getFirstName(), Player.get().getLastName());

                Intent toApp;
                if (Player.get().getRole()) {
                    toApp = new Intent(LoginActivity.this, AddQuestionActivity.class);
                } else {
                    waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);
                    toApp = new Intent(LoginActivity.this, MainActivity.class);
                }

                startActivity(toApp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkLoggedUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotsLayout = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        final int dotsNumber = viewPagerAdapter.getCount();
        dots = new ImageView[dotsNumber];

        //initialisation of dots
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

        //on fail
        displayFailedLoginMessage(getIntent());
    }

    public void onLoginButtonClick(View view) {
        RadioGroup roles = findViewById(R.id.StudentOrTeacherButtons);
        RadioButton checkedRole = findViewById(roles.getCheckedRadioButtonId());
        if(checkedRole == null) {
            Toast.makeText(this, R.string.text_when_no_role_selected, Toast.LENGTH_SHORT).show();
        } else {
            if(checkedRole.getId() == R.id.student) {
                Player.get().setRole(false);
            } else {
                Player.get().setRole(true);
            }
            String authURL = "https://studyup-authenticate.herokuapp.com/getCode";
            Intent authIntent = new Intent(Intent.ACTION_VIEW);
            authIntent.setData(Uri.parse(authURL));
            startActivity(authIntent);
        }

    }
}