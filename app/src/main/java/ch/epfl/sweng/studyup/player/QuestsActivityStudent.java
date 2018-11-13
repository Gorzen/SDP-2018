package ch.epfl.sweng.studyup.player;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

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
                onClickQuest(questions);
            }
        });
    }

    protected void onClickQuest(final List<Question> quests) {
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
                startActivity(DisplayQuestionActivity.getIntentForDisplayQuestion(parent.getContext(), quests.get(position)));
            }
        });
    }

    // Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }

    // Allows you to do an action with the toolbar (in a different way than with the navigation bar)
    // Corresponding activities are not created yet
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.top_navigation_settings) {
            Toast.makeText(QuestsActivity.this,
                    "You have clicked on Settings :)",
                    Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.top_navigation_infos) {
            Toast.makeText(QuestsActivity.this,
                    "You have clicked on Infos :)",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
