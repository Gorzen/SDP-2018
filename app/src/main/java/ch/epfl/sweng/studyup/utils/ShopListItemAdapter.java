package ch.epfl.sweng.studyup.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.Items;

public class ShopListItemAdapter extends BaseAdapter {
    private Context cnx;
    private Items[] items;

    public ShopListItemAdapter(Context cnx,Items[] items) {
        this.cnx=cnx;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(cnx, R.layout.shop_list_item_model, null);
        }
        Items item = items[position];
        ImageView shopItemImage = convertView.findViewById(R.id.shop_item_image);
        TextView shopItemName =  convertView.findViewById(R.id.shop_item_name);
        TextView shopItemPrice =  convertView.findViewById(R.id.shop_item_price);
        shopItemImage.setImageResource(item.getImageName());
        shopItemName.setText(item.getName());
        shopItemPrice.setText(item.getPrice());
        return convertView;
    }
}
