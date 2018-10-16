package ch.epfl.sweng.studyup;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Navigation extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public TextView view_username; //todo make it private in MainActivity when linking with firebase

    public final static int DEFAULT_INDEX = 0, QUESTS_INDEX=1, RANKINGS_INDEX=2, MAP_INDEX=3, CHAT_INDEX=4;

    public void navigationSwitcher(final Context cn, final Class<?> c, final int current_index) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem = menu.getItem(current_index);

        menuItem.setChecked(true); //give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: //destination
                        if (!c.equals(MainActivity.class)) {
                            Intent intent_m = new Intent(cn, MainActivity.class);
                            startActivity(intent_m);
                            //overridePendingTransition(R.anim.go_left_in, R.anim.go_left_out);
                            transitionForNavigation(current_index, DEFAULT_INDEX);
                        }
                        break;

                    case R.id.navigation_quests:
                        if (!c.equals(QuestsActivity.class)) {
                            Intent intent_q = new Intent(cn, QuestsActivity.class);
                            startActivity(intent_q);
                            transitionForNavigation(current_index, QUESTS_INDEX);
                        }
                        break;

                    case R.id.navigation_rankings:
                        if (!c.equals(RankingsActivity.class)) {
                            Intent intent_r = new Intent(cn, RankingsActivity.class);
                            startActivity(intent_r);
                            transitionForNavigation(current_index, RANKINGS_INDEX);
                        }
                        break;

                    case R.id.navigation_map:
                        if (!c.equals(MapActivity.class)) {
                            Intent intent_m = new Intent(cn, MapActivity.class);
                            startActivity(intent_m);
                            transitionForNavigation(current_index, MAP_INDEX);
                        }
                        break;

                    case R.id.navigation_chat:
                        if (!c.equals(ChatActivity.class)) {
                            Intent intent_c = new Intent(cn, ChatActivity.class);
                            startActivity(intent_c);
                            transitionForNavigation(current_index, CHAT_INDEX);
                        }
                        break;

                    default:
                        return false;
                }
                return false;
            }
        });
    }
    public void transitionForNavigation(int current_index, int destination_index) {
        if(destination_index>current_index) {
            overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
        }
        else if(destination_index<current_index) {
            overridePendingTransition(R.anim.go_left_in, R.anim.go_left_out);
        }
    }

}
