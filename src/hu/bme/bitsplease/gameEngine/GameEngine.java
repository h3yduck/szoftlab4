package hu.bme.bitsplease.gameEngine;

import hu.bme.bitsplease.TestApp;
import hu.bme.bitsplease.displayHandler.GUIDisplay;
import hu.bme.bitsplease.stepHandler.ConsoleInput;
import hu.bme.bitsplease.stepHandler.GUIInput;
import hu.bme.bitsplease.stepHandler.InputHandler;
import hu.bme.bitsplease.stepHandler.Step;
import hu.bme.bitsplease.stepHandler.Step.ActionType;
import hu.bme.bitsplease.displayHandler.ConsoleDisplay;
import hu.bme.bitsplease.displayHandler.DisplayHandler;
import hu.bme.bitsplease.levelHandler.FileLoader;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.LevelLoader;
import hu.bme.bitsplease.levelHandler.Position;
import hu.bme.bitsplease.playerHandler.LittleRobot;
import hu.bme.bitsplease.playerHandler.Player;
import hu.bme.bitsplease.playerHandler.Robot;
import hu.bme.bitsplease.levelHandler.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Created by h3yduck on 2/27/15.
 */
public class GameEngine {

	// A pályabetöltő objektum
	public LevelLoader levelLoader = null;
	public Level level = null;

	// A játékban hátramaradt körök száma
	public int remainingRounds;

	// Eredeti körök száma
	public int originalRounds;

	public List<Player> outPlayers; // Kiesett játékosok
	public List<Player> players; // A játékosok listája

	public GUIInput input;
	public GUIDisplay display;

	// Kis tisztító robotok
	public List<LittleRobot> littleRobots;

	// ha egy játékos kiesett, akkor belekerül az outPlayers listába, és az
	// értéke null lesz a players listában
	public Map<Player, Integer> playerScores; // A játékosok pontjai

	// ha egy játékos kiesett,akkor ez a függvény végzi el a játékos törlését a
	// pályáról, stb.
	public void deletePlayer(Player player) {
		if(player != null){
			outPlayers.add(player);
			players.set(players.indexOf(player), null);
			level.playerPositions.remove(player);
		}
	}

	// GameEngine paraméter nélküli konstruktora
	public GameEngine() {
		players = new ArrayList<Player>();
		outPlayers = new ArrayList<Player>();
		littleRobots = new ArrayList<LittleRobot>();
		playerScores = new HashMap<Player, Integer>();
		remainingRounds = 10;
		originalRounds = remainingRounds;
	}

	public void endGame(){
		Player maxScorePlayer = null;
		int maxScore = -1;
		// legtöbb pontot szerző játékos meghatározása

		// még játékban lévő játékosok vizsgálata
		for (Player player : players) {
			if (player != null && playerScores.get(player) > maxScore) {
				maxScorePlayer = player;
				maxScore = playerScores.get(maxScorePlayer);
			}
		}
		// a már kiesett játékosok vizsgálata
		for (Player player : outPlayers) {
			if (playerScores.get(player) > maxScore) {
				maxScorePlayer = player;
				maxScore = playerScores.get(maxScorePlayer);
			}
		}

		// Gratuláló üzenet kiírása a győztes játékos nevével
		display.displayCongrat(maxScorePlayer.name);
	}

	// Normál mód: command == null
	// Parancs-mód: command != null

