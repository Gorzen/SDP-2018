package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import ch.epfl.sweng.studyup.question.AddQuestionActivity;


public class MainActivity extends Navigation {
    CircularProgressIndicator levelProgress;

    //Text that will be displayed in the levelProgress layout
    private static final CircularProgressIndicator.ProgressTextAdapter LEVEL_PROGRESS_TEXT = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double progress) {
            return (progress*100+"% of level ").concat(String.valueOf(Player.get().getLevel()+1));
        }
    };

    private ImageButton pic_button;
    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/studyup_photos";
    private int GALLERY = 0, CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //user picture
        pic_button = (ImageButton) findViewById(R.id.pic_btn);
        imageview = (ImageView) findViewById(R.id.pic_imageview);
        pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bottom navigation bar
        navigationSwitcher(MainActivity.this, MainActivity.class, 0);

        //level progression bar
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                123);

        levelProgress = findViewById(R.id.level_progress);
        levelProgress.setProgress(Player.get().getLevelProgress(), 1);
        levelProgress.setStartAngle(270);
        levelProgress.setProgressTextAdapter(LEVEL_PROGRESS_TEXT);

    }

    //Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }


    //Allows you to do an action with the toolbar (in a different way than with the navigation bar)
    //Corresponding activities are not created yet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.top_navigation_settings) {
            Toast.makeText(MainActivity.this,
                    "You have clicked on Settings :)",
                    Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.top_navigation_infos) {
            Toast.makeText(MainActivity.this,
                    "You have clicked on Infos :)",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void questionAddTest(View view) {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        startActivity(intent);
    }

    public void onLocButtonClick(View view) {
        LocationTracker locationTracker = new LocationTracker(getApplicationContext());
        Location currLoc = locationTracker.getLocation();
        if (currLoc != null) {
            double currLat = currLoc.getLatitude();
            double currLong = currLoc.getLongitude();
            Toast.makeText(getApplicationContext(),
                    "Latitude: " + currLat + "\nLongitude: " + currLong,
                    Toast.LENGTH_LONG).show();
            TextView test = findViewById(R.id.helloWorld);
            test.setText("Latitude: " + currLat + "\nLongitude: " + currLong);  //TODO not recommended to concatenate here, should use resource string with placeholders (+hardcoded literals)
        }
    }

    /**
     * Function that is called when adding xp with the button
     *
     * @param view
     */
    public void addExpPlayer(View view) {
        Player.get().addExperience(Player.XP_STEP);
        levelProgress.setCurrentProgress(Player.get().getLevelProgress());
        levelProgress.setProgressTextAdapter(LEVEL_PROGRESS_TEXT);

    }

}
