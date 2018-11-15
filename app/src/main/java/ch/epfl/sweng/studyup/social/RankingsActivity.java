package ch.epfl.sweng.studyup.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.SettingsActivity;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import static ch.epfl.sweng.studyup.utils.Constants.*;

public class RankingsActivity extends NavigationStudent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        navigationSwitcher(RankingsActivity.this, RankingsActivity.class, RANKINGS_INDEX);
    }
}
