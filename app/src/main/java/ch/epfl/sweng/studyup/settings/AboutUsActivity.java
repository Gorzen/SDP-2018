package ch.epfl.sweng.studyup.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

public class AboutUsActivity extends NavigationStudent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    public void onBackSettings(View view) {
        finish();
    }

    public void onSendMailClick(View view) {


        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("plain/text");
        //add the object of the email
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Message about StudyUp app !");
        //hold e-mail addresses that should be delivered to
        String[] send_to = {"studyup@groupes.epfl.ch"};
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, send_to);

        //create an alert to choose how to send the email
        if (!MOCK_ENABLED) {
            startActivity(Intent.createChooser(intent, getString(R.string.send_mail)));
        }
    }
}
