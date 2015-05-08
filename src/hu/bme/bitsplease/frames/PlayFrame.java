/*
 * Created by JFormDesigner on Fri May 08 17:13:48 CEST 2015
 */

package hu.bme.bitsplease.frames;

import hu.bme.bitsplease.gameEngine.GameEngine;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author qwe asd
 */
public class PlayFrame extends JFrame {
    public PlayFrame() {
        initComponents();
    }

    private void initComponents() {
//        HOZZÁ NE NYÚLJ BENCEEEE
        label1 = new JLabel();
        playerName = new JLabel();
        angle = new JTextField();
        useOil = new JCheckBox();
        useStick = new JCheckBox();
        numOfOil = new JLabel();
        numOfStick = new JLabel();
        round = new JLabel();
        doStep = new JButton();
        scrollPane1 = new JScrollPane();
        canvas = new Canvas();

        //======== this ========
        setResizable(false);
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("K\u00f6r");

        //---- playerName ----
        playerName.setText("J\u00e1t\u00e9kos");

        //---- useOil ----
        useOil.setText("Olajfolt");

        //---- useStick ----
        useStick.setText("Ragacs");

        //---- doStep ----
        doStep.setText("Lép");

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(canvas);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addComponent(label1)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(round)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(numOfOil))
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addComponent(playerName)
                                                                .addGap(180, 180, 180)
                                                                .addComponent(angle, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(useOil)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(useStick)
                                                        .addComponent(numOfStick))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(doStep)))
                                .addContainerGap(3, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label1)
                                                .addComponent(round))
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(numOfOil)
                                                .addComponent(numOfStick)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(playerName)
                                        .addComponent(angle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(useOil)
                                        .addComponent(useStick)
                                        .addComponent(doStep))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(4, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(getOwner());

        doStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameEngine.notifyThread();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        canvas.repaint();
                    }
                });
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JLabel label1;
    public JLabel playerName;
    public JTextField angle;
    public JCheckBox useOil;
    public JCheckBox useStick;
    public JLabel numOfOil;
    public JLabel numOfStick;
    public JLabel round;
    public JButton doStep;
    public JScrollPane scrollPane1;
    public Canvas canvas;
    public GameEngine gameEngine;
}
