package ch.epfl.sweng.studyup.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.R;

public class ListItemAdapter extends BaseAdapter {

    private Context cnx;
    private ArrayList<String> names;
    private ArrayList<Integer> ids;

    public ListItemAdapter(Context cnx, ArrayList<String> names, ArrayList<Integer> ids) {
        this.cnx=cnx;
        this.names=names;
        this.ids=ids;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ids.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(cnx, R.layout.quest_item_model, null);
        }
        ImageView image_view = (ImageView) convertView.findViewById(R.id.is_quest_done);
        TextView text_view = (TextView) convertView.findViewById(R.id.quest_title);
        image_view.setImageResource(ids.get(position));
        text_view.setText(names.get(position));
        return convertView;
    }
}
