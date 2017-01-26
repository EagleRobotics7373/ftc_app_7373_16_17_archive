package org.firstinspires.ftc.teamcode.ER7373.sensors;

/**
 * Created by JordanMoss on 1/4/17.
 *
 * This class is for the Maxbotix Range finder using an analog connection on the CDIM
 *
 */
import com.qualcomm.robotcore.hardware.AnalogInput;


public class MaxbotixRangeObj {
    //paramaters
    AnalogInput range;
    double raw;
    double calculated;

    //values to approximate distance based on voltage
    public static final double[] corVoltage ={};
    public static final double[] corDistance = {};


    /**
     * constructor for the range sensor
     *
     * 1 arg for the sensor
     *
     * created using hardware map get for analogInput device
     */

    public MaxbotixRangeObj(AnalogInput r){ range = r;}

    //method to read the raw analog value
    public double rawRead(){
        return range.getVoltage();
    }

    //method to read the raw data and convert to distance
    public double distance(){
        raw = range.getVoltage();

        //code to convert using equation d in inches = 106.5 v + 0.0093
        calculated = 106.5*raw + 0.0093;

        return calculated;
    }
}
