package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "nick_drive_01.java")
public class nick_drive_01 extends LinearOpMode {
    @Override
    public void runOpMode(){
        robot_hardware robot = new robot_hardware();
        robot.init(hardwareMap,telemetry);
        //robot.chainlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//hold the arm up
        telemetry.addLine("done calibration, ready to start");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {
            

            //float leftstickx = this.gamepad1.left_stick_x;
            float leftsticky = this.gamepad1.left_stick_y;
            //float rightstickx = this.gamepad1.right_stick_x;
            float rightsticky = this.gamepad1.right_stick_y;
            

            robot.l.setPower(leftsticky*0.8);

            robot.r.setPower(rightsticky*0.8);
            
            }}

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
