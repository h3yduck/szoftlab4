package hu.bme.bitsplease;

import hu.bme.bitsplease.gameEngine.GameEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestApp {

    private static final PrintStream originalPs = System.out;
    private static final List<String> settingCommands = Arrays.asList(
            "setMap", "setPlayers", "setRounds", "setSpecialActionNumber", "setPlayerName", "setPlayersPositionRandom",
            "setPlayerPosition", "setLittleRobotPositionRandom"
    );
    private static final List<String> playCommands = Arrays.asList(
            "step", "setPlayerVelocity", "displayLevel", "listPlayers",
            "listLittleRobots", "listSpecialPositions", "setLittleRobotPosition"
    );
    public static GameEngine gameEngine;
    public static boolean playerRandom = true;
    public static boolean littleRandom = true;

    public static void main(String[] args) {

        if (args.length >= 1 && args[0].equalsIgnoreCase("console")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            run(br, false);
        } else {
            File folder = new File("tests/");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    File fIn = listOfFiles[i];
                    if (!fIn.getName().endsWith("out")) {
                        File fOut = new File(folder, fIn.getName() + "_out");
                        if (fOut.isFile()) {
                            //System.out.println("FileIN  " + fIn.getName());
                            //System.out.println("FileOUT " + fOut.getName());

                            System.out.println("Fájl tesztelése: " + fIn.getName());
                            System.out.flush();
                            try {
                                BufferedReader brIn = new BufferedReader(new FileReader(fIn));
                                BufferedReader brOut = new BufferedReader(new FileReader(fOut));

                                List<String> res = run(brIn, true);
                                Boolean ok = true;
                                for (int j = 0; j < res.size(); j++) {
                                    String lineRes = res.get(j).trim();
                                    String lineOut = brOut.readLine();
                                    if (lineOut == null)
                                        lineOut = "";
                                    lineOut = lineOut.trim();

                                    if (!lineOut.equals(lineRes)) {
                                        System.out.println("    hiba itt : " + (j+1) + ". sor");
                                        System.out.println("  várt érték : " + lineOut);
                                        System.out.println(" kapott érték: " + lineRes);
                                        System.out.flush();
                                        ok = false;
                                        System.out.println("Sikertelen tesztelés!");
                                        System.out.flush();
                                        break;
                                    }
                                }
                                if (ok) {
                                    System.out.println("Sikeres tesztelés!");
                                    System.out.flush();
                                }
                            } catch (Exception e) {
                                System.out.println("váratlan hiba tesztelés közben: " + e.getMessage());
                                System.out.flush();
                            }

                        } else {
                            // cannot find out file
                        }
                        System.out.println();
                    }
                }
            }
        }


    }

    public static List<String> run(BufferedReader br, Boolean redirectOutput) {
        System.out.flush();
        System.err.flush();

        List<String> result = new ArrayList<String>();
        ByteArrayOutputStream baos = null;
        PrintStream ps = null;
        if (redirectOutput) {
            baos = new ByteArrayOutputStream();
            ps = new PrintStream(baos);
            System.setOut(ps);
        }

        gameEngine = new GameEngine();
        while (true) {
            try {
                String line = br.readLine();
                if (line == null)
                    break;

                if (settingCommands.contains(line.split(" ")[0])) {
                    gameEngine.getSettings(line);
                } else if (playCommands.contains(line.split(" ")[0])) {
                    gameEngine.play(line);
                } else if (line.split(" ")[0].equals("exit")) {
                    break;
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        System.out.flush();
        System.err.flush();
        if (redirectOutput) {
            System.setOut(originalPs);

            BufferedReader br2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
            String line;
            try {
                while ((line = br2.readLine()) != null) {
                    result.add(line);
                }
            } catch (Exception e) {
            }

            try {
                baos.close();
                br2.close();
                ps.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

}
