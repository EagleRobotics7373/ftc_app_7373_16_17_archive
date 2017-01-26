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
package org.firstinspires.ftc.teamcode.ER7373.sensors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.functions.ArrayCompare;
import org.firstinspires.ftc.teamcode.ER7373.sensors.*;


@Autonomous(name="DATAOP", group="Autonomous")
@Disabled
public class DataOp extends LinearOpMode{

	/* Declare OpMode members. */
	private ElapsedTime runtime = new ElapsedTime();




	@Override
	public void runOpMode() throws InterruptedException {
		//initialize
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		// TODO:: NEW Values int[] floorRef = {350,200,200,0};
		//ArrayCompare array = new ArrayCompare();

		String stat;

		int[] ports = {2};

		//create mux and sensor objects
		MuxColor mux = new MuxColor(hardwareMap, "mux", "color", ports, 0xD6, 0x02);

		AdafruitRGBSensorObjMux colorFL = new AdafruitRGBSensorObjMux(mux, 1,
				hardwareMap.deviceInterfaceModule.get("dim"), 1);
		colorFL.initialize();

		AdafruitRGBSensorObjMux colorFR = new AdafruitRGBSensorObjMux(mux, 2,
				hardwareMap.deviceInterfaceModule.get("dim"), 2);
		colorFR.initialize();

		AdafruitRGBSensorObjMux colorBL = new AdafruitRGBSensorObjMux(mux, 3,
				hardwareMap.deviceInterfaceModule.get("dim"), 3);
		colorBL.initialize();

		AdafruitRGBSensorObjMux colorBR = new AdafruitRGBSensorObjMux(mux, 4,
				hardwareMap.deviceInterfaceModule.get("dim"), 4);
		colorBR.initialize();

		mux.startPolling();

		MaxbotixRangeObj range = new MaxbotixRangeObj(hardwareMap.analogInput.get("range"));



		//wait for start of op mode
		waitForStart();
		runtime.reset();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			telemetry.addData("Status", "Run Time: " + runtime.toString());
			telemetry.update();



			//light status
			colorBL.ledOFF();
			colorBR.ledOFF();
			colorFL.ledON();
			colorFR.ledON();

			telemetry.addData("Red: ", mux.getCRGB(2)[1]);
			telemetry.addData("Green: ", mux.getCRGB(2)[2]);
			telemetry.addData("Blue: ", mux.getCRGB(2)[3]);
			telemetry.addData("Clear: ", mux.getCRGB(2)[0]);



			/**
			//return values for each sensor with compares
			stat = "" + colorFL.rgbArray();
			telemetry.addData("Floor Left: ", stat);


			stat = "" + colorFR.rgbArray();
			telemetry.addData("Floor Right: ", stat);


			stat = "Red " + colorBL.rgbArray()[1] + " Blue " + colorBL.rgbArray()[3];
			telemetry.addData("Beacon Left: ", stat);


			stat = "Red " + colorBR.rgbArray()[1] + " Blue " + colorBR.rgbArray()[3];
			telemetry.addData("Beacon Right: ", stat);
			 */


			telemetry.addData("Range:", range.distance() + " Inches");





		}
	}
}
