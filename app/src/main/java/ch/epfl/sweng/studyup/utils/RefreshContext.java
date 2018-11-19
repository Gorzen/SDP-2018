package ch.epfl.sweng.studyup.utils;

import android.support.v7.app.AppCompatActivity;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

public class RefreshContext extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        setTheme(APP_THEME);
        MOST_RECENT_ACTIVITY = this;
    }
}
