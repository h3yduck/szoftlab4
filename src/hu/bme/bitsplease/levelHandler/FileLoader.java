package hu.bme.bitsplease.levelHandler;

import hu.bme.bitsplease.playerHandler.Robot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

	@Override
	public Level getLevel() {

		BufferedReader br = null;
		Level level = null;
		try {
			// BufferedReader a megfelelő file-hoz
			if(pathToLevelFile == null){
				throw new IOException("Nem megfelelő a megadott elérési út!");
			}
			br = new BufferedReader(new FileReader(pathToLevelFile));
			String line;

			if ((line = br.readLine()) == null) {
				br.close();
				throw new IOException("Nem megfelelő a fájl formátuma");
			}

			// Az első sor a pályát jelképező mátrix mérete, azaz két int
			String[] parts = line.split("\\s+");
			if (parts.length != 2) {
				br.close();
				throw new IOException(
						"Nem megfelelő a fájl formátuma, első sor");
			}
			Integer sizeX = Integer.parseInt(parts[0]);
			Integer sizeY = Integer.parseInt(parts[1]);

			// Létrehozzuk az új pályát
			level = new Level();
			level.fields = new Field[sizeX][sizeY];

			// Beállítjuk a megfelelő Típusokat a mezőkre
			for (Integer x = 0; x < sizeX; x++) {
				// Beolvassuk az új sort, ha nincs már sor, akkor nem
				// megfelelő a fájl, mert kevesebb sor van, mint a fájl
				// elején meg van adva
				if ((line = br.readLine()) == null) {
					br.close();
					throw new IOException(
							"Nem megfelelő a fájl formátuma, hiányzó sor.");
				}
				// Nem megfelelő a fájl, ha egy sor hossza nem egyezik meg a
				// fájl elején megadott oszlopszámmal
				if (line.length() != sizeY) {
					br.close();
					throw new IOException(
							"Nem megfelelő a fájl formátuma, sor hossza nem megfelelő.");
				}

				for (Integer y = 0; y < sizeY; y++) {
					Field.Type type = Field.Type.fromChar(line.charAt(y));
					level.fields[x][y] = new Field(type, 0);
				}
			}

			// léttrehozzuk a játékosok pozícióit tároló Map-et
			level.playerPositions = new HashMap<Robot, Position>();

			br.close();
		} catch (IOException ex) {

			// Ha nem megfelelő a megadott fájl, akkor újra bekérjük a fájl
			// nevét.
			System.err.println("Kérem adjon meg másik fájlt!");

			// null-al térünk vissza ha hiba van
			return null;
		}
		return level;
	}
}
