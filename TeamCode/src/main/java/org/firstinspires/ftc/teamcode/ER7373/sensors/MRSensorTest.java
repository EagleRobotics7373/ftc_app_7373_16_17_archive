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

package org.firstinspires.ftc.teamcode.ER7373.sensors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.sensors.*;


/**
 * Demonstrates empty OpMode
 */
@Autonomous(name = "Concept: MR Test", group = "Concept")
//@Disabled
public class MRSensorTest extends LinearOpMode {
  private ElapsedTime runtime = new ElapsedTime();

  public void runOpMode() throws InterruptedException{


      MRRange range = new MRRange(hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range"));

      MRColorRaw color = new MRColorRaw(hardwareMap.deviceInterfaceModule.get("dim"), 0, 0x1c);
      //color.ledOff();


      MRColorRaw color2 = new MRColorRaw(hardwareMap.deviceInterfaceModule.get("dim"), 2, 0x8c);
      //color2.ledOn();


    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();

    runtime.reset();
    while(opModeIsActive()){

      telemetry.addData("Status", "Run Time: " + runtime.toString());

        Thread.sleep(50);

       telemetry.addData("R: ", color.rgbc()[0]);

       telemetry.addData("G: ", color.rgbc()[1]);

       telemetry.addData("B: ", color.rgbc()[2]);

       telemetry.addData("C: ", color.rgbc()[3]);

        Thread.sleep(50);

        telemetry.addData("R2: ", color2.rgbc()[0]);

        telemetry.addData("G2: ", color2.rgbc()[1]);

        telemetry.addData("B2: ", color2.rgbc()[2]);

        telemetry.addData("C2: ", color2.rgbc()[3]);

       telemetry.addData("U: ", range.in()[0]);

       telemetry.addData("O: ", range.in()[1]);

        telemetry.update();

     }




  }
}
