package hu.bme.bitsplease.frames;

import hu.bme.bitsplease.levelHandler.Level;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Szabolcs on 2015.05.08..
 */
public class Canvas extends JPanel{
    public Level level;

    public Canvas() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int CELLSIZE = 40;
        this.setPreferredSize(new Dimension(level.fields[0].length * CELLSIZE, level.fields.length * CELLSIZE));

        for(int i = 0; i < level.fields.length; ++i) {
            g.drawLine(0, (i + 1) * CELLSIZE, level.fields[0].length*CELLSIZE, (i+1)*CELLSIZE);
        }
        for(int i = 0; i < level.fields[0].length; ++i){
            g.drawLine((i + 1) * CELLSIZE, 0, (i + 1) * CELLSIZE, level.fields.length*CELLSIZE);
        }

        for(int i = 0; i < level.fields.length; ++i) {
            for(int j = 0; j < level.fields[i].length; ++j) {
                switch (level.fields[i][j].fieldType){
                    case HOLE:
                        g.setColor(Color.black);
                        g.fillRect(i*CELLSIZE, j*CELLSIZE, CELLSIZE, CELLSIZE);
                        break;
                }
            }
        }
    }
}
