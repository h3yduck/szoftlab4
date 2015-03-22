package hu.bme.bitsplease;

import hu.bme.bitsplease.gameEngine.GameEngine;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.stepHandler.Step;

public class App {

    public static void main(String[] args) throws Exception {
        LevelLoader levelLoader;
        GameEngine gameEngine;
        if (args.length > 0){
            levelLoader = new FileLoader(args[0]);
            gameEngine = new GameEngine(levelLoader);
        }
        else{
        	gameEngine = new GameEngine();
        }
        gameEngine.startGame();
    }
    
    public static int getNumOfPlayer() {
    	return 5;
    }
    
    public static String getRobotName(){
    	return "asd";
    }
    
    public static int getGameLength() {
    	return 5;
    }
    
    public static int getSpecialActionTypeNumber() {
    	return 1;
    }

	public static String getLevel() {
		return "1";
	}

	public static void displayLevel(Level actualLevelState) {
		
	}

	public static Step getStep(String name) {
		Step actualStep =  new Step();
		actualStep.angle = 0;
		return actualStep;
	}
}
