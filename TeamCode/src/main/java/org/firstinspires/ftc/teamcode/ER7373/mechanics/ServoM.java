package org.firstinspires.ftc.teamcode.ER7373.mechanics;

/**
 * Created by Jordan Moss on 10/20/2016.
 */

import com.qualcomm.robotcore.hardware.Servo;

public class ServoM {
    //create variable to store the servo in and servo position
    private Servo servo;
    private float servoPos;

    //constructor to create a servo object at position 0
    public ServoM(Servo s){
        //assign values
        servo = s;
        servoPos = 0;

        //set servo to the initial position
        servo.setPosition(servoPos);
    }

    //constructor to create a servo object at position 0
    public ServoM(Servo s, float p){
        //assign values
        servo = s;
        servoPos = p;

        //set servo to the intial position
        servo.setPosition(servoPos);
    }

    //method to run servo to a particular position
    public void setPos(float pos){
        servo.setPosition(pos);
    }

    //method to run servo to a particular position in degrees
    public void setPosDeg(float posDeg){
        float pos = posDeg/360;
        servo.setPosition(pos);
    }

}
