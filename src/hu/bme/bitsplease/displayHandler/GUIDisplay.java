package hu.bme.bitsplease.displayHandler;

import hu.bme.bitsplease.frames.PlayFrame;
import hu.bme.bitsplease.frames.SettingsFrame;
import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.playerHandler.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by h3yduck on 4/15/15.
 */
public class GUIDisplay implements DisplayHandler {
    public SettingsFrame settingsFrame;
    public PlayFrame playFrame;

    public GUIDisplay() {
        settingsFrame = new SettingsFrame();
        playFrame = new PlayFrame();
    }

    @Override

    public void displayLevel(Level actualLevelState) {
        playFrame.canvas.level = actualLevelState;
        playFrame.canvas.repaint();
    }

    @Override
    public void displayScore(Map<Player, Integer> score) {

    }

    @Override
    public void displayRound(String round) {
        playFrame.round.setText(round);
    }

    @Override
    public void displayCongrat(String player) {
        JOptionPane.showMessageDialog(null, "Gratulalok " + player + " te nyertel!", "ConGrat", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void displaySpecialActionTypesNumber(int oil, int stick) {
        playFrame.numOfOil.setText(""+oil);
        playFrame.numOfStick.setText(""+stick);
    }

    @Override
    public void displayError(String error) {
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void displayPlayerName(String name) {
        playFrame.playerName.setText(name);
    }
}
