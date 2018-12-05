package ch.epfl.sweng.studyup.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.ScheduleActivityStudent;
import ch.epfl.sweng.studyup.teacher.ScheduleActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;

import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS_END;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS_ID;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS_LOCATION;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS_NAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS_START;

public abstract class FirestoreSchedule {
    private static final String TAG = FirestoreSchedule.class.getSimpleName();
    /**
     * Method that get the schedule of the current player, that he/she be teacher or student, and
     * will update the layout accordingly using the updateSchedule method in the activity given as
     * parameter and/or update the schedule of the player
     *
     * @param act  The activity displaying the layout (if it is a schedule activity, ignored otherwise)
     * @param role The role, which the caller can choose
     * @throws NullPointerException     If the format is incorrect on the database
     */
    public static void getCoursesSchedule(final FirebaseFirestore db, final Activity act, final Constants.Role role) throws NullPointerException {
        final Player p = Player.get();
        final CollectionReference coursesRef = db.collection(FB_COURSES);
        final List<WeekViewEvent> schedule = new ArrayList<>();

        final List<Constants.Course> courses = p.isTeacher() ? Player.get().getCoursesTeached() : Player.get().getCoursesEnrolled();
        // Iteration over all events of all needed courses
        for(final Constants.Course c : courses) {
            coursesRef.document(c.name()).collection(FB_EVENTS).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                if(!task.getResult().isEmpty()) {
                                    // Adding periods to the course
                                    for (QueryDocumentSnapshot q : task.getResult()) {
                                        schedule.add(queryDocumentSnapshotToWeekView(q));
                                    }

                                    onScheduleCompleted(act, role, schedule);
                                }
                            }
                        }
                    });
        }
    }

    private static WeekViewEvent queryDocumentSnapshotToWeekView(QueryDocumentSnapshot q) {
        long id = Long.parseLong(q.get(FB_EVENTS_ID).toString());
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(Long.parseLong(q.get(FB_EVENTS_START).toString()));
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(Long.parseLong(q.get(FB_EVENTS_END).toString()));
        String name = q.get(FB_EVENTS_NAME).toString();
        String location = q.get(FB_EVENTS_LOCATION).toString();


        return new WeekViewEvent(id, name, location, start, end);
    }

    private static void onScheduleCompleted(final Activity act, final Constants.Role role, List<WeekViewEvent> schedule) {

        if(act instanceof ScheduleActivityStudent) {
            ScheduleActivityStudent actCasted = (ScheduleActivityStudent) act;
            schedule.addAll(actCasted.getWeekViewEvents());
            actCasted.updateSchedule(schedule);
        } else if(act instanceof ScheduleActivityTeacher) {
            ScheduleActivityTeacher actCasted = (ScheduleActivityTeacher) act;
            schedule.addAll(actCasted.getWeekViewEvents());
            actCasted.updateSchedule(schedule);
        }


        if(role == Constants.Role.student) {
            Player.get().setScheduleStudent(schedule);
        }
    }

    /**
     * Add periods to the course on the server by removing the old ones with corresponding start
     * time and adding the new ones.
     *
     * @param c The course
     * @param periodsToAdd The times of the periods for the course, containing all needed data
     *                     (Room, startTime, endTime, etc...)
     */
    public static void setCourseEvents(final FirebaseFirestore db, final Constants.Course c, final List<WeekViewEvent> periodsToAdd) {
        final CollectionReference eventsOfCourse = db.collection(FB_COURSES).document(c.name()).collection(FB_EVENTS);



        eventsOfCourse.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.i(TAG, "Got the course's events to add new ones to server.");

                            if(!task.getResult().isEmpty()) {
                                // Deleting periods that are replaced
                                for(QueryDocumentSnapshot q : task.getResult()) {
                                    q.getReference().delete();
                                }
                            }

                            // Adding new periods
                            for(WeekViewEvent p : periodsToAdd) {
                                eventsOfCourse.add(WeekViewEventToMap(p));
                            }
                        }
                    }
                });
    }

    private static Map<String, Object> WeekViewEventToMap(WeekViewEvent e) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(FB_EVENTS_ID, e.getId());
        eventMap.put(FB_EVENTS_NAME, e.getName());
        eventMap.put(FB_EVENTS_LOCATION, e.getLocation());
        eventMap.put(FB_EVENTS_START, e.getStartTime().getTimeInMillis());
        eventMap.put(FB_EVENTS_END, e.getEndTime().getTimeInMillis());

        return eventMap;
    }
}
