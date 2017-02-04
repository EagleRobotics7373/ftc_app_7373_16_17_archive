package org.firstinspires.ftc.teamcode.ER7373.sensors;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

/**
 * Created by JordanMoss on 1/31/17.
 */

public class MRRange {
    ModernRoboticsI2cRangeSensor range;
    double[] raw = new double[2];
    double[] calc = new double[2];

    public MRRange(ModernRoboticsI2cRangeSensor r, int addr){
        range = r;
        range.setI2cAddress(I2cAddr.create8bit(addr));
    }

    public MRRange(ModernRoboticsI2cRangeSensor r){
        range = r;
    }

    //method to return the raw values U : O
    public double[] raw(){
        raw[0] = range.rawUltrasonic();
        raw[1] = range.rawOptical();
        return raw;
    }

    //get calculated values U : O
    public double[] cm(){
        calc[0] = range.cmUltrasonic();
        calc[1] = range.cmOptical();
        return calc;
    }

    //get calculated values in inches U : O
    public double[] in(){
        double[] met = cm();
        calc[0] = met[0]/2.54;
        calc[1] = met[1]/2.54;
        return calc;
    }

}
