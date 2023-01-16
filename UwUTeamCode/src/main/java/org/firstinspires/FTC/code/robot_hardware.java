package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class robot_hardware {
    HardwareMap hw = null;
    public DcMotor lf,lr,rr,rf,chainlift, duck;
    public Servo intake;
    public BNO055IMU imu;
    public robot_hardware(){

    }
    public void init(HardwareMap hwMap, Telemetry telemetry){
        hw = hwMap;
        try {
            lf = hw.get(DcMotor.class, "lf");//ok
            lr = hw.get(DcMotor.class, "lr");//ok
            rf = hw.get(DcMotor.class, "rf");//ok
            rr = hw.get(DcMotor.class, "rr");//ok
            chainlift = hw.get(DcMotor.class, "chainlift");//ok
            duck = hw.get(DcMotor.class, "duck");//ok
            intake = hw.get(Servo.class, "intake");//ok
            lf.setDirection(DcMotorSimple.Direction.REVERSE);
            lr.setDirection(DcMotorSimple.Direction.REVERSE);
            imu = hw.get(BNO055IMU.class, "imu");
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;
            imu.initialize(parameters);

            telemetry.addData("Mode", "calibrating...");
            telemetry.update();

            // make sure the imu gyro is calibrated before continuing.
            while (!imu.isGyroCalibrated()) {
                //idle
            }
        }
        catch (Exception e){
            telemetry.addLine("failed initialization, check your ids");
            telemetry.update();
        }
    }

}
