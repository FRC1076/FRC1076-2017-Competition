package org.usfirst.frc.team1076.robot.commands;

import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.components.Gyroscope;
import org.usfirst.frc.team1076.robot.subsystems.Drivetrain;

/**
 * TurnWithGyro takes a Strongback Gyroscope, a Drivetrain,
 * a speed, and a target angle in degrees and attempts to turn
 * to that angle as specified by the gyro. Note that positive angles turn *right*
 * and negative angles turn *left*.
 */
public class TurnWithGyro extends Command {
    public double reduction_threshold = 30;
    public double reduction_factor = 0.7;
    Gyroscope gyro;
    Drivetrain drivetrain;
    double speed;
    double targetAngle;
    
    /**
     * Create a new TurnWithGyro 
     * @param gyro        a Strongback Gyroscope
     * @param drivetrain  a drivetrain
     * @param speed       speed from -1 to 1 (inclusive) to drive at
     * @param targetAngle angle, in degrees, to turn. Note that positive is right, while negative is left
     */
    public TurnWithGyro(Gyroscope gyro, Drivetrain drivetrain, double speed, double targetAngle) {
        super(drivetrain);
        this.gyro = gyro;
        this.drivetrain = drivetrain;
        this.speed = speed;
        this.targetAngle = targetAngle;
    }

    @Override
    public void initialize() {
        gyro.zero();
    }

    @Override
    public boolean execute() {
        if (Math.abs(targetAngle - gyro.getAngle()) < reduction_threshold) {
            speed *= reduction_factor;
        }

        // If turning right
        if (targetAngle > 0) {
            drivetrain.setLeftSpeed(speed);
            drivetrain.setRightSpeed(-speed);
        } else { // Else if turning left
            drivetrain.setLeftSpeed(-speed);
            drivetrain.setRightSpeed(speed);
        }
        if (isFinished()) {
            Strongback.logger().info("done, gyro=" + gyro.getAngle());
            return true;
        } else {
            Strongback.logger().info("not done, gyro=" + gyro.getAngle());
            return false;
        }
    }

    public boolean isFinished() {
        return Math.abs(targetAngle) <= Math.abs(gyro.getAngle());
    }

    @Override
    public void end() {
        drivetrain.stop();
    }
}
