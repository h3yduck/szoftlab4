package hu.bme.bitsplease;

import hu.bme.bitsplease.displayHandler.GUIDisplay;
import hu.bme.bitsplease.gameEngine.GameEngine;
import hu.bme.bitsplease.stepHandler.GUIInput;

public class App {
	
	public static GameEngine gameEngine;
	
    public static void main(String[] args){
        gameEngine = new GameEngine();

        gameEngine.input = new GUIInput();
        gameEngine.display = new GUIDisplay();
        gameEngine.input.settingsFrame = gameEngine.display.settingsFrame;
        gameEngine.input.playFrame = gameEngine.display.playFrame;
        gameEngine.input.settingsFrame.gameEngine = gameEngine;
        gameEngine.input.playFrame.gameEngine = gameEngine;
        gameEngine.input.settingsFrame.setVisible(true);

//        gameEngine.getSettings(null);
//
//        gameEngine.play(null);
//
//        gameEngine.endGame();
    }
}