	public boolean getSettings(String command) {

		String[] commandArray = { "" };
		// a parancs széttördelése a szóközök mentén
		if (command != null) {
			commandArray = command.split(" ");
		}

		String levelName = null;

		// Normál módban ha a beállítások sikertelenek voltak, akkor a tömböket kiűríti
		if (command == null) {
			players.clear();
			outPlayers.clear();
			littleRobots.clear();
			playerScores.clear();
		}

		/*
		 * Minden parancshoz tartozik egy normál működés esetén lezajló esemény.
		 * Parancs-mód esetén: command != null Normál mód esetén: command == null
		 */

		// Pálya betöltése Normál mód esetén
		if (command == null) {
			level = null;
			levelName = input.getLevel();
			System.out.println(levelName);
			levelLoader = new FileLoader(levelName);
			level = levelLoader.getLevel();
			if(level == null) return false;
		}

		// Pálya betöltése Parancs-mód esetén a (setMap "fájlnév") paranccsal
		if (commandArray[0].equals("setMap") && commandArray.length > 1) {
			levelName = commandArray[1];
			levelLoader = new FileLoader(levelName);
			level = levelLoader.getLevel();
			players.clear();
			littleRobots.clear();
			outPlayers.clear();
		}
		
		// Ha nem sikerült beállítani, akkor beállítunk egy alapértelmezett pályát
		// a teszteléshez
		if (command != null && level == null) {
			level = new Level();
			level.fields = new Field[100][100];
						
			for(int i = 0; i < 100; i++){
				for(int j = 0; j < 100; j++){
					level.fields[i][j] = new Field(Field.Type.FREE, 0);
				}
			}
						
			level.fields[20][45].fieldType = Field.Type.USRPOS;
			level.fields[32][15].fieldType = Field.Type.USRPOS;
			level.fields[45][75].fieldType = Field.Type.USRPOS;
			level.fields[80][20].fieldType = Field.Type.USRPOS;
			level.fields[62][10].fieldType = Field.Type.USRPOS;
			level.fields[49][35].fieldType = Field.Type.USRPOS;
			level.fields[95][2].fieldType = Field.Type.USRPOS;
			level.fields[2][80].fieldType = Field.Type.USRPOS;
			level.fields[58][50].fieldType = Field.Type.USRPOS;
			level.fields[45][20].fieldType = Field.Type.USRPOS;
						
			level.playerPositions = new HashMap<Robot, Position>();
		}

		int specialTypeNum = 0;
		int numOfPlayers = 0;

		// speciális elemek és játékosok számának lekérdezése Normál mód esetén
		if (command == null) {
			specialTypeNum = input.getSpecialActionTypeNumber();
			numOfPlayers = input.getNumOfPlayer();
		}

		// játékosok számának beállítása a (setPlayers "szám") paranccsal
		if (commandArray[0].equals("setPlayers") && commandArray.length > 1) {
			try {
				numOfPlayers = Integer.parseInt(commandArray[1]);
				if(numOfPlayers > 10){
					throw new NumberFormatException();
				}
			} catch (NumberFormatException ex) {
				numOfPlayers = 0;
				System.err.println("0-nál nagyobb, 10-nél kisebb egész számot adj meg paraméterként!");
			}
		}

		// speciális elemek számának beállíta a (setSpecialActionNumber "szám") paranccsal
		if (commandArray[0].equals("setSpecialActionNumber") && commandArray.length > 2) {
			try {
				specialTypeNum = Integer.parseInt(commandArray[1]);
				int index = Integer.parseInt(commandArray[2]);
				if(players != null && index >= 0 && index < players.size()){
					players.get(index).actionNums.put(ActionType.OIL, specialTypeNum);
					players.get(index).actionNums.put(ActionType.STICK, specialTypeNum);
				}else{
					throw new NumberFormatException();
				}
			} catch (NumberFormatException ex) {
				specialTypeNum = 0;
				System.err.println("A megadott player index vagy foltszám nem megfelelő"
						+ "0-nál nagyobb egész számot adj meg a foltok számának!");
			}
		}

		// játékosok nevének megadása,ha nem adjuk meg, default: Player<i>, ahol i az index
		if (command == null || commandArray[0].equals("setPlayers")) {
			// Játékos adatok kitörlése új beállítása esetén
			players.clear();
			outPlayers.clear();
			
			if(level != null)
				level.playerPositions.clear();
			
			for (int i = 0; i < numOfPlayers; i++) {
				// jatekos nevenek lekerdezese
				String name = "Player" + (i + 1);
				if (command == null) {
					name = input.getRobotName();
				}

				Player player = new Player(input, display, name);
				players.add(player);
				playerScores.put(player, 0);
			}
		}
		
		if(commandArray[0].equals("setPlayerName") && commandArray.length > 2){
			/*
			 *  Beállítja a megadott játékos nevét
			 *  setPlayerName <név> <player index>
			 */
			
			try{
				int index = Integer.parseInt(commandArray[2]);
				// Ha az index megfelelő és nem null a players lista, beállítjuk a nevet
				if(players != null && index >= 0 && index < players.size()){
					players.get(index).name = commandArray[1];
				}else{
					throw new NumberFormatException();
				}
			}catch(NumberFormatException ex){
				System.err.println("Nem megfelelő a megadott index");
				return false;
			}
		}

		// specialis elemek számának beallitasa minden jatekos reszere oil és stickre is
		if (command == null) {
			for (Player player : players) {
				if (player != null) {
					player.actionNums.put(ActionType.OIL, specialTypeNum);
					player.actionNums.put(ActionType.STICK, specialTypeNum);
				}
			}
		}

		// Játék köreinek beállítása Normál mód esetén
		if (command == null) {
			remainingRounds = input.getGameLength();
			originalRounds = remainingRounds;
		}

		// Játék köreinek beállítása Parancs-mód esetén a (setRounds "szám")
		// paranccsal
		if (commandArray[0].equals("setRounds") && commandArray.length > 1) {
			try {
				remainingRounds = Integer.parseInt(commandArray[1]);
				if(remainingRounds < 0){
					throw new NumberFormatException();
				}
				originalRounds = remainingRounds;
			} catch (NumberFormatException ex) {
				// Ha nem megfelelő bemenet, akkor 
				remainingRounds = 0;
				originalRounds = remainingRounds;
				System.err.println("0-nál nagyobb egész számot adj meg paraméterként!");
			}
		}
		
		List<Position> positions = new ArrayList<Position>();
		//A pályát leíró objektum betöltése után, a játékosok 
		//kezdőpozíciójának meghatározása
		if (command == null || commandArray[0].equals("setMap")) {
			
			// USPROS mező = lehetséges kezdőpozíciók
			//positions lista feltöltése a lehetséges kezdőpozíciókkal
			for (int i = 0; i < level.fields.length; i++) {
				for (int j = 0; j < level.fields[i].length; j++) {
					if (level.fields[i][j].fieldType == Field.Type.USRPOS) {
						positions.add(new Position(j, i));
						// Az USRPOS mezőket átállítjuk FREE-re
						level.fields[i][j].fieldType = Field.Type.FREE;
					}
				}
			}
			
			//Ha több játékos van megadva, mint ahány mezője van a pályának,
			//akkor az eddigi beállítások érvénytelenek, újra kell futnia a getSettingsnek()
			if(command == null || TestApp.playerRandom){
				// Csak akkor, ha normál módú futás, vagy ha véletlenszerű a játékosok kiosztása
				
				if (positions.size() < players.size()) {
					display.displayError("Túl sok játékost adtál meg a kiválasztott pályához. "
							+ "Adj meg másik pályát vagy kevesebb játékost.");
					System.out.println("Túl sok játékost adtál meg a kiválasztott pályához.");
					Player.resetSNum();
					return true;
				}
			
				// A positions listában találhatő megfelelő kezdőpozíciók alapján
				// véletlenszerűen elhelyezzük a játékosokat
		
				for (Player player : players) {
					Random random = new Random();
					int rand = random.nextInt(positions.size());
					//játékos kezdőpozíciójának beállítása valamelyik lehetséges mezőre
					level.playerPositions.put(player, positions.get(rand));
					//a beállított pozíció törlése a lehetséges kezdőpozíciók közül,
					//mivel már foglalt
					positions.remove(rand);
				}
			}
		}

		//pálya megjelenítése Normál mód esetén
		if (command == null) {
			display.displayLevel(level);
		}
		
		//Parancs-mód esetén a (setPlayersPositionRandom "0/1") paranccsal megadható,
		//hogy a játékosok kezdőpozíciója random generálódjon, vagy manuálisan mi adjuk meg
		if (commandArray[0].equals("setPlayersPositionRandom")
				&& commandArray.length > 1) {
			try {
				//ha a parancs (setPlayersPositionRandom 0), akkor mi adjuk meg manuálisan
				if (Integer.parseInt(commandArray[1]) == 0) {
					TestApp.playerRandom = false;
				//ha a parancs (setPlayersPositionRandom 1), akkor random generálódik
				} else if (Integer.parseInt(commandArray[1]) == 1) {
					TestApp.playerRandom = true;
				} else {
					System.err.println("A parancs paramétere 0 vagy 1 legyen!");
				}
			} catch (NumberFormatException ex) {
				System.err.println("Hibás paraméterek az adott parancshoz!");
			}
		}
		
		//ha a parancs (setPlayerPosition "szám1" "szám2" "szám3") formájú,
		//akkor a (szám1, szám2) pozícióra beállítja a szám3 sorszámú játékost.
		if (commandArray[0].equals("setPlayerPosition") && commandArray.length > 3 && !TestApp.playerRandom) {
			try {
				//pozíció meghatározása szám1,szám2 paraméterek alapján
				Position pos = new Position(Integer.parseInt(commandArray[1]),
						Integer.parseInt(commandArray[2]));
				int index;
				//játékos sorszámának meghatározása szám3 paraméter alapján
				if ((index = Integer.parseInt(commandArray[3])) < players.size() && index >= 0 && level != null) {
					//megvizsgálja, hogy a megadott pozíció rajta van e a pályán
					if(pos.x < level.fields[0].length &&
							pos.x >= 0 &&
							pos.y < level.fields.length &&
							pos.y >= 0)
						level.playerPositions.put(players.get(index), pos);
					else{
						level.playerPositions.put(players.get(index), new Position(0, 0));
						System.err.println("Hibás pozíció! A megadott pozíció nem a pályán található");
					}
				}else
					System.err.println("A megadott sorszámú játékos nem létezik!");
			} catch (NumberFormatException ex) {
				System.err.println("Hibás paraméterek az adott parancshoz!");
			}
		}
		
		
		//Parancs-mód esetén a (setLittleRobotPositionRandom "0/1") paranccsal megadható,
		//hogy a kis robotok kezdőpozíciója random generálódjon, vagy manuálisan mi adjuk meg
		if (commandArray[0].equals("setLittleRobotPositionRandom") && commandArray.length > 1) {
			try {
				//ha a parancs (setLittleRobotPositionRandom 0), akkor manuálisan mi adjuk meg
				if (Integer.parseInt(commandArray[1]) == 0) {
					TestApp.littleRandom = false;
				//ha a parancs(setLittleRobotPositionRandom 1), akkor random generálódik
				} else if (Integer.parseInt(commandArray[1]) == 1) {
					TestApp.littleRandom = true;
				} else{
					System.err.println("0 vagy 1 paramétert adj meg a parancshoz!");
				}
			} catch (NumberFormatException ex) {
				System.err.println("Hibás paraméterek az adott parancshoz!");
			}
		}

		return false;
	}

