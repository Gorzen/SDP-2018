package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.ShopListItemAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import static ch.epfl.sweng.studyup.utils.Constants.*;

public class ShopActivity extends NavigationStudent {
    private ListView itemsToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        itemsToBuy = findViewById(R.id.list_view_shop);
        final ShopListItemAdapter shopListItemAdapter = new ShopListItemAdapter(getApplicationContext(), Items.values());
        itemsToBuy.setAdapter(shopListItemAdapter);
        itemsToBuy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(parent.getContext(), DisplayItemActivity.class).putExtra(buyItemActivity.class.getName(), shopListItemAdapter.getItem(position)));
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
