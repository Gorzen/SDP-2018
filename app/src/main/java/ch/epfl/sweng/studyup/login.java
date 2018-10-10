package ch.epfl.sweng.studyup;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i("Info","Starting login activity");

        Log.i("Info", "Attempting to get data from intent");

        Uri data = getIntent().getData();

        String dataNull = data == null ? "true" : "false";
        Log.i("Data null", dataNull);

        if (data != null && !TextUtils.isEmpty(data.getScheme())) {
            if ("studyup".equals(data.getScheme())) {
                String code = data.getQueryParameter("code");

                String tokenURL = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/token" +
                        "?client_id=" + "05deebf93b675fe41ce9415f@epfl.ch" +
                        "&client_secret=" + "875fb85762667d798d3ad90c2d3d2971" +
                        "&redirect_uri=" + "studyup://login" +
                        "&grant_type=authorization_code" +
                        "&code=" + code +
                        "&scope=" + "Tequila.profile";

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(tokenURL).openConnection();
                    InputStream stream = connection.getInputStream();
                    String tokenJsonStr = new Scanner(stream).useDelimiter("\\A").next();

                    JSONObject tokenJson = new JSONObject(tokenJsonStr);
                    String token = tokenJson.getString("access_token");

                    TextView loginData = findViewById(R.id.loginData);
                    loginData.setText(token);
                } catch (Exception e) {
                    throw new AssertionError(e);
                }
            }
        }
    }
}
