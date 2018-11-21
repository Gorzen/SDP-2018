package ch.epfl.sweng.studyup.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants.Course;


public class DisplayCourseStatsActivity extends CourseStatsActivity {

    private static final String TAG = "DisplayCourseStatsActivity";
    private Course course;
    private static List<Player> allUsers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_course_stats);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Intent intent = getIntent();
        course = Course.valueOf(intent.getStringExtra(DisplayQuestionActivity.class.getName()));

        TextView name_course = findViewById(R.id.course_name);
        name_course.setText(course.toString());

        TextView nb_students = findViewById(R.id.nb_students);
        List<Player> playerList = getStudentsFromCourse(course);
        nb_students.setText("Number of enrolled students: " + String.valueOf(playerList.size()));

        setupListViewQ(playerList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firestore.get().loadUsers(this);
    }

    public void onBackButton(View view) {
        finish();
    }

    //retrieve players from firebase
    public static void setPlayers(List<Player> playerList) {
        allUsers = playerList;
    }


    protected void setupListViewQ(final List<Player> playerList) {
        ListView listView = findViewById(R.id.listViewPlayer);

        ArrayList<Integer> rates = new ArrayList<>();
        ArrayList<Integer> nb_answer = new ArrayList<>();

        List<String> quests_course = getQuestsStringFromCourse(course);
        Set<String> s1 = new HashSet<>(quests_course);

        for (Player player : playerList) {
            HashMap<String, Boolean> answered_total = new HashMap<>(player.getAnsweredQuestion());
            double nb_good_answer_course = 0;
            int nb_answer_course = 0;
            Set<String> s2 = answered_total.keySet();
            s2.retainAll(s1); //s2 = only String Question Id from course which player answered to
            nb_answer_course = s2.size();
            for (String s : s2) {
                if(answered_total.get(s)) nb_good_answer_course++;
            }
            int rate = nb_answer_course == 0 ? 0 : (int)nb_good_answer_course/nb_answer_course*100;
            rates.add(rate);
            nb_answer.add(nb_answer_course);
        }
        ListPlayerAdapter listPlayerAdapter = new ListPlayerAdapter(this, playerList, rates, nb_answer, quests_course.size());
        listView.setAdapter(listPlayerAdapter);
    }



    //get all students for one course
    public List<Player> getStudentsFromCourse(Course course){
        List<Player> playersEnrolledInCourse = new ArrayList<>();
        for (Player p: allUsers) {
            if(p.getCourses().contains(course)){
                playersEnrolledInCourse.add(p);
            }
        }
        return playersEnrolledInCourse;
    }

    //get all questions for one course TODO
    public List<String> getQuestsStringFromCourse(Course course){
        List<String> questStrFromCourse = new ArrayList<>();
        for (Question q: getAllQuestions()) {
            if(q.getCourseName().equals(course.name())){
                questStrFromCourse.add(q.getQuestionId());
            }
        }
        return questStrFromCourse;
    }

    private class ListPlayerAdapter extends BaseAdapter {

        private Context cnx;
        private List<Player> players;
        private ArrayList<Integer> rates;
        private ArrayList<Integer> nb_answer;
        private int total_quests_for_course;

        public ListPlayerAdapter(Context cnx,
                                 List<Player> players,
                                 ArrayList<Integer> rates,
                                 ArrayList<Integer> nb_answer,
                                 int total_quests_for_course) {
            this.cnx=cnx;
            this.players=players;
            this.nb_answer=nb_answer;
            this.rates=rates;
            this.total_quests_for_course=total_quests_for_course;
        }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int position) {
            return players.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(players.get(position).getSciperNum());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(cnx, R.layout.player_stat_item_model, null);
            }
            TextView text_view_last = (TextView) convertView.findViewById(R.id.last_name);
            TextView text_view_first = (TextView) convertView.findViewById(R.id.first_firstname);
            TextView text_view_success = (TextView) convertView.findViewById(R.id.success_rate);
            TextView text_view_sciper = (TextView) convertView.findViewById(R.id.sciper);
            TextView text_view_nb_answer = (TextView) convertView.findViewById(R.id.quests_answered);

            text_view_last.setText(players.get(position).getLastName());
            String first_firstname = players.get(position).getFirstName();
            if(first_firstname.contains(" ")) {
                first_firstname = first_firstname.substring(0, first_firstname.indexOf(' '));
            }
            text_view_first.setText(first_firstname);
            String successString = "Success : "+rates.get(position)+"%";
            text_view_success.setText(successString);
            text_view_sciper.setText(players.get(position).getSciperNum());
            String qAnsString = "Quests answered : "+nb_answer.get(position)+"/"+total_quests_for_course;
            text_view_nb_answer.setText(qAnsString);

            return convertView;
        }
    }

}
