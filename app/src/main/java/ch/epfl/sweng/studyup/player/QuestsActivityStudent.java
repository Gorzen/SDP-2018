package ch.epfl.sweng.studyup.player;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;
import static ch.epfl.sweng.studyup.utils.Constants.QUESTS_INDEX_STUDENT;

public class QuestsActivityStudent extends NavigationStudent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests_student);
        navigationSwitcher(QuestsActivityStudent.this, QuestsActivityStudent.class, QUESTS_INDEX_STUDENT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firestore.get().loadQuestions(this);
        LiveData<List<Question>> questions = parseQuestionsLiveData(this.getApplicationContext());
        questions.observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                setupListView(questions);
            }
        });
    }

    public void setupListView(final List<Question> quests) {
        List<Integer> listImageID = new ArrayList<>();
        List<Integer> listLang = new ArrayList<>();

        Map<String, List<String>> answeredQuestion = Player.get().getAnsweredQuestion();
        Set<String> answeredQuestionId = answeredQuestion == null ? null : answeredQuestion.keySet();

        for (Question q : quests) {
            setCorrectImageAnswer(listImageID, answeredQuestion, answeredQuestionId, q);
            setCorrectFlagLang(listLang, q);
        }

        setupOnClickListenerListView(quests, quests, listImageID, listLang);
    }

    private void setCorrectFlagLang(List<Integer> listLang, Question q) {
        switch (q.getLang()) {
            case "fr":
                listLang.add(R.drawable.ic_fr_flag);
                break;
            case "en":
                listLang.add(R.drawable.ic_en_flag);
                break;
            default: // Error
                listLang.add(R.drawable.ic_cross_red_24dp);
                break;
        }
    }

    private void setCorrectImageAnswer(List<Integer> listImageID, Map<String, List<String>> answeredQuestion, Set<String> answeredQuestionId, Question q) {
        if (answeredQuestion == null || !answeredQuestionId.contains(q.getQuestionId())) {
            listImageID.add(R.drawable.ic_todo_grey_24dp);
        } else {
            List<String> pair = answeredQuestion.get(q.getQuestionId());
            boolean isAnswerTrue = Boolean.parseBoolean(pair.get(0));

            if (isAnswerTrue) {
                listImageID.add(R.drawable.ic_check_green_24dp);
            }
            else listImageID.add(R.drawable.ic_cross_red_24dp);
        }
    }

    private void setupOnClickListenerListView(final List<Question> quests, List<Question> questions, List<Integer> listImageID, List<Integer> listLang) {
        ListView listView = findViewById(R.id.listViewQuests);
        QuestListViewAdapterStudent adapter = new QuestListViewAdapterStudent(this, R.layout.quest_student_model, questions, listImageID, listLang);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(DisplayQuestionActivity.getIntentForDisplayQuestion(parent.getContext(), quests.get(position)));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }

    private class QuestListViewAdapterStudent extends BaseAdapter {
        private Context cnx;
        private List<Question> questions;
        private int idLayout;
        List<Integer> ids;
        List<Integer> lang;

        public QuestListViewAdapterStudent(Context cnx, int idLayout, List<Question> questions, List<Integer> ids, List<Integer> lang) {
            this.cnx = cnx;
            this.questions = questions;
            this.idLayout = idLayout;
            this.ids = ids;
            this.lang = lang;
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
            ImageView lang_view = convertView.findViewById(R.id.lang_img);
            lang_view.setImageResource(lang.get(position));
            TextView course = convertView.findViewById(R.id.course_quest);
            course.setText(questions.get(position).getCourseName());
            return convertView;
        }
    }
}