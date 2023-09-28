package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.Frame;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.vuforia.Frame;
import java.io.*;
import java.util.*;
import android.graphics.Bitmap;

@TeleOp(name = "Tank_drive_01.java")
public class Tank_drive_01 extends LinearOpMode {
    public VuforiaLocalizer vuforia;
    public WebcamName webcamName;
    int captureCounter = 0;
    File captureDirectory = AppUtil.ROBOT_DATA_DIR;
    
    
    
    public static final String TAG = "Vuforia Navigation Sample i hope this works";
    
    @Override
    public void runOpMode(){
        robot_hardware robot = new robot_hardware();
        robot.init(hardwareMap,telemetry);
        //robot.chainlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//hold the arm up
        telemetry.addLine("done calibration, ready to start (and commit war crimes)");
        telemetry.update();
        double servoPos = 0.5;
        robot.claw.setPosition(servoPos);
        
        
        
        webcamName = hardwareMap.get(WebcamName.class, "webcam1");
        
        
        //barbie = hardwareMap.get(ColorSensor.class, "barbie");
        
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AX1s7qf/////AAABmZWtZzvdwkftug/VtGHKfMxD/YhKMhR9zIDe+pfQ1ZfsIIP/Kvdk1cxmTbVFWCc4890tNh7EIjGliQxpolp53OQX1INTnAr6sKBs8f5N5aAOFKh27zhrkhObhwR4sXlfNTAuV7f7k769LqJol35sFaWuhbyZ4rI0cEZB7igSHWSot8m9ghO4Fzkj4n6eHsVTo8xTvFEuWqxh763S9+xy6p1FnTi79/K5wXiT+TNQsegkotT4eEGpdoH0+RE6Mw/Mg2COEGU14PuTF/jBe/cXPbJ138o4ScrPmwdF9IG6GvoShx8ettV7//BpBgiT3dDTLIvbXcGGDWEh6REmVqqxuSjTXy1goI1OBuWtz7nqJjwq";
        parameters.cameraName = webcamName;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        vuforia.enableConvertFrameToBitmap();
        AppUtil.getInstance().ensureDirectoryExists(captureDirectory);
        
        waitForStart();
        
        
        while(opModeIsActive()) {
            // idk what to name the variables below so i just gave them some placeholder names
            // please change them in the future for something better sounding
            telemetry.addLine(Double.toString(servoPos));
            telemetry.update();
            // arm control
            boolean bbutton = this.gamepad1.b;
            boolean abutton = this.gamepad1.a;
            boolean xbutton = this.gamepad1.x;
            

            // drivetrain controls

            float leftstickx = this.gamepad1.left_stick_x;
            float leftsticky = this.gamepad1.left_stick_y;
            float rightstickx = this.gamepad1.right_stick_x;
            float rightsticky = this.gamepad1.right_stick_y;
            
            float ltrig = this.gamepad1.left_trigger;
            float rtrig = this.gamepad1.right_trigger;
            boolean lbump = this.gamepad1.left_bumper;
            boolean rbump = this.gamepad1.right_bumper;
            
            boolean hatUp = this.gamepad1.dpad_up;
            boolean hatDown = this.gamepad1.dpad_down;
            boolean hatLeft = this.gamepad1.dpad_left;
            boolean hatRight = this.gamepad1.dpad_right;
            
            //servo position
            if(xbutton){
                robot.claw.setPosition(1);
            }
            else{
                robot.duck.setPower(hatUp? -0.3: (hatDown? 0.3: 0));
                float delta = (float)(hatLeft? 0.02: (hatRight? -0.02:0));
                if(delta + servoPos < 1.0 && delta + servoPos > 0.0){
                    servoPos += delta;
                    robot.claw.setPosition(servoPos);    
                }
            }
           
            
            if(!bbutton){
                robot.l.setPower(-(lbump? -1 : 1) * (ltrig*0.7 + (abutton? 0.2:0)));

                robot.r.setPower(-(rbump? -1 : 1) * (rtrig*0.7 + (abutton? 0.2:0)));
            }else{
                robot.l.setPower(0);
                robot.r.setPower(0);
            }
            
            
            }
            
            
            
            
        
    }
    
    void captureFrameToFile() {
        vuforia.getFrameOnce(Continuation.create(ThreadPool.getDefault(), new Consumer<Frame>()
            {
            @Override public void accept(Frame frame)
                {
                Bitmap bitmap = vuforia.convertFrameToBitmap(frame);
                if (bitmap != null) {
                    File file = new File(captureDirectory, String.format(Locale.getDefault(), "VuforiaFrame-%d.png", captureCounter++));
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        } finally {
                            outputStream.close();
                            telemetry.log().add("captured %s", file.getName());
                        }
                    } catch (IOException e) {
                        RobotLog.ee(TAG, e, "exception in captureFrameToFile()");
                    }
                }
            }
        }));
    }

        //telemetry.addData("arm position",robot.chainlift.getCurrentPosition());
        //telemetry.update();

  // private DcMotor motor1;

  // /**
  // * This function is executed when this Op Mode is selected from the Driver Station.
  // */
  // @Override
  // public void runOpMode() {
  //   motor1 = hardwareMap.get(DcMotor.class, "motor1");

  //   // Put initialization blocks here.
  //   waitForStart();
  //   if (opModeIsActive()) {
  //     // Put run blocks here.
  //     while (opModeIsActive()) {
  //       // Put loop blocks here.
  //       telemetry.update();
  //       float leftstickx = this.gamepad1.left_stick_x;
  //       motor1.setPower(leftstickx);
  //       telemetry.addLine(String.valueOf(leftstickx));
  //       telemetry.update();
  //     }
  //   }
  // }
}
