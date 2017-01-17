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

package org.firstinspires.ftc.teamcode.ER7373.autonomous.FirstQualifierArchive;

//import classes from the MR Library

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.Mecanum;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Motor;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Shooter;

//import classes from our own library


@Autonomous(name = "7373 BlueAuto", group = "Concept")
@Disabled

public class BlueShootAndPush1Q extends LinearOpMode  {

  private ElapsedTime runtime = new ElapsedTime();

  //variable to instantiate all objects once
  private boolean instantiated = false;

  //create all motor variables for the drive train
  DcMotor leftFront;
  DcMotor leftRear;
  DcMotor rightFront;
  DcMotor rightRear;

  //create all motor variables for the shooter
  DcMotor shooterLeft;
  DcMotor shooterRight;

  //motor variables for the intake
  DcMotor intakem;

  //servo variable for the ball stop and its 2 positions
  Servo ballStop;
  double closed = .4;
  double open = .95;


  //Logic Variables
  boolean shooterPower = false;
  boolean stopToggle = false;


  @Override
  public void runOpMode() throws InterruptedException{
      telemetry.addData("Status", "Initialized");

      //add all motors to the hardware map
      leftFront = hardwareMap.dcMotor.get("leftfront");
      leftRear = hardwareMap.dcMotor.get("leftrear");
      rightRear = hardwareMap.dcMotor.get("rightrear");
      rightFront = hardwareMap.dcMotor.get("rightfront");
      shooterLeft = hardwareMap.dcMotor.get("shooterleft");
      shooterRight = hardwareMap.dcMotor.get("shooterright");
      intakem = hardwareMap.dcMotor.get("intake");


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

      //set servo to closed position
      ballStop.setPosition(closed);


      //wait for start of program
      runtime.reset();
      waitForStart();
      telemetry.addData("Status", "Run Time: " + runtime.toString());


      //instantiate all objects for our mechanical systems

      //create Mecanum object to run mecanum wheels
      Mecanum mecanum = new Mecanum(leftFront, leftRear, rightFront, rightRear);

      //create new Shooter object to run the shooter
      Shooter shooter = new Shooter(shooterLeft, shooterRight);

      //create new Motor object for the intake
      Motor intake = new Motor(intakem);

    /**  Starting Position: ______
     * 1.  The Robot will move forward to get in position for shooting
     * 2.  The Robot will run shooting procedure for 2 balls
     * 3.  The robot will go on either path 1 or path 2
     * Path 1:  Move to and claim the Beacon
     * Path 2:  Push the cap ball off and park on the center tile
     */


    /**
     * Run through shooting procedures
     * 1.  Spin up shooter
     * 2.  Open Stop
     * 3.  Move Intake
     * 4.  Close stop to prevent double shot
     * 5.  Ball shot
     * 6.  Stop Intake
     * 7.  Repeat
     */

    shooter.rpmRun(1200);
    Thread.sleep(3000);
    ballStop.setPosition(open);
    Thread.sleep(1000);
    intake.runPower((float) .75);
    Thread.sleep(250);
    ballStop.setPosition(closed);
    Thread.sleep(3000);
    intake.runPower((float) 0);
    ballStop.setPosition(open);
    Thread.sleep(500);
    intake.runPower((float) .75);
    Thread.sleep(1000);
    intake.runPower(0);
    shooter.stop();

    //move the robot forward ___ seconds
    mecanum.run((float) .5, 0, 0);
    Thread.sleep(1500);
    mecanum.run((float) 0, 0, (float) .5);
    Thread.sleep(750);
    mecanum.stop();


    //stop the linear op mode
    stop();


    }

  }

