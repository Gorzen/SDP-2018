package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.RefreshContext;

public class DisplayItemActivity extends RefreshContext {

    private static final String TAG = DisplayItemActivity.class.getSimpleName();

    private Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        Intent intent = getIntent();
        item = Items.getItemFromName(intent.getStringExtra(DisplayItemActivity.class.getName()));
        TextView textViewName = findViewById(R.id.item_name);
        textViewName.setText(item.getName());
        TextView textViewDescription = findViewById(R.id.item_description);
        textViewDescription.setText(item.getDescription());
        ImageView img = findViewById(R.id.item_image);
        img.setImageResource(item.getImageName());

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
                try {
                    Player.get().consumeItem(item);
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                startActivity(new Intent(getApplicationContext(), InventoryActivity.class));
            }
        });
    }
}
