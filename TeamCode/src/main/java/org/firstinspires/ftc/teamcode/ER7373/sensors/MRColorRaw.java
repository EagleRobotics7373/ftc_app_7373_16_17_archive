package org.firstinspires.ftc.teamcode.ER7373.sensors;

/**
 * Created by JordanMoss on 2/3/17.
 */
    import android.graphics.Color;

    //import com.qualcomm.robotcore.hardware.ColorSensor;
    import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
    import com.qualcomm.robotcore.hardware.I2cAddr;
    import com.qualcomm.robotcore.hardware.I2cController;
    import com.qualcomm.robotcore.util.TypeConversion;

    import java.util.concurrent.locks.Lock;

    public class MRColorRaw implements I2cController.I2cPortReadyCallback {
        private static volatile I2cAddr ADDRESS_I2C;
        private static final int ADDRESS_COMMAND = 0x03;
        private static final int ADDRESS_COLOR_NUMBER = 4;
        private static final int OFFSET_COMMAND = 4;
        private static final int OFFSET_COLOR_NUMBER = 5;
        private static final int OFFSET_RED_READING = 6;
        private static final int OFFSET_GREEN_READING = 7;
        private static final int OFFSET_BLUE_READING = 8;
        private static final int OFFSET_ALPHA_VALUE = 9;
        private static final int BUFFER_LENGTH = 6;
        private static final int COMMAND_PASSIVE_LED = 0x01;
        private static final int COMMAND_ACTIVE_LED = 0x00;

        private final DeviceInterfaceModule a;
        private final byte[] b;
        private final Lock c;
        private final byte[] d;
        private final Lock e;
        private SATTE f;
        private volatile int g;
        private final int h;

        public MRColorRaw(DeviceInterfaceModule deviceInterfaceModule, int physicalPort, int a) {
            ADDRESS_I2C = I2cAddr.create8bit(a);
            this.f = SATTE.READ_MODE;
            this.g = 0;
            this.a = deviceInterfaceModule;
            this.h = physicalPort;
            this.b = deviceInterfaceModule.getI2cReadCache(physicalPort);
            this.c = deviceInterfaceModule.getI2cReadCacheLock(physicalPort);
            this.d = deviceInterfaceModule.getI2cWriteCache(physicalPort);
            this.e = deviceInterfaceModule.getI2cWriteCacheLock(physicalPort);
            deviceInterfaceModule.enableI2cReadMode(physicalPort, ADDRESS_I2C, ADDRESS_COMMAND, BUFFER_LENGTH);
            deviceInterfaceModule.setI2cPortActionFlag(physicalPort);
            deviceInterfaceModule.writeI2cCacheToController(physicalPort);
            deviceInterfaceModule.registerForI2cPortReadyCallback(this, physicalPort);
        }

        public int red() {
            return this.a(OFFSET_RED_READING);
        }

        public int green() {
            return this.a(OFFSET_GREEN_READING);
        }

        public int blue() {
            return this.a(OFFSET_BLUE_READING);
        }

        public int alpha() {
            return this.a(OFFSET_ALPHA_VALUE);
        }

        public int[] rgbc(){
            int[] rgbc = new int[4];
            rgbc[0] = red();
            rgbc[1] = green();
            rgbc[2] = blue();
            rgbc[3] = alpha();
            return rgbc;
        }

        public int argb() {
            return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
        }

        public void ledOn() {
            byte var2 = COMMAND_ACTIVE_LED;

            if(this.g != var2) {
            this.g = var2;
            this.f = SATTE.WRITE_DIRTY;

            try {
                this.e.lock();
                this.d[ADDRESS_COLOR_NUMBER] = var2;
            } finally {
                this.e.unlock();
            }
            }
        }

        public void ledOff() {
            byte var2 = COMMAND_PASSIVE_LED;

            if(this.g != var2) {
                this.g = var2;
                this.f = SATTE.WRITE_DIRTY;

                try {
                    this.e.lock();
                    this.d[ADDRESS_COLOR_NUMBER] = var2;
                } finally {
                    this.e.unlock();
                }
            }
        }

        private int a(int var1) {
            byte var2;
            try {
                this.c.lock();
                var2 = this.b[var1];
            } finally {
                this.c.unlock();
            }

            return TypeConversion.unsignedByteToInt(var2);
        }

        public String getDeviceName() {
            return "Modern Robotics Color Sensor";
        }

        public String getConnectionInfo() {
            return this.a.getConnectionInfo() + "; I2C port: " + this.h;
        }

        public int getVersion() {
            return 1;
        }

        public void close() {
        }

        public void portIsReady(int port) {
            this.a.setI2cPortActionFlag(this.h);
            this.a.readI2cCacheFromController(this.h);
            if(this.f == SATTE.WRITE_DIRTY) {
                this.a.enableI2cWriteMode(this.h, ADDRESS_I2C, ADDRESS_COMMAND, BUFFER_LENGTH);
                this.a.writeI2cCacheToController(this.h);
                this.f = SATTE.WRITE_MODE;
            } else if(this.f == SATTE.WRITE_MODE) {
                this.a.enableI2cWriteMode(this.h, ADDRESS_I2C, ADDRESS_COMMAND, BUFFER_LENGTH);
                this.a.writeI2cCacheToController(this.h);
                this.f = SATTE.READ_MODE;
            } else {
                this.a.writeI2cPortFlagOnlyToController(this.h);
            }
        }

        private enum SATTE {
            READ_MODE,
            WRITE_DIRTY,
            WRITE_MODE;

            SATTE() {
            }
        }
    }

