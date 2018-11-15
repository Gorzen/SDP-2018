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
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import static ch.epfl.sweng.studyup.utils.Constants.*;

import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;

public class QuestsActivityStudent extends NavigationStudent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests_student);
        navigationSwitcher(QuestsActivityStudent.this, QuestsActivityStudent.class, QUESTS_INDEX_STUDENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        LiveData<List<Question>> questions = parseQuestionsLiveData(this.getApplicationContext());
        questions.observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                setupListView(questions);
            }
        });
    }

    private void setupListView(final List<Question> quests) {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<Integer> listImageID = new ArrayList<>();

        Map<String, Boolean> answeredQuestion = Player.get().getAnsweredQuestion();
        Set<String> answeredQuestionId = answeredQuestion == null ? null : answeredQuestion.keySet();

        for(Question q: quests) {
            listTitle.add(q.getTitle());


            if(answeredQuestion == null || !answeredQuestionId.contains(q.getQuestionId())) {
                listImageID.add(R.drawable.ic_todo_grey_24dp);
            } else if(answeredQuestion.get(q.getQuestionId())) {
                listImageID.add(R.drawable.ic_check_green_24dp);
            } else {
                listImageID.add(R.drawable.ic_cross_red_24dp);
            }
        }

        setupOnClickListenerListView(quests, listTitle, listImageID);
    }

    private void setupOnClickListenerListView(final List<Question> quests, ArrayList<String> listTitle, ArrayList<Integer> listImageID) {
        ListView listView = findViewById(R.id.listViewQuests);
        QuestListViewAdapterStudent adapter = new QuestListViewAdapterStudent(this, R.layout.quest_list_view_student_model, listTitle, listImageID);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(DisplayQuestionActivity.getIntentForDisplayQuestion(parent.getContext(), quests.get(position)));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }

    public class QuestListViewAdapterStudent extends BaseAdapter {
        private Context cnx;
        private List<String> titles;
        private int idLayout;
        List<Integer> ids;

        public QuestListViewAdapterStudent(Context cnx, int idLayout, List<String> titles, List<Integer> ids) {
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
            ImageView image_view = convertView.findViewById(R.id.is_quest_done);
            image_view.setImageResource(ids.get(position));
            return convertView;
        }
    }
}
