/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.ER11364;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.Mecanum;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Motor;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Shooter;

//import modern robotics library classes
//import Eagle Robotics 7373 library classes

/**
 * Teleop program for 11364 for 1st Qualifier
 *
 */

@TeleOp(name = "11364 Teleop", group = "11364")
@Disabled
public class Q2Teleop11364 extends OpMode {

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


  private ElapsedTime runtime = new ElapsedTime();

  @Override
  public void init() {
    telemetry.addData("Status", "Initialized");

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

  }

  @Override
  public void init_loop() {
  }

  @Override
  public void start() {
    runtime.reset();
  }

  @Override
  public void loop() {
    telemetry.addData("Status", "Run Time: " + runtime.toString());

    //instantiate all objects for all systems
    Mecanum mecanum = new Mecanum(leftFront,leftRear, rightFront, rightRear);

    Motor intake = new Motor(intakeMotor);

    Motor shooter = new Motor(shooterM);



    //run the mecanum wheels   ****Add gear ratio
    mecanum.run(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x, k);

    //run the shooter .5 rotation down if gamepad 2 a is pressed and .5 rotation up if b is pressed
    if(gamepad2.a)shooter.runPower((float) -.5);
    else if(gamepad2.b)shooter.runPower((float).5);
    else shooter.runPower(-gamepad2.right_stick_y);


    //run the intake with left joystick on gamepad 2
    intake.runPower(-gamepad2.right_trigger+gamepad2.left_trigger);



    //run the servo for the intake using the dpad
    if (gamepad2.dpad_down){
      shooterBlock.setPosition((float) 0.0);
    } else if(gamepad2.dpad_up)
      shooterBlock.setPosition((float) .5);











  }
}
