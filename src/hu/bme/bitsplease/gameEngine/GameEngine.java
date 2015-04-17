package hu.bme.bitsplease.gameEngine;

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
	private LevelLoader levelLoader;
	private Level level;
	
	// A játékban hátramaradt körök száma
	private int remainingRounds;
	
	private int originalRounds;

	private List<Player> outPlayers; // Kisesett játékosok
	private List<Player> players; // A játékosok listája
	
	private InputHandler input;
	private DisplayHandler display;
	
	private List<LittleRobot> littleRobots;
	
	// ha egy játékos kiesett, akkor belekerül az outPlayers listába, és az
	// értéke null lesz a players listában
	private Map<Player, Integer> playerScores; // A játékosok pontjai

	private void deletePlayer(Player player) {
		outPlayers.add(player);
		players.set(players.indexOf(player), null);
		level.playerPositions.remove(player);
	}

	public GameEngine(LevelLoader levelLoader) {
		this.levelLoader = levelLoader;
		players = new ArrayList<Player>();
		outPlayers = new ArrayList<Player>();
		littleRobots = new ArrayList<LittleRobot>();
		playerScores = new HashMap<Player, Integer>();
		remainingRounds = 10;
		originalRounds = remainingRounds;
	}

	public GameEngine() {
		players = new ArrayList<Player>();
		outPlayers = new ArrayList<Player>();
		littleRobots = new ArrayList<LittleRobot>();
		playerScores = new HashMap<Player, Integer>();
		remainingRounds = 10;
		originalRounds = remainingRounds;
	}

	public void startGame() {
		while(getSettings(null));

		/*
		 * A játék addig fut, amíg a play függvény igazat ad vissza null ->
		 * parancsok nélkül futtatás
		 */

		while (play(null));
		
		Player maxScorePlayer = null;
		int maxScore = -1;
		for(Player player : players){
			if(player != null && playerScores.get(player) > maxScore){
				maxScorePlayer = player;
				maxScore = playerScores.get(maxScorePlayer);
			}
		}
		
		for(Player player : outPlayers){
			if(playerScores.get(player) > maxScore){
				maxScorePlayer = player;
				maxScore = playerScores.get(maxScorePlayer);
			}
		}
		
		display.displayCongrat(maxScorePlayer.name);
	}

	public boolean getSettings(String command) {

		input = new ConsoleInput();
		display = new ConsoleDisplay();
		
		players.clear();
		outPlayers.clear();
		littleRobots.clear();
		playerScores.clear();
		
		String levelName = input.getLevel();
		levelLoader = new FileLoader(levelName);
		level = levelLoader.getLevel();

		// specialis elemek szamanak lekerdezese
		int specialTypeNum = input.getSpecialActionTypeNumber();

		int numOfPlayers = input.getNumOfPlayer();
		

		for (int i = 0; i < numOfPlayers; i++) {
			// jatekos nevenek lekerdezese
			String name = input.getRobotName();

			Player player = new Player(input, display, name);
			players.add(player);
			playerScores.put(player, 0);
		}

		// specialis elemek szamanak beallitasa minden jatekos reszere oil és
		// stickre is
		for (int i = 0; i < players.size(); i++) {
			players.get(i).actionNums.put(ActionType.OIL, specialTypeNum);
			players.get(i).actionNums.put(ActionType.STICK, specialTypeNum);
		}

		remainingRounds = input.getGameLength();
		originalRounds = remainingRounds;

		// Az USRPOS mezőket átállítjuk FREE-re
		List<Position> positions = new ArrayList<Position>();
		for (int i = 0; i < level.fields.length; i++) {
			for (int j = 0; j < level.fields[i].length; j++) {
				if (level.fields[i][j].fieldType == Field.Type.USRPOS) {
					positions.add(new Position(j, i));
					level.fields[i][j].fieldType = Field.Type.FREE;
				}
			}
		}
		
		if(positions.size() < players.size()){
			display.displayError("Túl sok játékost adtál meg a kiválasztott pályához. "
					+ "Adj meg másik pályát vagy kevesebb játékost.");
			Player.resetSNum();
			return true;
		}
		
		// A megfelelő helyeken véletlenszerűen elhelyezzük a játékosokat
		for (Player player : players) {
			Random random = new Random();
			int rand = random.nextInt(positions.size());
			level.playerPositions.put(player, positions.get(rand));
			positions.remove(rand);
		}
		
		display.displayLevel(level);
		
		return false;
	}

	public boolean play(String command) {
		/*
		 * tenyleges jatek mechanika minden lepes vegen meg kell hivni minden
		 * jatekos DisplayHandleret, és kirajzolni a pályát minden lepeskor az
		 * aktualis jatekos StepHandleret, és bekérni a lépést frissiteni kell a
		 * jatekosok pontjait
		 */

		/*
		 * Minden játékostól megkérdezzük mit lép, és változtatjuk eszerint az
		 * állapotát
		 */

		for (Player player : players) {
			// Ha a player null, akkor a játékos már kiesett
			if (player == null) {
				continue;
			}

			// Megnézzük, hogy milyen mezőn áll a robot
			// Ha FREE-n, akkor bekérjük a lépést
			switch (level.fields[level.playerPositions.get(player).y]
					[level.playerPositions.get(player).x].fieldType) {
			case FREE:
				boolean goodStep = false;
				Step actualStep = null;

				while (!goodStep) {

					/*
					 * Ha nem megfelelő a lerakott elem, akkor újra kérdezzük a felhasználót
					 */

					goodStep = true;
					
					player.displaySpecialActionTypesNumber();
					
					actualStep = player.getStep();

					/*
					 * Ellenőrizzük, hogy rakott e le valamit, és hogy van e rá
					 * kapacitása Ha van, akkor lerakjuk a mezőre és csökkentjük
					 * a kapacitást
					 */

					if (actualStep.stepAction != null) {
						if (player.actionNums.get(actualStep.stepAction) > 0) {
							if (actualStep.stepAction == Step.ActionType.OIL)
								level.fields[level.playerPositions.get(player).y][level.playerPositions
										.get(player).x].fieldType = Field.Type
										.fromChar('O');
							else if (actualStep.stepAction == Step.ActionType.STICK)
								level.fields[level.playerPositions.get(player).y][level.playerPositions
										.get(player).x].fieldType = Field.Type
										.fromChar('S');
							level.fields[level.playerPositions.get(player).y][level.playerPositions
									.get(player).y].remainingRounds = 3;
							player.actionNums.put(actualStep.stepAction,
									player.actionNums
											.get(actualStep.stepAction) - 1);
						} else {
							player.displayError("Nincs már több " + actualStep.stepAction.toString() + " a foltkészletben");
							goodStep = false;
						}
					}
				}

				/*
				 * Kiszámoljuk a Step-ből az új sebességet
				 */

				if (actualStep != null && actualStep.angle >= 0) {
					double actualX = player.velocity.size
							* Math.cos(player.velocity.angle * Math.PI / 180)
							+ Math.cos(actualStep.angle * Math.PI / 180);
					double actualY = player.velocity.size
							* Math.sin(player.velocity.angle * Math.PI / 180)
							+ Math.sin(actualStep.angle * Math.PI / 180);
					player.velocity.size = Math.sqrt(Math.pow(actualX, 2)
							+ Math.pow(actualY, 2));
					player.velocity.angle = Math.atan2(actualY, actualX) * 180
							/ Math.PI;
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
				level.fields[level.playerPositions.get(player).y]
							[level.playerPositions.get(player).x].fieldType = Field.Type.FREE;
				break;
			}

			/*
			 * Kiszámoljuk a robot új helyeztét
			 */

			int actualX = level.playerPositions.get(player).x
					+ (int) Math.round(player.velocity.size
							* Math.cos(player.velocity.angle * Math.PI / 180));
			int actualY = level.playerPositions.get(player).y
					- (int) Math.round(player.velocity.size
							* Math.sin(player.velocity.angle * Math.PI / 180));

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
				 * Áthelyezzük a robotot az új helyére, majd kirajzoljuk a pályát
				 */
				
				level.playerPositions.put(player,
				
						new Position(actualX, actualY));
			}
			
			/*
			 * Ha a robot ragacsra lépett, akkor csökkentjük a ragacs élettartamát
			 * ha player null, akkor a robot lelépett a pályáról, ezért nem léphetett ragacsra
			 */
			
			if(player != null && level.fields[actualY][actualX].fieldType == Field.Type.STICK){
				level.fields[actualY][actualX].remainingRounds--;
			}
			
			display.displayLevel(level);
			
			
		
		}
		
		for(LittleRobot little : littleRobots){
			
			Position actual = level.playerPositions.get(little);
			
			if((!little.isCleaning())){
				
			}else if(little.getRemainingCleaningTime() == 0){
				level.fields[actual.y][actual.x].fieldType = Field.Type.FREE;
			}else{
				LinkedList<Position> positions = new LinkedList<Position>();
				positions.addLast(actual);
				Position specialPosition = getNearestGoodField(positions, true);
				if(specialPosition != null){
					little.velocity.size = 1;
					little.velocity.angle = Math.atan2(actual.y - specialPosition.y, actual.x - specialPosition.x) * 180 / Math.PI;
				}
			}
			
			int actualX = actual.x
					+ (int) Math.round(little.velocity.size
							* Math.cos(little.velocity.angle * Math.PI / 180));
			int actualY = actual.y
					- (int) Math.round(little.velocity.size
					* Math.sin(little.velocity.angle * Math.PI / 180));
			
			level.playerPositions.put(little,
					new Position(actualX, actualY));
			
			if(level.fields[actualY][actualX].fieldType == Field.Type.OIL 
			 ||level.fields[actualY][actualX].fieldType == Field.Type.STICK){
				little.setRemainingCleaningTime();
			}
			
		}

		/*
		 * Végignézzük a mezőket
		 * Ha oljafolt van, akkor csökkentjük az hátralévő időt
		 * Ha 0 a hátralévő idő, akkor töröljük
		 * 
		 */

		for (int i = 0; i < level.fields.length; i++) {
			for (int j = 0; j < level.fields[i].length; j++) {
				if (level.fields[i][j].remainingRounds > 0) {
					if (level.fields[i][j].fieldType == Field.Type.OIL) {
						level.fields[i][j].remainingRounds--;
					}
				}
				if (level.fields[i][j].remainingRounds == 0 && (level.fields[i][j].fieldType == Field.Type.OIL || level.fields[i][j].fieldType == Field.Type.STICK)) {
						level.fields[i][j].fieldType = Field.Type.FREE;
				}
			}
		}

		// Csökkentjük a hátralévő körök számát
		remainingRounds--;
		
		if(originalRounds - remainingRounds % 10 == 0){
			LittleRobot little = new LittleRobot();
			Random random = new Random();
			int newX = random.nextInt(level.fields[0].length);
			int newY = random.nextInt(level.fields.length);
			boolean goodPosition = true;
			for(Entry<Robot, Position> i : level.playerPositions.entrySet()){
				if(i.getValue().x == newX && i.getValue().y == newY){
					goodPosition = false;
				}
			}
			if(!goodPosition){
				LinkedList<Position> positions = new LinkedList<Position>();
				positions.addLast(new Position(newX, newY));
				Position newPosition = getNearestGoodField(positions, false);
				if(newPosition != null){
					level.playerPositions.put(little, newPosition);
					littleRobots.add(little);
				}
			}else{
				level.playerPositions.put(little, new Position(newX, newY));
				littleRobots.add(little);
			}
		}

		// Ha lejárt az idő vagy már csak egy játékos van, akkor kilépünk
		if (remainingRounds <= 0 || outPlayers.size() >= players.size() - 1) {
			return false;
		}
		
		display.displayRound(String.valueOf(remainingRounds));
		
		display.displayLevel(level);
		
		return true;

	}
	
	private Position getNearestGoodField(LinkedList<Position> positions, boolean special){
		Position actualPos;
		if((actualPos = positions.pollFirst()) == null){
			return null;
		}else{
			for(int i = actualPos.y - 1; i <= actualPos.y + 1; i++){
				for(int j = actualPos.x - 1; j <= actualPos.x + 1; j++){
					if(special){
						if(level.fields[j][i].fieldType == Field.Type.OIL || level.fields[j][i].fieldType == Field.Type.STICK){
							return new Position(j, i);
						}else{
							positions.addLast(new Position(j, i));
						}
					}else{
						boolean goodPosition = true;
						for(Entry<Robot, Position> entry : level.playerPositions.entrySet()){
							if(entry.getValue().x == actualPos.x && entry.getValue().y == actualPos.y){
								goodPosition = false;
							}
						}
						if(goodPosition){
							return new Position(j, i);
						}else{
							positions.addLast(new Position(j, i));
						}
					}
				}
			}
			return getNearestGoodField(positions, special);
		}
	}
}
