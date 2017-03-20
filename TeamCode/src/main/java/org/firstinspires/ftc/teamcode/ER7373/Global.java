package org.firstinspires.ftc.teamcode.ER7373;

/**
 * Created by JordanMoss on 3/14/17.
 */

public class Global {
    /**
     * This class contains a series of global variables for 7373 robot for the Super Regional Championship
     *
     * These variables are all public static final so they can be accesed but not changed
     *
     * Best practice is to store these variables within local variables where they are needed to reduce processor time
     * and to reduce bloat due to long referecnes
     */

    //array for the floor tile
    //gray floor
    public static final int[] GRAYFLOORREADING = {2,2,2,2};
    //black tile
    public static final int[] BLACKFLOORREADING = {0,0,0,0};

    //servo positions
    //button presser positions
    public static final double LEFT_BUTTONPRESS_OUT = 1;
    public static final double LEFT_BUTTONPRESS_IN = 0;
    public static final double RIGHT_BUTTONPRESS_OUT = 0;
    public static final double RIGHT_BUTTONPRESS_IN = 1;

    //servos for ball stop
    public static final double BALL_STOP_CLOSED = 1;
    public static final double BALL_STOP_OPEN = 0;

    //

    //shooter optimal RPM
    public static final int SHOOTERRPM = 1200;

}
