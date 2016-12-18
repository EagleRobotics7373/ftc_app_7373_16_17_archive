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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Demonstrates empty OpMode
 */
@Autonomous(name = "Concept: AdafruitTest", group = "Concept")
//@Disabled
public class AdafruitRGBTest extends OpMode {
  private ElapsedTime runtime = new ElapsedTime();

  //create variables for sensor and cdim
  ColorSensor color;
  DeviceInterfaceModule cdim;

  //create objects for color sensor
  AdafruitRGBSensorObj rgb;
  //bool for light
  boolean x = true;

  @Override
  public void init(){

    telemetry.addData("Status", "Initialized");
    //initialize color sensor and cdim
    color = hardwareMap.colorSensor.get("color");
    cdim = hardwareMap.deviceInterfaceModule.get("dim");

    //create a new rgb sensor object
    rgb = new AdafruitRGBSensorObj(color, cdim, 5);

    //initialize rgb sensor
    rgb.initialize();
  }

  /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

  @Override
  public void start() {
    runtime.reset();
    //turn off led for the sensor
    rgb.ledOFF();
  }

  @Override
  public void loop() {
    //add the data to telemetry
    telemetry.addData("Status", "Run Time: " + runtime.toString());
    telemetry.addData("Led Status: ", rgb.ledStat ? "on" : "off");
    telemetry.addData("Red: ", rgb.red());
    telemetry.addData("Green: ", rgb.green());
    telemetry.addData("Blue: ", rgb.blue());
    telemetry.addData("Alpha: ", rgb.alpha());

    if(gamepad1.x) x = !x;
    if(x)
      rgb.ledON();
    else
      rgb.ledOFF();
  }
}
