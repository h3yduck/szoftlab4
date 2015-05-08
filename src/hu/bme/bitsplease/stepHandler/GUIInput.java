package hu.bme.bitsplease.stepHandler;

import hu.bme.bitsplease.frames.PlayFrame;
import hu.bme.bitsplease.frames.SettingsFrame;

import javax.swing.*;
import java.io.File;

/**
 * Created by h3yduck on 4/15/15.
 */
public class GUIInput implements InputHandler {
    public SettingsFrame settingsFrame;
    public PlayFrame playFrame;

    private int curr = 0;

    @Override
    public Step getStep(String nameOfPlayer) {
        Step s = new Step();
        s.angle = Integer.parseInt(playFrame.angle.getText());
        if(playFrame.useOil.isSelected()){
            s.stepAction = Step.ActionType.OIL;
        } else if(playFrame.useStick.isSelected()){
            s.stepAction = Step.ActionType.STICK;
        }
        return s;
    }

    @Override
    public String getLevel() {
        curr = 0;
        return settingsFrame.mapPath;
    }

    @Override
    public int getNumOfPlayer() {
        return settingsFrame.numOfPlayers.getValue();
    }

    @Override
    public int getSpecialActionTypeNumber() {
        return settingsFrame.numOfSpecial.getValue();
    }

    @Override
    public int getGameLength() {
        return settingsFrame.numOfRounds.getValue();
    }

    @Override
    public String getRobotName() {
        return settingsFrame.playerNames.get(curr++).getText();
    }
}
