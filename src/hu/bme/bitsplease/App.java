package hu.bme.bitsplease;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.bme.bitsplease.gameEngine.GameEngine;
import hu.bme.bitsplease.levelHandler.*;
import hu.bme.bitsplease.playerHandler.Player;
import hu.bme.bitsplease.stepHandler.Step;

public class App {
	
	public static BufferedReader br;
	
    static GameEngine gameEngine;
    
    //Ez a változó tárolja, hogy melyik menüpontot választottuk
    public static int menuItem;
    
    //Ebben a tömbben vannak a kiíráshoz szükséges sorszámok
    public static List<Integer> sorszam = new ArrayList<Integer>();

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        
        /*
         * Beállítjuk a GameEngine-t és megnyitjuk a menüt
         */
        
        gameEngine = new GameEngine();
        menu();
        br.close();
    }
    
    public static int getNumOfPlayer() {
    	//Kiírjuk a függvény hierarchiában
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
    	//Kiírjuk a függvény hierarchiában
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
    	if(menuItem == 5){
    		//ha az 5-ös menuItem van, akkor nem kérjük be, mert nem szükséges a feladathoz
    		return 0;
    	}
    	//Kiírjuk a függvény hierarchiában
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
    	if(menuItem == 5){
    		//ha az 5-ös menuItem van, akkor nem kérjük be, mert nem szükséges a feladathoz
    		return 0;
    	}
    	//Kiírjuk a függvény hierarchiában
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
		//ha az 5-ös menuItem van, akkor nem kérjük be, mert nem szükséges a feladathoz
		if(menuItem == 5){
			return "1";
		}
		//Kiírjuk a függvény hierarchiában
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
		//Az első menüpontban nem rajzoljuk ki a pályát
		if(menuItem != 1){
			//Beállítunk nem lehetséges player pozíciókat
			int x = -1;
			int y = -1;
			if(gameEngine.getPlayer() != null){
				//Ha nem null a player, akkor lekérjük a beállított értéket
				x = gameEngine.getLevel().playerPositions.get(gameEngine.getPlayer()).x;
				y = gameEngine.getLevel().playerPositions.get(gameEngine.getPlayer()).y;
			}
			//kiírjuk a megfelelő karaktereket egy mátrixban
			for(int i = 0; i < actualLevelState.fields.length; i++){
				printTabs();
				for(int j = 0; j < actualLevelState.fields[i].length; j++){
					if(j == x && i == y)
						System.out.print("*");
					else
						System.out.print(actualLevelState.fields[i][j].fieldType.key);
				}
				System.out.println();
			}
			
		}
	}

	public static Step getStep(String name) {
		//Kiírjuk a függvényhierarchiát
		printList("[:App]getStep");
		Step actualStep =  new Step();
		boolean goodInput = false;
    	while(!goodInput){
	    	goodInput = true;
	    	try{
	    		printTabs();
	    		System.out.print("A lépés szöge? ");
	    		String line = br.readLine();
	    		actualStep.angle = Integer.parseInt(line);
	    		if(actualStep.angle < -1 || actualStep.angle > 360)
	    			throw new NumberFormatException();
	    		printTabs();
	    		System.out.print("Olajfolt vagy ragacs? ");
	    		line = br.readLine();
	    		if(line.equals("olajfolt")){
	    			actualStep.stepAction = Step.ActionType.OIL;
	    		}else if(line.equals("ragacs")){
	    			actualStep.stepAction = Step.ActionType.STICK;
	    		}else if(line.equals("semmi")){
	    			actualStep.stepAction = null;
	    		}else{
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
		return actualStep;
	}

    public static void printTabs(){
    	//Kiír megfelelő mennyiségű tabot
        for (int j = 0; j < sorszam.size()-1; ++j) System.out.print("\t");
    }

    public static void printList(String nev){
    	//Kiír megfelelő mennyiségű tabot, a sorszámot, majd a függvény nevét
        for (int j = 0; j < sorszam.size()-1; ++j) System.out.print("\t");
        for(int i = 0; i < sorszam.size(); ++i) {
            System.out.print(sorszam.get(i) + ".");
        }
        System.out.println(" " + nev + " ");
    }

    public static void newToList(){
    	//új sorszám elem a tömbbe
        sorszam.add(1);
    }

    public static void incrementList(){
    	//Növeljük a tömb utolsó elemét
        sorszam.set(sorszam.size() - 1, sorszam.get(sorszam.size() - 1) + 1);
    }

    public static void removeList(){
    	//Kivesszük a tömb utolsó elemét
        sorszam.remove(sorszam.size()-1);
    }

    public static void fromSpecItem(Player player, Level level){
    	//Bekérjük a speciális elemről elmozduláshoz szükséges adatokat
    	String line;
    	boolean goodInput = false;
    	while(!goodInput){
    		goodInput = true;
	    	try{
		    	printTabs();
				System.out.print("Olajfolt vagy ragacs? ");
				line = br.readLine();
				if(line.equals("olajfolt")){
					level.fields[4][4].fieldType = Field.Type.OIL;
				}else if(line.equals("ragacs")){
					level.fields[4][4].fieldType = Field.Type.STICK;
				}else{
					throw new NumberFormatException();
				}
				printTabs();
				System.out.print("Sebesség vektor szöge? ");
				line = br.readLine();
				player.velocity.angle = Integer.parseInt(line);
				if(player.velocity.angle > 360 || player.velocity.angle < 0)
					throw new NumberFormatException();
				printTabs();
				System.out.print("Sebesség vektor nagysága? ");
				line = br.readLine();
				player.velocity.size = Integer.parseInt(line);
				if(player.velocity.size > 10 || player.velocity.size < 5)
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
    }
    
    public static void getSpecialElement(Level level){
    	//Bekérjük, hogy milyen speciális elemet akarjuk, hogy eltűnjön
    	String line;
    	boolean goodInput = false;
    	while(!goodInput){
    		goodInput = true;
	    	try{
		    	printTabs();
				System.out.print("Olajfolt vagy ragacs? ");
				line = br.readLine();
				if(line.equals("olajfolt")){
					level.fields[0][0].fieldType = Field.Type.OIL;
					level.fields[0][0].remainingRounds = 3;
				}else if(line.equals("ragacs")){
					level.fields[0][0].fieldType = Field.Type.STICK;
					level.fields[0][0].remainingRounds = 3;
				}else{
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
    }
    
    public static void displayCongrat(String player){
    	//Kiírja a függvény hierarchiát, majd kiírja a gratulációs szöveget
    	printList("[:App]displayCongrat");
    	printTabs();
    	System.out.println("Gratulálok " + player);
    	System.out.println();
    }
    
    public static void displayScore(Map<Player, Integer> score){
    	//Kiírja a függvény hierarchiát, majd kiírja a pontszámokat
    	printList("[:App]displayScore");
    	for(Player p : score.keySet()){
    		printTabs();
    		System.out.println(p.name + " - " + score.get(p));
    	}
    }
    
    public static void menu(){
    	//Kiírjuk a menüpontokat
        System.out.println("\n1. Játék indítása\n" +
                "2. Lépés\n" +
                "3. Speciális elemre lépés\n" +
                "4. Eltűnik a speciális\n" +
                "5. Játék vége\n" +
                "6. Kilépés");
        String line;
        try {
            while (!(line = br.readLine()).equals("6")) {
            	//Amíg a választott sorszám nem 6-os
                int lineint = 0;
                try{
                	//Leellenőrizzük a bemenetet
                    lineint = Integer.parseInt(line);
                    if(lineint < 1 || lineint > 6)
                        throw  new NumberFormatException();
                }
                catch(NumberFormatException e){
                    System.out.println("Rossz menü kód!");
                    continue;
                }
                //Beállítjuk a menüItem-et a választott menüpontra
                menuItem = lineint;
                //Hozzáadjuk a sorszám listához
                sorszam.add(menuItem);
                try {
                	//elindítjuk a játékots
                	gameEngine = new GameEngine();
                	gameEngine.startGame();
                	//Ha vége a játéknak újra kiírjuk a menüt és kérünk be sorszámot
                	sorszam.clear();
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
