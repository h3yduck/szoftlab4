package hu.bme.bitsplease.frames;

import javax.swing.*;
import javax.swing.plaf.SliderUI;
import java.awt.*;
import java.util.List;

/**
 * Created by h3yduck on 4/15/15.
 */
public class SettingsFrame extends JFrame {
    public JButton selectMap;
    public JSlider numOfPlayers;
    public JSlider numOfSpecialElements;
    public List<JTextField> playerNames;

    public SettingsFrame() throws HeadlessException {
    }
}
