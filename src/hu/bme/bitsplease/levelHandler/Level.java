package hu.bme.bitsplease.levelHandler;

import hu.bme.bitsplease.playerHandler.Player;
import hu.bme.bitsplease.stepHandler.Step;

import java.util.List;
import java.util.Map;

/**
 * Created by h3yduck on 2/27/15.
 */
public class Level {
    public Field fields[][];
    public Map<Player, Position> playerPositions;
}
