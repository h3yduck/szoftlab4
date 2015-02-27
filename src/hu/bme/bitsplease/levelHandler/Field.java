package hu.bme.bitsplease.levelHandler;

/**
 * Created by h3yduck on 2/27/15.
 */

public class Field {
    public enum Type {
        USRPOS, HOLE, FREE, OIL, STICK
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
