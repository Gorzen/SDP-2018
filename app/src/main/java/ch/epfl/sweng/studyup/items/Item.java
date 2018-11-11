package ch.epfl.sweng.studyup.items;

import ch.epfl.sweng.studyup.player.Player;

public abstract class Item {
    protected boolean consumed = false;

    public void consume(Player player){
        if(!consumed){
            consumed = true;
            onConsume(player);
        }
    }

    public abstract void onConsume(Player player);
}
