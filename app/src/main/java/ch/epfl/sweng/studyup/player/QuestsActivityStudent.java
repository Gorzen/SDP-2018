package ch.epfl.sweng.studyup.player;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Utils;

public class QuestsActivityStudent extends QuestsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationSwitcher(QuestsActivityStudent.this, QuestsActivityStudent.class, Utils.QUESTS_INDEX_STUDENT);
    }

    @Override
    protected void onClickQuest(final List<Question> questions) {
        int nbrQuestion = questions.size();

        String[] list = new String[nbrQuestion];
        for(int i = 0; i < nbrQuestion; ++i) {
            list[i] = questions.get(i).getTitle();
        }

        ListView listView = findViewById(R.id.listViewQuests);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(DisplayQuestionActivity.getIntentForDisplayQuestion(parent.getContext(), questions.get(position)));
            }
        });
    }
}
