package ch.epfl.sweng.studyup.items;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public enum Items {
    XP_POTION(Items.XP_POTION_NAME, Items.XP_POTION_DESCRIPTION, Items.XP_POTION_PRICE),
    COIN_SACK(Items.COIN_SACK_NAME, Items.COIN_SACK_DESCRIPTION, Items.COIN_SACK_PRICE);

    //Names
    public static final String XP_POTION_NAME = "XP potion";
    public static final String COIN_SACK_NAME = "Sack of coin";

    //Descriptions
    public static final String XP_POTION_DESCRIPTION = "A potion that gives you some xp when drunk !";
    public static final String COIN_SACK_DESCRIPTION = "A sack containing lots of shiny coins !";

    //Prices
    public static final int XP_POTION_PRICE = 10;
    public static final int COIN_SACK_PRICE = 10;

    private final String name;
    private final String description;
    private final int price;

    Items(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getPrice(){
        return price;
    }

    public void consume() {
        switch (this) {
            case XP_POTION:
                Player.get().addExperience(XP_STEP, MOST_RECENT_ACTIVITY);
                break;
            case COIN_SACK:
                Player.get().addCurrency(CURRENCY_PER_LEVEL, MOST_RECENT_ACTIVITY);
                break;
            default:
        }
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getImageName() {
        switch (this) {
            case XP_POTION:
                return R.drawable.potion;
            case COIN_SACK:
                return R.drawable.coin_sack;
            default: throw new IllegalArgumentException("Unknown item");
        }
    }

    public static Items getItemFromName(String name){
        switch (name) {
            case XP_POTION_NAME:
                return XP_POTION;
            case COIN_SACK_NAME:
                return COIN_SACK;
            default: throw new IllegalArgumentException("Unknown name of item");
        }
    }

    public static ArrayList<String> getPlayersItemsNames() {
        List<Items> items = Player.get().getItems();
        ArrayList<String> itemsName = new ArrayList<>(items.size());
        for(int index = 0; index < items.size(); ++index) {
            itemsName.add(index, items.get(index).getName());
        }
        return itemsName;
    }

    public static int countItem(Items item) {
        List<Items> items = Player.get().getItems();
        int counter = 0;
        for (Items i : items) {
            if (i == item) {
                counter += 1;
            }
        }
        return counter;
    }

}
