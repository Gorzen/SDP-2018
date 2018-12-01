package ch.epfl.sweng.studyup.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.settings.SettingsActivity;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.NonScrollableListView;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

public class ManageCourseActivity extends NavigationTeacher{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_courses);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupListViews();
        checkSuperUser();
    }

    private void checkSuperUser() {
        if(!Player.get().getSciperNum().equals("262413") && !Player.get().getSciperNum().equals("272432") && !Player.get().getSciperNum().equals("274999") && !Player.get().getSciperNum().equals("300137")) {
            findViewById(R.id.courseRequestsTextView).setVisibility(View.GONE);
            findViewById(R.id.listViewCourseRequests).setVisibility(View.GONE);
        }
    }

    private void setupListViews() {
        List<Course> otherCourses = Arrays.asList(Course.values());

        Firestore.get().syncPlayerData(); // ?
        List<Course> acceptedCourses = Player.get().getCoursesTeached(); // Synchro Firebase?
        /* otherCourses.removeAll(acceptedCourses); // return value
        for(Course c : acceptedCourses) {
            otherCourses.remove(c);
        }*/

        // List<CourseRequest> requests = getRequestsFromFirebase();
        List<Course> requestsList = Arrays.asList(Course.values());
        List<CourseRequest> allRequests = new ArrayList<>();
        for(Course c : requestsList) {
            allRequests.add(new CourseRequest(c, Player.get().getSciperNum(), Player.get().getFirstName(), Player.get().getLastName()));
        }

        List<Course> playerPendingCourses = new ArrayList<>();
        List<Course> otherPendingCourses = new ArrayList<>();
        for(CourseRequest req : allRequests)  {
            if(req.sciper.equals(Player.get().getSciperNum())) {
                playerPendingCourses.add(req.course);
            } else {
                allRequests.remove(req); // Will be the demand of future teacher
            }
        }

        ((NonScrollableListView) findViewById(R.id.listViewOtherCourses)).setAdapter(new ListCourseAdapter(this, otherCourses, R.layout.model_course_send_request));
        ((NonScrollableListView) findViewById(R.id.listViewCourseRequests)).setAdapter(new requestListViewAdapter(this, allRequests));
        ((NonScrollableListView) findViewById(R.id.listViewAcceptedCourses)).setAdapter(new ListCourseAdapter(this, acceptedCourses, R.layout.course_item_model));
        ((NonScrollableListView) findViewById(R.id.listViewPendingCourses)).setAdapter(new ListCourseAdapter(this, otherPendingCourses, R.layout.course_item_model));
    }

    public void sendRequest(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private class requestListViewAdapter extends BaseAdapter {
        private Context cnx;
        private List<CourseRequest> requests;

        protected requestListViewAdapter(Context cnx, List<CourseRequest> requests) {
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
            TextView courseText = convertView.findViewById(R.id.pendingCourseName);
            TextView playerInfos = convertView.findViewById(R.id.sciperAndNamePending);
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
