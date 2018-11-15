package ch.epfl.sweng.studyup.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.studyup.R;

public class QuestListViewAdapter extends BaseAdapter {
    private Context cnx;
    private List<String> titles;
    private int idLayout;
    List<Integer> ids;

    public QuestListViewAdapter(Context cnx, int idLayout, List<String> titles, List<Integer> ids) {
        this.cnx=cnx;
        this.titles = titles;
        this.idLayout = idLayout;
        this.ids = ids;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ids.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(cnx, idLayout, null);
        }
        TextView text_view = convertView.findViewById(R.id.quest_title);
        text_view.setText(titles.get(position));
        return convertView;
    }
}
