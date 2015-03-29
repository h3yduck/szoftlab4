package hu.bme.bitsplease.stepHandler;

/**
 * Created by h3yduck on 2/27/15.
 */
public interface InputHandler {
    public Step getStep(String nameOfPlayer);
    public String getLevel();
    public int getNumOfPlayer();
    public int getSpecialActionTypeNumber();
    public int getGameLength();
    public String getRobotName();
}
