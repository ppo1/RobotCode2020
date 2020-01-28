package frc.robot.subsystems;

import frc.robot.*;
import frc.robot.commands.ShooterCommands.Accelerate;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.*;

public class Shooter extends SubsystemBase {
  private WPI_TalonSRX leader;
  private WPI_VictorSPX follower;
  
  private boolean acc = false;

  // constants
  public static final double throwingAngle = Math.toRadians(0);
  public Shooter() {
    this.leader = new WPI_TalonSRX(RobotMap.ShooterPorts.TALON_PORT);
    this.follower = new WPI_VictorSPX(RobotMap.ShooterPorts.VICTOR_PORT);
    this.follower.follow(this.leader);

    
    SmartDashboard.putNumber("Shooter_kP",0);
    SmartDashboard.putNumber("Shooter_kI",0);
    SmartDashboard.putNumber("Shooter_kD",0);
    SmartDashboard.putNumber("Shooter_kF",0);

    this.setDefaultCommand(new ConditionalCommand(new Accelerate(0.8),new Accelerate(0),() -> this.acc));
  }


  public void configMotors(){
    this.leader.config_kP(0,PIDF.kP.value);
    this.leader.config_kI(0,PIDF.kI.value);
    this.leader.config_kD(0,PIDF.KD.value);
    this.leader.config_kF(0,PIDF.kF.value);
  }

  public void switchAccelerate(){
    this.acc != this.acc;
  }

  

  public int getEncoderPos(){
    return this.leader.getSelectedSensorPosition();
  }
  public double getVelocity(){
    // in M/S
    return (double)this.leader.getSelectedSensorVelocity()/10;
  }
  public void setSpeed(double DesiredRPM){
    // sets the Velocity in RPM
    this.leader.set(ControlMode.Velocity,DesiredRPM/Constants.TICKS_PER_100MS_TO_PRM);
  }
  public double getDesiredVelocity(){
    double Vy = 
    
    return 2;
  }

  /**
   * @param distance 
   * returns true if the shooter is ready to shoot by the velocity of the shooter
   */
  public boolean isReadyForShooting(double distance){
    //TODO: the math that represantes that
    return false; // temporery for now
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * this function calculates the min distance the robot can be from the power
   * port for shooting
   */
  private double calculateMinDistance(){
    double MinThrowingSpeed = calculateMinSpeed();
    double Vy = MinThrowingSpeed*Math.sin(throwingAngle);
    double Vx = MinThrowingSpeed*Math.sin(throwingAngle);
    return Vx*Vy/Constants.G;
  }
  private double calculateMinSpeed(){
    double sin = Math.sin(throwingAngle);
    double heightDifference = Constants.POWER_PORT_HEIGHT - Constants.ROBOT_HEIGHT;
    return Math.sqrt(
      (2*Constants.G/Math.pow(sin, 2))*heightDifference
      );
  }
  enum PIDF{
    kP(0),
    kI(0),
    KD(0),
    kF(0),
    SD_kP(SmartDashboard.getNumber("Shooter_kP",0)),
    SD_kI(SmartDashboard.getNumber("Shooter_kI",0)),
    SD_KD(SmartDashboard.getNumber("Shooter_kD",0)),
    SD_kF(SmartDashboard.getNumber("Shooter_kF",0));
    
    public double value;
    private PIDF(double value){
      this.value = value;
    }
  }
}
