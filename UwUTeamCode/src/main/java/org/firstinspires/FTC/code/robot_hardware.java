package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class robot_hardware {
    HardwareMap hw = null;
    public DcMotor lf,lr,rr,rf,chainlift, duck, l, r;
    public Servo claw;
    public WebcamName webcam1;
    public BNO055IMU imu;
    public ColorSensor barbie;
    
    public robot_hardware(){

    }
    public void init(HardwareMap hwMap, Telemetry telemetry){
        hw = hwMap;
        try {
            l = hw.get(DcMotor.class, "l");//ok
            //lr = hw.get(DcMotor.class, "lr");//ok
            //rf = hw.get(DcMotor.class, "rf");//ok
            r = hw.get(DcMotor.class, "r");//ok
            //chainlift = hw.get(DcMotor.class, "chainlift");//ok
            duck = hw.get(DcMotor.class, "duck");//ok
            claw = hw.get(Servo.class, "claw");//ok
            webcam1 = hw.get(WebcamName.class, "webcam1");
            l.setDirection(DcMotorSimple.Direction.REVERSE);
            //r.setDirection(DcMotorSimple.Direction.REVERSE);
            imu = hw.get(BNO055IMU.class, "imu");
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;
            imu.initialize(parameters);
            
            barbie = hw.get(ColorSensor.class, "barbie");
            
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