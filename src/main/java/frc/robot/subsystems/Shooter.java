
package frc.robot.subsystems;
// IMPORTS
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

// CODE
public class Shooter extends SubsystemBase {

  // #region CRIAÇAO DAS VARIAVEIS
  // CRIANDO OS CONTROLADORES DO SISTEMA DE SHOOTER, PITCH E YAW
  //private CANSparkMax _right, _left;
  private WPI_TalonSRX ct_left;
  private VictorSPX ct_right, ct_yaw;

  // CRIANDO O SERVO
  private Servo at_pitchR, at_pitchL;
  private double pitchPos;

  // CRIANDO OS SENSORES DO SISTEMA DE PITCH E YAW
  private DigitalInput sn_limitLeft, sn_limitRight;

  // PID ENCODER
  private SparkMaxPIDController _encPID;
  private RelativeEncoder sn_shotEnc;
  private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, rev;

  // LIMELGHT
  private NetworkTable table;
  private NetworkTableEntry tx, tv, ty;

  // SHOOTER CONTROLE RPM
  private double RPM;

  // PITCH
  private double minAngle, maxAngle, maxPos;

  //CALCULO DE DISTANCIA
  private double valConvr, catHub;


  Timer t_time;

  //#endregion

  public Shooter() {

    SmartDashboard.putNumber("LL Angle", 0.0);

    pitchPos = 0;
    RPM      = 3400;

    minAngle = 16;
    maxAngle = 40;
    maxPos   = .5;

    valConvr = 215.0;
    catHub   = Math.pow(238.0, 2);

    //#region INICIALIZAÇAO DOS SISTEMAS

    //#region DEFININDO OS CONTROLADORES DO SISTEMA DE SHOOTER, PITCH E YAW
    try {

      ct_yaw   = new VictorSPX(10);
      ct_left  = new WPI_TalonSRX(1);
      ct_right = new VictorSPX (13);

    } catch (Exception ex) {
      System.out.println("Erro na busca de controlador, linha: " + ex.getStackTrace()[0]);
    }

    ct_right.setInverted(false);
    ct_left.setInverted(false);
    ct_left.follow(ct_right);

    //*/
    at_pitchR = new Servo(Constants.Motors.Shooter.PITCH_RIGHT);
    at_pitchL = new Servo(Constants.Motors.Shooter.PITCH_LEFT);
    //#endregion
    
    //#region DEFININDO OS SENSORES DO SISTEMA DE PITCH E YAW
    sn_limitLeft  = new DigitalInput(Constants.Sensors.LIMIT_LEFT);
    sn_limitRight = new DigitalInput(Constants.Sensors.LIMIT_RIGHT);
    //#endregion

    //#region TABELA DE VALORES LIMELIGHT
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx    = table.getEntry("tx");
    tv    = table.getEntry("tv");
    ty    = table.getEntry("ty");
    //#endregion

    //#region ENCODER SHOOTER
/*
    // DEFINE ENCODER SHOOTER
    _right.restoreFactoryDefaults();
    _shoEnc = _right.getEncoder(Type.kQuadrature, 4096);

    //#region PID DE CORREÇAO DO VALOR DO ENCODER
    _pidController = _right.getPIDController();
    _pidController.setFeedbackDevice(_shoEnc);

    // PID COEFICIENTES
    kP         = 0.1; 
    kI         = 1e-4;
    kD         = 1; 
    kIz        = 0; 
    kFF        = 0; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    rotations  = 0.0;

    // DEFINE PID COEFICIENTES
    _pidController.setP(_kP);
    _pidController.setI(_kI);
    _pidController.setD(_kD);
    _pidController.setIZone(_kIz);
    _pidController.setFF(_kFF);
    _pidController.setOutputRange(_kMinOutput, _kMaxOutput);

    // RESET ENCODER
    sn_shoEnc.setPosition(0.0);
    //*/
    //#endregion
  
  }

