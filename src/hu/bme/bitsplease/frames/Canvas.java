package hu.bme.bitsplease.frames;

import hu.bme.bitsplease.levelHandler.Level;
import hu.bme.bitsplease.playerHandler.*;

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

    public Dimension getPreferredSize(){
        if(level != null) return new Dimension(level.fields[0].length * 40, level.fields.length * 40);
        return new Dimension();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int CELLSIZE = 40;

        g.setColor(Color.white);
        g.fillRect(0,0,CELLSIZE*level.fields[0].length*CELLSIZE, CELLSIZE*level.fields.length*CELLSIZE);

        g.setColor(Color.black);
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
                        g.fillRect(j*CELLSIZE, i*CELLSIZE, CELLSIZE, CELLSIZE);
                        break;
                    case OIL:
                        g.setColor(Color.red);
                        g.fillRect(j*CELLSIZE, i*CELLSIZE, CELLSIZE, CELLSIZE);
                        break;
                    case STICK:
                        g.setColor(Color.blue);
                        g.fillRect(j*CELLSIZE, i*CELLSIZE, CELLSIZE, CELLSIZE);
                        break;
                }
            }
        }

        g.setColor(Color.black);
        for(hu.bme.bitsplease.playerHandler.Robot robot: level.playerPositions.keySet()){
            if(robot instanceof Player){
                Player player = (Player) robot;
                g.drawString(player.name, level.playerPositions.get(robot).x * CELLSIZE, level.playerPositions.get(robot).y * CELLSIZE +20);
            } else if (robot instanceof LittleRobot){
                g.drawString("little", level.playerPositions.get(robot).x * CELLSIZE, level.playerPositions.get(robot).y * CELLSIZE+20);
            }
        }
    }
}
