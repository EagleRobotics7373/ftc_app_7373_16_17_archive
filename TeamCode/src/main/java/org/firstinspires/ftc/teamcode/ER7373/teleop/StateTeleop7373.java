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

package org.firstinspires.ftc.teamcode.ER7373.teleop;

//import classes from the MR Library

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.Mecanum;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Motor;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Shooter;

//import java classes
//import classes from our own library


@TeleOp(name = "7373 Teleop", group = "Concept")
@Disabled

public class StateTeleop7373 extends LinearOpMode {

  private ElapsedTime runtime = new ElapsedTime();

  //create all motor variables for the drive train
  DcMotor leftFront;
  DcMotor leftRear;
  DcMotor rightFront;
  DcMotor rightRear;

  //create all motor variables for the shooter
  DcMotor shooterLeft;
  DcMotor shooterRight;

  //motor varaibles for the intake
  DcMotor intakem;

  //motor variable for capball lift
  DcMotor linearSlide;

  //servo variable for the ball stop and its 2 positions
  Servo ballStop;
  double closed = .4;
  double open = 1;

  //servo for cap ball lift
  Servo dropLift;


  //Logic Variables8
  boolean shooterPower = false;
  boolean stoptoggle = false;

  //enum for the gear
  enum gear{
    low, mid, high
  }
  gear gearstate = gear.high;


  @Override
  public void runOpMode() {
    telemetry.addData("Status", "Initialized");

    //add all motors to the hardware map
    leftFront = hardwareMap.dcMotor.get("leftfront");
    leftRear = hardwareMap.dcMotor.get("leftrear");
    rightRear = hardwareMap.dcMotor.get("rightrear");
    rightFront = hardwareMap.dcMotor.get("rightfront");
    shooterLeft = hardwareMap.dcMotor.get("shooterleft");
    shooterRight = hardwareMap.dcMotor.get("shooterright");
    intakem = hardwareMap.dcMotor.get("intake");
    linearSlide = hardwareMap.dcMotor.get("linearslide");


    //set all motors to their run modes
    leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    intakem.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    //add servo to hardware map
    ballStop = hardwareMap.servo.get("ballstop");
    dropLift = hardwareMap.servo.get("rightlift");

    //set servo to closed position
    ballStop.setPosition(closed);

    //set servo to start position
    dropLift.setPosition(0);


    runtime.reset();

    telemetry.addData("Status", "Run Time: " + runtime.toString());

    //instantiate all objects for our mechanical systems

    //create Mecanum object to run mecanum wheels
    Mecanum mecanum = new Mecanum(leftFront, leftRear, rightFront, rightRear);

    //create new Shooter object to run the shooter
    Shooter shooter = new Shooter(shooterLeft, shooterRight);

    //create new Motor object for the intake
    Motor intake = new Motor(intakem);

    //create new Motor object for the linear slide
    Motor ballLift = new Motor(linearSlide);

    //wait for start
    waitForStart();

    while(opModeIsActive()) {
      //call mecanum run method to send power values to the drivetrain from the controllers
      if(gamepad1.dpad_up){
        gearstate = gear.high;
      } else if(gamepad1.dpad_left || gamepad1.dpad_right){
        gearstate  = gear.mid;
      } else if(gamepad1.dpad_down){
        gearstate = gear.low;
      }
      switch (gearstate) {
        case high:
          mecanum.run((float) -Math.pow(gamepad1.left_stick_y, 3), (float) Math.pow(gamepad1.left_stick_x, 3), (float) Math.pow(gamepad1.right_stick_x, 3));
          break;
        case mid:
          mecanum.runCoef((float) Math.pow(gamepad1.left_stick_y, 3), (float) Math.pow(gamepad1.left_stick_x, 3), (float) Math.pow(gamepad1.right_stick_x, 3), (float).5);
          break;
        case low:
          mecanum.runCoef((float) Math.pow(gamepad1.left_stick_y, 3), (float) Math.pow(gamepad1.left_stick_x, 3), (float) Math.pow(gamepad1.right_stick_x, 3), (float).2);
          break;
      }


      /**
       * Run the intake
       *
       * Right Trigger draws in
       *
       * Left trigger releases
       */
      if (gamepad2.right_bumper) {
        intake.runPower((float) .75);
      } else if (gamepad2.left_bumper) {
        intake.runPower((float) -.75);
      } else {
        intake.stop();
      }

      /**
       * Move the ball stop manually
       *
       * Up D pad is open
       * Down D pad is closed
       */
      if (gamepad2.dpad_up) {
        ballStop.setPosition(closed);
      } else if (gamepad2.dpad_down) {
        ballStop.setPosition(open);
      } else {
      }


      /**
       * Macro to shoot a ball
       *
       * Press a to power on shooter
       *
       * Press b to toggle stop
       */
      if(!stoptoggle) {
        if (gamepad2.a) {
          shooter.rpmRun(1100);
          stoptoggle = true;
          //shooterPower = true;
        } else if (gamepad2.x) {
          shooter.rpmRun(1300);
          stoptoggle = true;
          //shooterPower = true;
        } else {
          //shooterPower = false;
        }
      }

      if(gamepad2.b) stoptoggle = false;


      //send each wheels current RPM values to the telemetry lines
      int rpml = shooter.rpmLeft();
      int rpmr = shooter.rpmRight();

      telemetry.addData("Status", "RPM Left:" + rpml);
      telemetry.addData("Status", "RPM Right:" + rpmr);

      // manual backup for running the shooter
      if (!stoptoggle) {
        shooter.powerRun((float) (-.7 * gamepad2.left_stick_y));
      }


      //code to run linear slide for cap ball
      ballLift.runPower(gamepad2.right_stick_y);

      //code for the servos for the cap ball lift
      if(gamepad2.right_stick_button){
        dropLift.setPosition(1);
      }

    }
  }
}
