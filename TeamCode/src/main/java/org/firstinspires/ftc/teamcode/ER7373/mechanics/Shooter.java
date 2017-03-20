package org.firstinspires.ftc.teamcode.ER7373.mechanics;

/**
 * Created by Jordan Moss on 10/9/2016.
 *
 * This class has one constructor Shooter which has 2 paramters for the left and right motor
 *
 * This class has 4 methods
 *
 * powerRun has 1 arg power which is a float that run the shooter to a set power value
 *
 * rpmRun has 1 arg rpm which is an integer that runs the shooter at a specific RPM
 *
 * rpmLeft has no args and returns the currents rpm of the left motor as an int
 * rpmRight has no args and returns the current rpm of the right motor as an int
 */

//import package for running DC Motors and Range clip

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


public class Shooter {

    //creating local variables for motors
    private DcMotor left;
    private DcMotor right;

    private int tps;

    private int ticksl, ticksr, rpml, rpmr;


    //object for the shooter mechanism with 2 motors left and right
    public Shooter(DcMotor leftArg, DcMotor rightArg){
        //set local motor variables for the Shooter object
        left = leftArg;
        right = rightArg;
    }


    /**
    Method for running the shooter to a set power

     It has one arg for the power to set the shooter to
     */
    public void powerRun(float power){
        //clipping power range
        power = Range.clip(power, -1, 1);

        //sending power to motors
        left.setMaxSpeed(100000);
        left.setPower(power);
        right.setMaxSpeed(100000);
        right.setPower(-power);
    }

    /**
    Method for running the shooter to a particular RPM

    This method has one arg which is an integer of the RPM

     It runs a control loop to maintain the set rpm which is controlled by the function:

     */
    public void rpmRun(int rpm){
        /**convert rpm to ticks per second
         * rpm/60 = rps
         * rps * 112 = tps
         */
        tps = ((rpm+40)/60)*112;

        //set motors to rpm based on ticks per second
        left.setMaxSpeed(tps);
        left.setPower(1);
        right.setMaxSpeed(tps);
        right.setPower(-1);
    }

    ///Method to run the motors x rotations for 11364
    public void rotation(int rotations){
        //figure out position based on number of rotations
        //one rotation is 1680 ticks
        int ticksl = left.getCurrentPosition() + (rotations * 1680);
        int ticksr = right.getCurrentPosition() + (rotations * 1680);

        //set run mode of motors
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //run motors
        left.setTargetPosition(ticksl);
        right.setTargetPosition(ticksr);
        if (rotations > 1) {
            left.setPower(1);
            right.setPower(1);
        } else if (rotations < 1){
            left.setPower(-1);
            left.setPower(-1);
        }

    }

    //method for getting the speed of the left motor
    public int rpmLeft(){
        //convert ticks to rpm by reversing conversion detailed above
        ticksl = left.getMaxSpeed();
        rpml = (ticksl/112)*60;

        //return rpm of left motor
        return rpml;

    }

    //same method as above but for getting the speed of the right motor
    //method for getting the speed of the left motor
    public int rpmRight(){
        //convert ticks to rpm by reversing conversion detailed above
        ticksr = right.getMaxSpeed();
        rpmr = (ticksr/112)*60;

        //return rpm of left motor
        return rpmr;

    }

    //Method to stop shooter
    public void stop(){
        //set both motor powers to 0
        left.setPower(0);
        right.setPower(0);
    }

}
