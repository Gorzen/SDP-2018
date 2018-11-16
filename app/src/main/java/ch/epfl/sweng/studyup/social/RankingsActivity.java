package ch.epfl.sweng.studyup.social;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.Constants.RANKINGS_INDEX;

public class RankingsActivity extends NavigationStudent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        navigationSwitcher(RankingsActivity.this, RankingsActivity.class, RANKINGS_INDEX);
    }
}
