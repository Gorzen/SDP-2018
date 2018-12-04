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
import ch.epfl.sweng.studyup.player.UserData;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants.Course;

import static ch.epfl.sweng.studyup.utils.Utils.setupToolbar;


public class DisplayCourseStatsActivity extends CourseStatsActivity {

    private Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_course_stats);
        setupToolbar(this);

        Intent intent = getIntent();
        course = Course.valueOf(intent.getStringExtra(DisplayQuestionActivity.class.getName()));

        TextView name_course = findViewById(R.id.course_name);
        name_course.setText(course.toString());

        TextView nb_students = findViewById(R.id.nb_students);
        List<UserData> userList = getStudentsFromCourse(course);
        nb_students.setText("Number of enrolled students: " + String.valueOf(userList.size()));
        setupListViewP(userList);


        TextView nb_quests = findViewById(R.id.nb_questions);
        List<String> qList = getQuestsStringFromCourse(course);
        nb_quests.setText("Number of questions for this course: " + String.valueOf(qList.size()));
        setupListViewQ(qList);

    }

    public void onBackButton(View view) {
        finish();
    }



    protected void setupListViewP(final List<UserData> userList) {
        ListView listView = findViewById(R.id.listViewPlayer);

        ArrayList<Integer> rates = new ArrayList<>();
        ArrayList<Integer> nb_answer = new ArrayList<>();

        List<String> quests_course = getQuestsStringFromCourse(course);
        Set<String> s1 = new HashSet<>(quests_course);

        for (UserData user : userList) {
            HashMap<String, List<String>> answered_total = new HashMap<>(user.getAnsweredQuestions());

            double nb_good_answer_course = 0;
            int nb_answer_course;
            Set<String> s2 = answered_total.keySet();
            s2.retainAll(s1); //s2 = only String Question Id (from course) which user answered to
            nb_answer_course = s2.size();
            for (String s : s2) {
                if(answered_total.get(s) != null) {
                    boolean isAnswerTrue = Boolean.parseBoolean(answered_total.get(s).get(0));
                    if(isAnswerTrue) nb_good_answer_course++;
                }
            }
            int rate_user_in_a_course = nb_answer_course == 0 ? 0 : (int)(100*nb_good_answer_course/nb_answer_course);
            rates.add(rate_user_in_a_course);
            nb_answer.add(nb_answer_course);
        }


        ListUserAdapter listUserAdapter = new ListUserAdapter(this,  userList, rates, nb_answer, quests_course.size());
        listView.setAdapter(listUserAdapter);
    }

    protected void setupListViewQ(final List<String> questions_course) {
        ListView listView = findViewById(R.id.listViewQuestionForStats);

        ArrayList<Integer> rates = new ArrayList<>();
        ArrayList<Integer> nb_answer = new ArrayList<>();

        List<UserData> students_in_course = getStudentsFromCourse(course);

        for (String question_string : questions_course) {
            int a_user_ans = 0;
            int a_user_good_ans = 0;
            for(UserData user : students_in_course) {
                HashMap<String, List<String>> user_ans_q = new HashMap<>(user.getAnsweredQuestions());
                if (user_ans_q.containsKey(question_string)) {
                    a_user_ans++;
                    boolean isAnswerTrue = Boolean.parseBoolean(user_ans_q.get(question_string).get(0));
                    if (isAnswerTrue) {
                        a_user_good_ans++;
                    }
                }
            }
            int rate = a_user_ans == 0 ? 0 : 100*a_user_good_ans/a_user_ans;
            rates.add(rate);
            nb_answer.add(a_user_ans);
        }
        ListQuestionStatAdapter listQAdapter = new ListQuestionStatAdapter(this, questions_course, rates, nb_answer, students_in_course.size());
        listView.setAdapter(listQAdapter);
    }





    //get all students for one course
    public List<UserData> getStudentsFromCourse(Course course){
        List<UserData> usersEnrolledInCourse = new ArrayList<>();
        for (UserData user: getAllUsers()) {
            if(user.getCourses().contains(course)){
                usersEnrolledInCourse.add(user);
            }
        }
        return usersEnrolledInCourse;
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

    private class ListUserAdapter extends BaseAdapter {

        private Context cnx;
        private List<UserData> user;
        private ArrayList<Integer> rates;
        private ArrayList<Integer> nb_answer;
        private int total_quests_for_course;

        ListUserAdapter(Context cnx,
                        List<UserData> user,
                        ArrayList<Integer> rates,
                        ArrayList<Integer> nb_answer,
                        int total_quests_for_course) {
            this.cnx=cnx;
            this.user =user;
            this.nb_answer=nb_answer;
            this.rates=rates;
            this.total_quests_for_course=total_quests_for_course;
        }

        @Override
        public int getCount() {
            return user.size();
        }

        @Override
        public Object getItem(int position) {
            return user.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(user.get(position).getSciperNum());

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(cnx, R.layout.player_stat_item_model, null);
            }
            TextView text_view_last = convertView.findViewById(R.id.last_name);
            TextView text_view_success = convertView.findViewById(R.id.success_rate);
            TextView text_view_sciper = convertView.findViewById(R.id.sciper);
            TextView text_view_nb_answer = convertView.findViewById(R.id.quests_answered);

            String name = user.get(position).getLastName();
            String first_firstname = user.get(position).getFirstName();
            if(first_firstname.contains(" ")) {
                first_firstname = first_firstname.substring(0, first_firstname.indexOf(' '));
            }
            name = name + ' ' + first_firstname;
            text_view_last.setText(name);
            String successString = "Success on answered : "+rates.get(position)+"%";
            text_view_success.setTextColor(setColor(rates.get(position), nb_answer.get(position)));
            text_view_success.setText(successString);
            text_view_sciper.setText(user.get(position).getSciperNum());
            String qAnsString = "Quests answered : "+nb_answer.get(position)+"/"+total_quests_for_course;
            text_view_nb_answer.setText(qAnsString);

            return convertView;
        }
    }

    private class ListQuestionStatAdapter extends BaseAdapter {

        private Context cnx;
        private List<String> questions;
        private ArrayList<Integer> rates;
        private ArrayList<Integer> nb_answer;
        private int total_questions_for_course;

        ListQuestionStatAdapter(Context cnx,
                                List<String> questions,
                                ArrayList<Integer> rates,
                                ArrayList<Integer> nb_answer,
                                int total_quests_for_course) {
            this.cnx=cnx;
            this.questions =questions;
            this.nb_answer=nb_answer;
            this.rates=rates;
            this.total_questions_for_course =total_quests_for_course;
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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(cnx, R.layout.player_stat_item_model, null);
            }
            TextView text_view_last = convertView.findViewById(R.id.last_name);
            TextView text_view_success = convertView.findViewById(R.id.success_rate);
            TextView text_view_sciper = convertView.findViewById(R.id.sciper);
            TextView text_view_nb_answer = convertView.findViewById(R.id.quests_answered);

            String Title = "";
            boolean isTF = false;
            List<Question> allQ = getAllQuestions();
            for (Question q: allQ) {
                if(q.getQuestionId().equals(questions.get(position))) {
                    Title = q.getTitle();
                    isTF = q.isTrueFalse();
                }
            }
            text_view_last.setText(Title.substring(0, Math.min(Title.length(), 25)));
            if(isTF) text_view_sciper.setText("T/F");
            else text_view_sciper.setText("MCQ");
            String successString = "Success answers : "+rates.get(position)+"%";
            text_view_success.setTextColor(setColor(rates.get(position), nb_answer.get(position)));
            text_view_success.setText(successString);
            String qAnsString = "Student answers : "+nb_answer.get(position)+"/"+ total_questions_for_course;
            text_view_nb_answer.setText(qAnsString);

            return convertView;
        }
    }

}
