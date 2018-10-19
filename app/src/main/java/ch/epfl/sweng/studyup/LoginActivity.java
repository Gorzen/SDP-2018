package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    // Display message from intent set by authentication activity upon failed login
    public void displayLoginMessage(Intent intent) {
        String loginMessage = intent.getStringExtra(getString(R.string.post_login_message_value));
        if (loginMessage != null) {
            Toast.makeText(getApplicationContext(), loginMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        displayLoginMessage(getIntent());
    }

    public void onLoginButtonClick(View view) {
        String authURL = "https://studyup-authenticate.herokuapp.com/getCode";
        Intent authIntent = new Intent(Intent.ACTION_VIEW);
        authIntent.setData(Uri.parse(authURL));
        startActivity(authIntent);
    }
}
