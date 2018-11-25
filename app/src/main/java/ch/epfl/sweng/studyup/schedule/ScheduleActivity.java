package ch.epfl.sweng.studyup.schedule;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

public class ScheduleActivity extends NavigationStudent {
    private List<WeekViewEvent> weekViewEvents;
    private WeekView weekView;
    private final int firstDay = 19;
    private final int lastDay = firstDay + 4;
    private final int month = 10;
    private final int year = 2018;
    private int id = 0;

    private final MonthLoader.MonthChangeListener monthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            if(newMonth == month + 1 && newYear == year) {
                return weekViewEvents;
            }else{
                return new ArrayList<>();
            }
        }
    };

    //Student
    private final WeekView.EventClickListener eventClickListenerStudent = new WeekView.EventClickListener() {
        @Override
        public void onEventClick(WeekViewEvent event, RectF eventRect) {}};


    private final WeekView.EmptyViewClickListener emptyViewClickListenerStudent = new WeekView.EmptyViewClickListener() {
        @Override
        public void onEmptyViewClicked(Calendar date) {}};

    //Teacher
    private final WeekView.EventClickListener eventClickListenerTeacher = new WeekView.EventClickListener() {
        @Override
        public void onEventClick(WeekViewEvent event, RectF eventRect) {
            Log.d("ScheduleActivityTeacher", "Clicked on event with id " + event.getId());
            weekViewEvents.remove(event);
            weekView.notifyDatasetChanged();
        }
    };


    private final WeekView.EmptyViewClickListener emptyViewClickListenerTeacher = new WeekView.EmptyViewClickListener() {
        @Override
        public void onEmptyViewClicked(Calendar time) {
            Log.d("ScheduleActivityTeacher", "time = " + time.toString());
            Log.d("ScheduleActivityTeacher", "Day of month = " + time.get(Calendar.DAY_OF_MONTH));
            Log.d("ScheduleActivityTeacher", "Hour = " + time.get(Calendar.HOUR_OF_DAY));
            int day = time.get(Calendar.DAY_OF_MONTH);
            int hour = time.get(Calendar.HOUR_OF_DAY);

            Calendar eventStart = Calendar.getInstance();
            eventStart.set(Calendar.YEAR, year);
            eventStart.set(Calendar.MONTH, month);
            eventStart.set(Calendar.DAY_OF_MONTH, day);
            eventStart.set(Calendar.HOUR_OF_DAY, hour);
            eventStart.set(Calendar.MINUTE, 0);

            Calendar eventEnd = Calendar.getInstance();
            eventEnd.set(Calendar.YEAR, year);
            eventEnd.set(Calendar.MONTH, month);
            eventEnd.set(Calendar.DAY_OF_MONTH, day);
            eventEnd.set(Calendar.HOUR_OF_DAY, hour);
            eventEnd.set(Calendar.MINUTE, 59);

            weekViewEvents.add(new WeekViewEvent(id, "Sweng", "CO_0_1", eventStart, eventEnd));
            id += 1;
            weekView.notifyDatasetChanged();
        }
    };


    private final WeekView.EventLongPressListener eventLongPressListener = new WeekView.EventLongPressListener() {
        @Override
        public void onEventLongPress(WeekViewEvent event, RectF eventRect) {}};


    private final WeekViewLoader weekViewLoader = new WeekViewLoader() {
        @Override
        public double toWeekViewPeriodIndex(Calendar instance) {
            return 0;
        }

        @Override
        public List<? extends WeekViewEvent> onLoad(int periodIndex) {
            return new ArrayList<>();
        }};


    private final DateTimeInterpreter dateTimeInterpreter = new DateTimeInterpreter() {
        @Override
        public String interpretDate(Calendar date) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
                return sdf.format(date.getTime()).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
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
                e.printStackTrace();
                return "";
            }
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        weekViewEvents = new ArrayList<>();
        weekView = findViewById(R.id.weekView);

        weekView.setEventLongPressListener(eventLongPressListener);
        weekView.setDateTimeInterpreter(dateTimeInterpreter);
        weekView.setWeekViewLoader(weekViewLoader);
        weekView.setMonthChangeListener(monthChangeListener);

        setStartAndEnd(weekView);

        if(Player.get().getRole().equals(Constants.Role.student)){
            weekView.setOnEventClickListener(eventClickListenerStudent);
            weekView.setEmptyViewClickListener(emptyViewClickListenerStudent);

            FloatingActionButton saveButton = findViewById(R.id.fab);
            saveButton.setVisibility(View.GONE);
        }else if (Player.get().getRole().equals(Constants.Role.teacher)){
            weekView.setOnEventClickListener(eventClickListenerTeacher);
            weekView.setEmptyViewClickListener(emptyViewClickListenerTeacher);
        }
    }

    private void setStartAndEnd(WeekView weekView) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, year);
        startDate.set(Calendar.MONTH, month);
        startDate.set(Calendar.DAY_OF_MONTH, firstDay);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, year);
        endDate.set(Calendar.MONTH, month);
        endDate.set(Calendar.DAY_OF_MONTH, lastDay);
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);

        weekView.goToDate(startDate);

        weekView.setMinDate(startDate);
        weekView.setMaxDate(endDate);

        weekView.setMinTime(8);
        weekView.setMaxTime(20);
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

    public void onSaveButtonClick(View view){
        //Save WeekViewEvents on firebase
    }
}
