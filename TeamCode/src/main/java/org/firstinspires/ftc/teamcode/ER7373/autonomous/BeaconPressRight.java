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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ER7373.mechanics.*;
import org.firstinspires.ftc.teamcode.ER7373.sensors.*;
import org.firstinspires.ftc.teamcode.ER7373.functions.*;



@Autonomous(name="BeaconPressRight", group="Autonomous")
@Disabled
public class BeaconPressRight extends LinearOpMode {

	/* Declare OpMode members. */
	private ElapsedTime runtime = new ElapsedTime();

	//variables
	public enum followline{on, leftOff, rightOff, off}
	followline linestat;
	followline laststate;

	ArrayCompare array;
	int[] floorRef = {350,200,200};

	int distanceNeeded = 1000;



	@Override
	public void runOpMode() throws InterruptedException{
		//initialize
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		//create objects for drive train and 2 color sensors and range finder
		Mecanum mecanum = new Mecanum(hardwareMap.dcMotor.get("leftfront"),
				hardwareMap.dcMotor.get("leftrear"),
				hardwareMap.dcMotor.get("rightfront"),
				hardwareMap.dcMotor.get("rightrear"));

		AdafruitRGBSensorObj colorFL = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorFL"),
				hardwareMap.deviceInterfaceModule.get("dim"), 5);
		AdafruitRGBSensorObj colorFR = new AdafruitRGBSensorObj(hardwareMap.colorSensor.get("colorFR"),
				hardwareMap.deviceInterfaceModule.get("dim"), 6);

		MaxbotixRangeObj range = new MaxbotixRangeObj(hardwareMap.analogInput.get("range"));


		//wait for start of op mode
		waitForStart();
		runtime.reset();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			telemetry.addData("Status", "Run Time: " + runtime.toString());
			telemetry.update();

			//drive to line code

            //Turn????

            //Drive Straight
			while(array.lessThan(colorFL.rgbArray(),floorRef) && array.lessThan(colorFR.rgbArray(), floorRef)){
				mecanum.run((float).5,0,0);
			}
			mecanum.stop();



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

            //slide right to next beacon
            mecanum.run(0,(float) .5, 0);
            Thread.sleep(1000);
            while(array.lessThan(colorFL.rgbArray(),floorRef) && array.lessThan(colorFR.rgbArray(), floorRef)){
                mecanum.run(0, (float) .5, 0);
            }

            //interact with beacon
            Thread.sleep(2000);

            //// STOP
            break;





		}
	}
}
