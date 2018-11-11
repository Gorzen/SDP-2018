package ch.epfl.sweng.studyup.items;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

public enum Items {
    XP_POTION,
    COIN_SACK;

    private static final String XP_POTION_NAME = "XP potion";
    private static final String COIN_SACK_NAME = "Sack of coin";

    private static final String XP_POTION_DESCRIPTION = "A potion that gives you some xp when drunk !";
    private static final String COIN_SACK_DESCRIPTION = "A sack containing lots of shiny coins !";

    public static void onConsume(Items item){
        switch (item){
            case XP_POTION:
                Player.get().addExperience(Utils.XP_STEP, Utils.mainActivity);
                break;
            case COIN_SACK:
                Player.get().addCurrency(Utils.CURRENCY_PER_LEVEL, Utils.mainActivity);
                break;
            default:
                throw new IllegalArgumentException("Unknown item");
        }
    }

    public static String getDescription(Items item){
        switch (item){
            case XP_POTION:
                return XP_POTION_DESCRIPTION;
            case COIN_SACK:
                return COIN_SACK_DESCRIPTION;
            default:
                throw new IllegalArgumentException("Unknown item");
        }
    }

    public static String getName(Items item){
        switch (item){
            case XP_POTION:
                return XP_POTION_NAME;
            case COIN_SACK:
                return COIN_SACK_NAME;
            default:
                throw new IllegalArgumentException("Unknown item");
        }
    }
}
