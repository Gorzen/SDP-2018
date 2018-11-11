package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.studyup.R;

public class DisplayItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        Intent intent = getIntent();
        Items item = Items.getItems(intent.getStringExtra(DisplayItemActivity.class.getName()));
        TextView textViewName = findViewById(R.id.item_name);
        textViewName.setText(Items.getName(item));
        TextView textViewDescritpion = findViewById(R.id.item_description);
        textViewDescritpion.setText(Items.getDescription(item));
        ImageView img = findViewById(R.id.item_image);
        img.setImageResource(R.drawable.coin_sack);
    }
}
