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
    private int counter = 1;

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

        TextView unitPrice = findViewById(R.id.unit_price);
        unitPrice.setText(getString(R.string.text_itemprice) + item.getPrice());
        TextView playerCurrency = findViewById(R.id.player_currency);
        playerCurrency.setText(getString(R.string.text_currencyyouhave) + Player.get().getCurrency());
        TextView playerItemNum = findViewById(R.id.player_item_num);
        playerItemNum.setText(getString(R.string.text_itemalreadyhave) + Items.countItem(item) + getString(R.string.text_itemtimes));

        updateTextViewCounter();
    }


    public void onPlusButton(View view) {
        counter += 1;
        updateTextViewCounter();
    }

    public void onMinusButton(View view) {
        if (counter > 1) {
            counter -= 1;
            updateTextViewCounter();
        }
    }

    public void updateTextViewCounter() {
        TextView currCounter = findViewById(R.id.counter);
        currCounter.setText(Integer.toString(counter));
        TextView totalPrice = findViewById(R.id.total_price);
        totalPrice.setText(getString(R.string.text_totalprice) + counter * item.getPrice());
    }

    public void onBuyButton(View view) {
        if (Player.get().getCurrency() >= counter * item.getPrice()) {
            for (int i = 0; i < counter; ++i) {
                Player.get().addCurrency(-item.getPrice(), this);
                Player.get().addItem(item);
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.text_notenoughmoney), Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackButtonBuyItem(View view) {
        finish();
    }
}
