package ch.epfl.sweng.studyup.player;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Utils;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import ch.epfl.sweng.studyup.utils.ListItemAdapter;
import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestionsLiveData;

public class QuestsActivityStudent extends NavigationStudent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests_student);
        navigationSwitcher(QuestsActivityStudent.this, QuestsActivityStudent.class, Utils.QUESTS_INDEX_STUDENT);

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

    private void onClickQuest(final List<Question> quests) {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<Integer> listImageID = new ArrayList<>();

        Map<String, Boolean> answeredQuestion = Player.get().getAnsweredQuestion();
        Set<String> answeredQuestionId = answeredQuestion.keySet();

        for(Question q: quests) {
            listTitle.add(q.getTitle());
            if(answeredQuestionId.contains(q.getQuestionId())){
                if(answeredQuestion.get(q.getQuestionId())){
                    listImageID.add(R.drawable.ic_check_green_24dp);
                }
                else listImageID.add(R.drawable.ic_cross_red_24dp);
            }
            else listImageID.add(R.drawable.ic_todo_grey_24dp);
        }

        onClickListView(quests, listTitle, listImageID);
    }

    private void onClickListView(final List<Question> quests, ArrayList<String> listTitle, ArrayList<Integer> listImageID) {
        ListView listView = findViewById(R.id.listViewQuests);
        ListItemAdapter adapter = new ListItemAdapter(this, listTitle, listImageID);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(DisplayQuestionActivity.getIntentForDisplayQuestion(parent.getContext(), quests.get(position)));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        navigationTopToolbar(item);
        return super.onOptionsItemSelected(item);
    }

}
