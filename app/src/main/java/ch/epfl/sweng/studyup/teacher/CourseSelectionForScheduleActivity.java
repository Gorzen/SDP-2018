package ch.epfl.sweng.studyup.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.teacher.ScheduleActivityTeacher.COURSE_NAME_INTENT_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.COURSE_SELECTION_FOR_SCHEDULE_INDEX;

public class CourseSelectionForScheduleActivity extends NavigationTeacher {
    private ListCourseAdapter listCourseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection_for_schedule);
        navigationSwitcher(CourseSelectionForScheduleActivity.this, CourseSelectionForScheduleActivity.class, COURSE_SELECTION_FOR_SCHEDULE_INDEX);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListView();
    }

    private void setupListView() {
        ListView listView = findViewById(R.id.listViewCourses);

        listCourseAdapter = new ListCourseAdapter(this, Player.get().getCoursesTeached(), R.layout.course_item_model, true);
        listView.setAdapter(listCourseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constants.Course c = (Constants.Course) listCourseAdapter.getItem(position);
                startActivity(new Intent(parent.getContext(), ScheduleActivityTeacher.class).putExtra(COURSE_NAME_INTENT_SCHEDULE, c.name()));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
