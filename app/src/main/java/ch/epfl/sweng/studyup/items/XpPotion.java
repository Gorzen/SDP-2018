package ch.epfl.sweng.studyup.items;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

public class XpPotion extends Item {
    private final int xp;

    public XpPotion(){
        xp = Utils.XP_STEP;
    }

    public XpPotion(int xp){
        this.xp = xp;
    }

    @Override
    public void onConsume(Player player) {
        player.addExperience(xp, Utils.mainActivity);
    }
}
