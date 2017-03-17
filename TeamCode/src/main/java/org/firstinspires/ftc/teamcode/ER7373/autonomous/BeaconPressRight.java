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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.*;
import org.firstinspires.ftc.teamcode.ER7373.sensors.*;
import org.firstinspires.ftc.teamcode.ER7373.functions.*;



@Autonomous(name="BeaconPressBlue7373", group="Auto 7373")
//@Disabled
public class BeaconPressRight extends LinearOpMode {

	/* Declare OpMode members. */
	private ElapsedTime runtime = new ElapsedTime();

	//servos
	//servo variable for the ball stop and its 2 positions
	Servo ballStop;
	double closed = 0;
	double open = 1;


	//servos for cap ball lift
	Servo dropLift;
	Servo ballHold;

	double closed2 = 0;
	double open2 = 1;

	//booelan for beacon press
	boolean beacon1 = false;
	boolean beacon2 = false;

	//variables
	public enum followline {
		on, off
	}

	public enum teamColor {red, blue}

	teamColor team = teamColor.blue;

	followline linestat;

	ArrayCompare array = new ArrayCompare();
	//adjusted for the gray floor
	public int[] floorRef = new int[4];
	public final double[] distanceIn = {4, 0};


	@Override
	public void runOpMode() throws InterruptedException {
		//initialize
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		DcMotor shooterLeft;
		DcMotor shooterRight;
		shooterLeft = hardwareMap.dcMotor.get("shooterleft");
		shooterRight = hardwareMap.dcMotor.get("shooterright");
		shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		Shooter shooter = new Shooter(shooterLeft, shooterRight);


		//create objects for drive train and 2 color sensors and range finder
		Mecanum mecanum = new Mecanum(hardwareMap.dcMotor.get("leftfront"),
				hardwareMap.dcMotor.get("leftrear"),
				hardwareMap.dcMotor.get("rightfront"),
				hardwareMap.dcMotor.get("rightrear"));

		DcMotor intakem = hardwareMap.dcMotor.get("intake");

		//create obj for cdim
		DeviceInterfaceModule dim = hardwareMap.deviceInterfaceModule.get("dim");

		//create color sensors
		MRColorRaw colorLeft = new MRColorRaw(dim, 5, 0x4c);
		MRColorRaw colorRight = new MRColorRaw(dim, 3, 0x9c);
		MRColorRaw colorFloor = new MRColorRaw(dim, 2, 0x1c);

		//create range sensor
		MRRange range = new MRRange(hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range"));

		//add servo to hardware map
		ballStop = hardwareMap.servo.get("ballstop");
		dropLift = hardwareMap.servo.get("rightlift");
		ballHold = hardwareMap.servo.get("ballhold");

		//set servo to closed position
		ballHold.setPosition(closed2);
		ballStop.setPosition(open);

		//set servo to start position
		dropLift.setPosition(.7);

		//get the reference value for the floor
		floorRef = colorFloor.rgbc();
		for (int i = 0; i < floorRef.length; i++) {
			floorRef[i]++;
		}

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
			while (colorFloor.rgbc()[0] <= floorRef[0] && opModeIsActive()) {
				mecanum.run73ND((float) -.3, 0, 0);

				//sleep 33 ms or the time it takes to get new data from the sensor
				Thread.sleep(33);
			}

			telemetry.addData("Status: ", "Found Line");
			telemetry.update();

			mecanum.stop();
			Thread.sleep(1000);


			//line follow code
			while (range.in()[0] > distanceIn[0] && opModeIsActive()) {
				Thread.sleep(33);
				//check status of position
				if (colorFloor.rgbc()[0] > floorRef[0]) {
					linestat = followline.on;
				} else {
					linestat = followline.off;

				}

				//print enum state to telemetry
				telemetry.addData("Line Stat: ", linestat.toString());
				telemetry.addData("Range: ", range.in()[0] + " " + range.in()[1]);
				telemetry.update();

				switch (linestat) {
					case on:
						mecanum.run73ND((float) -.25, 0, 0);
						break;
					case off:
						if (team == teamColor.red) {
							mecanum.run73ND(0, 0, -.15);
						} else if (team == teamColor.blue) {
							mecanum.run73ND(0, 0, .15);
						}
						break;

				}

			}

			//#TODO: write code to hit button with the servos and then shoot


		}

	}
}




