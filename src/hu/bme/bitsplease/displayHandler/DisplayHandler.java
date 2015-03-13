package hu.bme.bitsplease.displayHandler;

import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.playerHandler.Player;
import java.util.Map;

/**
 * Created by h3yduck on 2/27/15.
 */
public interface DisplayHandler {
    public void displayLevel(Level actualLevelState);
    public void displayScore(Map<Player, Integer> score);
    public void displayRound(String round);
    public void displayCongrat(String player);
}
