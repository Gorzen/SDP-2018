package ch.epfl.sweng.studyup.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;

import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.NonScrollableListView;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

public class ManageCourseActivity extends NavigationTeacher{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_courses);

        setupListViews();
    }

    private void setupListViews() {

        ListCourseAdapter a1 = new ListCourseAdapter(this, Arrays.asList(Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG));
        ListCourseAdapter a2 = new ListCourseAdapter(this, Arrays.asList(Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG));
        ListCourseAdapter a3 = new ListCourseAdapter(this, Arrays.asList(Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG, Constants.Course.SWENG, Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG,Constants.Course.SWENG));


        ((NonScrollableListView) findViewById(R.id.listViewOtherCourses)).setAdapter(a1);
        ((NonScrollableListView) findViewById(R.id.listViewPendingCourses)).setAdapter(a2);
        ((NonScrollableListView) findViewById(R.id.listViewAcceptedCourses)).setAdapter(a3);

    }
}
