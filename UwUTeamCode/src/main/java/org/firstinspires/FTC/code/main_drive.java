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

@TeleOp(name = "main_drive.java")
public class main_drive extends LinearOpMode {
    public VuforiaLocalizer vuforia;
    public WebcamName webcamName;
    /*int captureCounter = 0;
    File captureDirectory = AppUtil.ROBOT_DATA_DIR;*/
    
    
    
    public static final String TAG = "Vuforia Navigation Sample i hope this works";
    
    @Override
    public void runOpMode(){
        robot_hardware_claw robot = new robot_hardware_claw();
        robot.init(hardwareMap,telemetry);
        //robot.chainlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//hold the arm up
        telemetry.addLine("done calibration, ready to start (and commit war crimes)");
        telemetry.update();
        double arm1pos = 0, arm2pos = 0;
        robot.arm1.setPosition(arm1pos);
        robot.arm2.setPosition(arm2pos);
        
        
        //webcamName = hardwareMap.get(WebcamName.class, "webcam1");
        
        
        //barbie = hardwareMap.get(ColorSensor.class, "barbie");
        /*
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AX1s7qf/////AAABmZWtZzvdwkftug/VtGHKfMxD/YhKMhR9zIDe+pfQ1ZfsIIP/Kvdk1cxmTbVFWCc4890tNh7EIjGliQxpolp53OQX1INTnAr6sKBs8f5N5aAOFKh27zhrkhObhwR4sXlfNTAuV7f7k769LqJol35sFaWuhbyZ4rI0cEZB7igSHWSot8m9ghO4Fzkj4n6eHsVTo8xTvFEuWqxh763S9+xy6p1FnTi79/K5wXiT+TNQsegkotT4eEGpdoH0+RE6Mw/Mg2COEGU14PuTF/jBe/cXPbJ138o4ScrPmwdF9IG6GvoShx8ettV7//BpBgiT3dDTLIvbXcGGDWEh6REmVqqxuSjTXy1goI1OBuWtz7nqJjwq";
        parameters.cameraName = webcamName;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        vuforia.enableConvertFrameToBitmap();
        AppUtil.getInstance().ensureDirectoryExists(captureDirectory);
        */
        
        //for the claw
        double clawPos = 1, lmtClaw = 0.36, armPos = 0, armStep = 0.001;
        // stateRbump is for ___
        // lockClaw is for locking the claw in one position (controlled by rbump)
        boolean stateRbump = false, lockClaw = false;
        
        waitForStart();
        
        
        while(opModeIsActive()) {
            // idk what to name the variables below so i just gave them some placeholder names
            // please change them in the future for something better sounding
            //telemetry.addLine(Double.toString(servoPos));
            //telemetry.update();
            // arm control
            boolean bbutton = this.gamepad1.b;
            boolean abutton = this.gamepad1.a;
            boolean xbutton = this.gamepad1.x;
            

            // drivetrain controls

            // Tank drive - left and right stick x are not used atm
            float leftstickx = this.gamepad1.left_stick_x;
            float leftsticky = this.gamepad1.left_stick_y;
            float rightstickx = this.gamepad1.right_stick_x;
            float rightsticky = this.gamepad1.right_stick_y;
            
            // left trig and bump is for the arm beyond the elbow
            // rtrig is for actually opening or closing the claw
            // rbump is for locking the claw in a certain position
            float ltrig = this.gamepad1.left_trigger;
            float rtrig = this.gamepad1.right_trigger;
            boolean lbump = this.gamepad1.left_bumper;
            boolean rbump = this.gamepad1.right_bumper;
            
            // Controls the part of the arm that connects to the chassis
            // Up and down are for up and down movement
            boolean hatUp = this.gamepad1.dpad_up;
            boolean hatDown = this.gamepad1.dpad_down;
            // Left and right are for rotating the arm a full 360 degrees
            boolean hatLeft = this.gamepad1.dpad_left;
            boolean hatRight = this.gamepad1.dpad_right;
            
            //For the arm beyond the elbow (not hex)
            //if(0 < armPos && 0.3 > armPos){
            //    armPos += ltrig == 0? 0: (lbump? 0.02:-0.02);
            //    robot.arm1.setPosition(armPos);
            //}
            
            // For the arm beyond the elbow (arm1)
            if(0 <= armPos - armStep && ltrig > 0){
                armPos -= armStep;
            }else if(0.3 >= armPos + armStep && lbump){
                armPos += armStep;
            }
            
            robot.arm1.setPosition(armPos);
            telemetry.addLine(Double.toString(armPos));
            telemetry.update();
            
            
            // All for the claw
            // TODO: make two buttons (trig and bump) useful for closing and opening the claw
            // With the following code, if rbump is false, stateRbump will switch between false and true rapidly
            if(stateRbump) {
                stateRbump = false;
                if(rbump) {
                    lockClaw = true;
                } else {
                    lockClaw = false;
                }
            }
            if(!rbump && !stateRbump) {
                stateRbump = true;
            }

            if(!lockClaw) {
                // If rtrig is pressed, (1-lmtClaw) means closed
                // Otherwise, (1-rtrig) means open
                clawPos = (rtrig) >= lmtClaw? (1-lmtClaw) : (1-rtrig);
                robot.arm2.setPosition(clawPos);    
            }
            
            
            // Controls up and down movement of main arm
            robot.duck.setPower(hatUp? 0.8: (hatDown? -0.8: 0));
            // Controls rotational movement (360 degrees) of main arm
            robot.fred.setPower(hatRight? -0.4: (hatLeft? 0.4: 0));
            
            
           
            //movement
            // TODO: make tank drive easier to control (invert or switch a few values) (requires further irl testing bc I forgot how it works)
            if(abutton){
                robot.l.setPower(0.6);
                robot.r.setPower(0.6);
            }else{
                robot.l.setPower(-0.6*leftsticky);
                robot.r.setPower(-0.6*rightsticky);
            }
            /*
            if(!bbutton){
                
                //robot.l.setPower(-(lbump? -1 : 1) * (ltrig*0.7 + (abutton? 0.2:0)));
                //rightstickx
                telemetry.addLine(Float.toString(rtrig!=0? -rtrig:ltrig));
                telemetry.update();
                
                rightstickx *= 0.5;
                rightstickx += 0;
                
                robot.l.setPower(0.3*(rtrig!=0? -rtrig:ltrig) + rightstickx); 
                robot.r.setPower(0.3*(rtrig!=0? -rtrig:ltrig) + (-1*rightstickx)); 
            }else{
                robot.l.setPower(0);
                robot.r.setPower(0);
            }
            */
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
