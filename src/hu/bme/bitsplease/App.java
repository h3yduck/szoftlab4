package hu.bme.bitsplease;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import hu.bme.bitsplease.gameEngine.GameEngine;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.stepHandler.Step;

public class App {
	public static BufferedReader br;

    public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
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
        br.close();
    }
    
    public static int getNumOfPlayer() {
    	boolean goodInput = false;
    	int numOfPlayers = 0;
    	while(!goodInput){
	    	System.out.print("1.3 Játékosok száma? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		System.out.println();
	    		numOfPlayers = Integer.parseInt(line);
	    		
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    		System.out.println();
	    	}
	    	catch(Exception e){
	    		System.out.println(e.getMessage());
	    	}
    	}
    	return numOfPlayers;
    }
    
    public static String getRobotName(){
    	System.out.print("játékos neve? ");
    	String robotName = "";
    	try{
    		System.out.print("játékos neve? ");
    		robotName = br.readLine();	
    	}
    	catch(Exception e){}
    	return robotName;
    }
    
    public static int getGameLength() {
    	boolean goodInput = false;
    	int gameLength = 0;
    	while(!goodInput){
	    	System.out.print("1.5 Milyen hosszú legyen a játék? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		System.out.println();
	    		gameLength = Integer.parseInt(line);
	    		if(gameLength > 50 && gameLength < 10){
	    			throw new NumberFormatException();
	    		}
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    		System.out.println();
	    	}
	    	catch(Exception e){
	    		System.out.println(e.getMessage());
	    	}
    	}
    	return gameLength;
    }
    
    public static int getSpecialActionTypeNumber() {
    	boolean goodInput = false;
    	int specialActionTypeNumber = 0;
    	while(!goodInput){
	    	System.out.print("1.2 Mennyi legyen a speciális elem készlet? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		System.out.println();
	    		specialActionTypeNumber = Integer.parseInt(line);
	    		if(specialActionTypeNumber < 1 && specialActionTypeNumber > 5){
	    			throw new NumberFormatException();
	    		}
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    		System.out.println();
	    	}
	    	catch(Exception e){
	    		System.out.println(e.getMessage());
	    	}
    	}
    	return specialActionTypeNumber;
    }

	public static String getLevel() {
		String levelName = "";
		try{
			boolean goodInput = false;
			while(!goodInput){
				goodInput = true;
				System.out.print("1.1 Melyik pályán akarunk játszani?");
				levelName = br.readLine();
				if(!(levelName.equals("1") && levelName.equals("2"))){
					goodInput = false;
					System.out.println("A megadott bemenet nem megfelelő!");
					System.out.println();
				}
			}
		}
		catch(Exception e){}
		return levelName;
	}

	public static void displayLevel(Level actualLevelState) {
		for(int i = 0; i < actualLevelState.fields.length; i++){
			for(int j = 0; j < actualLevelState.fields[i].length; j++){
				System.out.print(actualLevelState.fields[i][j].fieldType.key);
			}
			System.out.println();
		}
		System.out.println();
	}

	public static Step getStep(String name) {
		Step actualStep =  new Step();
		boolean goodInput = false;
    	int specialActionTypeNumber = 0;
    	while(!goodInput){
	    	System.out.print("A lépés szöge? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		System.out.println();
	    		specialActionTypeNumber = Integer.parseInt(line);
	    		
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    		System.out.println();
	    	}
	    	catch(Exception e){
	    		System.out.println(e.getMessage());
	    	}
    	}
		actualStep.angle = 0;
		return actualStep;
	}
}
