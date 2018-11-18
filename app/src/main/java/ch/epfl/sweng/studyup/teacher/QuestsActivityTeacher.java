package ch.epfl.sweng.studyup.teacher;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;
import static ch.epfl.sweng.studyup.utils.Constants.QUESTS_INDEX_TEACHER;

public class QuestsActivityTeacher extends NavigationTeacher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests_teacher);
        navigationSwitcher(QuestsActivityTeacher.this, QuestsActivityTeacher.class, QUESTS_INDEX_TEACHER);

        LiveData<List<Question>> questions = parseQuestionsLiveData(this.getApplicationContext());
        questions.observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                setupListView(questions);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Firestore.get().loadQuestions(this);
    }

    //TODO For now the same as for the student! To be changed
    protected void setupListView(final List<Question> quests) {
        int nbrQuestion = quests.size();

        String[] list = new String[nbrQuestion];
        for(int i = 0; i < nbrQuestion; ++i) {
            list[i] = quests.get(i).getTitle();
        }

        ListView listView = findViewById(R.id.listViewQuests);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(parent.getContext(), AddQuestionActivity.class).putExtra(AddQuestionActivity.class.getSimpleName(), quests.get(position)));
            }
        });
    }
}
