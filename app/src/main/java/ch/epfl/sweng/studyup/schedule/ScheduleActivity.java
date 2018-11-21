package ch.epfl.sweng.studyup.schedule;

import android.graphics.RectF;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import ch.epfl.sweng.studyup.weekview.MonthLoader;
import ch.epfl.sweng.studyup.weekview.WeekView;
import ch.epfl.sweng.studyup.weekview.WeekViewEvent;
import ch.epfl.sweng.studyup.weekview.WeekViewLoader;

public class ScheduleActivity extends NavigationStudent {
    private final MonthLoader.MonthChangeListener monthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            return new ArrayList<>();
        }
    };

    private final WeekView.EventClickListener eventClickListener = new WeekView.EventClickListener() {
        @Override
        public void onEventClick(WeekViewEvent event, RectF eventRect) {

        }
    };

    private final WeekView.EventLongPressListener eventLongPressListener = new WeekView.EventLongPressListener() {
        @Override
        public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        WeekView mWeekView = findViewById(R.id.weekView);
        setStartAndEndDate(mWeekView);

        mWeekView.setMonthChangeListener(monthChangeListener);
        mWeekView.setOnEventClickListener(eventClickListener);
        mWeekView.setEventLongPressListener(eventLongPressListener);
        mWeekView.setWeekViewLoader(new WeekViewLoader() {
            @Override
            public double toWeekViewPeriodIndex(Calendar instance) {
                return 0;
            }

            @Override
            public List<? extends WeekViewEvent> onLoad(int periodIndex) {
                return new ArrayList<WeekViewEvent>(){
                    {
                        add(new WeekViewEvent(1, "Sweng", 2018, 11, 22, 8, 15, 2018, 11, 22, 10, 0));
                    }
                };
            }
        });
    }

    private void setStartAndEndDate(WeekView weekView){
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 2018);
        startDate.set(Calendar.MONTH, 10);
        startDate.set(Calendar.DAY_OF_MONTH, 19);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, 2018);
        endDate.set(Calendar.MONTH, 10);
        endDate.set(Calendar.DAY_OF_MONTH, 23);
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);

        weekView.setMinDate(startDate);
        weekView.setMaxDate(endDate);
    }

}
