package ch.epfl.sweng.studyup.teacher;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;
import static ch.epfl.sweng.studyup.utils.Constants.COURSE_INDEX;

public class CourseStatsActivity extends NavigationTeacher {

    private ListCourseAdapter listCourseAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stats);
        navigationSwitcher(CourseStatsActivity.this, CourseStatsActivity.class, COURSE_INDEX);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        setupListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firestore.get().loadQuestions(this);
    }

    private void setupListView() {
        ListView listView = findViewById(R.id.listViewCourses);

        ArrayList<Constants.Course> playerCourses = new ArrayList<>(Player.get().getCourses());
        ArrayList<Integer> successRates = new ArrayList<>(playerCourses.size());
        for (int i=0; i<playerCourses.size(); i++) {
            successRates.add(50);
        }
        listCourseAdapter = new ListCourseAdapter(this, playerCourses, successRates);
        listView.setAdapter(listCourseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constants.Course c = (Constants.Course) listCourseAdapter.getItem(position);
                startActivity(new Intent(parent.getContext(), DisplayCourseStatsActivity.class).putExtra(DisplayQuestionActivity.class.getName(), c.name()));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private static List<Question> listQ = new ArrayList<>();
    //get all questions TODO bug ici ! + remettre le loadPlayers dans cette classe + photos ?

    public List<Question> getAllQuestions(){
        LiveData<List<Question>> questions = parseQuestionsLiveData(this.getApplicationContext());
        questions.observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                listQ.addAll(questions);
            }
        });
        return listQ;
    }

    private class ListCourseAdapter  extends BaseAdapter {

        private Context cnx;
        private ArrayList<Constants.Course> courses;
        private ArrayList<Integer> rates;

        public ListCourseAdapter(Context cnx, ArrayList<Constants.Course> courses,
                                 ArrayList<Integer> rates) {
            this.cnx=cnx;
            this.courses=courses;
            this.rates=rates;
        }

        @Override
        public int getCount() {
            return courses.size();
        }

        @Override
        public Object getItem(int position) {
            return courses.get(position);
        }

        @Override
        public long getItemId(int position) { return courses.get(position).ordinal(); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(cnx, R.layout.course_stat_item_model, null);
            }
            TextView text_view_rate = (TextView) convertView.findViewById(R.id.success_rate);
            TextView text_view_title_nice = (TextView) convertView.findViewById(R.id.course_title);
            TextView text_view_title_bref = (TextView) convertView.findViewById(R.id.abbreviation);
            text_view_rate.setText(String.valueOf(rates.get(position)));
            String title_nice = courses.get(position).toString();
            int max_length = 31;
            text_view_title_nice.setText(title_nice.substring(0, Math.min(title_nice.length(), max_length)));
            text_view_title_bref.setText(courses.get(position).name());
            return convertView;
        }
    }

}
