package ch.epfl.sweng.studyup.player;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.utils.Utils;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.Constants.MONTH_OF_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.SCHEDULE_INDEX;
import static ch.epfl.sweng.studyup.utils.Constants.YEAR_OF_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

public class ScheduleActivityStudent extends NavigationStudent {
    private List<WeekViewEvent> weekViewEvents;
    private WeekView weekView;
    private int id = 0;

    // Listeners
    private final MonthLoader.MonthChangeListener monthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            if(newMonth == MONTH_OF_SCHEDULE + 1 && newYear == YEAR_OF_SCHEDULE) {
                return weekViewEvents;
            }else{
                return new ArrayList<>();
            }
        }
    };
    private final WeekView.EventClickListener eventClickListener = new WeekView.EventClickListener() {
        @Override
        public void onEventClick(WeekViewEvent event, RectF eventRect) {}};


    private final WeekView.EmptyViewClickListener emptyViewClickListener = new WeekView.EmptyViewClickListener() {
        @Override
        public void onEmptyViewClicked(Calendar date) {}};

    private final WeekView.EventLongPressListener eventLongPressListener = new WeekView.EventLongPressListener() {
        @Override
        public void onEventLongPress(WeekViewEvent event, RectF eventRect) {}};

    // Date format
    private final DateTimeInterpreter dateTimeInterpreter = new DateTimeInterpreter() {
        @Override
        public String interpretDate(Calendar date) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
                return sdf.format(date.getTime()).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace(); return "";
            }
        }

        @Override
        public String interpretTime(int hour, int minutes) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, 0);

            try {
                SimpleDateFormat sdf = DateFormat.is24HourFormat(getApplicationContext()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
                return sdf.format(calendar.getTime());
            } catch (Exception e) {
                e.printStackTrace(); return "";
            }
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_student);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        weekViewEvents = new ArrayList<>();
        weekView = findViewById(R.id.weekView);

        if(MOCK_ENABLED){
            weekView.setNumberOfVisibleDays(1);
        }

        Utils.setupWeekView(weekView, eventLongPressListener, dateTimeInterpreter, monthChangeListener, eventClickListener, emptyViewClickListener);

        navigationSwitcher(ScheduleActivityStudent.this, ScheduleActivityStudent.class, SCHEDULE_INDEX);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firestore.get().getCoursesSchedule(this, Player.get().getRole());
    }

    public void updateSchedule(List<WeekViewEvent> events){
        weekViewEvents.clear();
        for(WeekViewEvent event : events){
            weekViewEvents.add(event);
        }
        id += events.size();
        weekView.notifyDatasetChanged();
    }

    public List<WeekViewEvent> getWeekViewEvents(){
        return new ArrayList<>(weekViewEvents);
    }
}
