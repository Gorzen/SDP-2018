package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;

public class DisplayItemActivity extends AppCompatActivity {
    private Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        Intent intent = getIntent();
        item = Items.getItems(intent.getStringExtra(DisplayItemActivity.class.getName()));
        TextView textViewName = findViewById(R.id.item_name);
        textViewName.setText(Items.getName(item));
        TextView textViewDescription = findViewById(R.id.item_description);
        textViewDescription.setText(Items.getDescription(item));
        ImageView img = findViewById(R.id.item_image);
        img.setImageResource(Items.getImageName(item));

        Button backButton = findViewById(R.id.back_button_display_item);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button useButton = findViewById(R.id.use_button);
        useButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.get().consumeItem(item);
                startActivity(new Intent(getApplicationContext(), InventoryActivity.class));
            }
        });
    }
}
