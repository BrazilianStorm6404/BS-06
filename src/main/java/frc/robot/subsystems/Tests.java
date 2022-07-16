package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Tests extends SubsystemBase {
  
  private ADXRS450_Gyro _gyro;

  private WPI_TalonSRX _rightMaster, _leftMaster, _right, _left;

  private int kTimeoutMs          = 30;
  private double kNeutralDeadband = .001;
  private int PID_PRIMARY         = 0;
  private int kSlot_Distanc       = 0;
  private int kSlot_Velocity      = 1;
  private double[] PIDFDistanc    = {.08, .0, .0, 100, .5};//{p, i, d, f, integralZone, peakOutput}
  private double[] PIDFVelocity   = {.08, .0, .0, 100, .5};//{p, i, d, f, integralZone, peakOutput}

  private double targetPos;

  private PIDController _autDirPID;
  private double _correction;
  private double _angle;


  public Tests() {

    _gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
    _gyro.calibrate();
    _gyro.reset();

    _autDirPID.setPID(0.001, 0, 0);
    _autDirPID.setTolerance(2.0, 0.1);

    _rightMaster = new WPI_TalonSRX(1);
    _leftMaster  = new WPI_TalonSRX(16);
    //_right       = new WPI_TalonSRX(3);
    //_left        = new WPI_TalonSRX(4);
    
    configMotionMagic ();

    //_right.follow(_rightMaster);
    //_left.follow(_leftMaster);

		resetEnc();

  }

public void configMotionMagic () {
  // Para os motores
  _rightMaster.set(0);
  _leftMaster.set(0);

 // Padrao
  _rightMaster.configFactoryDefault();
  _leftMaster.configFactoryDefault();

  // Freio no ponto neutro (0)
  _leftMaster.setNeutralMode(NeutralMode.Brake);
  _rightMaster.setNeutralMode(NeutralMode.Brake);

  // Encoder esquerdo
  _leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PID_PRIMARY, kTimeoutMs);

  // Da acesso do encoder direito ao controlador
  _rightMaster.configRemoteFeedbackFilter(_leftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, kTimeoutMs);

  // Termos da soma da leitura
  _rightMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, kTimeoutMs);
  _rightMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, kTimeoutMs);
  _rightMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, PID_PRIMARY, kTimeoutMs);

  // Porcentagem da leitura (divide a soma por 2)
  _rightMaster.configSelectedFeedbackCoefficient(0.5, PID_PRIMARY, kTimeoutMs);
  
  // Direçao dos motores e da leitura dos encoders
  _leftMaster.setInverted(false);
  _leftMaster.setSensorPhase(true);
  _rightMaster.setInverted(true);
  _rightMaster.setSensorPhase(true);

  _rightMaster.configNeutralDeadband(kNeutralDeadband, kTimeoutMs);
  _leftMaster.configNeutralDeadband(kNeutralDeadband, kTimeoutMs);
  
  // Aceleraçao e velocidade
  _rightMaster.configMotionAcceleration(18400 * 0.75, kTimeoutMs);
  _rightMaster.configMotionCruiseVelocity(18400 * 0.75, kTimeoutMs);
  
  // Maximo e minimo de saida
  _leftMaster.configPeakOutputForward(1.0, kTimeoutMs);
  _leftMaster.configPeakOutputReverse(-1.0, kTimeoutMs);
  _rightMaster.configPeakOutputForward(1.0, kTimeoutMs);
  _rightMaster.configPeakOutputReverse(-1.0, kTimeoutMs);
  
  // Coeficientes do PIDF do modo de controle Motion Magic
  setPIDFValues(kSlot_Distanc, PIDFDistanc);

  // Coeficientes do PIDF do modo de controle Velocity
  setPIDFValues(kSlot_Velocity, PIDFVelocity);


  // Periodo dos Frames
  _rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, kTimeoutMs);
  _rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, kTimeoutMs);
  _rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 10);
  _leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, kTimeoutMs);

}

public void setPIDFValues (int slot, double[] k) {

  _rightMaster.config_kP(slot, k[0], kTimeoutMs);
  _rightMaster.config_kI(slot, k[1], kTimeoutMs);
  _rightMaster.config_kD(slot, k[2], kTimeoutMs);
  _rightMaster.config_kF(slot, k[3], kTimeoutMs);
  _rightMaster.config_IntegralZone(slot, k[4], kTimeoutMs);
  _rightMaster.configClosedLoopPeakOutput(slot, k[5], kTimeoutMs);

}

  public void moveMotionMagic (double dist, double dir) {

    // Slot de coeficientes para aplicar ao PID
    _rightMaster.selectProfileSlot(kSlot_Distanc, PID_PRIMARY);
    _leftMaster.selectProfileSlot(kSlot_Distanc, PID_PRIMARY);

    _angle += dir;
    _autDirPID.setSetpoint(_angle);

    targetPos = dist * 270;

    resetEnc ();

    _rightMaster.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, -_correction);
    _leftMaster.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, _correction);

  }

  public void moveVelocity (double speed, double turn) {

    // Slot de coeficientes para aplicar ao PID
    _rightMaster.selectProfileSlot(kSlot_Velocity, PID_PRIMARY);
    _leftMaster.selectProfileSlot(kSlot_Velocity, PID_PRIMARY);

    _rightMaster.set(ControlMode.Velocity, speed, DemandType.ArbitraryFeedForward, -turn);
    _leftMaster.set(ControlMode.Velocity, speed, DemandType.ArbitraryFeedForward, turn);

  }

  public void resetEnc () {

    _leftMaster.getSensorCollection().setQuadraturePosition(0, kTimeoutMs);
		_rightMaster.getSensorCollection().setQuadraturePosition(0, kTimeoutMs);

   }

  @Override
  public void periodic() {

    _correction = _autDirPID.calculate(_gyro.getAngle());
    //SmartDashboard.putNumber("", );

  }
}
