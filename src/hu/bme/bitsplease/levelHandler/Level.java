package hu.bme.bitsplease.levelHandler;

import hu.bme.bitsplease.playerHandler.Player;

import java.util.List;
import java.util.Map;

/**
 * Created by h3yduck on 2/27/15.
 */
public class Level {
    // valami matrix a palyanak, hozza fuggvenyek
    // azt is meg lehetne oldani, hogy a Player ne tudjon irni bele, csak olvasni
    // es majd a GameEngine irja minden korben
    private Field fields[][];
    private Map<Player, Position> playerPositions;

    public Level(int width, int height) {
        // fields
    }

    public void placePlayers(List<Player> players){

    }

}
