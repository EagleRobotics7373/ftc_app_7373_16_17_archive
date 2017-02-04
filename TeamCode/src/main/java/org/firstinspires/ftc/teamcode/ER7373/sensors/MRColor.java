package org.firstinspires.ftc.teamcode.ER7373.sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;

/**
 * Created by JordanMoss on 1/31/17.
 */

public class MRColor {
    ColorSensor color;
    boolean led;
    int[] rgbc = new int[4];
    I2cAddr addr;

    public MRColor(ColorSensor colorS,int a, boolean light){
        color = colorS;
        addr = I2cAddr.create8bit(a);

        if(light){
            ledON();
        } else {
            ledOFF();
        }

    }
    public MRColor(ColorSensor colorS, boolean light){
        color = colorS;

        if(light){
            ledON();
        } else {
            ledOFF();
        }

    }

    //method to turn on the light
    public void ledON(){
        //color.setI2cAddress(addr);
        color.enableLed(true);
    }

    //method to turn off the light
    public void ledOFF(){
        //color.setI2cAddress(addr);
        color.enableLed(false);
    }

    /*
        method to read values from the sensor and return an array
        0 : R
        1 : G
        2 : B
        3 : C
     */
    public int[] rgbc(){
        //color.setI2cAddress(addr);
        rgbc[0] = color.red();
        rgbc[1] = color.green();
        rgbc[2] = color.blue();
        rgbc[3] = color.alpha();
        return rgbc;
    }

}
