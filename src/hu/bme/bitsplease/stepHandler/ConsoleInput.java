package hu.bme.bitsplease.stepHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by h3yduck on 2/27/15.
 */
public class ConsoleInput implements InputHandler {

	/*
	 * Ez az osztály felelős az adatok bekéréséért a standard inputon. Kezeli a
	 * nem megelelően megadott bemenetet. Minden függvényben létrehozunk egy
	 * BufferedReader-t, amivel beolvassuk a felhasználó által megadott sorokat.
	 * A BufferedReader-eket bezárjuk a függvény végén. A goodInput változóval
	 * vizsgáljuk, hogy megfelelő e a bemenet. Ha nem megfelelő, akkor kiírja
	 * hibát és újrakéri az adatot.
	 */

	private BufferedReader br;
	private String line;

	public ConsoleInput() {
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public Step getStep(String nameOfPlayer) {
		Step newStep = null;
		line = null;
		boolean goodInput = false;
		while (!goodInput) {
			newStep = null;
			goodInput = true;
			try {
				newStep = new Step();
				// Kiírjuk a robot nevét, ezzel jelölve, hogy éppen melyik robot
				// lép.
				System.out
						.println(nameOfPlayer + " add meg a következő lépést:");
				System.out
						.println("A lépés szöge? (negatív szög esetén nem változik a sebesség)");
				line = br.readLine();

				// Megvizsgáljuk, hogy számot adott e meg a felhasználó. Ha nem,
				// akkor hiba, és újrakérjük.
				double lineDouble = Double.parseDouble(line);

				// A megadott szám 360-al vett maradékát vesszük, így a szám
				// -360 és 360 között lesz.
				newStep.angle = lineDouble % 360;
				System.out.println("Olajfolt(\'O\') vagy ragacs(\'S\')?");
				line = br.readLine();

				// Kivesszük a megadott string-ből a whitespace karaktereket.
				line.replaceAll("\\s+", "");

				switch (line) {
				// Ha 'O' vagy 'S', akkor beállítjuk a karakterből a foltot.
				case "O":
				case "S":
					newStep.stepAction = Step.ActionType.fromChar(line
							.charAt(0));
					break;

				// Egyébként, ha nem üres a string, akkor nem megfelelő a
				// bemenet, ezért újrakérjük.
				default:
					if (line.length() > 0) {
						throw new IOException(
								"A megadott bemenet nem megfelelő!");
					}
				}

			} catch (NumberFormatException ex) {
				// Ha a parse nem sikerül, akkor újrakerjük az adatot.
				System.err.println("A megadott bemenet nem megfelelő!");
				System.err.println();
				goodInput = false;
			} catch (IOException ex) {
				System.err.println("A megadott bemenet nem megfelelő!");
				System.err.println();
				goodInput = false;
			}
		}
		return newStep;
	}

	@Override
	public String getLevel() {
		boolean goodInput = false;
		line = null;
		while (!goodInput) {
			goodInput = true;
			try {
				System.out.println("Kérem adja meg a pálya elérési útját!");
				line = br.readLine();

				// Megnézzük, hogy létezik e a megadott elérési úton normál
				// fájl.
				File newFile = new File(line);
				if (!newFile.isFile()) {
					throw new IOException("A megadott fájl nem elérhető");
				}
			} catch (IOException ex) {
				// Ha nem létezik fájl, akkor újrakérjük.
				System.err.println(ex.getMessage());
				System.err.println();
				goodInput = false;
			}
		}
		return line;
	}

	@Override
	public int getNumOfPlayer() {
		boolean goodInput = false;
		line = null;

		// 0-nál nagyobbnak kell lennie
		int lineInt = 0;
		while (!goodInput) {
			lineInt = 0;
			goodInput = true;
			try {
				System.out.println("Kérem adja meg a játékosok számát!");
				line = br.readLine();

				// Integer-t csinálunk a bemenetből
				lineInt = Integer.parseInt(line);

				// A játékosok számának legalább kettőnek kell lennie
				if (lineInt < 2) {
					throw new IOException("A megadott bemenet nem megfelelő");
				}
			} catch (NumberFormatException ex) {
				// Ha nem megfelelő a bemenet, akkor újrakérjük
				System.err.println("A megadott bemenet nem megfelelő");
				System.err.println();
				goodInput = false;
			} catch (IOException ex) {
				// Ha nem megfelelő a bemenet, akkor újrakérjük
				System.err.println("A megadott bemenet nem megfelelő");
				System.err.println();
				goodInput = false;
			}
		}
		return lineInt;
	}

	@Override
	public int getSpecialActionTypeNumber() {
		boolean goodInput = false;
		line = null;

		// Lehet 0, ezért negatív az alap érték
		int lineInt = -1;
		while (!goodInput) {
			lineInt = 0;
			goodInput = true;
			try {
				System.out.println("Kérem adja meg a foltkészlet méretét!");
				line = br.readLine();

				// Integer-t csinálunk a bementből
				lineInt = Integer.parseInt(line);

				// Ha kisebb, mint nulla, akkor nem megfelelő a bemenet
				if (lineInt < 0) {
					throw new IOException("A megadott bemenet nem megfelelő");
				}
			} catch (NumberFormatException ex) {
				// Ha nem megfelelő a bemenet, akkor újrakérjük
				System.err.println("A megadott bemenet nem megfelelő");
				System.err.println();
				goodInput = false;
			} catch (IOException ex) {
				// Ha nem megfelelő a bemenet, akkor újrakérjük
				System.err.println("A megadott bemenet nem megfelelő");
				System.err.println();
				goodInput = false;
			}
		}
		return lineInt;
	}

	@Override
	public String getRobotName() {
		boolean goodInput = false;
		line = null;
		while (!goodInput) {
			try {
				goodInput = true;
				System.out.println("Kérem adja meg a robot nevét!");
				line = br.readLine();

				// A névnek legalább 1 karakterből kell állnia
				if (line.length() < 1) {
					System.out
							.println("A névnek legalább 1 karakterből kell állnia!");
					System.out.println();
					goodInput = false;
				}
			} catch (IOException ex) {
				// Ha nem megfelelő a bemenet, akkor újrakérjük
				System.err.println("A megadott bemenet nem megfelelő");
				System.err.println();
				goodInput = false;
			}
		}
		return line;
	}

	@Override
	public int getGameLength() {
		boolean goodInput = false;
		line = null;

		// 0-nál nagyobbnak kell lennie
		int lineInt = 0;
		while (!goodInput) {
			lineInt = 0;
			goodInput = true;
			try {
				System.out.println("Kérem adja meg a játék hosszát!");
				line = br.readLine();

				// Integer-t csinálunk a bementből
				lineInt = Integer.parseInt(line);

				// Ha kisebb, mint 10, akkor nem megfelelő a bemenet
				if (lineInt < 10) {
					throw new IOException(
							"A megadott bemenet nem megfelelő, a játék hossza legalább 10!");
				}
			} catch (NumberFormatException ex) {
				// Ha nem megfelelő a bemenet, akkor újrakérjük
				System.err.println("A megadott bemenet nem megfelelő");
				System.err.println();
				goodInput = false;
			} catch (IOException ex) {
				// Ha nem megfelelő a bemenet, akkor újrakérjük
				System.err.println("A megadott bemenet nem megfelelő");
				System.err.println();
				goodInput = false;
			}
		}

		return lineInt;
	}
}
