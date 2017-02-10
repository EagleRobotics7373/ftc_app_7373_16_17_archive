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
package org.firstinspires.ftc.teamcode.ER7373.autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.*;
import org.firstinspires.ftc.teamcode.ER7373.sensors.*;
import org.firstinspires.ftc.teamcode.ER7373.functions.*;



@Autonomous(name="BeaconPressRight", group="Autonomous")
//@Disabled
public class BeaconPressRight extends LinearOpMode {

	/* Declare OpMode members. */
	private ElapsedTime runtime = new ElapsedTime();

	//variables
	public enum followline {on, off}

	public enum teamColor {red, blue}
	teamColor team = teamColor.blue;

	followline linestat;

	ArrayCompare array;
	public static final int[] floorRef = {0, 0, 0, 0};
	public static final double[] distanceIn = {1, 1};


	@Override
	public void runOpMode() throws InterruptedException {
		//initialize
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		//create objects for drive train and 2 color sensors and range finder
		Mecanum mecanum = new Mecanum(hardwareMap.dcMotor.get("leftfront"),
				hardwareMap.dcMotor.get("leftrear"),
				hardwareMap.dcMotor.get("rightfront"),
				hardwareMap.dcMotor.get("rightrear"));

		//create obj for cdim
		DeviceInterfaceModule dim = hardwareMap.deviceInterfaceModule.get("dim");

		//create color sensors
		MRColorRaw colorLeft = new MRColorRaw(dim, 1, 0x1c);
		MRColorRaw colorRight = new MRColorRaw(dim, 2, 0x2c);
		MRColorRaw colorFloor = new MRColorRaw(dim, 0, 0x3c);

		//create range sensor
		MRRange range = new MRRange(hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range"));

		//wait for start of op mode
		waitForStart();
		runtime.reset();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			telemetry.addData("Status", "Run Time: " + runtime.toString());
			telemetry.update();

			//drive to line code

			//Shoot and Turn????

			//Drive Straight
			while (array.lessThanEqual(colorFloor.rgbc(), floorRef)) {
				mecanum.run((float) .5, 0, 0);
			}
			mecanum.stop();


			//line follow code
			while (array.lessThan(range.in(), distanceIn)){
				//check status of position
				if (array.greaterEqual(colorFloor.rgbc(), floorRef)) {
					linestat = followline.on;
				} else {
					linestat = followline.off;
				}

				//print enum state to telemetry
				telemetry.addData("Line Stat: ", linestat.toString());

				//act based on the state of the color sensors
				switch (linestat) {
					case on:
						mecanum.run((float).3,0,0);
						break;
					case off:
						if(team == teamColor.red) {
							mecanum.run(0, 0, (float) .3);
						} else if(team == teamColor.blue){
							mecanum.run(0,0, (float) -.3);
						}
						break;

				}

			}

			mecanum.stop();


			//interact with beacon
			Thread.sleep(2000);
			switch(team) {
				case red:
					if (colorLeft.rgbc()[0] > colorLeft.rgbc()[2]
							&& colorRight.rgbc()[2] > colorRight.rgbc()[0]) {
						//slide left to hit red
						while(colorRight.rgbc()[2] > colorRight.rgbc()[0]){
							mecanum.run(0, 0, (float) .3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					} else if (colorLeft.rgbc()[2] > colorLeft.rgbc()[0]
							&& colorRight.rgbc()[0] > colorRight.rgbc()[2]) {
						//slide right to hit red
						while(colorLeft.rgbc()[2] > colorLeft.rgbc()[0]){
							mecanum.run(0, 0, (float) -.3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					}
					break;
				case blue:
					if (colorLeft.rgbc()[2] > colorLeft.rgbc()[0]
							&& colorRight.rgbc()[0] > colorRight.rgbc()[2]) {
						//slide left to hit blue
						while(colorRight.rgbc()[0] > colorRight.rgbc()[2]){
							mecanum.run(0, 0, (float) .3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					} else if (colorLeft.rgbc()[0] > colorLeft.rgbc()[2]
							&& colorRight.rgbc()[2] > colorRight.rgbc()[0]) {
						//slide right to hit blue
						while(colorLeft.rgbc()[0] > colorLeft.rgbc()[2]){
							mecanum.run(0, 0, (float) -.3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					}
					break;
			}

			//push button
			mecanum.run((float) -.3, 0, 0);
			Thread.sleep(1000);
			mecanum.stop();

			//run back
			mecanum.run((float) .3,0,0);
			Thread.sleep(1000);
			mecanum.stop();

			//slide left or right depending on the team color
			if(team == teamColor.red){
				mecanum.run(0,0,(float) -.3);
				Thread.sleep(2000);

				while(array.lessThanEqual(colorFloor.rgbc(), floorRef)) {
					mecanum.run(0, 0, (float) -.3);
				}

			} else if(team == teamColor.blue){
				mecanum.run(0,0,(float) .3);
				Thread.sleep(2000);
				while(array.lessThanEqual(colorFloor.rgbc(), floorRef)) {
					mecanum.run(0, 0, (float) .3);
				}

			}

			//line follow code
			while (array.lessThan(range.in(), distanceIn)){
				//check status of position
				if (array.greaterEqual(colorFloor.rgbc(), floorRef)) {
					linestat = followline.on;
				} else {
					linestat = followline.off;
				}

				//print enum state to telemetry
				telemetry.addData("Line Stat: ", linestat.toString());

				//act based on the state of the color sensors
				switch (linestat) {
					case on:
						mecanum.run((float).3,0,0);
						break;
					case off:
						if(team == teamColor.red) {
							mecanum.run(0, 0, (float) .3);
						} else if(team == teamColor.blue){
							mecanum.run(0,0, (float) -.3);
						}
						break;

				}

			}

			mecanum.stop();

			//interact with beacon
			Thread.sleep(2000);
			switch(team) {
				case red:
					if (colorLeft.rgbc()[0] > colorLeft.rgbc()[2]
							&& colorRight.rgbc()[2] > colorRight.rgbc()[0]) {
						//slide left to hit red
						while(colorRight.rgbc()[2] > colorRight.rgbc()[0]){
							mecanum.run(0, 0, (float) .3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					} else if (colorLeft.rgbc()[2] > colorLeft.rgbc()[0]
							&& colorRight.rgbc()[0] > colorRight.rgbc()[2]) {
						//slide right to hit red
						while(colorLeft.rgbc()[2] > colorLeft.rgbc()[0]){
							mecanum.run(0, 0, (float) -.3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					}
					break;
				case blue:
					if (colorLeft.rgbc()[2] > colorLeft.rgbc()[0]
							&& colorRight.rgbc()[0] > colorRight.rgbc()[2]) {
						//slide left to hit blue
						while(colorRight.rgbc()[0] > colorRight.rgbc()[2]){
							mecanum.run(0, 0, (float) .3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					} else if (colorLeft.rgbc()[0] > colorLeft.rgbc()[2]
							&& colorRight.rgbc()[2] > colorRight.rgbc()[0]) {
						//slide right to hit blue
						while(colorLeft.rgbc()[0] > colorLeft.rgbc()[2]){
							mecanum.run(0, 0, (float) -.3);
						}
						mecanum.stop();
						Thread.sleep(1000);
					}
					break;
			}

			//push button
			mecanum.run((float) -.3, 0, 0);
			Thread.sleep(1000);
			mecanum.stop();

			//run back
			mecanum.run((float) .3,0,0);
			Thread.sleep(1000);
			mecanum.stop();


			////STOP
			break;


		}

	}
}



