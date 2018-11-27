package ch.epfl.sweng.studyup.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sweng.studyup.R;

public class ListCourseAdapter extends BaseAdapter {
    private Context cnx;
    private List<Constants.Course> courses;

    public ListCourseAdapter(Context cnx, List<Constants.Course> courses) {
        this.cnx=cnx;
        //sort courses to display
        ArrayList<Constants.Course> sortedCourses = new ArrayList<>(courses);
        Collections.sort(sortedCourses, new Comparator<Constants.Course>() {
            @Override
            public int compare(Constants.Course o1, Constants.Course o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(cnx, R.layout.course_item_model, null);
        }
        TextView text_view_title_nice = convertView.findViewById(R.id.course_title);
        TextView text_view_title_bref = convertView.findViewById(R.id.abbreviation);
        text_view_title_nice.setText(courses.get(position).toString());
        text_view_title_bref.setText(courses.get(position).name());

        return convertView;
    }
}