	public boolean play(String command) {
		/*
		 * tényleges játek mechanika minden lépés végén meg kell hivni minden
		 * játékos DisplayHandleret, és kirajzolni a pályát minden lépéskor az
		 * aktuális játékos StepHandleret, és bekérni a lépést, frissiteni kell a
		 * jatekosok pontjait
		 */

		/*
		 * Minden játékostól megkérdezzük mit lép, és változtatjuk eszerint az
		 * állapotát
		 */

		String[] commandArray = { "" };
		//a parancs tördelése a szóközök mentén
		if (command != null) {
			commandArray = command.split(" ");
		}
		//(step "szám1" "O/S/F" "szám3") parancs megadása, vagy normál mód esetén, ha lépés történt
		//szám1= sebesség szöge
		//O/S/F=Oil, Stick vagy Free
		//szám3= játékos sorszáma
		if (command == null || commandArray[0].equals("step")) {
			for (Player player : players) {

				if (player == null) {
					continue;
				}

				display.displayRound(String.valueOf(remainingRounds));
				player.displaySpecialActionTypesNumber();
				display.displayPlayerName(player.name);
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (commandArray[0].equals("step")) {
					try {
						int index;
						//játékos sorszámának meghatározása
						if ((index = Integer.parseInt(commandArray[3])) < players
								.size() && index >= 0)  {
							player = players.get(index);
							if (player == null) {
								return false;
							}
						} else {
							return false;
						}
					} catch (NumberFormatException ex) {
						System.err.println("Hibás paraméterek az adott parancshoz!");
						return false;
					}
				}

				// Ha a player null, akkor a játékos már kiesett

				// Megnézzük, hogy milyen mezőn áll a robot
				// Ha FREE-n, akkor bekérjük a lépést
				switch (level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).x].fieldType) {
				case FREE:
					boolean goodStep = false;
					Step actualStep = null;

					while (!goodStep) {

						/*
						 * Ha nem megfelelő a lerakott elem, akkor újra
						 * kérdezzük a felhasználót
						 */

						goodStep = true;
						if (command == null) {
							player.displaySpecialActionTypesNumber();

							actualStep = player.getStep();
						} else {
							try {
								//Az új lépés összeállítása:
								actualStep = new Step();
								//szög meghatározása
								actualStep.angle = Integer.parseInt(commandArray[1]) % 360;
								//Oil vagy Stick lesz-e elhelyezve a hátrahagyott mezőn
								if(commandArray[2].equals("O") || commandArray[2].equals("S")) {
									actualStep.stepAction = Step.ActionType.fromChar(commandArray[2].charAt(0));
								}else if(!commandArray[2].equals("F")){
									System.err.println("Hibás paraméterek az adott parancshoz!");
									return false;
								}
							} catch (NumberFormatException ex) {
								System.err.println("Hibás paraméterek az adott parancshoz!");
								return false;
							}
						}

						/*
						 * Ellenőrizzük, hogy rakott e le valamit, és hogy van e
						 * rá kapacitása Ha van, akkor lerakjuk a mezőre és
						 * csökkentjük a kapacitást
						 */

						if (actualStep.stepAction != null) {
							if (player.actionNums.get(actualStep.stepAction) > 0) {
								if (actualStep.stepAction == Step.ActionType.OIL){
									level.fields[level.playerPositions
											.get(player).y][level.playerPositions
											.get(player).x].fieldType = Field.Type
											.fromChar('O');
									level.fields[level.playerPositions.get(player).y][level.playerPositions.get(player).y]
											.remainingRounds = 4;
								}
								else if (actualStep.stepAction == Step.ActionType.STICK){
									level.fields[level.playerPositions
											.get(player).y][level.playerPositions
											.get(player).x].fieldType = Field.Type
											.fromChar('S');
								level.fields[level.playerPositions.get(player).y][level.playerPositions
										.get(player).y].remainingRounds = 4;
								}
								player.actionNums
										.put(actualStep.stepAction,
												player.actionNums
														.get(actualStep.stepAction) - 1);
							} else {
								player.displayError("Nincs már több "
										+ actualStep.stepAction.toString()
										+ " a foltkészletben");
								goodStep = false;
							}
						}
					}

					/*
					 * Kiszámoljuk a Step-ből az új sebességet
					 */

					if (actualStep != null && actualStep.angle >= 0) {
						double actualX = player.velocity.size
								* Math.cos(player.velocity.angle * Math.PI
										/ 180)
								+ Math.cos(actualStep.angle * Math.PI / 180);
						double actualY = player.velocity.size
								* Math.sin(player.velocity.angle * Math.PI
										/ 180)
								+ Math.sin(actualStep.angle * Math.PI / 180);
						player.velocity.size = Math.sqrt(Math.pow(actualX, 2)
								+ Math.pow(actualY, 2));
						player.velocity.angle = Math.atan2(actualY, actualX)
								* 180 / Math.PI;
					}

					break;
				//ha olajon áll a robot
				case OIL:
					// nem változik a sebesség
					break;
				//ha ragacson áll a robot
				case STICK:
					/*
					 * A sebesség a fele az eddiginek
					 */
					player.velocity.size /= 2;
					int pos[] = { level.playerPositions.get(player).x,
							level.playerPositions.get(player).y };
					//a ragacs "koptatása"
					level.fields[pos[0]][pos[1]].remainingRounds--;
					break;
				default:
					// Nem lehetséges
					level.fields[level.playerPositions.get(player).y][level.playerPositions
							.get(player).x].fieldType = Field.Type.FREE;
					break;
				}

				/*
				 * Kiszámoljuk a robot új helyeztét
				 */

				int actualX = level.playerPositions.get(player).x
						+ (int) Math.round(player.velocity.size
								* Math.cos(player.velocity.angle * Math.PI
										/ 180));
				int actualY = level.playerPositions.get(player).y
						- (int) Math.round(player.velocity.size
								* Math.sin(player.velocity.angle * Math.PI
										/ 180));

				Player weakPlayer = null;
				
				Iterator<Entry<Robot, Position>> it = level.playerPositions.entrySet().iterator();
			    while (it.hasNext()) {
			    	Map.Entry<Robot, Position> i = (Entry<Robot, Position>)it.next();
					if(i.getValue().x == actualX && i.getValue().y == actualY && !i.getKey().equals(player)){
						//ha a robot olyan mezőre lép, ahol egy másik robot vagy kisrobot található,
						//akkor a mezőn olajfolt keletkezik
						level.fields[i.getValue().x][i.getValue().y].fieldType = Field.Type.OIL;
						level.fields[i.getValue().x][i.getValue().y].remainingRounds = 4;

						if(i.getKey().getClass().toString().equals("class hu.bme.bitsplease.playerHandler.LittleRobot")){
							littleRobots.remove(i.getKey());
							it.remove();
						//ha a robot egy olyan mezőre lép, ahol egy másik robot található,
						//akkor kiesik az a robot, amelynek kisebb a sebessége.	
						}else if(i.getKey().getClass().toString().equals("class hu.bme.bitsplease.playerHandler.Player")){
							
							double newX = player.velocity.size
									* Math.cos(player.velocity.angle * Math.PI
											/ 180)
									+ i.getKey().velocity.size * Math.cos(i.getKey().velocity.angle * Math.PI / 180);
							double newY = player.velocity.size
									* Math.sin(player.velocity.angle * Math.PI
											/ 180)
									+ i.getKey().velocity.size * Math.sin(i.getKey().velocity.angle * Math.PI / 180);
							
							if(i.getKey().velocity.size > player.velocity.size){
								i.getKey().velocity.size = Math.sqrt(Math.pow(newX, 2)
										+ Math.pow(newY, 2));
								i.getKey().velocity.angle = Math.atan2(newY, newX)
										* 180 / Math.PI;
								deletePlayer(player);
								player = null;
							}else{
								player.velocity.size = Math.sqrt(Math.pow(actualX, 2)
										+ Math.pow(actualY, 2));
								player.velocity.angle = Math.atan2(actualY, actualX)
										* 180 / Math.PI;
								weakPlayer = (Player)i.getKey();
							}
						}
						break;
					}
				}
			    
			    if(weakPlayer != null){
			    	deletePlayer(weakPlayer);
			    }
				
				/*
				 * Ha kiesik, akkor nullra állítjuk a listában és belerakjuk a
				 * kiesettek listájába
				 */

			    if(player != null){
					if ((actualX < 0)
							|| (actualX >= level.fields[0].length)
							|| (actualY < 0)
							|| (actualY >= level.fields.length)
							|| (level.fields[actualY][actualX].fieldType == Field.Type.HOLE)) {
						deletePlayer(player);
						player = null;
					} else {
						// Hozzáadjuk az új pontszámot az eddigihez
						int newScore = Math.abs(actualX
								- level.playerPositions.get(player).x)
								+ Math.abs(actualY
										- level.playerPositions.get(player).y);
						playerScores.put(player, newScore);
	
						player.addScore(newScore);
	
						/*
						 * Áthelyezzük a robotot az új helyére, majd kirajzoljuk a
						 * pályát
						 */
	
						level.playerPositions.put(player,
	
						new Position(actualX, actualY));
					}
			    }


				/*
				 * Ha a robot ragacsra lépett, akkor csökkentjük a ragacs
				 * élettartamát ha player null, akkor a robot lelépett a
				 * pályáról, ezért nem léphetett ragacsra
				 */

				if (player != null && level.fields[actualY][actualX].fieldType == Field.Type.STICK) {
					level.fields[actualY][actualX].remainingRounds--;
				}

				if (command == null) {
					display.displayLevel(level);
				}

				if (commandArray[0].equals("step")) {
					break;
				}

			}

