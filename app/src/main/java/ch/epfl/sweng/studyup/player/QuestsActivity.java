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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Navigation;
import ch.epfl.sweng.studyup.utils.Utils;

import ch.epfl.sweng.studyup.utils.ListItemAdapter;
import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;

/**
 * QuestsActivity
 *
 * Quests.
 */
public class QuestsActivity extends Navigation {
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        navigationSwitcher(QuestsActivity.this, QuestsActivity.class, Utils.QUESTS_INDEX);

        LiveData<List<Question>> questions = parseQuestionsLiveData(this.getApplicationContext());
        questions.observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                showQuestions(questions);
            }
        });


    }

    private void showQuestions(final List<Question> questions) {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<Integer> listImageID = new ArrayList<>();

        Map<String, Boolean> answeredQuestion = Player.get().getAnsweredQuestion();
        Set<String> answeredQuestionId = answeredQuestion.keySet();
        Set<String> correctQuestionId = new HashSet<>();
        Set<String> wrongQuestionId = new HashSet<>();

        for(String id : answeredQuestionId) {
            if(answeredQuestion.get(id).equals(Boolean.TRUE)) {
                correctQuestionId.add(id);
            } else wrongQuestionId.add(id);
        }

        for(Question q: questions){
            listTitle.add(q.getTitle());
            if(correctQuestionId.contains(q.getQuestionId()))
                listImageID.add(R.drawable.ic_check_green_24dp);
            else if(wrongQuestionId.contains(q.getQuestionId()))
                listImageID.add(R.drawable.ic_cross_red_24dp);
            else listImageID.add(R.drawable.ic_todo_grey_24dp);
        }

        ListView listView = findViewById(R.id.listViewQuests);
        ListItemAdapter adapter = new ListItemAdapter(this, listTitle, listImageID);
        listView.setAdapter(adapter);//todo

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(DisplayQuestionActivity.getIntentForDisplayQuestion(parent.getContext(), questions.get(position)));
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
