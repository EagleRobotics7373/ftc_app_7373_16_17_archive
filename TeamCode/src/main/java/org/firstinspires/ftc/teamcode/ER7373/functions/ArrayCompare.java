package org.firstinspires.ftc.teamcode.ER7373.functions;

/**
 * Created by JordanMoss on 1/9/17.
 */

public class ArrayCompare {
    public ArrayCompare(){}

    //method for great than or equal to
    public boolean greaterEqual(int[] ar1, int[] ar2){
        if (ar1.length == ar2.length){
            for(int i = 0; i < ar1.length; i++){
                if (ar1[i] >= ar2[i]){}
                    else return false;
            }
        } else return false;
        return true;
    }

    //method for great than or equal to
    public boolean greaterEqual(double[] ar1, double[] ar2){
        if (ar1.length == ar2.length){
            for(int i = 0; i < ar1.length; i++){
                if (ar1[i] >= ar2[i]){}
                else {return false;}
            }
        } else return false;
        return true;
    }

    //method for less than
    public boolean lessThan(int[] ar1, int[] ar2){
        if (ar1.length == ar2.length){
            for(int i = 0; i < ar1.length; i++){
                if (ar1[i] < ar2[i]){}
                else return false;
            }
        } else return false;
        return true;
    }

    public boolean lessThan(double[] ar1, double[] ar2){
        if (ar1.length == ar2.length){
            for(int i = 0; i < ar1.length; i++){
                if (ar1[i] < ar2[i]){}
                else return false;
            }
        } else return false;
        return true;
    }

    public boolean lessThanEqual(int[] ar1, int[] ar2){
        if (ar1.length == ar2.length){
            for(int i = 0; i < ar1.length; i++){
                if (ar1[i] <= ar2[i]){}
                else return false;
            }
        } else return false;
        return true;
    }

}
