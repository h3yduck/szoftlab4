package hu.bme.bitsplease.stepHandler;

import javax.swing.*;

/**
 * Created by h3yduck on 4/15/15.
 */
public class GUIInput implements InputHandler {
    public JFrame settingsFrame;
    public JFrame playFrame;

    @Override
    public Step getStep(String nameOfPlayer) {
        return null;
    }

    @Override
    public String getLevel() {
        return null;
    }

    @Override
    public int getNumOfPlayer() {
        return 0;
    }

    @Override
    public int getSpecialActionTypeNumber() {
        return 0;
    }

    @Override
    public int getGameLength() {
        return 0;
    }

    @Override
    public String getRobotName() {
        return null;
    }
}
