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

package org.firstinspires.ftc.teamcode.ER11364.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.Mecanum;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Motor;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.ServoM;

//import modern robotics library classes
//import Eagle Robotics 7373 library classes

/**
 * Teleop program for 11364 for 1st Qualifier
 *
 */
@Autonomous(name = "11364 Auto Shoot", group = "11364")
@Disabled
public class Q2AutoShoot11364 extends LinearOpMode {

  //create all motor variables for the drive train
  DcMotor leftFront;
  DcMotor leftRear;
  DcMotor rightFront;
  DcMotor rightRear;

  //create all motor variables for the shooter
  DcMotor shooterM;

  DcMotor intake;

  //create all motor variables for the stop servo
  Servo stop;

  private ElapsedTime runtime = new ElapsedTime();

  @Override
  public void runOpMode() throws InterruptedException{
    telemetry.addData("Status", "Initialized");

    //add all motors to the hardware map
    leftFront = hardwareMap.dcMotor.get("leftfront");
    leftRear = hardwareMap.dcMotor.get("leftrear");
    rightRear = hardwareMap.dcMotor.get("rightrear");
    rightFront = hardwareMap.dcMotor.get("rightfront");
    shooterM = hardwareMap.dcMotor.get("shooter");
    intake = hardwareMap.dcMotor.get("intake");

    //get servo from hardware map
    stop = hardwareMap.servo.get("shooterblock");



    //set all motors to their run modes
    leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    shooterM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    telemetry.addData("Status", "Run Time: " + runtime.toString());

    //instantiate all objects for all systems
    Mecanum mecanum = new Mecanum(leftFront,leftRear, rightFront, rightRear);

    Motor shooter = new Motor(shooterM);

    Motor in = new Motor(intake);

    //create servo objects for the upper and lower servos
    ServoM stopServo = new ServoM(stop);
    stopServo.setPos((float)1);


    waitForStart();

    /**
     * 1.  Drive forward a set distance
     * 2.  Shoot a ball
     * 3.  Reset shooter
     * 4.  Load new ball
     * 5.  Shoot ball
     * 6.  Reset Shooter
     */

    mecanum.run((float) -.3, 0, 0);
    Thread.sleep(900);
    mecanum.stop();

    Thread.sleep(1500);
    shooter.runPower((float)-.5);
    Thread.sleep(700);
    shooter.stop();

    Thread.sleep(1500);

    shooter.runPower((float).5);
    Thread.sleep(700);
    shooter.stop();

    stopServo.setPos((float).5);
    in.runPower(-1);
    Thread.sleep(3000);
    in.runPower(0);

    shooter.runPower((float)-.5);
    Thread.sleep(700);
    shooter.stop();

    Thread.sleep(1500);
    shooter.runPower((float).5);
    Thread.sleep(700);
    shooter.stop();

    mecanum.run((float) -.75,0,0);
    Thread.sleep(500);



    mecanum.stop();

  }
}
