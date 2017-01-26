package org.firstinspires.ftc.teamcode.ER7373.sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.I2cAddr;


/**
 * Created by Jordan Moss on 12/3/2016.
 *
 * Name color sensor anything
 *
 */
public class AdafruitRGBSensorObjMux {
	//variables for the color sensor and led and CDIM
	private int led;
	private DeviceInterfaceModule cdim;
	private MuxColor mux;
	private int muxPort;

	//array to store the RGB values of the sensor
	private int[] rgbVal;

	//boolean for led status
	boolean ledStat = false;


	//constructor for color sensor
	public AdafruitRGBSensorObjMux(MuxColor MUX, int port,DeviceInterfaceModule coredim, int digitalPin){
		//assign mux and port
		mux = MUX;
		muxPort = port;
		//get cdim from hardware map
		cdim = coredim;
		//set value for digital pin
		led = digitalPin;
	}

	//method to initialize the sensor
	public void initialize(){
		//initialize new rgb array
		rgbVal = new int[4];
		//intialize the digital channel for the led
		cdim.setDigitalChannelMode(led, DigitalChannelController.Mode.OUTPUT);
		//turn led on needs to be turned off on start
		ledON();
	}

	//method to turn led on
	public void ledON(){
		ledStat = true;
		cdim.setDigitalChannelState(led, ledStat);
	}

	//method to turn led off
	public void ledOFF(){
		ledStat = false;
		cdim.setDigitalChannelState(led, ledStat);
	}

	//method to check led status
	public boolean ledStat(){
		return ledStat;
	}

	//method to return red value
	public int red(){
		rgbArrayUpdate();
		return rgbVal[0];
	}

	//method to return blue value
	public int blue(){
		rgbArrayUpdate();
		return rgbVal[2];
	}

	//method to return green value
	public int green(){
		rgbArrayUpdate();
		return rgbVal[1];
	}

	//method to return the alpha value
	public int alpha(){
		rgbArrayUpdate();
		return rgbVal[3];
	}

	//method to return array of rgb values
	public int[] rgbArray(){
		rgbArrayUpdate();
		return rgbVal;
	}

	//method to return array of rgb values
	public void rgbArrayUpdate(){
		rgbVal[0] = mux.getCRGB(muxPort)[1];
		rgbVal[1] = mux.getCRGB(muxPort)[2];
		rgbVal[2] = mux.getCRGB(muxPort)[3];
		rgbVal[3] = mux.getCRGB(muxPort)[0];
	}
}
