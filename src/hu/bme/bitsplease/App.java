package hu.bme.bitsplease;

import hu.bme.bitsplease.gameEngine.GameEngine;

public class App {
	
	public static GameEngine gameEngine;
	
    public static void main(String[] args) throws Exception {
    	
        gameEngine = new GameEngine();
        gameEngine.startGame();
    }
}
