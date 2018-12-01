package ch.epfl.sweng.studyup.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.settings.SettingsActivity;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.NonScrollableListView;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSE_REQUESTS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_REQUESTED_COURSES;

public class ManageCourseActivity extends NavigationTeacher{
    private List<CourseRequest> requests;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_courses);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupListViews(){
        List<Course> otherCourses = Arrays.asList(Course.values());
        List<Course> pendingCourses = Player.get().getCoursesPending();
        List<Course> teachingCourses = Player.get().getCoursesTeached();

        otherCourses.removeAll(pendingCourses);
        otherCourses.removeAll(teachingCourses);

        //Other courses
        ((NonScrollableListView) findViewById(R.id.listViewOtherCourses)).setAdapter(new ListCourseAdapter(this, otherCourses, R.layout.model_course_send_request));

        //Pending courses
        if(Player.get().isSuperUser()){
            ((NonScrollableListView) findViewById(R.id.listViewPendingCourses)).setAdapter(new requestListViewAdapter(this, requests));
        }else{
            ((NonScrollableListView) findViewById(R.id.listViewPendingCourses)).setAdapter(new ListCourseAdapter(this, pendingCourses, R.layout.course_item_model));
        }

        //Accepted courses
        ((NonScrollableListView) findViewById(R.id.listViewAcceptedCourses)).setAdapter(new ListCourseAdapter(this, teachingCourses, R.layout.course_item_model));
    }

    private void getAllRequests() {
        requests = new ArrayList<>();

        Firestore.get().getDb().collection(FB_COURSE_REQUESTS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for(QueryDocumentSnapshot q : task.getResult()) {
                                    String sciper = q.getId();
                                    String firstname = q.get(FB_FIRSTNAME).toString();
                                    String lastname = q.get(FB_LASTNAME).toString();
                                    List<String> courses;
                                    try {
                                        courses = Arrays.asList((String[]) q.get(FB_REQUESTED_COURSES));
                                    } catch (ClassCastException e) { Toast.makeText(ManageCourseActivity.this, "Wrong format of courses requested.", Toast.LENGTH_SHORT).show(); return; }

                                    for(String c : courses) {
                                        requests.add(new CourseRequest(Course.valueOf(c), sciper, firstname, lastname));
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void addRequest(String course, String sciper, String firstname, String lastname) {
        final CourseRequest req = new CourseRequest(Course.valueOf(course), sciper, firstname, lastname);
        final DocumentReference playerRequestsRef = Firestore.get().getDb().collection(FB_COURSE_REQUESTS).document(req.getSciper());
        playerRequestsRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> userData = documentSnapshot.getData();
                        List<String> userRequestedCourses = Arrays.asList((String[]) userData.get(FB_REQUESTED_COURSES));
                        userRequestedCourses.add(req.course.name());
                        userData.put(FB_REQUESTED_COURSES, userRequestedCourses);
                        playerRequestsRef.set(userData);
                    }
                });
    }

    private class requestListViewAdapter extends BaseAdapter {
        private Context cnx;
        private List<CourseRequest> requests;

        requestListViewAdapter(Context cnx, List<CourseRequest> requests) {
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

        CourseRequest(Course course, String sciper, String firstname, String lastname) {
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
