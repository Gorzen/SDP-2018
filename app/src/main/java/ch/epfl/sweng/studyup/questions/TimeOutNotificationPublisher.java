package ch.epfl.sweng.studyup.questions;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.studyup.player.Player;

public class TimeOutNotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION = "notification";
    public static final String QUESTIONID = "questionID";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        String questionID = intent.getParcelableExtra(QUESTIONID);

        Player player = Player.get();

        if (player.getAnsweredQuestion().containsKey(questionID)) {
            //The question has been answered in the meantime
            return;
        }

        player.addAnsweredQuestion(questionID, false);

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

    }
}
