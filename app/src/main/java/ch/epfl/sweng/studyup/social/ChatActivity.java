package ch.epfl.sweng.studyup.social;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Navigation;
import ch.epfl.sweng.studyup.utils.Utils;

public class ChatActivity extends Navigation {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        navigationSwitcher(ChatActivity.this, ChatActivity.class, Utils.MAX_INDEX);
    }


    //Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }


}
