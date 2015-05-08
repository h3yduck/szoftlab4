/*
 * Created by JFormDesigner on Fri May 08 17:37:01 CEST 2015
 */

package hu.bme.bitsplease.frames;

import hu.bme.bitsplease.gameEngine.GameEngine;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.GroupLayout;

public class SettingsFrame extends JFrame {
    public SettingsFrame() {
        initComponents();
    }

    private void initComponents() {
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        numOfPlayers = new JSlider();
        selectMap = new JButton();
        numOfSpecial = new JSlider();
        numOfRounds = new JSlider();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        textField4 = new JTextField();
        textField5 = new JTextField();
        textField6 = new JTextField();
        textField7 = new JTextField();
        textField8 = new JTextField();
        ok = new JButton();

        //======== this ========
        setResizable(false);
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("J\u00e1t\u00e9kosok sz\u00e1ma");

        //---- label2 ----
        label2.setText("Speci\u00e1lis elemek sz\u00e1ma");

        //---- label3 ----
        label3.setText("K\u00f6r\u00f6k sz\u00e1ma");

        //---- label4 ----
        label4.setText("J\u00e1t\u00e9kos  1");

        //---- label6 ----
        label6.setText("J\u00e1t\u00e9kos 2");

        //---- label7 ----
        label7.setText("J\u00e1t\u00e9kos 3");

        //---- label8 ----
        label8.setText("J\u00e1t\u00e9kos 4");

        //---- label9 ----
        label9.setText("J\u00e1t\u00e9kos 5");

        //---- label10 ----
        label10.setText("J\u00e1t\u00e9kos 6");

        //---- label11 ----
        label11.setText("J\u00e1t\u00e9kos 7");

        //---- label12 ----
        label12.setText("J\u00e1t\u00e9kos 8");

        //---- selectMap ----
        selectMap.setText("P\u00e1lya bet\u00f6lt\u00e9se");

        ok.setText("OK");

        numOfPlayers.setMinimum(2);
        numOfPlayers.setMaximum(8);

        //Turn on labels at major tick marks.
        numOfPlayers.setMajorTickSpacing(1);
        numOfPlayers.setMinorTickSpacing(1);
        numOfPlayers.setPaintTicks(true);
        numOfPlayers.setPaintLabels(true);

        numOfSpecial.setMinimum(3);
        numOfSpecial.setMaximum(10);

        //Turn on labels at major tick marks.
        numOfSpecial.setMajorTickSpacing(1);
        numOfSpecial.setMinorTickSpacing(1);
        numOfSpecial.setPaintTicks(true);
        numOfSpecial.setPaintLabels(true);

        numOfRounds.setMinimum(20);
        numOfRounds.setMaximum(60);

        //Turn on labels at major tick marks.
        numOfRounds.setMajorTickSpacing(10);
        numOfRounds.setMinorTickSpacing(1);
        numOfRounds.setPaintTicks(true);
        numOfRounds.setPaintLabels(true);



        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(label1)
                                                        .addComponent(label2)
                                                        .addComponent(label3)
                                                        .addComponent(label4))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(numOfSpecial, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(selectMap, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(ok, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(numOfPlayers, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(numOfRounds, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(textField1))))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(label6)
                                                        .addComponent(label7)
                                                        .addComponent(label8)
                                                        .addComponent(label9)
                                                        .addComponent(label10)
                                                        .addComponent(label11)
                                                        .addComponent(label12))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(textField8, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                                        .addComponent(textField7, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                                        .addComponent(textField6, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                                        .addComponent(textField5, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                                        .addComponent(textField4, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                                        .addComponent(textField3, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                                        .addComponent(textField2, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))))
                                .addContainerGap(5, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(ok)
                                .addComponent(selectMap)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(numOfPlayers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label2)
                                        .addComponent(numOfSpecial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label3)
                                        .addComponent(numOfRounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label4)
                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label6)
                                        .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label7)
                                        .addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label8)
                                        .addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label9)
                                        .addComponent(textField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label10)
                                        .addComponent(textField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label11)
                                        .addComponent(textField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label12)
                                        .addComponent(textField8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        playerNames = new ArrayList<>();
        playerNames.add(textField1);
        playerNames.add(textField2);
        playerNames.add(textField3);
        playerNames.add(textField4);
        playerNames.add(textField5);
        playerNames.add(textField6);
        playerNames.add(textField7);
        playerNames.add(textField8);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!gameEngine.getSettings(null)){
                    gameEngine.input.settingsFrame.dispose();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(!gameEngine.play(null));
                        }
                    });
                    t.start();
                    gameEngine.input.playFrame.setVisible(true);
                }
            }
        });

        selectMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();

                fc.setCurrentDirectory(new File(".\\maps"));
                int returnVal = fc.showOpenDialog(SettingsFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName() + ".");
                    mapPath = file.getAbsolutePath();
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JLabel label1;
    public JLabel label2;
    public JLabel label3;
    public JLabel label4;
    public JLabel label6;
    public JLabel label7;
    public JLabel label8;
    public JLabel label9;
    public JLabel label10;
    public JLabel label11;
    public JLabel label12;
    public JSlider numOfPlayers;
    public JButton selectMap;
    public JSlider numOfSpecial;
    public JSlider numOfRounds;
    public JTextField textField1;
    public JTextField textField2;
    public JTextField textField3;
    public JTextField textField4;
    public JTextField textField5;
    public JTextField textField6;
    public JTextField textField7;
    public JTextField textField8;
    public java.util.List<JTextField> playerNames;
    public JButton ok;
    public GameEngine gameEngine;
    public String mapPath = null;
}
