package org.firstinspires.ftc.teamcode.ER7373.mechanics;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.lang.Math;

/**
 * Created by Jordan Moss on 9/24/2016.
 */
public class Mecanum {
    // constants
    private static final double pi4 = Math.PI/4;
    //create parameter variables for each motor
    private DcMotor leftfront;
    private DcMotor leftrear;
    private DcMotor rightfront;
    private DcMotor rightrear;

    //drive power values
    private double powLF;
    private double powLR;
    private double powRF;
    private double powRR;
    private double[] power = new double[4];

    //constructor for mecanum object with 4 parameters of each motor on the drive train
    public Mecanum(DcMotor lf, DcMotor lr, DcMotor rf, DcMotor rr){
        //set the private vars equal to the parameters of the object
        leftfront = lf;
        leftrear = lr;
        rightfront = rf;
        rightrear = rr;
    }
    /*
    Method for running the mecanum wheels
    x is input for forward/reverse
    y is input for left/right
    z is input for rotation
     */

    //general form to run meacanum wheels with a deadband
    public void run(float x, float y, float z)
    {
        //set a deadzone
        if(x <= .05 && x >= -.05) x = 0;
        if(y <= .05 && y >= -.05) y = 0;
        if(z <= .05 && z >= -.05) z = 0;

        //calculate each wheel power and clip it
        float powLF = x + y + z;
        powLF = Range.clip(powLF, -1, 1);
        float powLR = x - y + z;
        powLR = -Range.clip(powLR, -1, 1);
        float powRF = x - y - z;
        powRF = -Range.clip(powRF, -1, 1);
        float powRR = x + y - z;
        powRR = Range.clip(powRR, -1, 1);

        //send the power to each wheel
        leftfront.setPower(powLF);
        leftrear.setPower(powLR);
        rightfront.setPower(.8*powRF);
        rightrear.setPower(.8*powRR);

    }



    //method to run 7373 meacnum wheels with a deadband
    //x is input for forward/reverse
    //y is input for left/right
    //z is input for rotation
    public void run73(double x, double y, double z)
    {
        //set a deadzone
        if(x <= .05 && x >= -.05) x = 0;
        if(y <= .05 && y >= -.05) y = 0;
        if(z <= .05 && z >= -.05) z = 0;

        //calculate each wheel power and clip it
        double powLF = x + y + z;
        powLF = Range.clip(powLF, -1, 1);
        double powLR = x - y + z;
        powLR = Range.clip(powLR, -1, 1);
        double powRF = x - y - z;
        powRF = -Range.clip(powRF, -1, 1);
        double powRR = x + y - z;
        powRR = -Range.clip(powRR, -1, 1);

        //send the power to each wheel
        leftfront.setPower(powLF);
        leftrear.setPower(powLR);
        rightfront.setPower(powRF);
        rightrear.setPower(powRR);

    }

    //method to run 7373 meacnum wheels without a deadband
    //x is input for forward/reverse
    //y is input for left/right
    //z is input for rotation
    public void run73ND(double x, double y, double z)
    {
        //calculate each wheel power and clip it
        double powLF = x + y + z;
        powLF = Range.clip(powLF, -1, 1);
        double powLR = x - y + z;
        powLR = Range.clip(powLR, -1, 1);
        double powRF = x - y - z;
        powRF = -Range.clip(powRF, -1, 1);
        double powRR = x + y - z;
        powRR = -Range.clip(powRR, -1, 1);

        //send the power to each wheel
        leftfront.setPower(powLF);
        leftrear.setPower(powLR);
        rightfront.setPower(powRF);
        rightrear.setPower(powRR);

    }

    /**
     Method for running the mecanum wheels
     x is input for forward/reverse
     y is input for left/right
     z is input for rotation
     k is coef for drive
     */
    public void runCoef(float x, float y, float z, float k)
    {
        //set a deadzone
        if(x <= .05 && x >= -.05) x = 0;
        if(y <= .05 && y >= -.05) y = 0;
        if(z <= .05 && z >= -.05) z = 0;

        //calculate each wheel power and clip it
        powLF = x + y + z;
        powLF = (float)k*Range.clip(powLF, -1, 1);
        powLR = x - y + z;
        powLR = (float)-k*Range.clip(powLR, -1, 1);
        powRF = x - y - z;
        powRF = (float)-k*Range.clip(powRF, -1, 1);
        powRR = x + y - z;
        powRR =(float)k*Range.clip(powRR, -1, 1);

    }

    //return drive vals
    public double[] runVal(){
        //send the power to each wheel
        leftfront.setPower(powLF);
        power[0] = powLF;
        leftrear.setPower(powLR);
        power[1] = powLR;
        rightfront.setPower(powRF);
        power[2] = powRF;
        rightrear.setPower(powRR);
        power[3] = powRR;

        return power;
    }

    /**
     * Method for running the wheels a set distance forward or backward
     *
     * Reset Encoders
     *
     * Converts a distance in inches to ticks to run the wheels
     * inches/4 = rotations
     * rotations * 1120 = ticks        ** Cast to Int and round
     *
     *
     */
    public void runDistance(int inches, float power){
        double rotations = inches / 4;
        double ticks = rotations * 1120;
        ticks = (int)(ticks +.5);

        rightrear.setMode(DcMotor.RunMode.RESET_ENCODERS);
        rightrear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while(rightrear.getCurrentPosition() != ticks){
            if(ticks > 0) {
                run(power, 0, 0);
            } else {
                run(-power,0,0);
            }
        }


    }

    //class to stop wheels
    public void stop(){
        //stop all motors
        leftfront.setPower(0);
        leftrear.setPower(0);
        rightfront.setPower(0);
        rightrear.setPower(0);
    }
}
