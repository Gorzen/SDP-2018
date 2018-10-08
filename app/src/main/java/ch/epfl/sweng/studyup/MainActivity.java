package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                123);
    }


    public void toCharacterHomePage(View view) {
        Intent intent = new Intent(this, CharacterHomepageActivity.class);
        startActivity(intent);
    }

    public void onLocButtonClick(View view) {
        LocationTracker locationTracker = new LocationTracker(getApplicationContext());
        Location currLoc = locationTracker.getLocation();
        if (currLoc != null) {
            double currLat = currLoc.getLatitude();
            double currLong = currLoc.getLongitude();

            Toast.makeText(getApplicationContext(),
                    "Latitude: " + currLat + "\nLongitude: " + currLong,
                    Toast.LENGTH_LONG).show();

            TextView test = findViewById(R.id.helloWorld);
            test.setText("Latitude: " + currLat + "\nLongitude: " + currLong);
        }
    }
}
