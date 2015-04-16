package hu.bme.bitsplease.playerHandler;

import hu.bme.bitsplease.displayHandler.DisplayHandler;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.stepHandler.Step;
import hu.bme.bitsplease.stepHandler.InputHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by h3yduck on 2/27/15.
 */
public class Player extends Robot{
    private InputHandler stepHandler;
    private DisplayHandler displayHandler;

    public String name;
    private int score;
    
    public int number;
    private static int snumber = 1;

    public Map<Step.ActionType, Integer> actionNums;

    public Player(InputHandler inputHandler, DisplayHandler displayHandler, String name) {
        this.stepHandler = inputHandler;
        this.displayHandler = displayHandler;
        this.name = name;
        this.score = 0;
        actionNums = new HashMap<Step.ActionType, Integer>();
        actionNums.put(Step.ActionType.OIL, 0);
        actionNums.put(Step.ActionType.STICK, 0);
        velocity = new Velocity();
        number = snumber++;
    }
    
    public Step getStep() {
    	//Bekérjük névvel a lépést
        Step actualStep = stepHandler.getStep(name);
    	return actualStep;
    }
    
    public void displaySpecialActionTypesNumber(){
    	displayHandler.displaySpecialActionTypesNumber(actionNums.get(Step.ActionType.OIL), actionNums.get(Step.ActionType.STICK));
    }

    public void displayLevel(Level actualLevelState) {
        displayHandler.displayLevel(actualLevelState);
    }
    
    public void displayError(String error) {
        displayHandler.displayError(error);
    }

    public void addScore(int plusScore) {
        score += plusScore;
    }
    
    public int getScore(){
    	return score;
    }
}
