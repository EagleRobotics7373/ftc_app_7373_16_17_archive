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
package org.firstinspires.ftc.teamcode.ER11364.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.functions.ArrayCompare;
import org.firstinspires.ftc.teamcode.ER7373.sensors.AdafruitRGBSensorObj;
import org.firstinspires.ftc.teamcode.ER7373.sensors.MaxbotixRangeObj;


@Autonomous(name="DATAOP", group="Autonomous")
//@Disabled
public class Q2DataOp extends LinearOpMode{

	/* Declare OpMode members. */
	private ElapsedTime runtime = new ElapsedTime();


	int[] floorRef = {350,200,200};

	String stat = "";

	@Override
	public void runOpMode() throws InterruptedException {
		//initialize
		telemetry.addData("Status", "Initialized");
		telemetry.update();



		ArrayCompare array = new ArrayCompare();

		//create sensor objects
		AdafruitRGBSensorObj colorFL = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorFL"),
				hardwareMap.deviceInterfaceModule.get("dim"), 0);

		AdafruitRGBSensorObj colorFR = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorFR"),
				hardwareMap.deviceInterfaceModule.get("dim"), 1);

		/**

		//create color sensors for beacon and turn off LED
		AdafruitRGBSensorObj colorBL = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorBL"),
				hardwareMap.deviceInterfaceModule.get("dim"), 2);

		AdafruitRGBSensorObj colorBR = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorBR"),
				hardwareMap.deviceInterfaceModule.get("dim"), 3);


		MaxbotixRangeObj range = new MaxbotixRangeObj(hardwareMap.analogInput.get("range"));
		*/


		//wait for start of op mode
		waitForStart();
		runtime.reset();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			telemetry.addData("Status", "Run Time: " + runtime.toString());
			telemetry.update();

			//light status
			colorFL.ledON();
			colorFR.ledON();
			//colorBL.ledOFF();
			//colorBR.ledOFF();


			//return values for each sensor
			stat = array.greaterEqual(colorFL.rgbArray(),floorRef) ? "Line" : "Floor";
			telemetry.addData("Floor Left:", stat);
			Thread.sleep(5);

			stat = array.greaterEqual(colorFR.rgbArray(),floorRef) ? "Line" : "Floor";
			telemetry.addData("Floor Right:", stat);
			Thread.sleep(5);
/**
			stat = colorBL.red()>colorBL.blue() ? "Red" : "Blue";
			telemetry.addData("Beacon Left:", stat);
			Thread.sleep(5);

			stat =  colorBR.red()>colorBR.blue() ? "Red" : "Blue";
			telemetry.addData("Beacon Left:", stat);
			Thread.sleep(5);

			telemetry.addData("Range:", range.rawRead());
			Thread.sleep(5);
 */

		}
	}
}
