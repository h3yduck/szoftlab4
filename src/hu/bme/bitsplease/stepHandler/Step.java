package hu.bme.bitsplease.stepHandler;

/**
 * Created by h3yduck on 2/27/15.
 */
public class Step {
    // iranyvaltoztatasi egysegvektor szoge + olaj/ragacs/semmi
    public enum ActionType {
    	OIL('O'),
        STICK('S');

    	// típust leíró karakter
        public char key;

        // Karakter beállítása
        ActionType(char key) {
            this.key = key;
        }
        
        // Karakterből beállítja a típust
        public static ActionType fromChar(char c) {
            for (ActionType t : ActionType.values())
                if (c == t.key)
                    return t;
            return null;
        }
    }
    
    public ActionType stepAction;
    public double angle;
    
    public Step(){
    	angle = -1;
    	stepAction = null;
    }

}