			for (LittleRobot little : littleRobots) {

				if (little == null) {
					continue;
				}
				
				Position actual = level.playerPositions.get(little);
				
				//Ha a kisrobot nem takarít, akkor megy a legközelebbi foltos mezőhöz
				//Ha a kisrobotnak most járt le a törlési ideje, akkor a mező üres lesz
				if (!little.isCleaning()) {
					//ha a kis robot olyan mezőre lépett, ahol olaj vagy ragacs van,
					//akkor megkezdjük a takarítást.
					
					if (level.fields[actual.x][actual.y].fieldType == Field.Type.OIL
							|| level.fields[actual.x][actual.y].fieldType == Field.Type.STICK) {
						little.setRemainingCleaningTime();
						little.velocity.size = 0;
						little.velocity.angle = 0;
						little.isCleaning();
						
					}else{
						
						LinkedList<Position> positions = new LinkedList<Position>();
						positions.addLast(actual);
						int rows = level.fields.length;
						int coloumns = level.fields[0].length;
						boolean[][] visited = new boolean[rows][];
						for(int i = 0; i < level.fields.length; i++){
							visited[i] = new boolean[coloumns];
							for(int j = 0; j < level.fields.length; j++){
								visited[i][j] = false;
							}
						}
						visited[actual.x][actual.y] = true;
						//megkeresi a kisrobothoz legközelebb levő takarítandó mezőt
						Position specialPosition = getNearestGoodField(visited, positions, true);
						//ha talált ilyen mezőt, akkor elindul annak az irányába
						if (specialPosition != null) {
							little.velocity.size = 1;
							little.velocity.angle = Math.atan2(specialPosition.y
									- actual.y, specialPosition.x
									- actual.x)
									* 180 / Math.PI;
						}
					}
				} 
				else if (little.getRemainingCleaningTime() == 0) {
					level.fields[actual.y][actual.x].fieldType = Field.Type.FREE;
				}
				
				//új pozíció kiszámítása
				int actualX = actual.x
						+ (int) Math.round(little.velocity.size
								* Math.cos(little.velocity.angle * Math.PI
										/ 180));
				int actualY = actual.y
						- (int) Math.round(little.velocity.size
								* Math.sin(little.velocity.angle * Math.PI
										/ 180));
				
				//megnézi, hogy az új pozíción található-e másik robot vagy bent maradt e a pályán
				boolean goodPosition = true;
				
				if(actualX < 0 || actualY < 0 || actualX >= level.fields[0].length || actualY >= level.fields.length)
					goodPosition = false;
				
				if(goodPosition){
					// Ha bent maradt a pályán, akkorm megnézi, hogy ven e ott robot
					for (Entry<Robot, Position> i : level.playerPositions.entrySet()) {
						if ((i.getValue().x == actualX && i.getValue().y == actualY) && !i.getKey().equals(little) ){
							goodPosition = false;
						}
					}
				}
				//ha van rajta másik robot, akkor erre a mezőre nem léphet, ekkor keres egy másik
				//üreset mellette, ha nincs nem lép
				if (!goodPosition) {
					LinkedList<Position> positions = new LinkedList<Position>();
					positions.addLast(new Position(actualX, actualY));
					Position newPosition = getNearestGoodField(null, positions, false);
					if (newPosition != null) {
						level.playerPositions.put(little, newPosition);
						actualX = newPosition.x;
						actualY = newPosition.y;
					}
					//ha nincs rajta másik robot, akkor beállítjuk arra a mezőre a kis robotot
				} else {
					level.playerPositions.put(little, new Position(actualX,actualY));
				}
			}

