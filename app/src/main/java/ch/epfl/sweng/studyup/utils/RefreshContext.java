package ch.epfl.sweng.studyup.utils;

import android.support.v7.app.AppCompatActivity;

public class RefreshContext extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Utils.mainContext = getApplicationContext();
    }
}
