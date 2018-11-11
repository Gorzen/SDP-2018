package ch.epfl.sweng.studyup.items;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

public enum Items {
    XP_POTION;

    public static void onConsume(Items item){
        switch (item){
            case XP_POTION:
                Player.get().addExperience(Utils.XP_STEP, Utils.mainActivity);
                break;
            default:
                throw new IllegalArgumentException("Unknown item");
        }
    }
}
