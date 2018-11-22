package ch.epfl.sweng.studyup.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.specialQuest.SpecialSpecialQuest;

public class SpecialQuestListViewAdapter extends BaseAdapter {

    private Context context;
    private List<SpecialSpecialQuest> specialQuests;
    private int idLayout;
    List<Integer> ids;

    public SpecialQuestListViewAdapter(Context context, int idLayout, List<SpecialSpecialQuest> specialQuests, List<Integer> ids) {
        this.context = context;
        this.specialQuests = specialQuests;
        this.idLayout = idLayout;
        this.ids = ids;
    }

    @Override
    public int getCount() {
        return specialQuests.size();
    }

    @Override
    public Object getItem(int position) {
        return specialQuests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ids.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, idLayout, null);
        }
        TextView text_view = convertView.findViewById(R.id.special_quest_title);
        text_view.setText(specialQuests.get(position).getTitle());
        ImageView image_view = convertView.findViewById(R.id.is_special_quest_done);
        image_view.setImageResource(ids.get(position));
        return convertView;
    }
}