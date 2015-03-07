package hu.bme.bitsplease.gameEngine;

import hu.bme.bitsplease.stepHandler.Step;
import hu.bme.bitsplease.stepHandler.Step.ActionType;
import hu.bme.bitsplease.App;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.levelHandler.Position;
import hu.bme.bitsplease.playerHandler.Player;
import hu.bme.bitsplease.levelHandler.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by h3yduck on 2/27/15.
 */
public class GameEngine {
    private LevelLoader levelLoader; //csak egy palyabetolto lehet
    private Level level;
    private int remainingRounds;

    private List<Player> outPlayers; //Kisesett játékosok
    private List<Player> players; // gyakorlatilag barmennyi jatekos, mind sajat StepHandlerrel, es DisplayHandlerrel
    private Map<Player, Integer> playerScores; // igy nem tudna csalni a pontjaval

    public GameEngine(LevelLoader levelLoader) {
        this.levelLoader = levelLoader;
        players = new ArrayList<Player>();
    	outPlayers = new ArrayList<Player>();
    	playerScores = new HashMap<Player, Integer>();
    }

    public GameEngine() {
    	players = new ArrayList<Player>();
    	outPlayers = new ArrayList<Player>();
    	playerScores = new HashMap<Player, Integer>();
    }

    public void startGame() throws Exception {
        App.printList("startGame");
        App.newToList();
        getSettings();
        App.incrementList();
        play();
        App.removeList();
    }

    public void getSettings() throws Exception {
        App.printList("getSettings");
        if(levelLoader == null){
            //Ha nincs betöltött pálya, akkor lekérdezzük a felhasználótól
            String levelName = App.getLevel();
            levelLoader = new FileLoader(levelName);

        }
        App.newToList();
        level = levelLoader.getLevel();

        //specialis elemek szamanak lekerdezese
        App.incrementList();
        int specialTypeNum = App.getSpecialActionTypeNumber();
        //specialis elemek szamanak beallitasa minden jatekos reszere oil és stickre is
        for(int i = 0; i < players.size(); i++) {
            players.get(i).actionNums.put(ActionType.OIL, specialTypeNum);
            players.get(i).actionNums.put(ActionType.STICK, specialTypeNum);
        }



    	// elso kepernyo, jatekosok szamanak stb... bekerese
        //jatekosok szamanak lekerdezese
        App.incrementList();
        int numOfPlayers = App.getNumOfPlayer();
        //jatekosok hozzaadas a listahoz, sajat stephandlerrel,displayhandlerrel, konzolbol megadott nevvel
        App.printTabs();
        System.out.println("Játékosok nevei?");
        for(int i = 0; i < numOfPlayers; i++) {
        	//jatekos nevenek lekerdezese
        	boolean goodInput = false;
        	String name = "";
        	while(!goodInput){
        		goodInput = true;
                App.printTabs();
        		System.out.print(i+1 + ". ");
        		name = App.getRobotName();
        		if(name.length() > 10 && name.length() < 1){
        			goodInput = false;
        		}
        	}
        	Player player = new Player(null,null, name);
            players.add(player);
            //jatekos pontszamanak beallitasa
            playerScores.put(player, 0);
        }
        
	    //kezdeti korok szamanak lekerdezese
        App.incrementList();
        remainingRounds = App.getGameLength();

        List<Position> positions = new ArrayList<Position>();
        for(int i = 0; i < level.fields.length; i++){
            for(int j = 0; j < level.fields[i].length; j++){
                if(level.fields[i][j].fieldType == Field.Type.USRPOS){
                    positions.add(new Position(j ,i));
                }
            }
        }
        for(Player player : players){
            Random random = new Random();
            level.playerPositions.put(player, positions.get(random.nextInt(positions.size())));
        }
        App.removeList();
    }

