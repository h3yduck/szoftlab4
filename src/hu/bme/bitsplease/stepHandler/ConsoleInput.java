package hu.bme.bitsplease.stepHandler;

import hu.bme.bitsplease.levelHandler.Level;

/**
 * Created by h3yduck on 2/27/15.
 */
public class ConsoleInput implements InputHandler {
    @Override
    public Step getStep() {
        return null;
    }

    @Override
    public Level getLevel(){
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
}
