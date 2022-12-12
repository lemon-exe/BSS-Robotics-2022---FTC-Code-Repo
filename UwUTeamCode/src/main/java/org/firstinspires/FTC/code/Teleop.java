package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Experimental_Teleop")
public class Teleop extends LinearOpMode {
    @Override
    public void runOpMode(){
        robot_hardware robot = new robot_hardware();
        robot.init(hardwareMap,telemetry);
        robot.chainlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//hold the arm up
        telemetry.addLine("done calibration, ready to start");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {
            // idk what to name the variables below so i just gave them some placeholder names
            // please change them in the future for something better sounding

            // arm control
            boolean bbutton = this.gamepad1.b;
            boolean abutton = this.gamepad1.a;
            int raisestate = 1;
            int armtarget = 100;
            int circumference = 100;
            int targetrotations = Math.round(armtarget/circumference);

            if(bbutton) {
                robot.chainlift.setPower(1);
                robot.chainlift.setTargetPosition(targetrotations * raisestate);
                raisestate *= -1;
            }

            // drivetrain controls

            float leftstickx = this.gamepad1.left_stick_x;
            float leftsticky = this.gamepad1.left_stick_y;
            float rightstickx = this.gamepad1.right_stick_x;
            float rightsticky = this.gamepad1.right_stick_x;
            boolean duck = this.gamepad1.x;

            robot.lf.setPower(leftsticky + leftstickx - rightstickx);
            robot.lr.setPower(leftsticky - leftstickx + rightstickx);
            robot.rf.setPower(leftsticky - leftstickx + rightstickx);
            robot.rr.setPower(leftsticky + leftstickx - rightstickx);
            
            }

        telemetry.addData("arm position",robot.chainlift.getCurrentPosition());
        telemetry.update();
    }
}
