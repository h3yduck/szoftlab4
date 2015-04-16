package hu.bme.bitsplease.gameEngine;

import hu.bme.bitsplease.stepHandler.ConsoleInput;
import hu.bme.bitsplease.stepHandler.InputHandler;
import hu.bme.bitsplease.stepHandler.Step;
import hu.bme.bitsplease.stepHandler.Step.ActionType;
import hu.bme.bitsplease.displayHandler.ConsoleDisplay;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.levelHandler.Position;
import hu.bme.bitsplease.playerHandler.LittleRobot;
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
	//A pályabetöltő objektum
    private LevelLoader levelLoader; 
    private Level level;
    //A játékban hátramaradt körök száma
    private int remainingRounds;

    private List<Player> outPlayers; //Kisesett játékosok
    private List<Player> players; // A játékosok listája
    private List<LittleRobot> littleRobots;
    //ha egy játékos kiesett, akkor belekerül az outPlayers listába, és az értéke null lesz a players listában
    private Map<Player, Integer> playerScores; // A játékosok pontjai
    
    private void deletePlayer(Player player){
    	outPlayers.add(player);
		players.set(players.indexOf(player), null);
    }
    
    public GameEngine(LevelLoader levelLoader) {
        this.levelLoader = levelLoader;
        players = new ArrayList<Player>();
    	outPlayers = new ArrayList<Player>();
    	littleRobots = new ArrayList<LittleRobot>();
    	playerScores = new HashMap<Player, Integer>();
    }

    public GameEngine() {
    	players = new ArrayList<Player>();
    	outPlayers = new ArrayList<Player>();
    	playerScores = new HashMap<Player, Integer>();
    }

    public void startGame() {
    	getSettings(null);
    	
        /*
         *  A játék addig fut, amíg a play függvény igazat ad vissza
         *  null -> parancsok nélkül futtatás
         */
    	
    	while(play(null));
    }
    
    public void getSettings(String command){
    	
    	InputHandler inputHandler = new ConsoleInput();
        
        if(levelLoader == null){
            //Ha nincs betöltött pálya, akkor lekérdezzük a felhasználótól
            String levelName = inputHandler.getLevel();
            levelLoader = new FileLoader(levelName);
        }
        level = levelLoader.getLevel();

        //specialis elemek szamanak lekerdezese
        int specialTypeNum = inputHandler.getSpecialActionTypeNumber();
        
        

        int numOfPlayers = inputHandler.getNumOfPlayer();
        
        for(int i = 0; i < numOfPlayers; i++) {
        	//jatekos nevenek lekerdezese
        	boolean goodInput = false;
        	String name = "";
        	while(!goodInput){
        		goodInput = true;
        		name = inputHandler.getRobotName();
        		if(name.length() > 10 && name.length() < 1){
        			goodInput = false;
        		}
        	}
        	
        	Player player = new Player(new ConsoleInput(), new ConsoleDisplay(), name);
            players.add(player);
        }
        
      //specialis elemek szamanak beallitasa minden jatekos reszere oil és stickre is
        for(int i = 0; i < players.size(); i++) {
            players.get(i).actionNums.put(ActionType.OIL, specialTypeNum);
            players.get(i).actionNums.put(ActionType.STICK, specialTypeNum);
        }

        remainingRounds = inputHandler.getGameLength();

        //Az USRPOS mezőket átállítjuk FREE-re
        List<Position> positions = new ArrayList<Position>();
        for(int i = 0; i < level.fields.length; i++){
            for(int j = 0; j < level.fields[i].length; j++){
                if(level.fields[i][j].fieldType == Field.Type.USRPOS){
                    positions.add(new Position(j ,i));
                    level.fields[i][j].fieldType = Field.Type.FREE;
                }
            }
        }
        //A megfelelő helyeken véletlenszerűen elhelyezzük a játékosokat
        for(Player player : players){
        	if(positions.size() > 0){
        		Random random = new Random();
        		level.playerPositions.put(player, positions.get(random.nextInt(positions.size())));
        	}
        }
    }

    public boolean play(String comamnd) {
    	/*
         * tenyleges jatek mechanika
         * minden lepes vegen meg kell hivni minden jatekos DisplayHandleret, és kirajzolni a pályát
         * minden lepeskor az aktualis jatekos StepHandleret, és bekérni a lépést
         * frissiteni kell a jatekosok pontjait
    	 */
    	
    	/*
    	 * Kirajzoljuk minden játékosnál a pályát
    	 */
    	for(Player player : players){ 
    		player.displayLevel(level);
    	}


            /*
             *  Minden játékostól megkérdezzük mit lép, és változtatjuk eszerint az állapotát
             */

            for(Player player : players){
            	// Ha a player null, akkor a játékos már kiesett
            	if(player == null){
            		continue;
            	}
            	
            	// Megnézzük, hogy milyen mezőn áll a robot
            	// Ha FREE-n, akkor bekérjük a lépést
            	switch(level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).x].fieldType){
            	case FREE:
            		boolean goodStep = false;
            		Step actualStep = null;
            		
            		while(!goodStep){
            			
            			/*
            			 * Ha nem megfelelő a lerakott elem, akkor újra kérdezzük a felhasználót
            			 */
            			
            			//goodStep = true;
            			
	            		actualStep = player.getStep();
	            		
	            		/*
	            		 * Ellenőrizzük, hogy rakott e le valamit, és hogy van e rá kapacitása
	            		 * Ha van, akkor lerakjuk a mezőre és csökkentjük a kapacitást
	            		 */

                        /*if(!(actualStep.stepAction == null)){
	            			if(player.actionNums.get(actualStep.stepAction) > 0){
	            				if(actualStep.stepAction == Step.ActionType.OIL)
	            					level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).x].fieldType = Field.Type.fromChar('O');
	            				else if(actualStep.stepAction == Step.ActionType.STICK)
	            					level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).x].fieldType = Field.Type.fromChar('S');
	            				level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).y].remainingRounds = 3;
	            				player.actionNums.put(actualStep.stepAction, player.actionNums.get(actualStep.stepAction)-1);
	            			}else{
	            				goodStep = false;
	            			}
	            		}*/
            		}
            		
            		/*
            		 * Kiszámoljuk a Step-ből az új sebességet
            		 */
            		
            		if(actualStep != null && actualStep.angle >= 0){
	            		double actualX = player.velocity.size*Math.cos(player.velocity.angle * Math.PI / 180)
	            				+ Math.cos(actualStep.angle * Math.PI / 180);
	            		double actualY = player.velocity.size*Math.sin(player.velocity.angle * Math.PI / 180)
	                    		+ Math.sin(actualStep.angle * Math.PI / 180);
	            		player.velocity.size = Math.sqrt(Math.pow(actualX, 2) + Math.pow(actualY, 2));
	            		player.velocity.angle = Math.atan2(actualY, actualX) * 180 / Math.PI;
            		}
            		
            	break;
            	case OIL:
            		//nem változik a sebesség
            	break;
            	case STICK:
            		/*
            		 * A sebesség a fele az eddiginek
            		 */
            		player.velocity.size /= 2;
            		int pos[] = {
            			level.playerPositions.get(player).x, 
            			level.playerPositions.get(player).y
            		};
            		level.fields[pos[0]][pos[1]].remainingRounds--;
            	break;
            	default:
            		// Nem lehetséges
            	break;
                }
            	
            	/*
            	 * Kiszámoljuk a robot új helyeztét
            	 */
            	
            	int actualX = level.playerPositions.get(player).x + (int) Math.round(player.velocity.size*Math.cos(player.velocity.angle * Math.PI / 180));
            	int actualY = level.playerPositions.get(player).y - (int) Math.round(player.velocity.size*Math.sin(player.velocity.angle * Math.PI / 180));
            	
            	/*
            	 * Ha kiesik, akkor nullra állítjuk a listában
            	 * és belerakjuk a kiesettek listájába
            	 */
            	
            	if((actualX < 0) || (actualX >= level.fields[0].length)
            	|| (actualY < 0) || (actualY >= level.fields.length)
            	|| (level.fields[actualY][actualX].fieldType == Field.Type.HOLE)){
            		deletePlayer(player);
            		player = null;
            	}else{
            		//Hozzáadjuk az új pontszámot az eddigihez
            		int newScore = Math.abs(actualX-level.playerPositions.get(player).x)
            				     + Math.abs(actualY-level.playerPositions.get(player).y);
            		playerScores.put(player, newScore);

            		player.addScore(newScore);
            		
            		level.playerPositions.put(player, new Position(actualX, actualY));
            	}
            	
            	if(player != null){
            		//Ha a játékos null, akkor a játékos kiesett
            		//egyébként kirjzoljuk az új pálya állapotot
            		player.displayLevel(level);
    			}
            	
            }
            
            /*
             * Csökkentjük az időt, amíg fenn vannak a speciális elemek
             * Ha elfogyott az idő az előző körben, akkor levesszük
             */
            
            for(int i = 0; i < level.fields.length; i++){
        		for(int j = 0; j < level.fields[i].length; j++){
        			if(level.fields[i][j].remainingRounds > 0){
        					if(level.fields[i][j].fieldType == Field.Type.OIL){
        						level.fields[i][j].remainingRounds--;
        					}
        			}
        			else{
        				if(level.fields[i][j].fieldType == Field.Type.OIL || level.fields[i][j].fieldType == Field.Type.STICK){
        					level.fields[i][j].fieldType = Field.Type.FREE;
        				}
        			}
        		}
        	}
            
            // Csökkentjük a hátralévő körök számát
            remainingRounds--;
        	
            //Ha lejárt az idő vagy már csak egy játékos van, akkor kilépünk
        	if(remainingRounds <= 0 || outPlayers.size() >= players.size()-1){
        		return false;
        	}
        	return true;
        	
    }
}
