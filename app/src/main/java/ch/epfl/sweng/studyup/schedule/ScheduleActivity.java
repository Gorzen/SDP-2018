package ch.epfl.sweng.studyup.schedule;

import android.graphics.RectF;
import android.media.MediaDrm;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.Constants.INVENTORY_INDEX;

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

}
