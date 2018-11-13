package ch.epfl.sweng.studyup.items;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

public enum Items {
    XP_POTION(Items.XP_POTION_NAME, Items.XP_POTION_DESCRIPTION),
    COIN_SACK(Items.COIN_SACK_NAME, Items.COIN_SACK_DESCRIPTION);

    //Names
    public static final String XP_POTION_NAME = "XP potion";
    public static final String COIN_SACK_NAME = "Sack of coin";

    //Descriptions
    public static final String XP_POTION_DESCRIPTION = "A potion that gives you some xp when drunk !";
    public static final String COIN_SACK_DESCRIPTION = "A sack containing lots of shiny coins !";

    private final String name;
    private final String description;

    Items(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void consume() {
        switch (this) {
            case XP_POTION:
                Player.get().addExperience(XP_STEP, MAIN_ACTIVITY);
                break;
            case COIN_SACK:
                Player.get().addCurrency(CURRENCY_PER_LEVEL, MAIN_ACTIVITY);
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
}
