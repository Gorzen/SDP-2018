package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.ListItemAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import static ch.epfl.sweng.studyup.utils.Constants.*;

public class InventoryActivity extends NavigationStudent {
    private ArrayAdapter<String> adapter;
    private ListItemAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        navigationSwitcher(InventoryActivity.this, InventoryActivity.class, INVENTORY_INDEX);

        ArrayList<String> itemsName = Items.getItemsNames();
        ListView ownedItems = findViewById(R.id.listViewItems);
        listItemAdapter = new ListItemAdapter(getApplicationContext(), Items.values(), false);
        ownedItems.setAdapter(listItemAdapter);
        ownedItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Items item = (Items)listItemAdapter.getItem(position);
                startActivity(new Intent(parent.getContext(), DisplayItemActivity.class).putExtra(DisplayItemActivity.class.getName(), item.getName()));
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
}
