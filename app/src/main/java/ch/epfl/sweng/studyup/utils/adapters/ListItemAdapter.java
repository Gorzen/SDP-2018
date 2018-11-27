package ch.epfl.sweng.studyup.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.Items;

public class ListItemAdapter extends BaseAdapter {
    private Context cnx;
    private ArrayList<Items> items;
    private boolean displayPriceAndCoin;

    public ListItemAdapter(Context cnx, ArrayList<Items> items, boolean displayPriceAndCoin) {
        this.cnx=cnx;
        ArrayList<Items> sortedItems = new ArrayList<>(items);
        Collections.sort(sortedItems, new Comparator<Items>() {
            @Override
            public int compare(Items i1, Items i2) {
                return i1.getName().compareToIgnoreCase(i2.getName());
            }
        });
        this.items = sortedItems;
        this.displayPriceAndCoin = displayPriceAndCoin;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(cnx, R.layout.shop_list_item_model, null);
        }
        Items item = items.get(position);
        ImageView shopItemImage = convertView.findViewById(R.id.shop_item_image);
        TextView shopItemName =  convertView.findViewById(R.id.shop_item_name);
        shopItemImage.setImageResource(item.getImageName());
        shopItemName.setText(item.getName());
        if(displayPriceAndCoin) {
            ImageView shopCoin = convertView.findViewById(R.id.shop_coin);
            shopCoin.setImageResource(R.drawable.gold_coin);
            TextView shopItemPrice =  convertView.findViewById(R.id.shop_item_price);
            shopItemPrice.setText(Integer.toString(item.getPrice()));
        } else {
            TextView inventoryItemQuantity = convertView.findViewById(R.id.inventory_item_quantity);
            String quantity = Integer.toString(Items.countItem(item));
            inventoryItemQuantity.setText(quantity + "x"); //NON-NLS
        }
        return convertView;
    }

}
