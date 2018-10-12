package ch.epfl.sweng.studyup;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Navigation extends AppCompatActivity {

    public void navigationSwitcher(final Context cn,final Class<?> c, int itemIndex) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem = menu.getItem(itemIndex);
        menuItem.setChecked(true); //give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (!c.equals(MainActivity.class)) {
                            Intent intent_m = new Intent(cn, MainActivity.class);
                            startActivity(intent_m);
                            overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.navigation_quests:
                        if (!c.equals(QuestsActivity.class)) {
                            Intent intent_q = new Intent(cn, QuestsActivity.class);
                            startActivity(intent_q);
                            overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.navigation_rankings:
                        if (!c.equals(RankingsActivity.class)) {
                            Intent intent_r = new Intent(cn, RankingsActivity.class);
                            startActivity(intent_r);
                            overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.navigation_map:
                        if (!c.equals(MapActivity.class)) {
                            Intent intent_m = new Intent(cn, MapActivity.class);
                            startActivity(intent_m);
                            overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.navigation_chat:
                        if (!c.equals(ChatActivity.class)) {
                            Intent intent_c = new Intent(cn, ChatActivity.class);
                            startActivity(intent_c);
                            overridePendingTransition(0, 0);
                        }
                        break;

                    default:
                        return false;
                }
                return false;
            }
        });
    }
}
