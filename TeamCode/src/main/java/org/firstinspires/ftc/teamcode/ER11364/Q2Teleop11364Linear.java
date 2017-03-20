/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode.ER11364;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.Mecanum;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Motor;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="11364 Teleop Q2", group="11364")  // @Autonomous(...) is the other common choice
@Disabled
public class Q2Teleop11364Linear extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    //create all motor variables for the drive train
    DcMotor leftFront;
    DcMotor leftRear;
    DcMotor rightFront;
    DcMotor rightRear;
    float k = 1;
    public enum gear{low, mid, high}




    //create all motor variables for the shooter
    DcMotor shooterM;

    //create all motor variables for the intake
    DcMotor intakeMotor;

    //create servo variables for the shooter
    Servo shooterBlock;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //add all motors to the hardware map
        leftFront = hardwareMap.dcMotor.get("leftfront");
        leftRear = hardwareMap.dcMotor.get("leftrear");
        rightRear = hardwareMap.dcMotor.get("rightrear");
        rightFront = hardwareMap.dcMotor.get("rightfront");
        shooterM = hardwareMap.dcMotor.get("shooter");
        intakeMotor = hardwareMap.dcMotor.get("intake");


        //add servos to the hardware map
        shooterBlock = hardwareMap.servo.get("shooterblock");


        //set all motors to their run modes
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        //instantiate all objects for all systems
        Mecanum mecanum = new Mecanum(leftFront,leftRear, rightFront, rightRear);

        Motor intake = new Motor(intakeMotor);

        Motor shooter = new Motor(shooterM);

        gear gearState = gear.high;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();


            //run the mecanum wheels   ****Added gear ratio
            if(gamepad1.dpad_up)gearState = gear.high;
            else if(gamepad1.dpad_left||gamepad1.dpad_right)gearState = gear.mid;
            else if(gamepad1.dpad_down)gearState = gear.low;

            telemetry.addData("Status", "Gear State: " + gearState.toString());

            switch(gearState){
                case high:
                    k = 1;
                    break;
                case mid:
                    k = (float) .5;
                    break;
                case low:
                    k = (float) .25;
                    break;
                default:
                    break;
            }

            mecanum.runCoef(gamepad1.left_stick_y,-gamepad1.right_stick_x,-gamepad1.left_stick_x, k);
            telemetry.addData("Drive Values: ", mecanum.runVal()[0]
                    + " " + mecanum.runVal()[1]
                    + " " + mecanum.runVal()[2]
                    + " " + mecanum.runVal()[3]);


            //run the shooter .5 rotation down if gamepad 2 a is pressed and .5 rotation up if b is pressed
            if(gamepad2.a)shooter.runPower((float) -.5);
            else if(gamepad2.b)shooter.runPower((float).5);
            else shooter.runPower(-gamepad2.right_stick_y);


            //run the intake with left joystick on gamepad 2
            intake.runPower(-gamepad2.right_trigger+gamepad2.left_trigger);



            //run the servo for the intake using the dpad
            if (gamepad2.dpad_up){
                shooterBlock.setPosition((float) 0.5);
            } else if(gamepad2.dpad_down)
                shooterBlock.setPosition((float) 1.0);
        }
    }
}
