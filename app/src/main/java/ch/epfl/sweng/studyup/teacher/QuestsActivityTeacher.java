package ch.epfl.sweng.studyup.teacher;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

public class QuestsActivityTeacher extends RefreshContext {
    private static final String TAG = QuestsActivityTeacher.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests_teacher);
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
        List<Integer> ids = new ArrayList<>();
        List<Integer> listLang = new ArrayList<>();

        for (Question q : quests) {
            ids.add(0);
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

        ListView listView = findViewById(R.id.listViewQuests);
        QuestListViewAdapterTeacher adapter = new QuestListViewAdapterTeacher(this, R.layout.quest_teacher_model, quests, ids, listLang);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(parent.getContext(), AddQuestionActivity.class).putExtra(AddQuestionActivity.class.getSimpleName(), quests.get(position)));
            }
        });
    }

    public void addNewQuestion(View view) {
        startActivity(new Intent(this.getApplicationContext(), AddQuestionActivity.class));
    }

    public void onLogOutButtonAddQuestion(View view) {
        MainActivity.clearCacheToLogOut(QuestsActivityTeacher.this);
        Intent intent = new Intent(QuestsActivityTeacher.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
    }

    private class QuestListViewAdapterTeacher extends BaseAdapter {
        private Context cnx;
        private int idLayout;
        private List<Question> questions;
        private List<Integer> ids;
        private List<Integer> lang;

        public QuestListViewAdapterTeacher(Context cnx, int idLayout, List<Question> questions, List<Integer> ids, List<Integer> lang) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(cnx, idLayout, null);
            }
            TextView text_view = convertView.findViewById(R.id.quest_title);
            text_view.setText(questions.get(position).getTitle());
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
                                    Firestore.get().deleteQuestion(questions.get(position).getQuestionId());
                                    if (!MOCK_ENABLED) {
                                        Utils.waitAndTag(500, TAG);
                                        onResume();
                                    }
                                }
                            });
                    alertDialogDelete.show();
                }
            });

            ImageView lang_view = convertView.findViewById(R.id.lang_img);
            lang_view.setImageResource(lang.get(position));

            return convertView;
        }
    }
}
