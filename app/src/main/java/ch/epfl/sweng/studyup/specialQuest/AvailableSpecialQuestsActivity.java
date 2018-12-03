package ch.epfl.sweng.studyup.specialQuest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.adapters.SpecialQuestListViewAdapter;

import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_INDEX_KEY;

public class AvailableSpecialQuestsActivity extends RefreshContext {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_special_quests);

        populateYourSpecialQuestsList();
        populateAvailableSpecialQuestsList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateYourSpecialQuestsList();
        populateAvailableSpecialQuestsList();
    }

    public void populateYourSpecialQuestsList() {

        final List<SpecialQuest> yourSpecialQuestsList = Player.get().getSpecialQuests();

        List<Integer> iconList = new ArrayList<>();
        for (SpecialQuest currSpecialQuest : yourSpecialQuestsList) {
            if (currSpecialQuest.getProgress() == 1) {
                iconList.add(R.drawable.ic_check_green_24dp);
            }
            else {
                iconList.add(R.drawable.ic_todo_grey_24dp);
            }
        }

        ListView specialQuestsListView = findViewById(R.id.your_special_quests_list_view);
        SpecialQuestListViewAdapter listAdapter =
                new SpecialQuestListViewAdapter(this, R.layout.special_quest_model, yourSpecialQuestsList, iconList);
        specialQuestsListView.setAdapter(listAdapter);

        /*
        Set onClick listeners for all special quests that will open SpecialQuestDisplayActivity,
        passing the Special Quest as a serializable object. It will be de-serialized and used in that activity.
         */
        specialQuestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displaySpecialQuestion = new Intent(AvailableSpecialQuestsActivity.this, SpecialQuestDisplayActivity.class);
                displaySpecialQuestion.putExtra(SPECIAL_QUEST_INDEX_KEY, position);

                startActivity(displaySpecialQuestion);
            }});
    }

    public void populateAvailableSpecialQuestsList() {

        List<SpecialQuest> alreadyEnrolledSpecialQuests = Player.get().getSpecialQuests();
        final List<SpecialQuest> availableSpecialQuestList = new ArrayList<>();
        List<Integer> iconList = new ArrayList<>();

        /*
        Create special quests for enrollment only if the current player isn't
        already enrolled in a special quest of a given type.
         */
        for (final SpecialQuestType specialQuestType : SpecialQuestType.values()) {
            SpecialQuest newSpecialQuest = new SpecialQuest(specialQuestType);
            if(!alreadyEnrolledSpecialQuests.contains(newSpecialQuest)) {
                availableSpecialQuestList.add(newSpecialQuest);
                iconList.add(R.drawable.ic_todo_grey_24dp);
            }
        }

        ListView availableQuestsListView = findViewById(R.id.available_special_quests_list_view);
        SpecialQuestListViewAdapter listAdapter =
                new SpecialQuestListViewAdapter(this, R.layout.special_quest_model, availableSpecialQuestList, iconList);
        availableQuestsListView.setAdapter(listAdapter);
    }

    public void onBackButtonAvailableSpecialQuests(View v) {
        finish();
    }
}
