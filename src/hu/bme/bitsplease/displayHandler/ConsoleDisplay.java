package hu.bme.bitsplease.displayHandler;

import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.levelHandler.Position;
import hu.bme.bitsplease.playerHandler.Player;
import hu.bme.bitsplease.playerHandler.Robot;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by h3yduck on 2/27/15.
 */
public class ConsoleDisplay implements DisplayHandler {
    @Override
    public void displayLevel(Level actualLevelState) {
    	for(int i = 0; i < actualLevelState.fields.length; i++){
    		for(int k = 1; k <= 3; k++){
				for(int j = 0; j < actualLevelState.fields[i].length; j++){
					for(int l = 1; l <= 3; l++){
						if(k == 2 && l == 2){
							String myChar = String.valueOf(actualLevelState.fields[i][j].fieldType.key);
							for(Entry<Robot, Position> robot : actualLevelState.playerPositions.entrySet()){
								if(robot.getValue().x == j && robot.getValue().y == i){
									if(robot.getClass().toString().equals("class hu.bme.bitsplease.playerHandler.LittleRobot")){
										myChar = "L";
										break;
									}else{
										Player p = (Player) robot.getKey();
										myChar = String.valueOf(p.number);
									}
								}
							}
							System.out.print(myChar);
						}else{
							System.out.print(actualLevelState.fields[i][j].fieldType.key);
						}
					}
					System.out.print(" ");
				}
				System.out.println();
    		}
    		System.out.println();
		}
    }
    
    public void displaySpecialActionTypesNumber(int oil, int stick){
    	System.out.println("Olaj: " + oil + ", Ragacs: " + stick);
    	System.out.println();
    }

    @Override
    public void displayScore(Map<Player, Integer> score){
    	for(Entry<Player, Integer> entry : score.entrySet()){
    		System.out.println(entry.getKey().name + " - " + entry.getValue());
    	}
    	System.out.println();
    }

    @Override
    public void displayRound(String round){
    	System.out.println("Hátralévő körök száma: " + round);
    	System.out.println();
    }

    @Override
    public void displayCongrat(String player){
    	System.out.println("Gratulálok " + player + " te nyertél!");
    	System.out.println();
    }

	@Override
	public void displayError(String error) {
		System.err.println(error);
		System.err.println();
	}
}
