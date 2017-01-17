package org.firstinspires.ftc.teamcode.ER7373.sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import java.lang.InterruptedException;


/**
 * Created by Jordan Moss on 12/3/2016.
 *
 * Name color sensor anything
 *
 */
public class AdafruitRGBSensorObj {
	//variables for the color sensor and led and CDIM
	private int led;
	private ColorSensor rgb;
	private DeviceInterfaceModule cdim;

	//array to store the RGB values of the sensor
	private int[] rgbVal;

	//boolean for led status
	boolean ledStat = false;


	//constructor for color sensor
	public AdafruitRGBSensorObj(ColorSensor color,DeviceInterfaceModule coredim, int digitalPin){
		//get color sensor from hardware map
		rgb = color;
		//get cdim from hardware map
		cdim = coredim;
		//set value for digital pin
		led = digitalPin;
	}

	//method to initialize the sensor
	public void initialize(){
		//initialize new rgb array
		rgbVal = new int[3];
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
		return rgb.alpha();
	}

	//method to return array of rgb values
	public int[] rgbArray(){
		rgbVal[0] = rgb.red();
		rgbVal[1] = rgb.green();
		rgbVal[2] = rgb.blue();
		return rgbVal;
	}

	//method to return array of rgb values
	public void rgbArrayUpdate(){
		rgbVal[0] = rgb.red();
		rgbVal[1] = rgb.green();
		rgbVal[2] = rgb.blue();
		//rgbVal[3] = rgb.alpha();
	}
}
