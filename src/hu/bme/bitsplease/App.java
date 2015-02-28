package hu.bme.bitsplease;

import hu.bme.bitsplease.gameEngine.GameEngine;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.LevelLoader;

public class App {

    public static void main(String[] args) {
        LevelLoader levelLoader;
        if(args.length > 0)
            levelLoader = new FileLoader(args[0]);
        else
            levelLoader = null;
        GameEngine gameEngine = new GameEngine(levelLoader);
        gameEngine.startGame();

    }
}
