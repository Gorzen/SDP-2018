package ch.epfl.sweng.studyup.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.ShopActivity;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

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
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        // Add object and subject
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"study.up@epfl.ch"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Message about StudyUp app !");

        // Start the activity and ask for a mail
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail)));
    }
}
