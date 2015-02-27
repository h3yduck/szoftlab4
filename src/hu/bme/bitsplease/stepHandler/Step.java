package hu.bme.bitsplease.stepHandler;

/**
 * Created by h3yduck on 2/27/15.
 */
public class Step {
    // iranyvaltoztatasi egysegvektor szoge + olaj/ragacs/semmi
    public enum ActionType {
        OIL, STICK
    }

    public ActionType stepAction;
    public double angle;
}
