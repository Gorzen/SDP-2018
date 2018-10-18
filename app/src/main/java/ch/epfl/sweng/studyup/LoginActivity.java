package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class LoginActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotsLayout;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotsLayout = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        final int dotsNumber = viewPagerAdapter.getCount();
        dots = new ImageView[dotsNumber];

        //initialisation of dots
        for(int i = 0; i<dotsNumber; i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_non_active_24dp));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsLayout.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_active_24dp));

        //dots change colors in function of the current Page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i<dotsNumber; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_non_active_24dp));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_active_24dp));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    public void onLoginButtonClick(View view) {
        String authURL = "https://studyup-authenticate.herokuapp.com/getCode";
        Intent authIntent = new Intent(Intent.ACTION_VIEW);
        authIntent.setData(Uri.parse(authURL));
        startActivity(authIntent);
    }

}
