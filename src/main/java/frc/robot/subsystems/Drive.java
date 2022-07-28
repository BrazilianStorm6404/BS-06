package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Drive extends SubsystemBase {

  private PWMSparkMax _bRight;
  private Jaguar _fRight;
  private CANSparkMax _fLeft;
  private WPI_VictorSPX _bLeft;

  private MotorControllerGroup _left, _right;

  private DifferentialDrive _drivetrain;

  public Drive() {

    _bRight = new PWMSparkMax(0);
    _fRight = new Jaguar(1);
    _fLeft  = new CANSparkMax(25, MotorType.kBrushed);
    _bLeft  = new WPI_VictorSPX(13);

    _bLeft.setInverted(false);
    _fLeft.setInverted(true);
    _bRight.setInverted(true);
    _fRight.setInverted(true);

    _bLeft.setNeutralMode(NeutralMode.Brake);
    _fLeft.setIdleMode(IdleMode.kBrake);

    _left = new MotorControllerGroup(_bLeft, _fLeft);
    _right = new MotorControllerGroup(_bRight, _fRight);

    _drivetrain = new DifferentialDrive(_left, _right);

  }

  public void jaguarDrive (double y, double x) { 

    _drivetrain.arcadeDrive(y, x);

  }

  @Override
  public void periodic() {
  }
}
