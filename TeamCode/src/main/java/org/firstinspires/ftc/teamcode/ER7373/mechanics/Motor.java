package org.firstinspires.ftc.teamcode.ER7373.mechanics;

/**
 * Created by Jordan Moss on 10/15/2016.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


public class Motor {
    //create private motor variable
    private DcMotor motor;

    //constructor for Motor object
    public Motor(DcMotor motorName){
        motor = motorName;
    }

    //method to stop motor
    public void stop(){
        motor.setPower(0);
    }

    //method to run motor to power
    public void runPower(float power){
        //clip range of power
        power = Range.clip(power, -1, 1);

        //set the motor to that power
        motor.setPower(power);

    }

    //method to run the motor one rotation
    public void rotations60(double rotations){
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setMode(DcMotor.RunMode.RESET_ENCODERS);
        int ticks = (int)((rotations * 1680)+.5);
        motor.setTargetPosition(ticks);
        if(ticks > 0) motor.setPower(1);
            else motor.setPower(-1);
    }


}
