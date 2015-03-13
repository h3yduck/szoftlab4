package hu.bme.bitsplease.stepHandler;

import hu.bme.bitsplease.levelHandler.Level;

/**
 * Created by h3yduck on 2/27/15.
 */
public interface InputHandler {
    public Step getStep();
    public Level getLevel();
    public int getNumOfPlayer();
    public int getSpecialActionTypeNumber();
    public String getRobotName();
}
