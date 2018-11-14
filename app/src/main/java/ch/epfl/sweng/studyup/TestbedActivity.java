package ch.epfl.sweng.studyup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;

import ch.epfl.sweng.studyup.utils.navigation.Navigation;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.LOCATION_PROVIDER_CLIENT;

public class TestbedActivity extends NavigationStudent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testbed);

        LOCATION_PROVIDER_CLIENT = new FusedLocationProviderClient(this);
    }
}
