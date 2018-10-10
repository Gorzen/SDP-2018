package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true); //give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;

                    case R.id.navigation_quests:
                        Intent intent_q = new Intent(MainActivity.this, QuestsActivity.class);
                        startActivity(intent_q);
                        //todo R.anim.### does not work if we want better transitions
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_rankings:
                        Intent intent_r = new Intent(MainActivity.this, RankingsActivity.class);
                        startActivity(intent_r);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_map:
                        Intent intent_m = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(intent_m);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_chat:
                        Intent intent_c = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(intent_c);
                        overridePendingTransition(0, 0);
                        break;

                    default:
                        return false;
                }
                return false;
            }
        });

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                123);

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

    public void toCharacterHomePage(View view) {
        Intent intent = new Intent(this, CharacterHomepageActivity.class);
        startActivity(intent);
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
            test.setText("Latitude: " + currLat + "\nLongitude: " + currLong);
        }
    }
}
