package hu.bme.bitsplease;

import hu.bme.bitsplease.gameEngine.GameEngine;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.LevelLoader;

public class App {

    public static void main(String[] args) throws Exception {
        LevelLoader levelLoader;
        if (args.length > 0)
            levelLoader = new FileLoader(args[0]);
        else
            levelLoader = null;
        GameEngine gameEngine = new GameEngine(levelLoader);
        gameEngine.startGame();
    }
    
    public static int numOfPlayers() {
    	return 5;
    }
    
    public static String getName(){
    	return null;
    }
    
    public static int getGameLength() {
    	return 5;
    }
    
    public static int getSpecialActionTypeNumber() {
    	return 1;
    }
}
