package ch.epfl.sweng.studyup.questions;

        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.widget.Toast;

        import ch.epfl.sweng.studyup.player.Player;

public class TimeOutNotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION = "notification";
    public static final String QUESTIONID = "questionID";
    public static final String ANSWER_NUMBER = "answerNumber";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        String questionID = intent.getStringExtra(QUESTIONID);
        int answerNumber = Integer.parseInt(intent.getStringExtra(ANSWER_NUMBER));

        Player player = Player.get();

        if (player.getAnsweredQuestion().containsKey(questionID)) {
            //The question has been answered in the meantime
            return;
        }

        //Compatibility with Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(DisplayQuestionActivity.CHANNEL_ID,
                    "Timed out question", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        player.addAnsweredQuestion(questionID, false, answerNumber);

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}