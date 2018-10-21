package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.app.PendingIntent.getActivity;

public class LoginActivity extends AppCompatActivity {

    // Display message from intent set by authentication activity upon failed login
    public void displayFailedLoginMessage(Intent intent) {
        String failureMessage = intent.getStringExtra(getString(R.string.post_login_message_value));
        if (failureMessage != null) {
            Toast.makeText(LoginActivity.this, failureMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        displayFailedLoginMessage(getIntent());
    }

    public void onLoginButtonClick(View view) {
        String authURL = "https://studyup-authenticate.herokuapp.com/getCode";
        Intent authIntent = new Intent(Intent.ACTION_VIEW);
        authIntent.setData(Uri.parse(authURL));
        startActivity(authIntent);
    }
}