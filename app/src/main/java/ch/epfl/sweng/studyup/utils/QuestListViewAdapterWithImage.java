package ch.epfl.sweng.studyup.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ch.epfl.sweng.studyup.R;

public class QuestListViewAdapterWithImage extends QuestListViewAdapter {

    public QuestListViewAdapterWithImage(Context cnx, int idLayout, List<String> names, List<Integer> ids) {
        super(cnx, idLayout, names, ids);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ImageView image_view = convertView.findViewById(R.id.is_quest_done);
        image_view.setImageResource(ids.get(position));
        return convertView;
    }
}
