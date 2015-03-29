package hu.bme.bitsplease.levelHandler;

import hu.bme.bitsplease.playerHandler.Robot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Created by h3yduck on 2/27/15.
 */
public class FileLoader implements LevelLoader {
    private String pathToLevelFile;

    public FileLoader(String pathToLevelFile) {
        this.pathToLevelFile = pathToLevelFile;
    }

    public void setPathToLevelFile(String pathToLevelFile) {
        this.pathToLevelFile = pathToLevelFile;
    }

    public FileLoader() {
    }

    @Override
    public Level getLevel() throws Exception {
    	
        BufferedReader br = null;
        try {
        	//BufferedReader a megfelelő file-hoz
            br = new BufferedReader(new FileReader(pathToLevelFile));
            String line;

            if ((line = br.readLine()) == null)
                throw new Exception("Invalid file format, maybe empty file");
            
            //Az első sor a pályát jelképező mátrix mérete, azaz két int
            String[] parts = line.split("\\s+");
            if (parts.length != 2)
                throw new Exception("Invalid file format");
            Integer sizeX = Integer.parseInt(parts[0]);
            Integer sizeY = Integer.parseInt(parts[1]);

            //Létrehozzuk az új pályát
            Level level = new Level();
            level.fields = new Field[sizeX][sizeY];

            //Beállítjuk a megfelelő Típusokat a mezőkre
            for (Integer x = 0; x < sizeX; x++) {
                if ((line = br.readLine()) == null)
                    throw new Exception("Invalid file format, missing line");

                if (line.length() != sizeY)
                    throw new Exception("Invalid file format, line length is not ok");

                for (Integer y = 0; y < sizeY; y++) {
                    Field.Type type = Field.Type.fromChar(line.charAt(y));
                    level.fields[x][y] = new Field(type, 10);
                }
            }

            //léttrehozzuk a játékosok pozícióit tároló Map-et
            level.playerPositions = new HashMap<Robot, Position>();
            return level;
        } catch (Exception e) {
            throw new Exception("Error on level file loading", e);
        }
        finally{
        	//Ha nem null a BufferedReader, akkor bezárjuk
        	if(br != null)
        		br.close();
        }
    }
}
