package ch.epfl.sweng.studyup.items;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

public enum Items {
    XP_POTION,
    COIN_SACK;

    private static final String XP_POTION_DESCRITPION = "A potion that gives you some xp when drunk !";
    private static final String COIN_SACK_DESCRITPION = "A sack containing lots of shiny coins !";

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
                return XP_POTION_DESCRITPION;
            case COIN_SACK:
                return COIN_SACK_DESCRITPION;
            default:
                throw new IllegalArgumentException("Unknown item");
        }
    }
}
