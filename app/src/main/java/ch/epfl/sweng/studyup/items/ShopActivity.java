package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.ListItemAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import static ch.epfl.sweng.studyup.utils.Constants.*;

public class ShopActivity extends NavigationStudent {
    private ListView itemsToBuy;
    private ListItemAdapter listItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        itemsToBuy = findViewById(R.id.list_view_shop);
        listItemAdapter = new ListItemAdapter(getApplicationContext(), new ArrayList<>(Arrays.asList(Items.values())), true);
        itemsToBuy.setAdapter(listItemAdapter);
        itemsToBuy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Items item = (Items) listItemAdapter.getItem(position);
                startActivity(new Intent(parent.getContext(), BuyItemActivity.class).putExtra(BuyItemActivity.class.getName(), item.getName()));
            }
        });
        navigationSwitcher(ShopActivity.this, ShopActivity.class, SHOP_INDEX);

    }

    //Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }


}
