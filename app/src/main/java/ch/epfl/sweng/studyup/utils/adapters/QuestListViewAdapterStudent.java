package ch.epfl.sweng.studyup.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.Question;

public class QuestListViewAdapterStudent extends BaseAdapter {
    private Context cnx;
    private List<Question> questions;
    private int idLayout;
    List<Integer> ids;

    public QuestListViewAdapterStudent(Context cnx, int idLayout, List<Question> questions, List<Integer> ids) {
        this.cnx = cnx;
        this.questions = questions;
        this.idLayout = idLayout;
        this.ids = ids;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ids.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(cnx, idLayout, null);
        }
        TextView text_view = convertView.findViewById(R.id.quest_title);
        text_view.setText(questions.get(position).getTitle());
        ImageView image_view = convertView.findViewById(R.id.is_quest_done);
        image_view.setImageResource(ids.get(position));
        return convertView;
    }
}