package ch.epfl.sweng.studyup.teacher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.utils.Constants.COURSE_INDEX;

public class CourseStatsActivity extends NavigationTeacher {

    private ListCourseAdapter listCourseAdapter;
    private static List<Player> allUsers = new ArrayList<>();
    private static List<Question> allQuestions = new ArrayList<>();



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
        Firestore.get().loadQuestionsForStats(this);
        Firestore.get().loadUsersForStats(this);
    }


    //retrieve players from firebase
    public static void setPlayers(List<Player> playerList) {
        allUsers = playerList;
    }
    //retrieve questions from firebase
    public static void setQuestions(List<Question> qList) { allQuestions = qList;}

    public List<Player> getAllUsers(){ return allUsers; }

    public List<Question> getAllQuestions(){ return allQuestions; }



    private void setupListView() {
        ListView listView = findViewById(R.id.listViewCourses);

        ArrayList<Course> playerCourses = new ArrayList<>(Player.get().getCourses());

        /*HashMap<Course, ArrayList<Integer>> mapData = new HashMap<>(course_To_RatesByPlayers_And_NbQuests(playerCourses));

        ArrayList<Integer> successRates = new ArrayList<>();
        ArrayList<Integer> total_nb_quests_course = new ArrayList<>();
        for(Course course : playerCourses) {
            ArrayList<Integer> rates_player_course = mapData.get(course);
            int rate_course = 0;
            int counter = 0;
            total_nb_quests_course.add(rates_player_course.remove(rates_player_course.size()-1));
            for(int i:rates_player_course) {
                if(i!=0) {
                    rate_course+=i;
                    counter++;
                }
                rate_course = counter == 0 ? 0 : rate_course/counter;
                successRates.add(rate_course);
            }
        }
        */

        ArrayList<Integer> successRates2 = new ArrayList<>(playerCourses.size());
        ArrayList<Integer> total_nb_quests_course2 = new ArrayList<>(playerCourses.size());
        for(int i = 0; i<playerCourses.size();i++) {
            successRates2.add(0);
            total_nb_quests_course2.add(0);
        }
        listCourseAdapter = new ListCourseAdapter(this, playerCourses, successRates2, total_nb_quests_course2);
        listView.setAdapter(listCourseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course c = (Course) listCourseAdapter.getItem(position);
                startActivity(new Intent(parent.getContext(), DisplayCourseStatsActivity.class).putExtra(DisplayQuestionActivity.class.getName(), c.name()));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

/*
    //get all students for one course
    public List<Player> getStudentsFromCourse(Course course){
        List<Player> playersEnrolledInCourse = new ArrayList<>();
        for (Player p: getAllUsers()) {
            if(p.getCourses().contains(course)){
                playersEnrolledInCourse.add(p);
            }
        }
        return playersEnrolledInCourse;
    }


    public List<String> getQuestsStringFromCourse(Course course){
        List<String> questStrFromCourse = new ArrayList<>();
        for (Question q: getAllQuestions()) {
            if(q.getCourseName().equals(course.name())){
                questStrFromCourse.add(q.getQuestionId());
            }
        }
        return questStrFromCourse;
    }


    public HashMap<Course, ArrayList<Integer>> course_To_RatesByPlayers_And_NbQuests(List<Course> courses_interated) {
        //ArrayList<Integer> successRates = new ArrayList<>(playerCourses.size());
        int total_nb_quests_course = 0;
        int nb_goodans_player_course = 0;
        int nb_ans_player_course;

        HashMap<Course, ArrayList<Integer>> course_to_rates = new HashMap<>();

        for(Course course : courses_interated) {
            List<String> quest_course = getQuestsStringFromCourse(course);
            total_nb_quests_course = quest_course.size();
            ArrayList<Integer> successRates = new ArrayList<>();

            for(Player p : getStudentsFromCourse(course)) {

                HashMap<String, Boolean> questions_answered_player = new HashMap<>(p.getAnsweredQuestion());
                HashSet<String> questions_answered_player_course = new HashSet<>(questions_answered_player.keySet());
                questions_answered_player_course.retainAll(quest_course);
                nb_ans_player_course = questions_answered_player_course.size();
                for(String s : questions_answered_player_course) {
                    if(questions_answered_player.get(s)) {
                        nb_goodans_player_course++;
                    }
                }
                int rate = total_nb_quests_course==0 ? 0 : nb_goodans_player_course*100/nb_ans_player_course;
                successRates.add(rate);
            }
            successRates.add(total_nb_quests_course); //add the number of quests for this course to have it in memory
            course_to_rates.put(course, successRates);
        }

        return course_to_rates;
    }*/






















    private class ListCourseAdapter  extends BaseAdapter {

        private Context cnx;
        private ArrayList<Course> courses;
        private ArrayList<Integer> rates;
        private ArrayList<Integer> nb_quests;

        public ListCourseAdapter(Context cnx, ArrayList<Course> courses,
                                 ArrayList<Integer> rates, ArrayList<Integer> nb_quests) {
            this.cnx=cnx;
            this.courses=courses;
            this.rates=rates;
            this.nb_quests=nb_quests;
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
            TextView text_view_nb = (TextView) convertView.findViewById(R.id.nb_quests);
            String success_rate = "Success Rate : " + rates.get(position);
            text_view_rate.setText(success_rate);
            text_view_rate.setTextColor(setColor(rates.get(position)));
            String title_nice = courses.get(position).toString();
            int max_length = 25;
            text_view_title_nice.setText(title_nice.substring(0, Math.min(title_nice.length(), max_length)));
            text_view_title_bref.setText(courses.get(position).name());
            String nb_text = "Number of questions : " + nb_quests.get(position);
            text_view_nb.setText(nb_text);

            return convertView;
        }
    }

    public int setColor(int rate) {
        if (rate==0) return Color.parseColor("#6C6F6F");
        else if(rate>70) return Color.parseColor("#63B97F");
        else if(rate<40) return Color.parseColor("#CB0814");
        else return Color.parseColor("#EC804D");
    }

}
