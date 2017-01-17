package org.firstinspires.ftc.teamcode.ER7373.mechanics;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Jordan Moss on 9/24/2016.
 */
public class Mecanum {
    //create parameter variables for each motor
    private DcMotor leftfront;
    private DcMotor leftrear;
    private DcMotor rightfront;
    private DcMotor rightrear;

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
    public void run(float x, float y, float z)
    {
        //calculate each wheel power and clip it
        float powLF = x + y + z;
        powLF = (float)Range.clip(powLF, -1, 1);
        float powLR = x - y + z;
        powLR = (float)Range.clip(powLR, -1, 1);
        float powRF = x - y - z;
        powRF = (float)-Range.clip(powRF, -1, 1);
        float powRR = x + y - z;
        powRR =(float)-Range.clip(powRR, -1, 1);

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
    public void run(float x, float y, float z, float k)
    {
        //calculate each wheel power and clip it
        float powLF = x + y + z;
        powLF = (float)Range.clip(powLF, -1, 1);
        float powLR = x - y + z;
        powLR = (float)Range.clip(powLR, -1, 1);
        float powRF = x - y - z;
        powRF = (float)-Range.clip(powRF, -1, 1);
        float powRR = x + y - z;
        powRR =(float)-Range.clip(powRR, -1, 1);

        //send the power to each wheel
        leftfront.setPower(k*powLF);
        leftrear.setPower(k*powLR);
        rightfront.setPower(k*powRF);
        rightrear.setPower(k*powRR);

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
