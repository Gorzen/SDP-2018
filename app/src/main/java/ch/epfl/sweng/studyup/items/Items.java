package ch.epfl.sweng.studyup.items;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

public enum Items {
    XP_POTION(Items.XP_POTION_VALUE, Items.XP_POTION_NAME, Items.XP_POTION_DESCRIPTION),
    COIN_SACK(Items.COIN_SACK_VALUE, Items.COIN_SACK_NAME, Items.COIN_SACK_DESCRIPTION);

    //Values
    private static final int XP_POTION_VALUE = 0;
    private static final int COIN_SACK_VALUE = 1;

    //Names
    public static final String XP_POTION_NAME = "XP potion";
    public static final String COIN_SACK_NAME = "Sack of coin";

    //Descriptions
    public static final String XP_POTION_DESCRIPTION = "A potion that gives you some xp when drunk !";
    public static final String COIN_SACK_DESCRIPTION = "A sack containing lots of shiny coins !";

    private final String name;
    private final String description;
    private final int value;

    Items(int value, String name, String description) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public void consume() {
        switch (value) {
            case XP_POTION_VALUE:
                Player.get().addExperience(Utils.XP_STEP, Utils.mainActivity);
                break;
            case COIN_SACK_VALUE:
                Player.get().addCurrency(Utils.CURRENCY_PER_LEVEL, Utils.mainActivity);
                break;
            default:
                throw new IllegalArgumentException("Unknown item");
        }
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public static Items getItems(Long value) {
        int val;

        if(value == 0l){
            val = 0;
        }else if(value == 1l){
            val = 1;
        }else{
            throw new IllegalArgumentException("Unknown item");
        }

        switch (val) {
            case XP_POTION_VALUE:
                return XP_POTION;
            case COIN_SACK_VALUE:
                return COIN_SACK;
            default: throw new IllegalArgumentException("Unknown item");
        }
    }

    public static Items getItems(String name) {
        if (name.equals(XP_POTION_NAME))
            return XP_POTION;
        if (name.equals(COIN_SACK_NAME))
            return COIN_SACK;
        throw new IllegalArgumentException("Unknown item");
    }

    public int valueOf(){
        return value;
    }

    public int getImageName() {
        switch (value) {
            case XP_POTION_VALUE:
                return R.drawable.potion;
            case COIN_SACK_VALUE:
                return R.drawable.coin_sack;
            default: throw new IllegalArgumentException("Unknown item");
        }
    }
}
