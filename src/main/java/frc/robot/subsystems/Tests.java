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
  
  private ADXRS450_Gyro sn_gyro;

  private WPI_TalonSRX ct_rightMaster, ct_leftMaster, ct_right, ct_left;

  private int timeoutMs      = 30;
  private int primyPID       = 0;
  private int slotDistc      = 0;
  private int slotVelty      = 1;
  private double neutralDb   = .001;
  private double[] PIDFDistc = {.08, .0, .0, 100, .5};//{p, i, d, f, integralZone, peakOutput}
  private double[] PIDFVelty = {.08, .0, .0, 100, .5};//{p, i, d, f, integralZone, peakOutput}

  private double targetPos;

  private PIDController _autDirPID;
  private double corrt, _angle;


  public Tests() {

    sn_gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
    sn_gyro.calibrate();
    sn_gyro.reset();

    _autDirPID.setPID(0.001, 0, 0);
    _autDirPID.setTolerance(2.0, 0.1);

    try {

      ct_rightMaster = new WPI_TalonSRX(1);
      ct_leftMaster  = new WPI_TalonSRX(16);
      //ct_right       = new WPI_TalonSRX(3);
      //ct_left        = new WPI_TalonSRX(4);

    } catch (Exception ex) {

      System.out.printf("\n\nERRO NA BUSCA DE CONTROLADOR, LINHA: %s\n\n", ex.getStackTrace()[0]);
      return;

    }
    
    configMotionMagic ();

    //ct_right.follow(ct_rightMaster);
    //ct_left.follow(ct_leftMaster);

		resetEnc();

  }

public void configMotionMagic () {
  // Para os motores
  ct_rightMaster.set(0);
  ct_leftMaster.set(0);

 // Padrao
  ct_rightMaster.configFactoryDefault();
  ct_leftMaster.configFactoryDefault();

  // Freio no ponto neutro (0)
  ct_leftMaster.setNeutralMode(NeutralMode.Brake);
  ct_rightMaster.setNeutralMode(NeutralMode.Brake);

  // Encoder esquerdo
  ct_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, primyPID, timeoutMs);

  // Da acesso do encoder direito ao controlador
  ct_rightMaster.configRemoteFeedbackFilter(ct_leftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, timeoutMs);

  // Termos da soma da leitura
  ct_rightMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, timeoutMs);
  ct_rightMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, timeoutMs);
  ct_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, primyPID, timeoutMs);

  // Porcentagem da leitura (divide a soma por 2)
  ct_rightMaster.configSelectedFeedbackCoefficient(0.5, primyPID, timeoutMs);
  
  // Direçao dos motores e da leitura dos encoders
  ct_leftMaster.setInverted(false);
  ct_leftMaster.setSensorPhase(true);
  ct_rightMaster.setInverted(true);
  ct_rightMaster.setSensorPhase(true);

  ct_rightMaster.configNeutralDeadband(neutralDb, timeoutMs);
  ct_leftMaster.configNeutralDeadband(neutralDb, timeoutMs);
  
  // Aceleraçao e velocidade
  ct_rightMaster.configMotionAcceleration(18400 * 0.75, timeoutMs);
  ct_rightMaster.configMotionCruiseVelocity(18400 * 0.75, timeoutMs);
  
  // Maximo e minimo de saida
  ct_leftMaster.configPeakOutputForward(1.0, timeoutMs);
  ct_leftMaster.configPeakOutputReverse(-1.0, timeoutMs);
  ct_rightMaster.configPeakOutputForward(1.0, timeoutMs);
  ct_rightMaster.configPeakOutputReverse(-1.0, timeoutMs);
  
  // Coeficientes do PIDF do modo de controle Motion Magic
  setPIDFValues(slotDistc, PIDFDistc);

  // Coeficientes do PIDF do modo de controle Velocity
  setPIDFValues(slotVelty, PIDFVelty);


  // Periodo dos Frames
  ct_rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, timeoutMs);
  ct_rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, timeoutMs);
  ct_rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 10);
  ct_leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, timeoutMs);

}

public void setPIDFValues (int slot, double[] k) {

  ct_rightMaster.config_kP(slot, k[0], timeoutMs);
  ct_rightMaster.config_kI(slot, k[1], timeoutMs);
  ct_rightMaster.config_kD(slot, k[2], timeoutMs);
  ct_rightMaster.config_kF(slot, k[3], timeoutMs);
  ct_rightMaster.config_IntegralZone(slot, k[4], timeoutMs);
  ct_rightMaster.configClosedLoopPeakOutput(slot, k[5], timeoutMs);

}

  public void moveMotionMagic (double dist, double dir) {

    // Slot de coeficientes para aplicar ao PID
    ct_rightMaster.selectProfileSlot(slotDistc, primyPID);
    ct_leftMaster.selectProfileSlot(slotDistc, primyPID);

    _angle += dir;
    _autDirPID.setSetpoint(_angle);

    targetPos = dist * 270;

    resetEnc ();

    ct_rightMaster.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, -corrt);
    ct_leftMaster.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, corrt);

  }

  public void moveVelocity (double speed, double turn) {

    // Slot de coeficientes para aplicar ao PID
    ct_rightMaster.selectProfileSlot(slotVelty, primyPID);
    ct_leftMaster.selectProfileSlot(slotVelty, primyPID);

    ct_rightMaster.set(ControlMode.Velocity, speed, DemandType.ArbitraryFeedForward, -turn);
    ct_leftMaster.set(ControlMode.Velocity, speed, DemandType.ArbitraryFeedForward, turn);

  }

  public void resetEnc () {

    ct_leftMaster.getSensorCollection().setQuadraturePosition(0, timeoutMs);
		ct_rightMaster.getSensorCollection().setQuadraturePosition(0, timeoutMs);

   }

  @Override
  public void periodic() {

    corrt = _autDirPID.calculate(sn_gyro.getAngle());
    //SmartDashboard.putNumber("", );

  }
}
