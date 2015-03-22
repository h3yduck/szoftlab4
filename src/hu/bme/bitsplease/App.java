package hu.bme.bitsplease;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import hu.bme.bitsplease.gameEngine.GameEngine;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.stepHandler.Step;

public class App {
	public static BufferedReader br;
    static GameEngine gameEngine;
    public static int menuItem;
    public static List<Integer> sorszam = new ArrayList<Integer>();

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        LevelLoader levelLoader;
        if (args.length > 0){
            levelLoader = new FileLoader(args[0]);
            gameEngine = new GameEngine(levelLoader);
        }
        else{
        	gameEngine = new GameEngine();
        }
//        gameEngine.startGame();
        menu();
        br.close();
    }
    
    public static int getNumOfPlayer() {
        printList("[:App]getNumOfPlayer");
    	boolean goodInput = false;
    	int numOfPlayers = 0;
    	while(!goodInput){
            printTabs();
	    	System.out.print("Játékosok száma? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		numOfPlayers = Integer.parseInt(line);
                if(numOfPlayers < 2 || numOfPlayers > 4)
                    throw new NumberFormatException();
	    		
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
                printTabs();
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    	}
	    	catch(Exception e){
                printTabs();
	    		System.out.println(e.getMessage());
	    	}
    	}
    	return numOfPlayers;
    }
    
    public static String getRobotName(){
    	printList("[:App]getRobotName");
    	String robotName = "";
    	try{
    		App.printTabs();
    		System.out.print("Játékos neve? ");
    		robotName = br.readLine();	
    	}
    	catch(Exception e){}
    	return robotName;
    }
    
    public static int getGameLength() {
        printList("[:App]getGameLength");
    	boolean goodInput = false;
    	int gameLength = 0;
    	while(!goodInput){
            printTabs();
	    	System.out.print("Milyen hosszú legyen a játék? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		gameLength = Integer.parseInt(line);
	    		if(gameLength > 50 || gameLength < 10){
	    			throw new NumberFormatException();
	    		}
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
                printTabs();
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    	}
	    	catch(Exception e){
                printTabs();
	    		System.out.println(e.getMessage());
	    	}
    	}
    	return gameLength;
    }
    
    public static int getSpecialActionTypeNumber() {
    	printList("[:App]getSpecialActionTypeNumber");
    	boolean goodInput = false;
    	int specialActionTypeNumber = 0;
    	while(!goodInput){
            printTabs();
	    	System.out.print("Mennyi legyen a speciális elem készlet? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		specialActionTypeNumber = Integer.parseInt(line);
	    		if(specialActionTypeNumber < 1 || specialActionTypeNumber > 5){
	    			throw new NumberFormatException();
	    		}
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
                printTabs();
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    	}
	    	catch(Exception e){
                printTabs();
	    		System.out.println(e.getMessage());
	    	}
    	}
    	return specialActionTypeNumber;
    }

	public static String getLevel() {
		printList("[:App]getLevel");
		String levelName = "";
		try{
			boolean goodInput = false;
			while(!goodInput){
				goodInput = true;
                printTabs();
				System.out.print("Melyik pályán akarunk játszani?");
				levelName = br.readLine();
				if(!(levelName.equals("1") || levelName.equals("2"))){
					goodInput = false;
                    printTabs();
					System.out.println("A megadott bemenet nem megfelelő!");
				}
			}
        }
		catch(Exception e){}
		return levelName;
	}

	public static void displayLevel(Level actualLevelState) {
		if(menuItem != 1){
			for(int i = 0; i < actualLevelState.fields.length; i++){
				for(int j = 0; j < actualLevelState.fields[i].length; j++){
					System.out.print(actualLevelState.fields[i][j].fieldType.key);
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	public static Step getStep(String name) {
		printList("[:App]getStep");
		Step actualStep =  new Step();
		boolean goodInput = false;
    	int specialActionTypeNumber = 0;
    	/*while(!goodInput){
            printTabs();
	    	System.out.print("A lépés szöge? ");
	    	goodInput = true;
	    	try{
	    		String line = br.readLine();
	    		specialActionTypeNumber = Integer.parseInt(line);
	    		
	    	}
	    	catch(NumberFormatException e){
	    		goodInput = false;
                printTabs();
	    		System.out.println("A megadott bemenet nem megfelelő!");
	    	}
	    	catch(Exception e){
                printTabs();
	    		System.out.println(e.getMessage());
	    	}
    	}*/
		actualStep.angle = -1;
		return actualStep;
	}

    public static void printTabs(){
        for (int j = 0; j < sorszam.size()-1; ++j) System.out.print("\t");
    }

    public static void printList(String nev){
        for (int j = 0; j < sorszam.size()-1; ++j) System.out.print("\t");
        for(int i = 0; i < sorszam.size(); ++i) {
            System.out.print(sorszam.get(i) + ".");
        }
        System.out.println(" " + nev + " ");
    }

    public static void newToList(){
        sorszam.add(1);
    }

    public static void incrementList(){
        sorszam.set(sorszam.size() - 1, sorszam.get(sorszam.size() - 1) + 1);
    }

    public static void removeList(){
        sorszam.remove(sorszam.size()-1);
    }

    public static void menu(){
        System.out.println("1. Játék indítása\n" +
                "2. Lépés\n" +
                "3. Speciális elemre lépés\n" +
                "4. Eltűnik a speciális\n" +
                "5. Játék vége\n" +
                "6. Kilépés");
        String line;
        try {
            while (!(line = br.readLine()).equals("6")) {
                int lineint = 0;
                try{
                    lineint = Integer.parseInt(line);
                    if(lineint < 1 || lineint > 6)
                        throw  new NumberFormatException();
                }
                catch(NumberFormatException e){
                    System.out.println("Rossz menü kód!");
                    continue;
                }
                menuItem = lineint;
                sorszam.add(menuItem);
                try {
                    gameEngine.startGame();
                    System.out.println("1. Játék indítása\n" +
                            "2. Lépés\n" +
                            "3. Speciális elemre lépés\n" +
                            "4. Eltűnik a speciális\n" +
                            "5. Játék vége\n" +
                            "6. Kilépés");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
