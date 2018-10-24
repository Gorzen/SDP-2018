package ch.epfl.sweng.studyup.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Navigation;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.questions.QuestionParser.parseQuestions;

/**
 * QuestsActivity
 *
 * Quests.
 */
public class QuestsActivity extends Navigation {
    List<Question> questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        navigationSwitcher(QuestsActivity.this, QuestsActivity.class, Utils.QUESTS_INDEX);

        final List<Question> questions = parseQuestions(this.getApplicationContext());
        int nbrQuestion = questions.size();
        String[] list = new String[nbrQuestion];
        for(int i = 0; i < nbrQuestion; ++i) {
            if(questions.get(i).isTrueFalse()) {
                list[i] = "Question "+i+": true or false";
            } else {
                list[i] = "Question "+i+": multiple choice";
            }

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
