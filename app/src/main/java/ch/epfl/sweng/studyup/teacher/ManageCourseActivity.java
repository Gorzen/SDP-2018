package ch.epfl.sweng.studyup.teacher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants.Course;
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

    private class ManageRequestListViewAdapter extends BaseAdapter {
        private Context cnx;
        private List<CourseRequest> requests;

        protected ManageRequestListViewAdapter(Context cnx, List<CourseRequest> requests) {
            this.cnx = cnx;
            this.requests = requests;
        }

        @Override
        public int getCount() { return requests.size(); }

        @Override
        public Object getItem(int position) { return requests.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = View.inflate(cnx, R.layout.model_course_request_super, null);
            }
            CourseRequest req = requests.get(position);
            TextView courseText = findViewById(R.id.pendingCourseName);
            TextView playerInfos = findViewById(R.id.sciperAndNamePending);
            courseText.setText(req.getCourse().name());
            String infoFormat = req.getSciper()+", "+req.getLastname().toUpperCase()+" "+req.getFirstname();
            playerInfos.setText(infoFormat);

            return convertView;
        }
    }

    private class CourseRequest {
        private final Course course;
        private final String sciper;
        private final String firstname;
        private final String lastname;

        protected CourseRequest(Course course, String sciper, String firstname, String lastname) {
            this.course = course;
            this.sciper = sciper;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        protected Course getCourse() { return course; }
        protected String getSciper() { return sciper; }
        protected String getFirstname() { return firstname; }
        protected String getLastname() { return lastname; }
    }
}
