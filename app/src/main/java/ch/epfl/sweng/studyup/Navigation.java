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

import static ch.epfl.sweng.studyup.Utils.idToAct;

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
                try {
                    Class activity = idToAct.get(item.getItemId());

                    if(!this.getClass().getName().equals(activity.getName())) {
                        Intent intent_m = new Intent(cn, activity);
                        startActivity(intent_m);
                        //overridePendingTransition(R.anim.go_left_in, R.anim.go_left_out);
                        transitionForNavigation(current_index, DEFAULT_INDEX);
                    }
                    return true;
                } catch (NullPointerException e) {
                    return false;
                }
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
