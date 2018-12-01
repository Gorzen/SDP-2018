package ch.epfl.sweng.studyup.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.ManageCourseActivity;
import ch.epfl.sweng.studyup.utils.Constants;

import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSE_REQUESTS;

public class ListCourseAdapter extends BaseAdapter {
    private Activity act;
    private int idLayout;
    private List<Constants.Course> courses;

    public ListCourseAdapter(Activity act, List<Constants.Course> courses, int idLayout) {
        this.act=act;
        //sort courses to display
        ArrayList<Constants.Course> sortedCourses = new ArrayList<>(courses);
        Collections.sort(sortedCourses, new Comparator<Constants.Course>() {
            @Override
            public int compare(Constants.Course o1, Constants.Course o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
        this.idLayout = idLayout;
        this.courses=sortedCourses;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) { return courses.get(position).ordinal(); }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(act, idLayout, null);
        }
        TextView text_view_title_nice = convertView.findViewById(R.id.course_title);
        TextView text_view_title_bref = convertView.findViewById(R.id.abbreviation);
        text_view_title_nice.setText(courses.get(position).toString());
        text_view_title_bref.setText(courses.get(position).name());

        if(idLayout == R.layout.model_course_send_request) {
            TextView send = convertView.findViewById(R.id.send_course_request);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firestore.get().getDb().collection(FB_COURSE_REQUESTS).document(Player.get().getSciperNum()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(act instanceof ManageCourseActivity) {
                                        ((ManageCourseActivity) act).addRequest(courses.get(position).name(),
                                                Player.get().getSciperNum(),
                                                Player.get().getFirstName(),
                                                Player.get().getLastName());
                                    }
                                }
                            });
                }
            });
        }

        return convertView;
    }
}
