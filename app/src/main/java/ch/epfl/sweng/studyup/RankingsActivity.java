package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class RankingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true); //give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent_h = new Intent(RankingsActivity.this, MainActivity.class);
                        startActivity(intent_h);
                        overridePendingTransition(0 ,0);
                        break;

                    case R.id.navigation_quests:
                        Intent intent_q = new Intent(RankingsActivity.this, QuestsActivity.class);
                        startActivity(intent_q);
                        overridePendingTransition(0 ,0);
                        break;

                    case R.id.navigation_rankings:
                        break;

                    case R.id.navigation_map:
                        Intent intent_m = new Intent(RankingsActivity.this, MapActivity.class);
                        startActivity(intent_m);
                        overridePendingTransition(0 ,0);
                        break;

                    case R.id.navigation_chat:
                        Intent intent_c = new Intent(RankingsActivity.this, ChatActivity.class);
                        startActivity(intent_c);
                        overridePendingTransition(0 ,0);
                        break;

                    default:
                        return false;
                }
                return false;
            }
        });
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
        if(item.getItemId()==R.id.top_navigation_settings) {
            Toast.makeText(RankingsActivity.this,
                    "You have clicked on Settings :)",
                    Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==R.id.top_navigation_infos) {
            Toast.makeText(RankingsActivity.this,
                    "You have clicked on Infos :)",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
