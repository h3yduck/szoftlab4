package hu.bme.bitsplease.gameEngine;

import hu.bme.bitsplease.stepHandler.Step.ActionType;
import hu.bme.bitsplease.App;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.playerHandler.Player;

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

    public void startGame() throws Exception {
        getSettings();
        play();
    }

    public void getSettings() throws Exception {
    	// elso kepernyo, jatekosok szamanak stb... bekerese
        //jatekosok szamanak lekerdezese
        int numOfPlayers = App.numOfPlayers();
        //jatekosok hozzaadas a listahoz, sajat stephandlerrel,displayhandlerrel, konzolbol megadott nevvel
        for(int i = 0; i < numOfPlayers; i++) {
        	//jatekos nevenek lekerdezese
        	String name = App.getName();
        	Player player = new Player(null,null, name);
            players.add(player);
            //jatekos pontszamanak beallitasa
            playerScores.put(player, 0);
        }
        
        // jelenleg az App->main-ban beallitottuk parancssori parameter alapjan
        //palya betoltese
        level = levelLoader.getLevel();
        
        //kezdeti korok szamanak lekerdezese
        int remainingRounds = App.getGameLength();
        //hatralevo korok szamanak beallitasa
        for(int i = 0; i < level.fields.length; i++){
        	for(int j = 0; j < level.fields[0].length; j++) {
        		level.fields[i][j].remainingRounds = remainingRounds;
        	}
        }
        
        //specialis elemek szamanak lekerdezese
        int specialTypeNum = App.getSpecialActionTypeNumber();
        //specialis elemek szamanak beallitasa minden jatekos reszere oil és stickre is
        for(int i = 0; i < players.size(); i++) {
        	players.get(i).actionNums.put(ActionType.OIL, specialTypeNum);
        	players.get(i).actionNums.put(ActionType.STICK, specialTypeNum);
        }
        

       
    }

    public void play() {
        // tenyleges jatek mechanika
        // minden lepes vegen meg kell hivni minden jatekos DisplayHandleret
        // minden lepeskor az aktualis jatekos StepHandleret
        // frissiteni kell a jatekosok pontjait
    }
}
