package ch.epfl.sweng.studyup.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.npc.NPC;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_BLUE;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_ORANGE;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_GREEN;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_MULTI;
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static ch.epfl.sweng.studyup.utils.Constants.allNPCs;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public enum Items implements Serializable {
    XP_POTION(Items.XP_POTION_NAME_ID, Items.XP_POTION_DESCRIPTION_ID, Items.XP_POTION_PRICE, Items.XP_POTION_IMAGE),
    UNSTABLE_POTION(Items.UNSTABLE_POTION_NAME_ID, Items.UNSTABLE_POTION_DESCRIPTION_ID, Items.UNSTABLE_POTION_PRICE, Items.UNSTABLE_POTION_IMAGE),
    TOMBOLA(Items.TOMBOLA_NAME_ID, Items.TOMBOLA_DESCRIPTION_ID, Items.TOMBOLA_PRICE, Items.TOMBOLA_IMAGE),
    COIN_SACK(Items.COIN_SACK_NAME_ID, Items.COIN_SACK_DESCRIPTION_ID, Items.COIN_SACK_PRICE, Items.COIN_SACK_IMAGE),
    MAP(Items.MAP_NAME_ID, Items.MAP_DESCRIPTION_ID, Items.MAP_PRICE, Items.MAP_IMAGE),
    GREEN_THEME(Items.GREEN_NAME_ID, Items.GREEN_DESCRIPTION_ID, Items.GREEN_PRICE, Items.GREEN_IMAGE),
    BLUE_THEME(Items.BLUE_NAME_ID, Items.BLUE_DESCRIPTION_ID, Items.BLUE_PRICE, Items.BLUE_IMAGE),
    ORANGE_THEME(Items.ORANGE_NAME_ID, Items.ORANGE_DESCRIPTION_ID, Items.ORANGE_PRICE, Items.ORANGE_IMAGE),
    MULTI_THEME(Items.MULTI_NAME_ID, Items.MULTI_DESCRIPTION_ID, Items.MULTI_PRICE, Items.MULTI_IMAGE);

    //Names
    public static final int XP_POTION_NAME_ID = R.string.item_xp_potion_name;
    public static final int UNSTABLE_POTION_NAME_ID = R.string.item_unstable_potion_name;
    public static final int TOMBOLA_NAME_ID = R.string.item_tombola_name;
    public static final int COIN_SACK_NAME_ID = R.string.item_coin_sack_name;
    public static final int MAP_NAME_ID = R.string.item_map_name;
    public static final int GREEN_NAME_ID = R.string.green_name;
    public static final int BLUE_NAME_ID = R.string.blue_name;
    public static final int ORANGE_NAME_ID = R.string.orange_name;
    public static final int MULTI_NAME_ID = R.string.multi_colour_name;

    //Descriptions
    public static final int XP_POTION_DESCRIPTION_ID = R.string.item_xp_potion_description;
    public static final int UNSTABLE_POTION_DESCRIPTION_ID = R.string.item_unstable_potion_description;
    public static final int TOMBOLA_DESCRIPTION_ID = R.string.item_tombola_description;
    public static final int COIN_SACK_DESCRIPTION_ID = R.string.item_coin_sack_description;
    public static final int MAP_DESCRIPTION_ID = R.string.item_map_description;
    public static final int GREEN_DESCRIPTION_ID = R.string.green_description;
    public static final int BLUE_DESCRIPTION_ID = R.string.blue_description;
    public static final int ORANGE_DESCRIPTION_ID = R.string.brown_description;
    public static final int MULTI_DESCRIPTION_ID = 0;

    //Prices
    public static final int XP_POTION_PRICE = 10;
    public static final int UNSTABLE_POTION_PRICE = 50;
    public static final int TOMBOLA_PRICE = 200;
    public static final int COIN_SACK_PRICE = 10;
    public static final int MAP_PRICE = 150;
    public static final int GREEN_PRICE = 50;
    public static final int BLUE_PRICE = 50;
    public static final int ORANGE_PRICE = 50;
    public static final int MULTI_PRICE = 50;

    //Images
    public static final int XP_POTION_IMAGE = R.drawable.item_potion;
    public static final int UNSTABLE_POTION_IMAGE = R.drawable.item_unstable;
    public static final int TOMBOLA_IMAGE = R.drawable.item_tombola;
    public static final int COIN_SACK_IMAGE = R.drawable.item_bag;
    public static final int MAP_IMAGE = R.drawable.item_map;
    public static final int GREEN_IMAGE = R.drawable.item_theme_green;
    public static final int BLUE_IMAGE = R.drawable.item_theme_blue;
    public static final int ORANGE_IMAGE = R.drawable.item_theme_orange;
    public static final int MULTI_IMAGE = R.drawable.item_theme_d4rk;

    private final int nameId;
    private final int descriptionId;
    private final int price;
    private final int image;

    Items(int nameId, int descriptionId, int price, int image) {
        this.nameId = nameId;
        this.descriptionId = descriptionId;
        this.price = price;
        this.image = image;
    }

    public int getPrice(){
        return price;
    }

    public void consume() {

        switch (this) {
            case XP_POTION:
                Player.get().addExperience(XP_STEP, MOST_RECENT_ACTIVITY);
                break;
            case UNSTABLE_POTION:
                Player.get().addExperience(randomIntWithoutZero(Constants.XP_TO_LEVEL_UP * 2), MOST_RECENT_ACTIVITY);
                break;
            case TOMBOLA:
                Player.get().addCurrency(randomIntWithoutZero(TOMBOLA_PRICE * 3), MOST_RECENT_ACTIVITY);
                break;
            case COIN_SACK:
                Player.get().addCurrency(CURRENCY_PER_LEVEL, MOST_RECENT_ACTIVITY);
                break;
            case MAP:
                for(NPC npc : allNPCs) {
                    Player.get().addKnownNPC(npc);
                }
                break;
            case GREEN_THEME:
                Player.get().addTheme(SETTINGS_COLOR_GREEN);
                break;
            case ORANGE_THEME:
                Player.get().addTheme(SETTINGS_COLOR_ORANGE);
                break;
            case BLUE_THEME:
                Player.get().addTheme(SETTINGS_COLOR_BLUE);
                break;
            case MULTI_THEME:
                Player.get().addTheme(SETTINGS_COLOR_MULTI);
                break;
            default:
        }
    }

    //for tests purposes
    private int randomIntWithoutZero(int bound) {
        Random random = new Random();
        int retValue;
        while((retValue = random.nextInt(bound)) == 0);
        return retValue;
    }

    public String getDescription() {
        return MOST_RECENT_ACTIVITY.getString(descriptionId);
    }

    public String getName() {
        return MOST_RECENT_ACTIVITY.getString(nameId);
    }

    public int getImageName() {
        return image;
    }

    public static Items getItemFromName(String name){
        for(Items i : Items.values()){
            if(name.equals(i.getName()))
                return i;
        }
        throw new IllegalArgumentException("Unknown item: " + name);
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