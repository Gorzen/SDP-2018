package ch.epfl.sweng.studyup.items;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import ch.epfl.sweng.studyup.utils.Utils;

public class InventoryActivity extends NavigationStudent {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        navigationSwitcher(InventoryActivity.this, InventoryActivity.class, Utils.INVENTORY_INDEX);

        ArrayList<String> itemsName = getItemsNames();
        ListView listView = findViewById(R.id.listViewItems);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsName);
        listView.setAdapter(adapter);

    }


    //Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }

    public ArrayList<String> getItemsNames() {
        List<Items> items = Player.get().getItems();
        ArrayList<String> itemsName = new ArrayList<>(items.size());
        for(int index = 0; index < items.size(); ++index) {
            itemsName.add(index, Items.getName(items.get(index)));
        }
        return itemsName;
    }
}