  // CALCULA CORRECAO ENCODER
  public void encoderCorrection (){

    double p   = kP;
    double i   = kI;
    double d   = kD;
    double iz  = kIz;
    double ff  = kFF;
    double max = kMaxOutput;
    double min = kMinOutput;
    double rot = rev;

    if (p != kP)   { _encPID.setP(p); kP       = p; }
    if (i != kI)   { _encPID.setI(i); kI       = i; }
    if (d != kD)   { _encPID.setD(d); kD       = d; }
    if (iz != kIz) { _encPID.setIZone(iz); kIz = iz; }
    if (ff != kFF) { _encPID.setFF(ff); kFF    = ff; }
    
    if ((max != kMaxOutput) || (min != kMinOutput)) { 
      _encPID.setOutputRange(min, max); 
      kMinOutput = min; 
      kMaxOutput = max;
    }

    _encPID.setReference(rot, CANSparkMax.ControlType.kPosition);

  }

  // ATIVA O SHOOTER
  public void setActivate(double rpm){

    ct_right.set(ControlMode.PercentOutput, rpm / RPM);
//*/
}

  // YAW
  public void rotation(double y) {
      ct_yaw.set(VictorSPXControlMode.PercentOutput, y);
  
  //*/
}

  // RETORNA INDENTIFICAÇAO
  public boolean isLimelightDetected() {

    return tv.getDouble(0.0) == 1.0 ? true : false;
  
  }

  // ATIVA CONTROLE DA LIMELIGHT NO YAW
  public void limelightYawControl(){

    ct_yaw.set(VictorSPXControlMode.PercentOutput, tx.getDouble(0) * 0.1);

  }
///*
  public void chute(boolean pitchDualMove) {
    limelightPitchControl(pitchDualMove);
    setActivate(3400);
  }

//*/
  // ATIVA CONTROLE DA LIMELIGHT NO PITCH
  public void limelightPitchControl(boolean dual) {

    // Relaçao proporcional (linha direta)
    pitchPos = minAngle;//_ty.getDouble(_maxPosition);//SmartDashboard.getNumber("LL Angle", 16.0);
    pitchPos = pitchPos - maxAngle;
    pitchPos = pitchPos / ((minAngle - maxAngle) / maxPos);

    /*
    // Relaçao exponencial (linha curva)
    _pitchPos = _ty.getDouble(0.0) + 15;
    _pitchPos = _pitchPos - _maxAngle;
    _pitchPos = Math.pow(_pitchPos / (_minAngle - _maxAngle), 2);
    _pitchPos = _pitchPos * _maxPosition;
    //*/


    if (pitchPos > maxPos) pitchPos = maxPos;
    else if (pitchPos < 0) pitchPos = 0;

    //SmartDashboard.putNumber("pitchPos", _pitchPos);

    if (dual) {

    at_pitchL.set(pitchPos);

    }

    at_pitchR.set(-pitchPos + 0.5);


  }

  // PITCH
  public void servoMov (double p) {

    at_pitchR.set((-p + 1.0) * maxPos);
    at_pitchL.set(p * maxPos);

  }

  public void resetPitch () {

    at_pitchL.set(0);
    at_pitchR.set(maxPos);
    
  }

  public void servoDisable () {

    at_pitchR.setDisabled();
    at_pitchL.setDisabled();

  }

  
  //CALCULA DISTANCIA ENTRE O ROBO E O HUB
/*  public double distRoboHub(){
    
    return Math.sqrt(Math.pow(_valConvr / _ta.getDouble(0.0), 2) - _catHub); //catetoDistanciaRoboHub = raizQuadrada(hipotAtual - catetoDaAlturaDoHub)
  
  }//*/

  // PERIODICA
  @Override
  public void periodic() {
    // ATUALIZA CORREÇAO DO ENCODER DO SHOOTER
    //encoderCorrection();

    SmartDashboard.putNumber("Servo 0", at_pitchR.get());
    SmartDashboard.putNumber("Servo 1", at_pitchL.get());

  }
}