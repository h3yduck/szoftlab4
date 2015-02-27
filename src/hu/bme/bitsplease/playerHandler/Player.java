package hu.bme.bitsplease.playerHandler;

import hu.bme.bitsplease.displayHandler.DisplayHandler;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.stepHandler.Step;
import hu.bme.bitsplease.stepHandler.StepHandler;

/**
 * Created by h3yduck on 2/27/15.
 */
public class Player {
    private StepHandler stepHandler;
    private DisplayHandler displayHandler;

    private String name;
    private int score; // ha kozpontilag (is) tarolnank(GameEngine-be) nem tudna a ponttal csalni

    public Player(StepHandler stepHandler, DisplayHandler displayHandler, String name) {
        this.stepHandler = stepHandler;
        this.displayHandler = displayHandler;
        this.name = name;
        this.score = 0;
    }

    public Step getStep(){
        return stepHandler.getStep();
    }

    public void displayLevel(Level actualLevelState){
        displayHandler.displayLevel(actualLevelState);
    }

    public void addScore(int plusScore){
        score += plusScore;
    }
}