    public void play() {
        // tenyleges jatek mechanika
        // minden lepes vegen meg kell hivni minden jatekos DisplayHandleret
        // minden lepeskor az aktualis jatekos StepHandleret
        // frissiteni kell a jatekosok pontjait
    	Boolean EndOfTheGame = false;
        /*
         *  A játék addig fut, amíg az EndOfTheGame változó false
         */
        while(!EndOfTheGame){

            /*
             *  Minden játékostól megkérdezzük mit lép, és változtatjuk eszerint az állapotát
             */

            for(Player player : players){
            	switch(level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).x].fieldType){
            	case FREE:
            		boolean goodStep = false;
            		Step actualStep = null;
            		
            		while(!goodStep){
            			goodStep = true;
	            		actualStep = player.getStep();
	            		
	            		/*
	            		 * Ellenőrizzük, hogy rakott e le valamit, és hogy van e rá kapacitása
	            		 * Ha van, akkor lerakjuk a mezőre és csökkentjük a kapacitást
	            		 */
	            		
	            		if(actualStep.stepAction == null){
	            			goodStep = false;
	            		}else{
	            			if(player.actionNums.get(actualStep.stepAction) > 0){
	            				if(actualStep.stepAction == Step.ActionType.OIL)
	            					level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).x].fieldType = Field.Type.fromChar('O');
	            				else if(actualStep.stepAction == Step.ActionType.STICK)
	            					level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).x].fieldType = Field.Type.fromChar('S');
	            				level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).y].remainingRounds = 3;
	            				player.actionNums.put(actualStep.stepAction, player.actionNums.get(actualStep.stepAction)-1);
	            			}else{
	            				System.out.println("You don't have any" + actualStep.stepAction.toString());
	            				goodStep = false;
	            			}
	            		}
            		}
            		/*
            		 * Kiszámoljuk a Step-ből az új sebességet
            		 */
            		if(actualStep != null && actualStep.angle >= 0){
	            		double actualX = Math.pow(player.velocity.size*Math.cos(player.velocity.angle * Math.PI / 180)
	            				+ Math.cos(actualStep.angle * Math.PI / 180), 2);
	            		double actualY = Math.pow(player.velocity.size*Math.sin(player.velocity.angle * Math.PI / 180)
	                    		+ Math.sin(actualStep.angle * Math.PI / 180), 2);
	            		player.velocity.size = Math.sqrt(actualX + actualY);
	            		player.velocity.angle = Math.atan(actualY/actualX);
            		}
            		
            		
            	break;
            	case OIL:
            	break;
            	case STICK:
            		/*
            		 * A sebesség a fele az eddiginek
            		 */
            		player.velocity.size /= 2;
            	break;
            	default:
            	break;
                }
            	
            	/*
            	 * Kiszámoljuk a robot új helyeztét
            	 */
            	int actualX = level.playerPositions.get(player).x + (int) Math.round(player.velocity.size*Math.cos(player.velocity.angle * Math.PI / 180));
            	int actualY = level.playerPositions.get(player).y + (int) Math.round(player.velocity.size*Math.sin(player.velocity.angle * Math.PI / 180));
            	
            	/*
            	 * Ha kiesik, akkor kivesszük a listából és belerakjuk a kiesettek listájába
            	 */
            	
            	if((actualX < 0) || (actualX >= level.fields[0].length)
            	|| (actualY < 0) || (actualY >= level.fields.length)
            	|| (level.fields[actualY][actualX].fieldType == Field.Type.HOLE)){
            		outPlayers.add(player);
            		player = null;
            	}else{
            		int newScore = Math.abs(actualX-level.playerPositions.get(player).x)
            				     + Math.abs(actualY-level.playerPositions.get(player).y);
            		playerScores.put(player, newScore);
            		player.addScore(newScore);
            		level.playerPositions.put(player, new Position(actualX, actualY));
            	}
            	
            	player.displayLevel(level);
            	
            }
            
            for(int i = 0; i < level.fields.length; i++){
        		for(int j = 0; j < level.fields[i].length; j++){
        			if(level.fields[i][j].remainingRounds > 0){
        				level.fields[i][j].remainingRounds--;
        			}
        			else{
        				if(level.fields[i][j].fieldType == Field.Type.OIL || level.fields[i][j].fieldType == Field.Type.STICK){
        					level.fields[i][j].fieldType = Field.Type.FREE;
        				}
        			}
        		}
        	}
            for(Player player : players){
            	if(player != null)
            		player.displayLevel(level);
            }
            
            remainingRounds--;
        	
        	if(remainingRounds <= 0 || outPlayers.size() >= players.size()-1){
        		EndOfTheGame = true;
        	}
        }
    }
}
