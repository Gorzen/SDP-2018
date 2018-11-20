package ch.epfl.sweng.studyup.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.R;

public class QuestListViewAdapter extends BaseAdapter {

    private Context cnx;
    private ArrayList<String> names;
    private ArrayList<Integer> ids;
    private ArrayList<Integer> langs;

    public QuestListViewAdapter(Context cnx, ArrayList<String> names, ArrayList<Integer> ids, ArrayList<Integer> langs) {
        this.cnx=cnx;
        this.names=names;
        this.ids=ids;
        this.langs = langs;
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

    public long getLangs(int position) {
        return langs.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(cnx, R.layout.quest_item_model, null);
        }
        ImageView image_view = (ImageView) convertView.findViewById(R.id.is_quest_done);
        ImageView lang_image = (ImageView) convertView.findViewById(R.id.lang_img);;
        TextView text_view = (TextView) convertView.findViewById(R.id.quest_title);
        image_view.setImageResource(ids.get(position));
        lang_image.setImageResource(langs.get(position));
        text_view.setText(names.get(position));
        return convertView;
    }
}
