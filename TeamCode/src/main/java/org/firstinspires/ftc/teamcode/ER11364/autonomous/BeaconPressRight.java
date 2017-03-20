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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.teamcode.ER7373.functions.ArrayCompare;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Mecanum;
import org.firstinspires.ftc.teamcode.ER7373.mechanics.Motor;
import org.firstinspires.ftc.teamcode.ER7373.sensors.AdafruitRGBSensorObj;
import org.firstinspires.ftc.teamcode.ER7373.sensors.MaxbotixRangeObj;


@Autonomous(name="BeaconPressBlue", group="Autonomous")
@Disabled
public class BeaconPressRight extends LinearOpMode {

	/* Declare OpMode members. */
	private ElapsedTime runtime = new ElapsedTime();

	public enum followline{on, leftOff, rightOff, off}
	followline linestat;
	followline laststate;


	@Override
	public void runOpMode() throws InterruptedException{
		//initialize
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		//variables
		ArrayCompare array = new ArrayCompare();
		int[] floorRef = {350,200,200,0};

		int distanceNeeded = 1000;

		//create objects for drive train and shooter and servo
		Mecanum mecanum = new Mecanum(hardwareMap.dcMotor.get("leftfront"),
				hardwareMap.dcMotor.get("leftrear"),
				hardwareMap.dcMotor.get("rightfront"),
				hardwareMap.dcMotor.get("rightrear"));

		Servo shooterBlock = hardwareMap.servo.get("shooterblock");

		//Motor shooter = new Motor(shooterM);


		AdafruitRGBSensorObj colorFL = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorFL"),
				hardwareMap.deviceInterfaceModule.get("dim"), 0);
		//set i2c address to 0x3c
		colorFL.initialize(0x3c);

		AdafruitRGBSensorObj colorFR = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorFR"),
				hardwareMap.deviceInterfaceModule.get("dim"), 1);
		//set i2c address to 0x4c
		colorFR.initialize(0x4c);

		//create color sensors for beacon and turn off LED
		AdafruitRGBSensorObj colorBL = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorBL"),
				hardwareMap.deviceInterfaceModule.get("dim"), 2);
		//set i2c address to 0x5c
		colorBL.initialize(0x5c);

		AdafruitRGBSensorObj colorBR = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorBR"),
				hardwareMap.deviceInterfaceModule.get("dim"), 3);
		//set i2c address to 0x6c
		colorBR.initialize(0x6c);


		MaxbotixRangeObj range = new MaxbotixRangeObj(hardwareMap.analogInput.get("range"));


		//wait for start of op mode
		waitForStart();
		runtime.reset();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			telemetry.addData("Status", "Run Time: " + runtime.toString());
			telemetry.update();

			/**
			 * OVERALL PLAN:
			 * 1. ??? Shoot
			 * 		Shoot Ball
			 * 		Load New Ball
			 * 		Shoot Ball
			 * 		Close Gate
			 * 2. ??? Turn to face line
			 * 3. Drive forward up until the line
			 * 4. Get Aligned with the line
			 * 5. Stop at x Distance from the wall
			 * 6. Read the beacon and decide which side to move on
			 * 7. Turn to left or right to hit button
			 * 8 ??? Confirm hit and correct
			 * 9.??? Move to next beacon
			 */



            //Drive Straight up until hit line
			while(array.lessThan(colorFL.rgbArray(),floorRef) && array.lessThan(colorFR.rgbArray(), floorRef)){
				mecanum.run((float).5,0,0);
			}


			//line follow code
			while(range.rawRead() >= distanceNeeded){
				//check state of color sensors
				if(array.greaterEqual(colorFL.rgbArray(),floorRef) && array.greaterEqual(colorFR.rgbArray(), floorRef)){
					linestat = followline.on;
				} else if(array.lessThan(colorFL.rgbArray(),floorRef) && array.lessThan(colorFR.rgbArray(), floorRef)){
					linestat = followline.off;
				} else if(!array.greaterEqual(colorFL.rgbArray(),floorRef) && array.greaterEqual(colorFR.rgbArray(), floorRef)){
					linestat = followline.leftOff;
				} else if(array.greaterEqual(colorFL.rgbArray(),floorRef) && !array.greaterEqual(colorFR.rgbArray(), floorRef)){
					linestat = followline.rightOff;
				}

                //print enum state to telemetry
                telemetry.addData("Line Stat: ", linestat.toString());


				//act based on the state of the color sensors
				switch(linestat){
					case on:
                        telemetry.addData("Working State: ", linestat.toString());
						mecanum.run((float).5,0,0);
					    break;

					case off:
						if (laststate == followline.leftOff){
                            telemetry.addData("Working State: ", "OFF OFF LEFT");

							//turn right until line
							while(array.lessThan(colorFL.rgbArray(),floorRef) || array.lessThan(colorFR.rgbArray(), floorRef)){
								mecanum.run(0, 0, (float) .5);
							}

						} else if (laststate == followline.rightOff){
                            telemetry.addData("Working State: ", "OFF OFF RIGHT");

							//turn left until line
							while(array.lessThan(colorFL.rgbArray(),floorRef) || array.lessThan(colorFR.rgbArray(), floorRef)){
								mecanum.run(0, 0, (float) .5);
							}

						} else {
                            telemetry.addData("Working State: ", "OFF OFF");

							//turn left or right in increasing intervals until line is found
							for (int i = 1; array.lessThan(colorFL.rgbArray(),floorRef) || array.lessThan(colorFR.rgbArray(), floorRef); i ++) {
                                mecanum.run(0, 0, (float) .5);
								Thread.sleep(i * 500);
								if (array.greaterEqual(colorFL.rgbArray(), floorRef) && !array.greaterEqual(colorFR.rgbArray(), floorRef)) {
									break;
								}
								mecanum.run(0, 0, (float) -.5);
								Thread.sleep(i * 500);
								if (array.greaterEqual(colorFL.rgbArray(), floorRef) && !array.greaterEqual(colorFR.rgbArray(), floorRef)) {
									break;
								}
							}
						}
					    break;

                    case leftOff:
                        mecanum.run(0,0,(float) .5);

                        break;

                    case rightOff:
                        mecanum.run(0,0,(float) -.5);
                        break;
				}

				laststate = linestat;

			}

            //interact with beacon
            Thread.sleep(2000);

			if (colorBL.red()>colorBL.blue() && colorBR.blue()>colorBR.red()){
				//Left = Blue
				//Right = Red
				//red so turn right
				mecanum.run(0,0,(float) .5);
				Thread.sleep(1000);
			} else {
				//Left = Red
				//Right = Blue
				//red so turn left
				mecanum.run(0,0,(float) -.5);
				Thread.sleep(1000);
			}

            //slide left to next beacon
            mecanum.run(0,(float) -.5, 0);
            while(array.lessThan(colorFL.rgbArray(),floorRef) || array.lessThan(colorFR.rgbArray(), floorRef)){
                mecanum.run(0, (float) .5, 0);
            }

            //interact with beacon
            Thread.sleep(2000);

			if (colorBL.red()>colorBL.blue() && colorBR.blue()>colorBR.red()){
				//Left = Blue
				//Right = Red
				//red so turn right
				mecanum.run(0,0,(float) .5);
				Thread.sleep(1000);
			} else {
				//Left = Red
				//Right = Blue
				//red so turn left
				mecanum.run(0,0,(float) -.5);
				Thread.sleep(1000);
			}

            //// STOP
            break;





		}
	}
}
