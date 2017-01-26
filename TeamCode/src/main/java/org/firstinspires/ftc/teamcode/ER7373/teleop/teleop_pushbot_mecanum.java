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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.Mecanum;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;


@TeleOp(name = "Pushbot Teleop", group = "Concept")
@Disabled


public class teleop_pushbot_mecanum extends OpMode {

  private ElapsedTime runtime = new ElapsedTime();

  //create all motor variables
  DcMotor leftfront;
  DcMotor leftrear;
  DcMotor rightfront;
  DcMotor rightrear;

  /*
  This method takes input from the controller and drives the mecanum wheels
  This is a COD style drive system
  The left joystick will move it on the x-z plane
  The right joystick will rotate it about the y-axis
   */

  @Override
  public void init() {
    telemetry.addData("Status", "Initialized");

    //add all motors to the hardware map
    leftfront = hardwareMap.dcMotor.get("leftfront");
    leftrear = hardwareMap.dcMotor.get("leftrear");
    rightrear = hardwareMap.dcMotor.get("rightrear");
    rightfront = hardwareMap.dcMotor.get("rightfront");




    //set all motors to their run modes
    leftfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    leftrear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightrear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

  }


  @Override
  public void init_loop(){

  }


  @Override
  public void start() {
    runtime.reset();
  }


  @Override
  public void loop() {
    telemetry.addData("Status", "Run Time: " + runtime.toString());

    //call mecanum method to run pushbot
    Mecanum mecanum = new Mecanum(leftfront,leftrear,rightfront,rightrear);
    mecanum.run(-gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);


  }
}
