package hu.bme.bitsplease.levelHandler;

/**
 * Created by h3yduck on 2/27/15.
 */

public class Field {
    public enum Type {
        USRPOS('U'),
        HOLE('H'),
        FREE('F'),
        OIL('O'),
        STICK('S');

        public char key;

        Type(char key) {
            this.key = key;
        }

        public static Type fromChar(char c) {
            for (Type t : Type.values())
                if (c == t.key)
                    return t;
            return null;
        }
    }

    public Type fieldType;
    public int remainingRounds;

    public Field() {
        fieldType = Type.FREE;
        remainingRounds = 0;
    }

    public Field(Type fieldType, int remainingRounds) {
        this.fieldType = fieldType;
        this.remainingRounds = remainingRounds;
    }
}
