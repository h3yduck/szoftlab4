package hu.bme.bitsplease;

import hu.bme.bitsplease.gameEngine.GameEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class TestApp {

	public static GameEngine gameEngine;
	
	public static boolean playerRandom = true;
	public static boolean littleRandom = true;
	
	public static void main(String[] args) {
		
		List<String> settingCommands = Arrays.asList(
				"setMap", "setPlayers", "setRounds", "setSpecialActionNumber", "setPlayerName", "setPlayersPositionRandom",
				"setPlayerPosition", "setLittleRobotPositionRandom"
		);
		
		List<String> playCommands = Arrays.asList(
				"step", "setPlayerVelocity", "displayLevel", "listPlayers",
				"listLittleRobots", "listSpecialPositions", "setLittleRobotPosition"
		);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		gameEngine = new GameEngine();
    	while(true){
	    	try{
	    		String line = br.readLine();
	    		if(settingCommands.contains(line.split(" ")[0])){
	    			gameEngine.getSettings(line);
	    		}else if(playCommands.contains(line.split(" ")[0])){
	    			gameEngine.play(line);
	    		}else if(line.split(" ")[0].equals("exit")){
	    			break;
	    		}
	    	}
	    	catch(IOException ex){
	    		System.out.println(ex.getMessage());
	    	}
    	}

	}

}
