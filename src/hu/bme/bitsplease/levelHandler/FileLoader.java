package hu.bme.bitsplease.levelHandler;

import hu.bme.bitsplease.App;
import hu.bme.bitsplease.playerHandler.Player;

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
    	if(App.menuItem == 1)
    		App.printList("[:FileLoader]getLevel");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(pathToLevelFile));
            String line;

            if ((line = br.readLine()) == null)
                throw new Exception("Invalid file format, maybe empty file");
            String[] parts = line.split("\\s+");
            if (parts.length != 2)
                throw new Exception("Invalid file format");
            Integer sizeX = Integer.parseInt(parts[0]);
            Integer sizeY = Integer.parseInt(parts[1]);

            Level level = new Level();
            level.fields = new Field[sizeX][sizeY];

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

            level.playerPositions = new HashMap<Player, Position>();
            return level;
        } catch (Exception e) {
            throw new Exception("Error on level file loading", e);
        }
        finally{
        	br.close();
        }
    }
}
