package hu.bme.bitsplease.frames;

import javax.swing.*;
import java.awt.*;

/**
 * Created by h3yduck on 4/15/15.
 */
public class PlayFrame extends JFrame{
    public JCheckBox useStick;
    public JCheckBox useOil;
    public JButton doStep;
    public JTextField angle;
    public JLabel playerName, round, numOfOil, numOfStick;
    public JPanel canvas;

    public PlayFrame() throws HeadlessException {
    }
}
