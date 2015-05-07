package hu.bme.bitsplease.displayHandler;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.playerHandler.Player;
import hu.bme.bitsplease.stepHandler.InputHandler;
import hu.bme.bitsplease.stepHandler.Step;

import javax.swing.*;
import java.util.Map;

/**
 * Created by h3yduck on 4/15/15.
 */
public class GUIDisplay implements DisplayHandler {
    public JFrame settingsFrame;
    public JFrame playFrame;

    @Override
    public void displayLevel(Level actualLevelState) {

    }

    @Override
    public void displayScore(Map<Player, Integer> score) {

    }

    @Override
    public void displayRound(String round) {

    }

    @Override
    public void displayCongrat(String player) {

    }

    @Override
    public void displaySpecialActionTypesNumber(int oil, int stick) {

    }

    @Override
    public void displayError(String error) {

    }
}
