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
	//A pályabetöltő objektum
    private LevelLoader levelLoader; 
    private Level level;
    //A játékban hátramaradt körök száma
    private int remainingRounds;

    private List<Player> outPlayers; //Kisesett játékosok
    private List<Player> players; // A játékosok listája
    //ha egy játékos kiesett, akkor belekerül az outPlayers listába, és az értéke null lesz a players listában
    private Map<Player, Integer> playerScores; // A játékosok pontjai
    
    public Level getLevel(){
    	return level;
    }
    
    private void deletePlayer(Player player){
    	if(App.menuItem == 2)
    		App.printList("[:GameEngine]deletePlayer");
    	outPlayers.add(player);
		players.set(players.indexOf(player), null);
    }
    
    public Player getPlayer(){
    	//Visszaadja az első játékost, hogy kívülről tudjuk vizsgálni a szkeletonban
    	return players.get(0);
    }
    
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
    	//kiírja a függvényhierarchiát
        App.printList("[:GameEngine]startGame");
        //switch-case az egyes menüpontokhoz, hogy mit csináljon a gameEngine
        switch(App.menuItem){
        case 1:
        	/*
        	 * Bekéri a játék elkezdéséhez szükséges adatokat
        	 */
        	App.newToList();
        	getSettings();
        	break;
        case 2:
        	//Beállít megfelelő értékeket, majd elindítja a játékot, melyben egy lépést mutat be
        	levelLoader = new FileLoader("1");
        	players.add(new Player(null, null, "TestPlayer"));
        	players.get(0).actionNums.put(ActionType.OIL, 3);
            players.get(0).actionNums.put(ActionType.STICK, 3);
            remainingRounds = 1;
            level = levelLoader.getLevel();
            level.playerPositions.put(players.get(0), new Position(0, 0));
            for(int i = 0; i < level.fields.length; i++){
                for(int j = 0; j < level.fields[i].length; j++){
                    if(level.fields[i][j].fieldType == Field.Type.USRPOS){
                        level.fields[i][j].fieldType = Field.Type.FREE;
                    }
                }
            }
            App.newToList();
        	play();
        	App.removeList();
            break;
        case 3:
        	//Beállít megfelelő értékeket, majd elindítja a játékot, melyben ellép egy speciális elemről
        	levelLoader = new FileLoader("2");
        	players.add(new Player(null, null, "TestPlayer"));
        	players.get(0).actionNums.put(ActionType.OIL, 3);
            players.get(0).actionNums.put(ActionType.STICK, 3);
            remainingRounds = 1;
            level = levelLoader.getLevel();
            level.playerPositions.put(players.get(0), new Position(4, 4));
            for(int i = 0; i < level.fields.length; i++){
                for(int j = 0; j < level.fields[i].length; j++){
                    if(level.fields[i][j].fieldType == Field.Type.USRPOS){
                        level.fields[i][j].fieldType = Field.Type.FREE;
                    }
                }
            }
            App.fromSpecItem(players.get(0), level);
            App.newToList();
        	play();
        	App.removeList();
        	break;
        case 4:
        	//Beállít egy speciális elemet a 0,0 pozícióra, majd elindítja a játékot
        	//Látható, hpgy eltűnik a speciális elemm
        	levelLoader = new FileLoader("1");
            remainingRounds = 4;
            players.add(null);
            players.add(null);
            level = levelLoader.getLevel();
            for(int i = 0; i < level.fields.length; i++){
                for(int j = 0; j < level.fields[i].length; j++){
                    if(level.fields[i][j].fieldType == Field.Type.USRPOS){
                        level.fields[i][j].fieldType = Field.Type.FREE;
                    }
                }
            }
            App.getSpecialElement(level);
            App.newToList();
        	play();
        	App.removeList();
        	break;
        case 5:
        	//Bekéri a játékosok adatait, majd kiírj az eredményeket
        	App.newToList();
        	getSettings();
        	App.incrementList();
        	App.displayScore(playerScores);
        	App.incrementList();
        	App.displayCongrat(players.get(0).name);
        	App.removeList();
        	break;
        }
    }
    
    public void getSettings() throws Exception {
    	//Kiírja a függvényhierarchiát
        App.printList("[:GameEngine]" + "getSettings");
        
        if(levelLoader == null){
            //Ha nincs betöltött pálya, akkor lekérdezzük a felhasználótól
        	if(App.menuItem == 1)
        		//Csak ha az egyes menüpont van kiválasztva
        		App.newToList();
            String levelName = App.getLevel();
            levelLoader = new FileLoader(levelName);

        }
        
        if(App.menuItem == 1)
        	//Csak ha az egyes menüpont van kiválasztva
        	App.incrementList();
        level = levelLoader.getLevel();

        //specialis elemek szamanak lekerdezese
        if(App.menuItem == 1)
        	//Csak ha az egyes menüpont van kiválasztva
        	App.incrementList();
        int specialTypeNum = App.getSpecialActionTypeNumber();
        
        //specialis elemek szamanak beallitasa minden jatekos reszere oil és stickre is
        for(int i = 0; i < players.size(); i++) {
            players.get(i).actionNums.put(ActionType.OIL, specialTypeNum);
            players.get(i).actionNums.put(ActionType.STICK, specialTypeNum);
        }



    	// elso kepernyo, jatekosok szamanak stb... bekerese
        //jatekosok szamanak lekerdezese
        if(App.menuItem == 1)
        	//Csak ha az egyes menüpont van kiválasztva
        	App.incrementList();
        else
        	//Egyébként ez az első
        	App.newToList();
        int numOfPlayers = App.getNumOfPlayer();
        
        //jatekosok hozzaadas a listahoz, sajat stephandlerrel,displayhandlerrel, konzolbol megadott nevvel
        App.printTabs();
        System.out.println("Játékosok nevei?");
        App.incrementList();
        for(int i = 0; i < numOfPlayers; i++) {
        	//jatekos nevenek lekerdezese
        	boolean goodInput = false;
        	String name = "";
        	while(!goodInput){
        		goodInput = true;
        		name = App.getRobotName();
        		if(name.length() > 10 && name.length() < 1){
        			goodInput = false;
        		}
        	}
        	Player player = new Player(null,null, name);
            players.add(player);
            
            //jatekos pontszamanak beallitasa
            if(App.menuItem != 5){
            	playerScores.put(player, 0);
            }
            else{
            	//Az 5-ös pontban mi adjuk meg a számokat
            	playerScores.put(player, 100 - 10 * i);
            }
        }
        
	    //kezdeti korok szamanak lekerdezese
        if(App.menuItem == 1)
        	//Csak ha az egyes menüpont van kiválasztva
        	App.incrementList();
        remainingRounds = App.getGameLength();

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
        App.removeList();
    }

    public void play() {
    	App.printList("[:GameEngine]play");
    	/*
         * tenyleges jatek mechanika
         * minden lepes vegen meg kell hivni minden jatekos DisplayHandleret, és kirajzolni a pályát
         * minden lepeskor az aktualis jatekos StepHandleret, és bekérni a lépést
         * frissiteni kell a jatekosok pontjait
    	 */
    	
    	Boolean EndOfTheGame = false;
    	
        /*
         *  A játék addig fut, amíg az EndOfTheGame változó false
         */
    	App.newToList();
        while(!EndOfTheGame){

            /*
             *  Minden játékostól megkérdezzük mit lép, és változtatjuk eszerint az állapotát
             */

            for(Player player : players){
            	// Ha a player null, akkor a játékos már kiesett
            	if(player == null){
            		continue;
            	}
            	// Megjelenítjük a kezdeti pályát
            	App.displayLevel(level);
            	
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
            			
            			goodStep = true;
            			
	            		actualStep = player.getStep();
	            		
	            		/*
	            		 * Ellenőrizzük, hogy rakott e le valamit, és hogy van e rá kapacitása
	            		 * Ha van, akkor lerakjuk a mezőre és csökkentjük a kapacitást
	            		 */

                        if(!(actualStep.stepAction == null)){
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
            		if(App.menuItem == 2)
            			App.incrementList();
            		deletePlayer(player);
            		player = null;
            	}else{
            		//Hozzáadjuk az új pontszámot az eddigihez
            		int newScore = Math.abs(actualX-level.playerPositions.get(player).x)
            				     + Math.abs(actualY-level.playerPositions.get(player).y);
            		playerScores.put(player, newScore);
            		
            		App.incrementList();
            		player.addScore(newScore);
            		
            		level.playerPositions.put(player, new Position(actualX, actualY));
            	}
            	
            	App.incrementList();
            	if(player == null){
            		//Ha a játékos null, akkor a játékos kiesett
            		System.out.println();
            		App.printTabs();
    				System.out.println("A játékos kiesett!");
    				System.out.println();
    				App.displayLevel(level);
    			}else{
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
        				level.fields[i][j].remainingRounds--;
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
        		EndOfTheGame = true;
        	}
        	
        	//A 4-es menüpontban azt nézzük, hogy hogyan tűnik el egy folt
        	if(App.menuItem == 4){
        		App.printTabs();
            	System.out.println("A hátraléve körök, amíg ott lesz a folt: " + (level.fields[0][0].remainingRounds));
            	App.displayLevel(level);
            }
        	
        	App.incrementList();
        	
        }
        
        App.removeList();
    }
}
