package ch.epfl.sweng.studyup.items;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.RefreshContext;

public class BuyItemActivity extends RefreshContext {
    private Items item;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);

        Intent intent = getIntent();
        item = Items.getItemFromName(intent.getStringExtra(BuyItemActivity.class.getName()));
        TextView textViewName = findViewById(R.id.item_name);
        textViewName.setText(item.getName());
        TextView textViewDescription = findViewById(R.id.item_description);
        textViewDescription.setText(item.getDescription());
        ImageView img = findViewById(R.id.item_image);
        img.setImageResource(item.getImageName());
    }

    public void onPlusButton(View view){
        counter += 1;
        updateTextViewCounter();
    }

    public void onMinusButton(View view){
        if(counter > 0){
            counter -= 1;
            updateTextViewCounter();
        }
    }

    public void updateTextViewCounter(){
        TextView currCounter = findViewById(R.id.counter);
        currCounter.setText(Integer.toString(counter));
    }

    public void onBuyButton(View view) {
        if (Player.get().getCurrency() >= counter * item.getPrice()) {
            if (counter > 0) {
                for (int i = 0; i < counter; ++i) {
                    Player.get().addCurrency(-item.getPrice(), this);
                    Player.get().addItem(item);
                }
                startActivity(new Intent(getApplicationContext(), ShopActivity.class));
            }
        }else {
            Toast.makeText(getApplicationContext(), "You don't have enough money !", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackButtonBuyItem(View view){
        finish();
    }
}
