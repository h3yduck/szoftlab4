package hu.bme.bitsplease.stepHandler;

/**
 * Created by h3yduck on 2/27/15.
 */
public class ConsoleInput implements InputHandler {
    @Override
    public Step getStep(String nameOfPlayer) {
        return null;
    }

    @Override
    public String getLevel(){
        return null;
    }

    @Override
    public int getNumOfPlayer(){
        return 0;
    }

    @Override
    public int getSpecialActionTypeNumber(){
        return 0;
    }

    @Override
    public String getRobotName(){
        return null;
    }

	@Override
	public int getGameLength() {
		return 0;
	}
}
