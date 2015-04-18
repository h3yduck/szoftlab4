package hu.bme.bitsplease.gameEngine;

import hu.bme.bitsplease.TestApp;
import hu.bme.bitsplease.stepHandler.ConsoleInput;
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
	private LevelLoader levelLoader = null;
	private Level level = null;

	// A játékban hátramaradt körök száma
	private int remainingRounds;

	// Eredeti körök száma
	private int originalRounds;

	private List<Player> outPlayers; // Kiesett játékosok
	private List<Player> players; // A játékosok listája

	private InputHandler input;
	private DisplayHandler display;

	// Kis tisztító robotok
	private List<LittleRobot> littleRobots;

	// ha egy játékos kiesett, akkor belekerül az outPlayers listába, és az
	// értéke null lesz a players listában
	private Map<Player, Integer> playerScores; // A játékosok pontjai

	// ha egy játékos kiesett,akkor ez a függvény végzi el a játékos törlését a
	// pályáról, stb.
	private void deletePlayer(Player player) {
		outPlayers.add(player);
		players.set(players.indexOf(player), null);
		level.playerPositions.remove(player);
	}

	// GameEngine paraméteres konstruktora, ahol a paraméter a pályabetöltő
	// objektum
	public GameEngine(LevelLoader levelLoader) {
		this.levelLoader = levelLoader;
		players = new ArrayList<Player>();
		outPlayers = new ArrayList<Player>();
		littleRobots = new ArrayList<LittleRobot>();
		playerScores = new HashMap<Player, Integer>();
		input = new ConsoleInput();
		display = new ConsoleDisplay();
		remainingRounds = 10;
		originalRounds = remainingRounds;
	}

	// GameEngine paraméter nélküli konstruktora
	public GameEngine() {
		players = new ArrayList<Player>();
		outPlayers = new ArrayList<Player>();
		littleRobots = new ArrayList<LittleRobot>();
		playerScores = new HashMap<Player, Integer>();
		input = new ConsoleInput();
		display = new ConsoleDisplay();
		remainingRounds = 10;
		originalRounds = remainingRounds;
	}

	public void startGame() {
		while (getSettings(null))
			;

		/*
		 * A játék addig fut, amíg a play függvény igazat ad vissza null ->
		 * parancsok nélkül futtatás
		 */

		while (play(null))
			;

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

		// Ha a beállítások sikertelen voltak, akkor a tömböket kiűríti
		if (command == null) {
			players.clear();
			outPlayers.clear();
			littleRobots.clear();
			playerScores.clear();
		}

		/*
		 * Minden parancshoz tartozik egy normál működés esetén lezajló esemény.
		 * Parancs-mód esetén: command != null Normál mód esetén: command ==
		 * null
		 */

		// Pálya betöltése Normál mód esetén
		if (command == null) {
			level = null;
			while (level == null) {
				levelName = input.getLevel();
				levelLoader = new FileLoader(levelName);
				level = levelLoader.getLevel();
			}
		}

		// Pálya betöltése Parancs-mód esetén a (setMap "fájlnév") paranccsal
		if (commandArray[0].equals("setMap") && commandArray.length > 1) {
			levelName = commandArray[1];
			levelLoader = new FileLoader(levelName);
			level = levelLoader.getLevel();
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
			} catch (NumberFormatException ex) {
				numOfPlayers = 0;
				System.err.println("0-nál nagyobb egész számot adj meg paraméterként!");
			}
		}

		// speciális elemek számának beállíta a (setSpecialActionNumber "szám")
		// paranccsal
		if (commandArray[0].equals("setSpecialActionNumber")
				&& commandArray.length > 1) {
			try {
				specialTypeNum = Integer.parseInt(commandArray[1]);
			} catch (NumberFormatException ex) {
				specialTypeNum = 0;
				System.err.println("0-nál nagyobb egész számot adj meg paraméterként!");
			}
		}

		// játékosok nevének megadása,ha nem adjuk meg, default: TestName
		if (command == null || commandArray[0].equals("setPlayers")) {
			for (int i = 0; i < numOfPlayers; i++) {
				// jatekos nevenek lekerdezese
				String name = "TestName";
				if (command == null) {
					name = input.getRobotName();
				}

				Player player = new Player(input, display, name);
				players.add(player);
				playerScores.put(player, 0);
			}
		}

		// specialis elemek számának beallitasa minden jatekos reszere oil és
		// stickre is
		if (command == null || commandArray[0].equals("setSpecialActionNumber")) {
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
				originalRounds = remainingRounds;
			} catch (NumberFormatException ex) {
				remainingRounds = 0;
				originalRounds = remainingRounds;
				System.err.println("0-nál nagyobb egész számot adj meg paraméterként!");
			}
		}
		
		List<Position> positions = new ArrayList<Position>();
		//A pályát leíró objektum betöltése után, a játékosok 
		//kezdőpozíciójának meghatározása
		if (command == null || commandArray[0].equals("setMap")) {
			
			if (level == null) {
				levelLoader = new FileLoader("testMap3.txt");
				level = levelLoader.getLevel();
			}
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
			if (positions.size() < players.size()) {
				display.displayError("Túl sok játékost adtál meg a kiválasztott pályához. "
						+ "Adj meg másik pályát vagy kevesebb játékost.");
				Player.resetSNum();
				return true;
			}
			// A positions listában találhatő megfelelő kezdőpozíciók alapján
			// véletlenszerűen elhelyezzük a játékosokat
			if (command == null || TestApp.playerRandom) {
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
		
		//ha a parancs (setPlayersPositionRandom "szám1" "szám2" "szám3") formájú,
		//akkor a (szám1, szám2) pozícióra beállítja a szám3 sorszámú játékost.
		if (commandArray[0].equals("setPlayersPositionRandom")
				&& commandArray.length > 3 && !TestApp.playerRandom) {
			try {
				//pozíció meghatározása szám1,szám2 paraméterek alapján
				Position pos = new Position(Integer.parseInt(commandArray[1]),
						Integer.parseInt(commandArray[2]));
				int index;
				//játékos sorszámának meghatározása szám3 paraméter alapján
				if ((index = Integer.parseInt(commandArray[3])) < players
						.size() && index >= 0 && level != null) {
					//megvizsgálja, hogy a megadott pozíció rajta van e a pályán
					if(Integer.parseInt(commandArray[1]) <= level.fields[0].length - 1 &&
							Integer.parseInt(commandArray[1]) >= 0 &&
							Integer.parseInt(commandArray[2]) <= level.fields.length - 1 &&
							Integer.parseInt(commandArray[2]) >= 0)
					level.playerPositions.put(players.get(index), pos);
					else
						System.err.println("Hibás pozíció! A megadott pozíció nem a pályán található");
				}else
					System.err.println("A megadott sorszámú játékos nem létezik!");
			} catch (NumberFormatException ex) {
				System.err.println("Hibás paraméterek az adott parancshoz!");
			}
		}
		
		
		//Parancs-mód esetén a (setLittleRobotPositionRandom "0/1") paranccsal megadható,
		//hogy a kis robotok kezdőpozíciója random generálódjon, vagy manuálisan mi adjuk meg
		if (commandArray[0].equals("setLittleRobotPositionRandom")
				&& commandArray.length > 1) {
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
		//lépés parancs megadása, vagy normál mód esetén, ha lépés történt
		if (command == null || commandArray[0].equals("step")) {
			for (Player player : players) {

				if (commandArray[0].equals("step")) {
					try {
						int index;
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
				if (player == null) {
					continue;
				}

				// Megnézzük, hogy milyen mezőn áll a robot
				// Ha FREE-n, akkor bekérjük a lépést
				switch (level.fields[level.playerPositions.get(player).y][level.playerPositions
						.get(player).x].fieldType) {
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
								actualStep = new Step();
								actualStep.angle = Integer
										.parseInt(commandArray[1]) % 360;
								switch (commandArray[2]) {
								case "O":
								case "F":
									actualStep.stepAction = Step.ActionType
											.fromChar(commandArray[2].charAt(0));
									break;
								}
							} catch (NumberFormatException ex) {

							}
						}

						/*
						 * Ellenőrizzük, hogy rakott e le valamit, és hogy van e
						 * rá kapacitása Ha van, akkor lerakjuk a mezőre és
						 * csökkentjük a kapacitást
						 */

						if (actualStep.stepAction != null) {
							if (player.actionNums.get(actualStep.stepAction) > 0) {
								if (actualStep.stepAction == Step.ActionType.OIL)
									level.fields[level.playerPositions
											.get(player).y][level.playerPositions
											.get(player).x].fieldType = Field.Type
											.fromChar('O');
								else if (actualStep.stepAction == Step.ActionType.STICK)
									level.fields[level.playerPositions
											.get(player).y][level.playerPositions
											.get(player).x].fieldType = Field.Type
											.fromChar('S');
								level.fields[level.playerPositions.get(player).y][level.playerPositions
										.get(player).y].remainingRounds = 3;
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
				case OIL:
					// nem változik a sebesség
					break;
				case STICK:
					/*
					 * A sebesség a fele az eddiginek
					 */
					player.velocity.size /= 2;
					int pos[] = { level.playerPositions.get(player).x,
							level.playerPositions.get(player).y };
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
						level.fields[i.getValue().x][i.getValue().y].fieldType = Field.Type.OIL;
						level.fields[i.getValue().x][i.getValue().y].remainingRounds = 3;
						if(i.getKey().getClass().toString().equals("LittleRobot")){
							it.remove();
						}else if(i.getKey().getClass().toString().equals("Player")){
							if(i.getKey().velocity.size > player.velocity.size){
								deletePlayer(player);
								player = null;
							}else{
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

				/*
				 * Ha a robot ragacsra lépett, akkor csökkentjük a ragacs
				 * élettartamát ha player null, akkor a robot lelépett a
				 * pályáról, ezért nem léphetett ragacsra
				 */

				if (player != null
						&& level.fields[actualY][actualX].fieldType == Field.Type.STICK) {
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

				Position actual = level.playerPositions.get(little);

				if (little == null) {
					continue;
				}

				if (!little.isCleaning()) {

				} else if (little.getRemainingCleaningTime() == 0) {
					level.fields[actual.y][actual.x].fieldType = Field.Type.FREE;
				} else {
					LinkedList<Position> positions = new LinkedList<Position>();
					positions.addLast(actual);
					Position specialPosition = getNearestGoodField(positions,
							true);
					if (specialPosition != null) {
						little.velocity.size = 1;
						little.velocity.angle = Math.atan2(actual.y
								- specialPosition.y, actual.x
								- specialPosition.x)
								* 180 / Math.PI;
					}
				}

				int actualX = actual.x
						+ (int) Math.round(little.velocity.size
								* Math.cos(little.velocity.angle * Math.PI
										/ 180));
				int actualY = actual.y
						- (int) Math.round(little.velocity.size
								* Math.sin(little.velocity.angle * Math.PI
										/ 180));

				boolean goodPosition = true;
				for (Entry<Robot, Position> i : level.playerPositions
						.entrySet()) {
					if (i.getValue().x == actualX && i.getValue().y == actualY) {
						goodPosition = false;
					}
				}
				if (!goodPosition) {
					LinkedList<Position> positions = new LinkedList<Position>();
					positions.addLast(new Position(actualX, actualY));
					Position newPosition = getNearestGoodField(positions, false);
					if (newPosition != null) {
						level.playerPositions.put(little, newPosition);
						littleRobots.add(little);
					}
				} else {
					level.playerPositions.put(little, new Position(actualX,
							actualY));
					littleRobots.add(little);
				}

				if (level.fields[actualY][actualX].fieldType == Field.Type.OIL
						|| level.fields[actualY][actualX].fieldType == Field.Type.STICK) {
					little.setRemainingCleaningTime();
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

		if (command == null || commandArray[0].equals("step")
				|| commandArray[0].equals("setLittleRobotPosition"))
			if (TestApp.littleRandom) {
				if (originalRounds - remainingRounds % 10 == 0
						|| commandArray[0].equals("setLittleRobotPosition")) {
					LittleRobot little = new LittleRobot();
					int newX = 0;
					int newY = 0;
					if (command == null || commandArray[0].equals("step")) {
						Random random = new Random();
						newX = random.nextInt(level.fields[0].length);
						newY = random.nextInt(level.fields.length);
					} else if (commandArray.length > 3 && !TestApp.littleRandom) {
						try {
							int index;
							if ((index = Integer.parseInt(commandArray[3])) < littleRobots
									.size()) {
								if (littleRobots.get(index) == null) {
									littleRobots.set(index, new LittleRobot());
								}
								little = littleRobots.get(index);
							} else {
								List<LittleRobot> littles = new ArrayList<LittleRobot>();
								for (LittleRobot littleIter : littleRobots) {
									littles.add(littleIter);
								}
								while (index < littles.size() - 1) {
									littles.add(null);
								}
								littles.add(little);
							}
							newX = Integer.parseInt(commandArray[1]);
							newY = Integer.parseInt(commandArray[2]);
						} catch (NumberFormatException ex) {
						}
					} else {
						return false;
					}
					boolean goodPosition = true;
					for (Entry<Robot, Position> i : level.playerPositions
							.entrySet()) {
						if (i.getValue().x == newX && i.getValue().y == newY) {
							goodPosition = false;
						}
					}
					if (!goodPosition) {
						LinkedList<Position> positions = new LinkedList<Position>();
						positions.addLast(new Position(newX, newY));
						Position newPosition = getNearestGoodField(positions,
								false);
						if (newPosition != null) {
							level.playerPositions.put(little, newPosition);
							littleRobots.add(little);
						}
					} else {
						level.playerPositions.put(little, new Position(newX,
								newY));
						littleRobots.add(little);
					}
				}
			}

		if (command == null) {
			// Ha lejárt az idő vagy már csak egy játékos van, akkor kilépünk
			if (remainingRounds <= 0 || outPlayers.size() >= players.size() - 1) {
				return false;
			}

			display.displayRound(String.valueOf(remainingRounds));

		}

		if (command == null || commandArray[0].equals("displayLevel")) {
			display.displayLevel(level);
		}

		if (commandArray[0].equals("setPlayerVelocity")
				&& commandArray.length > 3) {
			try {
				Player player = players.get(Integer.parseInt(commandArray[3]));
				player.velocity.size = Integer.parseInt(commandArray[1]);
				player.velocity.angle = Integer.parseInt(commandArray[2]) % 360;
			} catch (NumberFormatException ex) {
			}
		}

		if (commandArray[0].equals("listPlayers")) {
			for (Player player : players) {
				if (player == null) {
					continue;
				}
				System.out.print(player.name + " "
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
				System.out.print(player.name + " "
						+ level.playerPositions.get(player).x + " "
						+ level.playerPositions.get(player).y + " "
						+ player.actionNums.get(Step.ActionType.OIL) + " "
						+ player.actionNums.get(Step.ActionType.STICK) + " "
						+ player.velocity.size + " " + player.velocity.angle
						+ " igen");
			}
		}

		if (commandArray[0].equals("listLittleRobots")) {
			for (LittleRobot little : littleRobots) {
				if (little == null) {
					continue;
				}
				System.out.print(level.playerPositions.get(little).x + " "
						+ level.playerPositions.get(little).y + " "
						+ little.velocity.size + " " + little.velocity.angle
						+ " nem");
			}
		}

		if (commandArray[0].equals("listSpecialPositions")) {
			for (int i = 0; i < level.fields.length; i++) {
				for (int j = 0; j < level.fields[0].length; j++) {
					if (level.fields[j][i].fieldType == Field.Type.OIL) {
						System.out.println("O " + j + " " + i + " "
								+ level.fields[j][i].remainingRounds);
					} else if (level.fields[j][i].fieldType == Field.Type.STICK) {
						System.out.println("R " + j + " " + i + " "
								+ level.fields[j][i].remainingRounds);
					}
				}
			}
		}

		return true;

	}

	private Position getNearestGoodField(LinkedList<Position> positions,
			boolean special) {
		Position actualPos;
		if ((actualPos = positions.pollFirst()) == null) {
			return null;
		} else {
			for (int i = actualPos.y - 1; i <= actualPos.y + 1; i++) {
				for (int j = actualPos.x - 1; j <= actualPos.x + 1; j++) {
					if (special) {
						if (level.fields[j][i].fieldType == Field.Type.OIL
								|| level.fields[j][i].fieldType == Field.Type.STICK) {
							return new Position(j, i);
						} else {
							positions.addLast(new Position(j, i));
						}
					} else {
						boolean goodPosition = true;
						for (Entry<Robot, Position> entry : level.playerPositions
								.entrySet()) {
							if (entry.getValue().x == actualPos.x
									&& entry.getValue().y == actualPos.y) {
								goodPosition = false;
							}
						}
						if (goodPosition) {
							return new Position(j, i);
						} else {
							positions.addLast(new Position(j, i));
						}
					}
				}
			}
			return getNearestGoodField(positions, special);
		}
	}
}
