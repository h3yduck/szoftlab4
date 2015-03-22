package hu.bme.bitsplease.playerHandler;

import hu.bme.bitsplease.displayHandler.DisplayHandler;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.stepHandler.Step;
import hu.bme.bitsplease.stepHandler.InputHandler;

import java.util.Map;

/**
 * Created by h3yduck on 2/27/15.
 */
public class Player {
    private InputHandler stepHandler;
    private DisplayHandler displayHandler;

    public String name;
    private int score;
    public Velocity velocity;

    public Map<Step.ActionType, Integer> actionNums;

    public Player(InputHandler inputHandler, DisplayHandler displayHandler, String name) {
        this.stepHandler = inputHandler;
        this.displayHandler = displayHandler;
        this.name = name;
        this.score = 0;
    }

    public Step getStep() {
        return stepHandler.getStep(name);
    }

    public void displayLevel(Level actualLevelState) {
        displayHandler.displayLevel(actualLevelState);
    }

    public void addScore(int plusScore) {
        score += plusScore;
    }
    public int getScore(){
    	return score;
    }
}
