package ch.epfl.sweng.studyup.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.QuestsActivity;
import ch.epfl.sweng.studyup.social.ChatActivity;
import ch.epfl.sweng.studyup.social.RankingsActivity;


/**
 * Navigation
 *
 * Used to navigate between principal activities
 */
public class Navigation extends RefreshContext implements ActivityCompat
        .OnRequestPermissionsResultCallback {

    public void navigationSwitcher(final Context cn, final Class<?> activity, final int current_index) {
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem = menu.getItem(current_index);

        menuItem.setChecked(false); // Give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final ArrayList<Integer> button_ids = new ArrayList<>(Arrays.asList(
                        R.id.navigation_home,
                        R.id.navigation_quests,
                        R.id.navigation_rankings,
                        R.id.navigation_map,
                        R.id.navigation_chat));
                final ArrayList<Class> activities = new ArrayList<Class>(Arrays.asList(
                        MainActivity.class,
                        QuestsActivity.class,
                        RankingsActivity.class,
                        MapsActivity.class,
                        ChatActivity.class));
                final ArrayList<Integer> activities_id = new ArrayList<>(Arrays.asList(
                        Utils.DEFAULT_INDEX,
                        Utils.QUESTS_INDEX,
                        Utils.RANKINGS_INDEX,
                        Utils.MAP_INDEX,
                        Utils.MAX_INDEX));

                for (int i = 0; i <= Utils.MAX_INDEX; i++) {
                    if (item.getItemId() == button_ids.get(i) && !activity.equals(activities.get(i))) {
                        Intent intent = new Intent(cn, activities.get(i));
                        startActivity(intent);
                        transitionForNavigation(current_index, activities_id.get(i));
                    }
                }
                return false;

            }
        });
    }

    public void transitionForNavigation(int current_index, int destination_index) {
        if (destination_index > current_index) {
            overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);


        } else if (destination_index < current_index) {
            overridePendingTransition(R.anim.go_left_in, R.anim.go_left_out);
        }
    }
}