			/*
			 * Végignézzük a mezőket Ha oljafolt van, akkor csökkentjük az
			 * hátralévő időt Ha 0 a hátralévő idő, akkor töröljük
			 */

			for (int i = 0; i < level.fields.length; i++) {
				for (int j = 0; j < level.fields[i].length; j++) {
					if (level.fields[i][j].remainingRounds > 0) {
						if (level.fields[i][j].fieldType == Field.Type.OIL) {
							level.fields[i][j].remainingRounds--;
						}
					}
					if (level.fields[i][j].remainingRounds == 0
							&& (level.fields[i][j].fieldType == Field.Type.OIL || level.fields[i][j].fieldType == Field.Type.STICK)) {
						level.fields[i][j].fieldType = Field.Type.FREE;
					}
				}
			}

			// Csökkentjük a hátralévő körök számát
			remainingRounds--;

		}
		//ha a parancs step vagy setLittleRobotPosition volt, vagy normál mód fut, akkor
		//minden 10 körben, vagy minden alkalommal, amikor setLittleRobotPosition parancsot adtunk meg
		if (command == null || commandArray[0].equals("step")
				|| commandArray[0].equals("setLittleRobotPosition")){
				//ha lejárt a 10 kör, vagy meghívtuk a setLittleRobotPosition parancsot
				if ((originalRounds - remainingRounds) % 10 == 0
						|| commandArray[0].equals("setLittleRobotPosition")) {
					//létrehozunk egy új kis robotot
					LittleRobot little = new LittleRobot();
					int newX = 0;
					int newY = 0;
					//ha lejárt a 10 kör, akkor random helyre generáljuk az egyik kis robotot
					if (command == null || (commandArray[0].equals("step") && TestApp.littleRandom)) {
						Random random = new Random();
						newX = random.nextInt(level.fields[0].length);
						newY = random.nextInt(level.fields.length);
					//ha parancs miatt helyezzük át a kis robotot
					} else if (commandArray.length > 3 
							&& !TestApp.littleRandom) {
						try {
							int index;
							// ha elegendő robot, akkor az adott számút átteszi a megfelelő helyre
							if ((index = Integer.parseInt(commandArray[3])) < littleRobots
									.size() && index >= 0) {
								if (littleRobots.get(index) == null) {
									littleRobots.set(index, new LittleRobot());
								}
								little = littleRobots.get(index);
							// ha nincs annyi robot, ahanyadik sorszámút akarta, akkor berakhatja,
							// a meglévő robotok után
							} else {
								littleRobots.add(little);
							}
							
							//Az új pozíció meghatározása a 2 parancsparaméterből
							if(Integer.parseInt(commandArray[1]) < level.fields[0].length &&
								Integer.parseInt(commandArray[1]) >= 0 &&
								Integer.parseInt(commandArray[2]) < level.fields.length &&
								Integer.parseInt(commandArray[2]) >= 0){
									newX = Integer.parseInt(commandArray[1]);
									newY = Integer.parseInt(commandArray[2]);
							}else
								System.err.println("Hibás paraméter az adott parancshoz!");
						} catch (NumberFormatException ex) {
							System.err.println("Hibás paraméter az adott parancshoz!");
						}
					} else {
						return false;
					}
					
					//megvizsgálja, hogy a kis robot léphet-e az új pozícióra
					boolean goodPosition = true;
					for (Entry<Robot, Position> i : level.playerPositions
							.entrySet()) {
						// ha ott van másik robot, akkor nem
						if (i.getValue().x == newX && i.getValue().y == newY) {
							goodPosition = false;
						}
					}
					//ha van másik robot az adott mezőn, akkor nem léphet oda
					//Ilyenkor megkeresi a legközelebb lévő tisztítandó mezőt
					if (!goodPosition) {
						LinkedList<Position> positions = new LinkedList<Position>();
						int rows = level.fields.length;
						int coloumns = level.fields[0].length;
						boolean[][] visited = new boolean[rows][];
						for(int i = 0; i < level.fields.length; i++){
							visited[i] = new boolean[coloumns];
							for(int j = 0; j < level.fields.length; j++){
								visited[i][j] = false;
							}
						}
						visited[newX][newY] = true;
						positions.addLast(new Position(newX, newY));
						Position newPosition = getNearestGoodField(visited, positions,false);
						//Beállítja arra a mezőre a kisrobot
						if (newPosition != null) {
							level.playerPositions.put(little, newPosition);
							littleRobots.add(little);
						}
					} else {
						level.playerPositions.put(little, new Position(newX,newY));
						if(!littleRobots.contains(little))
							littleRobots.add(little);
					}
					
					if(command != null && level.playerPositions.get(little) == null){
						display.displayError("Nem sikerült elhelyezni a kisrobotot!");
						littleRobots.remove(little);
					}
				}
		}

		if (command == null) {
			// Ha lejárt az idő vagy már csak egy játékos van, akkor kilépünk
			if (remainingRounds <= 0 || outPlayers.size() >= players.size() - 1) {
				return false;
			}
			//Megjelenítjük a hátralévő körök számát.
			display.displayRound(String.valueOf(remainingRounds));

		}
		//Újrarajzoljuk a pályát
		if (command == null || commandArray[0].equals("displayLevel")) {
			
			if(command != null && level == null){
				System.err.println("Nincs megadva még pálya!");
				return false;
			}
			
			display.displayLevel(level);
		}
		//(SetPlayerVelocity "szám1" "szám2" "szám3") parancs megadása esetén
		//szám1=sebesség nagysága
		//szám2=sebesség szöge
		//szám3=a robot sorszáma
		if (commandArray[0].equals("setPlayerVelocity") && commandArray.length > 3) {
			try {
				if(Integer.parseInt(commandArray[3]) < players.size() &&
						Integer.parseInt(commandArray[3]) >= 0 ){
					Player player = players.get(Integer.parseInt(commandArray[3]));
					player.velocity.size = Integer.parseInt(commandArray[1]);
					player.velocity.angle = Integer.parseInt(commandArray[2]) % 360;
				} else {
					System.err.println("Hibás paraméter az adott parancshoz!");
				}
			} catch (NumberFormatException ex) {
				System.err.println("Hibás paraméter az adott parancshoz!");
			}
		}
		//listPlayers parancs megadása esetén a játékosok kilistázása
		if (commandArray[0].equals("listPlayers")) {
			
			if(level == null){
				System.err.println("Nincs megadva még pálya!");
				return false;
			}
			
			boolean wasOut = false;
			for (Player player : players) {
				if (player == null) {	
					continue;
				}
				if(level.playerPositions.get(player) == null){
					System.err.println("Nem helyezted még el a játékost a pályán!");
					return false;
				}
				wasOut = true;
				System.out.println(player.name + " "
						+ level.playerPositions.get(player).x + " "
						+ level.playerPositions.get(player).y + " "
						+ player.actionNums.get(Step.ActionType.OIL) + " "
						+ player.actionNums.get(Step.ActionType.STICK) + " "
						+ player.velocity.size + " " + player.velocity.angle
						+ " nem");
			}
			for (Player player : outPlayers) {
				if (player == null) {
					continue;
				}
				wasOut = true;
				System.out.println(player.name + " "
						+ "-1 "
						+ "-1 "
						+ player.actionNums.get(Step.ActionType.OIL) + " "
						+ player.actionNums.get(Step.ActionType.STICK) + " "
						+ player.velocity.size + " " + player.velocity.angle
						+ " igen");
			}
			if(!wasOut){
				System.out.println("-");
			}
		}
		//listLittleRobots parancs esetén a kis robotok kilistázása
		if (commandArray[0].equals("listLittleRobots")) {
			
			if(level == null){
				System.err.println("Nincs megadva még pálya!");
				return false;
			}
			
			boolean wasOut = false;
			for (LittleRobot little : littleRobots) {
				
				if (little == null) {
					continue;
				}
				wasOut = true;
				if(level.playerPositions.get(little) == null){
					System.err.println("Nem helyezted még el a kisrobotot a pályán!");
					return false;
				}
				System.out.println(level.playerPositions.get(little).x + " "
						+ level.playerPositions.get(little).y + " "
						+ little.velocity.size + " " + little.velocity.angle);
			}
			if(!wasOut){
				System.out.println("-");
			}
		}
		//listSpecialPositions parancs esetén a pályán lévő speciális elemek kilistázása
		if (commandArray[0].equals("listSpecialPositions")) {
			
			if(level == null){
				System.err.println("Nincs megadva még pálya!");
				return false;
			}
			
			boolean wasOut = false;
			for (int i = 0; i < level.fields.length; i++) {
				for (int j = 0; j < level.fields[0].length; j++) {
					if (level.fields[j][i].fieldType == Field.Type.OIL) {
						System.out.println("O " + j + " " + i + " "
								+ level.fields[j][i].remainingRounds);
						wasOut = true;
					} else if (level.fields[j][i].fieldType == Field.Type.STICK) {
						System.out.println("R " + j + " " + i + " "
								+ level.fields[j][i].remainingRounds);
						wasOut = true;
					}
				}
			}
			// Ha nem volt egy folt sem akkor "-"
			if(!wasOut){
				System.out.println("-");
			}
		}

		return false;

	}
	
	public Position getNearestGoodField(boolean[][] visited, LinkedList<Position> positions, boolean special) {
		/*
		 *  Ha special == true ,megkeresi a legközelebbi foltos mezőt
		 *  Egyébként megkeresi a legközelebbi olyan mezőt, ahol nincs Robot
		 *  Ha nem talál megfelelő mezőt, akkor null a visszatérési érték
		 *  Szélességi bejárás
		 */
		
		
		Position actualPos;
		if (positions.size() <= 0) {
			// Ha nincs már több mező a sorban, akkor nem talált megfelelő mezőt
			return null;
		} else {
			actualPos = positions.pollFirst();
			// Ha van még mező a sorban, akkor megnézi a szomszédait
			for (int i = actualPos.y - 1; i <= actualPos.y + 1; i++) {
				if(i >= 0 && i < level.fields.length) {
					for (int j = actualPos.x - 1; j <= actualPos.x + 1; j++) {
						if(j >= 0 && j < level.fields[0].length && !visited[j][i]) {
							if (special) {
								visited[j][i] = true;
								
								// Megnézi foltos e a mező, ha igen akkor ez a legközelebbi
								// Ha nem, akkor hozzáadja a sorhoz
								if (level.fields[j][i].fieldType == Field.Type.OIL || level.fields[j][i].fieldType == Field.Type.STICK) {
									return new Position(j, i);
								} else {
									positions.addLast(new Position(j, i));
								}
							} else {
								// Megnézi, hogy van e rajta Robot
								boolean goodPosition = true;
								for (Entry<Robot, Position> entry : level.playerPositions.entrySet()) {
									if (entry.getValue().x == actualPos.x && entry.getValue().y == actualPos.y) {
										goodPosition = false;
									}
								}
								if (goodPosition) {
									// Ha nincs, akkor ide lép, ha van, akkor belekerül a sorba
									return new Position(j, i);
								} else {
									return positions.getFirst();
								}
							}
						}
					}
				}
			}
			return getNearestGoodField(visited, positions, special);
		}
	}

	synchronized public void notifyThread(){
		this.notify();
	}
}
