package ch.epfl.sweng.studyup.teacher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants.Course;
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


    }

    private class ManageCourseListViewAdapter extends BaseAdapter {
        private Context cnx;
        private int idLayout;
        private List<Course> courses;

        protected ManageCourseListViewAdapter(Context cnx, int idLayout, List<Course> courses) {
            this.cnx = cnx;
            this.idLayout = idLayout;
            this.courses = courses;
        }

        @Override
        public int getCount() { courses.size(); }

        @Override
        public Object getItem(int position) { return courses.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(cnx, idLayout, null);
            }
            return convertView;
        }
    }
}
