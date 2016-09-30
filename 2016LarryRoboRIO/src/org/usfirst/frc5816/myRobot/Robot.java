package org.usfirst.frc5816.myRobot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;

public class Robot extends IterativeRobot {
	
    SpeedController frontLeft;
    SpeedController backLeft;
    SpeedController frontRight;
    SpeedController backRight;
    SpeedController intakeMotor;
    RobotDrive driveTrain;
    Compressor compressor;
    Solenoid intakeSolenoid;
    Joystick leftJoystick;
    Joystick rightJoystick;
    Joystick XboxController;

    double intakeSpeed;
    double curve;
    double fastDrive;
    double defaultDrive;
	double breakSpeed;
	

	public void robotInit() {

        frontLeft = new VictorSP(0);
        backLeft = new VictorSP(1);       
        frontRight = new VictorSP(2);
        backRight = new VictorSP(3);
        intakeMotor = new VictorSP(4);
        compressor = new Compressor(0);
        intakeSolenoid = new Solenoid(0);

        
        driveTrain = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
        
        driveTrain.setSafetyEnabled(false);
        driveTrain.setExpiration(0.1);
        driveTrain.setSensitivity(0.5);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
        
    	XboxController = new Joystick(2);

        rightJoystick = new Joystick(1);
        
        leftJoystick = new Joystick(0);
	}

	public void disabledInit() {

	}

	public void disabledPeriodic() {

	}

	public void autonomousInit() {
		curve = 0.0;
		breakSpeed = 0.05;
		
		driveTrain.drive(-0.90, 0.0);
		Timer.delay(1.5);
		driveTrain.drive(0, 0.0);
		driveBreak(.75, -1);
	}

	public void autonomousPeriodic() {
		
	}
	
	public void driveBreak(double time, int direction){
		driveTrain.drive(direction*this.breakSpeed, 0.0);
		Timer.delay(time);
		driveTrain.drive(0.0, 0.0);
	}

	public void teleopInit() {
		defaultDrive = 0.75;
		fastDrive = 1.0;
		intakeSpeed = 1.0;
	}

	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
    	driveTrain.tankDrive(leftJoystick, rightJoystick);
    	
		if (XboxController.getRawAxis(2) > 0) {
    		intakeSolenoid.set(true);
    		intakeMotor.set(intakeSpeed);
    		System.out.println("Motor Forward Recieved");
    	} else if (XboxController.getRawAxis(3) > 0) {
    		intakeSolenoid.set(true);
    		intakeMotor.set(-intakeSpeed);
    		System.out.println("Motor Backward Recieved");
    	} else if (XboxController.getRawButton(5)) {
    		intakeSolenoid.set(true);
    	} else {
    		intakeMotor.set(0.0);
    		intakeSolenoid.set(false);
    		System.out.println("Motor Stop Recieved");
    	}
    	
    	if (XboxController.getRawButton(5)) {
    		intakeSolenoid.set(true);
    	}
	
    	if (leftJoystick.getRawButton(7)) {
    		driveTrain.setMaxOutput(fastDrive);
    	} else {
			driveTrain.setMaxOutput(defaultDrive);
    	}   	
    	
	}

	public void testPeriodic() {
		LiveWindow.run();
	}
}
