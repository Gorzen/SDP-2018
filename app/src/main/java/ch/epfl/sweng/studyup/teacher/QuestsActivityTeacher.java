package ch.epfl.sweng.studyup.teacher;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Utils;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;
import static ch.epfl.sweng.studyup.utils.Constants.QUESTS_INDEX_TEACHER;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

public class QuestsActivityTeacher extends NavigationTeacher {
    private static final String TAG = QuestsActivityTeacher.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests_teacher);
        navigationSwitcher(QuestsActivityTeacher.this, QuestsActivityTeacher.class, QUESTS_INDEX_TEACHER);
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

    //TODO For now the same as for the student! To be changed
    protected void setupListView(final List<Question> quests) {
        List<String> titles = new ArrayList<>();
        List<String> UUIDs = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        for(Question q : quests) {
            titles.add(q.getTitle());
            UUIDs.add(q.getQuestionId());
            ids.add(0); //Basic id, that is not used in this adapter
        }

        ListView listView = findViewById(R.id.listViewQuests);
        QuestListViewAdapterTeacher adapter = new QuestListViewAdapterTeacher(this, R.layout.quest_list_view_teacher_model, titles, ids, UUIDs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(DisplayQuestionActivity.getIntentForDisplayQuestion(parent.getContext(), quests.get(position)));
            }
        });
    }

    private class QuestListViewAdapterTeacher extends BaseAdapter {
        private Context cnx;
        private int idLayout;
        private List<String> titles;
        private List<String> UUIDs;
        private List<Integer> ids;

        public QuestListViewAdapterTeacher(Context cnx, int idLayout, List<String> titles, List<Integer> ids, List<String> UUIDs) {
            this.cnx=cnx;
            this.titles = titles;
            this.idLayout = idLayout;
            this.UUIDs = UUIDs;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(cnx, idLayout, null);
            }
            TextView text_view = convertView.findViewById(R.id.quest_title);
            text_view.setText(titles.get(position));
            ImageView delete = convertView.findViewById(R.id.delete_question);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(cnx)
                            .setTitle(R.string.ask_confirmation_deletion_quest)
                            .setNegativeButton(R.string.no_upper, null)
                            .setPositiveButton(R.string.yes_upper, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Firestore.get().deleteQuestion(UUIDs.get(position));
                                    if(!MOCK_ENABLED) { Utils.waitAndTag(500, TAG); onResume(); }
                                }
                            });
                    alertDialogDelete.show();
                }
            });

            return convertView;
        }
    }
}
