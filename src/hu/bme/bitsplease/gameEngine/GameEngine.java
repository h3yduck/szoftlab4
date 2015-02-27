package hu.bme.bitsplease.gameEngine;

import hu.bme.bitsplease.displayHandler.DisplayHandler;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.playerHandler.Player;
import hu.bme.bitsplease.stepHandler.StepHandler;

import java.util.List;
import java.util.Map;

/**
 * Created by h3yduck on 2/27/15.
 */
public class GameEngine {
    private LevelLoader levelLoader; //csak egy palyabetolto lehet
    private Level level;

    private List<Player> players; // gyakorlatilag barmennyi jatekos, mind sajat StepHandlerrel, es DisplayHandlerrel
    private Map<Player, Integer> playerScores; // igy nem tudna csalni a pontjaval

    public GameEngine(LevelLoader levelLoader) {
        this.levelLoader = levelLoader;
    }

    public GameEngine() {
    }

    public void startGame(){
        getSettings();
        play();
    }

    public void getSettings(){
        // elso kepernyo, jatekosok szamanak stb... bekerese
        // a handler-ek hozzarendelese jatekosokhoz

        // jelenleg az App->main-ban beallitottuk parancssori parameter alapjan
        level = levelLoader.getLevel();
    }

    public void play(){
        // tenyleges jatek mechanika
        // minden lepes vegen meg kell hivni minden jatekos DisplayHandleret
        // minden lepeskor az aktualis jatekos StepHandleret
        // frissiteni kell a jatekosok pontjait
    }
}
