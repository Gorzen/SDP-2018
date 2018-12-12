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
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static ch.epfl.sweng.studyup.utils.Constants.allNPCs;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

@SuppressWarnings("HardCodedStringLiteral") // Pain in the ass to translate.
public enum Items implements Serializable {
    XP_POTION(Items.XP_POTION_NAME_ID, Items.XP_POTION_DESCRIPTION_ID, Items.XP_POTION_PRICE),
    UNSTABLE_POTION(Items.UNSTABLE_POTION_NAME_ID, Items.UNSTABLE_POTION_DESCRIPTION_ID, Items.UNSTABLE_POTION_PRICE),
    TOMBOLA(Items.TOMBOLA_NAME_ID, Items.TOMBOLA_DESCRIPTION_ID, Items.TOMBOLA_PRICE),
    COIN_SACK(Items.COIN_SACK_NAME_ID, Items.COIN_SACK_DESCRIPTION_ID, Items.COIN_SACK_PRICE),
    MAP(Items.MAP_NAME_ID, Items.MAP_DESCRIPTION_ID, Items.MAP_PRICE),
    RED_THEME(Items.RED_THEME_ID, Items.RED_DESCRIPTION_ID, Items.RED_PRICE),
    GREEN_THEME(Items.GREEN_THEME_ID, Items.GREEN_DESCRIPTION_ID, Items.GREEN_PRICE),
    BLUE_THEME(Items.BLUE_THEME_ID, Items.BLUE_DESCRIPTION_ID, Items.BLUE_PRICE),
    BROWN_THEME(Items.BROWN_THEME_ID, Items.BROWN_DESCRIPTION_ID, Items.BROWN_PRICE),
    MULTI_THEME(Items.MULTI_THEME_ID, Items.MULTI_DESCRIPTION_ID, Items.MULTI_PRICE);


    //Names
    public static final int XP_POTION_NAME_ID = R.string.item_xp_potion_name;
    public static final int UNSTABLE_POTION_NAME_ID = R.string.item_unstable_potion_name;
    public static final int TOMBOLA_NAME_ID = R.string.item_tombola_name;
    public static final int COIN_SACK_NAME_ID = R.string.item_coin_sack_name;
    public static final int MAP_NAME_ID = R.string.item_map_name;
    public static final int RED_THEME_ID = 0;
    public static final int GREEN_THEME_ID = 0;
    public static final int BLUE_THEME_ID = 0;
    public static final int BROWN_THEME_ID = 0;
    public static final int MULTI_THEME_ID = 0;


    //Descriptions
    public static final int XP_POTION_DESCRIPTION_ID = R.string.item_xp_potion_description;
    public static final int UNSTABLE_POTION_DESCRIPTION_ID = R.string.item_unstable_potion_description;
    public static final int TOMBOLA_DESCRIPTION_ID = R.string.item_tombola_description;
    public static final int COIN_SACK_DESCRIPTION_ID = R.string.item_coin_sack_description;
    public static final int MAP_DESCRIPTION_ID = R.string.item_map_description;
    public static final int RED_DESCRIPTION_ID = R.string.red_description;
    public static final int GREEN_DESCRIPTION_ID = R.string.green_description;
    public static final int BLUE_DESCRIPTION_ID = R.string.blue_description;
    public static final int BROWN_DESCRIPTION_ID = R.string.brown_description;
    public static final int MULTI_DESCRIPTION_ID = 0;

    //Prices
    public static final int XP_POTION_PRICE = 10;
    public static final int UNSTABLE_POTION_PRICE = 50;
    public static final int TOMBOLA_PRICE = 200;
    public static final int COIN_SACK_PRICE = 10;
    public static final int MAP_PRICE = 150;
    public static final int RED_PRICE = 50;
    public static final int GREEN_PRICE = 50;
    public static final int BLUE_PRICE = 50;
    public static final int BROWN_PRICE = 50;
    public static final int MULTI_PRICE = 50;

    private final int nameId;
    private final int descriptionId;
    private final int price;

    Items(int nameId, int descriptionId, int price) {
        this.nameId = nameId;
        this.descriptionId = descriptionId;
        this.price = price;
    }

    public int getPrice(){
        return price;
    }

    public void consume() {
        Random random = new Random();

        switch (this) {
            case XP_POTION:
                Player.get().addExperience(XP_STEP, MOST_RECENT_ACTIVITY);
                break;
            case UNSTABLE_POTION:
                Player.get().addExperience(random.nextInt(Constants.XP_TO_LEVEL_UP * 2), MOST_RECENT_ACTIVITY);
                break;
            case TOMBOLA:
                Player.get().addCurrency(random.nextInt(TOMBOLA_PRICE * 3), MOST_RECENT_ACTIVITY);
                break;
            case COIN_SACK:
                Player.get().addCurrency(CURRENCY_PER_LEVEL, MOST_RECENT_ACTIVITY);
                break;
            case MAP:
                for(NPC npc : allNPCs) {
                    Player.get().addKnownNPC(npc);
                }
                break;
            default:
        }
    }

    public String getDescription() {
        return MOST_RECENT_ACTIVITY.getString(descriptionId);
    }

    public String getName() {
        return MOST_RECENT_ACTIVITY.getString(nameId);
    }

    public int getImageName() {
        switch (this) {
            case XP_POTION:
                return R.drawable.potion;
            case UNSTABLE_POTION:
                return R.drawable.unstable_potion;
            case TOMBOLA:
                return R.drawable.tombola;
            case COIN_SACK:
                return R.drawable.coin_sack;
            case MAP:
                return R.drawable.map_item;
            default: throw new IllegalArgumentException("Unknown item");
        }
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