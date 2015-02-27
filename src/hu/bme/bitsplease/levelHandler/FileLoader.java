package hu.bme.bitsplease.levelHandler;

/**
 * Created by h3yduck on 2/27/15.
 */
public class FileLoader implements LevelLoader{
    private String pathToLevelFile;

    public FileLoader(String pathToLevelFile) {
        this.pathToLevelFile = pathToLevelFile;
    }

    public void setPathToLevelFile(String pathToLevelFile) {
        this.pathToLevelFile = pathToLevelFile;
    }

    @Override

    public Level getLevel() {
        return null;
    }
}
