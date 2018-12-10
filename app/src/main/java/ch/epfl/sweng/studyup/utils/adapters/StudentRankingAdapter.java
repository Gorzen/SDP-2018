package ch.epfl.sweng.studyup.utils.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.studyup.R;

import static ch.epfl.sweng.studyup.utils.Constants.bronze;
import static ch.epfl.sweng.studyup.utils.Constants.gold;
import static ch.epfl.sweng.studyup.utils.Constants.silver;

public class StudentRankingAdapter extends BaseAdapter {

    private Context context;
    private List<Pair<String, Integer>> studentRankings;
    private int idLayout;

    private int currRank;
    private int currRankNumCorrect;

    public StudentRankingAdapter(Context context, int idLayout, List<Pair<String, Integer>> studentRankings) {
        this.context = context;
        this.idLayout = idLayout;
        this.studentRankings = studentRankings;
        currRank = 1;
        currRankNumCorrect = 1;
    }

    @Override
    public int getCount() { return studentRankings.size(); }

    @Override
    public Object getItem(int position) { return studentRankings.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, idLayout, null);
        }
        TextView studentRank = convertView.findViewById(R.id.student_rank);
        TextView studentName = convertView.findViewById(R.id.leaderboard_student_name);
        TextView studentNumCorrectAnswers = convertView.findViewById(R.id.leaderboard_num_correct_answers);

        Pair<String, Integer> studentRankData = studentRankings.get(position);
        if (studentRankData.second < currRankNumCorrect) {
            currRank ++;
            currRankNumCorrect = studentRankData.second;
        }
        studentRank.setText(String.valueOf(currRank));
        studentName.setText(studentRankData.first);
        studentNumCorrectAnswers.setText(String.valueOf(studentRankData.second));

        // Set rank color to gold, silver, bronze for first, second, and third place, respectively
        switch(currRank) {
            case 1:
                studentRank.setTextColor(Color.parseColor(gold));
                break;
            case 2:
                studentRank.setTextColor(Color.parseColor(silver));
                break;
            case 3:
                studentRank.setTextColor(Color.parseColor(bronze));
                break;
        }

        return convertView;
    }
}
