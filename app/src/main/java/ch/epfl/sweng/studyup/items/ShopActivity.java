package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestDisplayActivity;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.adapters.ListItemAdapter;

import static ch.epfl.sweng.studyup.utils.Utils.setupToolbar;

public class ShopActivity extends RefreshContext {
    private ListView itemsToBuy;
    private ListItemAdapter listItemAdapter;
    private List<Items> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        items = Arrays.asList((Items[])intent.getSerializableExtra(Items.class.getName()));

        setupToolbar(this);
        
        itemsToBuy = findViewById(R.id.list_view_shop);
        listItemAdapter = new ListItemAdapter(this, new ArrayList<>(items), R.layout.shop_list_item_model, true);
        itemsToBuy.setAdapter(listItemAdapter);

        itemsToBuy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Items item = (Items) listItemAdapter.getItem(position);
                startActivity(new Intent(parent.getContext(), BuyItemActivity.class).putExtra(BuyItemActivity.class.getName(), item.getName()));
            }
        });
    }

    public void onBackButtonShop(View v) {
        Utils.restartHomeActivity(ShopActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.restartHomeActivity(ShopActivity.this);
    }
}
