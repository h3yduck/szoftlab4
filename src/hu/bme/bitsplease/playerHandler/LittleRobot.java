package hu.bme.bitsplease.playerHandler;


public class LittleRobot extends Robot {
	
	//Tárolja, hogy még meddig takarít
	private int remainingCleaningTime;
	
	public LittleRobot(){
		remainingCleaningTime = 0;
		velocity = new Velocity();
		velocity.size = 1;
	}
	
	public boolean isCleaning() {
		if(remainingCleaningTime > 0){
			remainingCleaningTime--;
			return true;
		}
		return false;
	}
}
