
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// IMPORTS
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
  private WPI_TalonSRX _left;
  private VictorSPX _right;
  private VictorSPX _yaw;

  // CRIANDO O SERVO
  private Servo _pitchRight, _pitchLeft;
  private double _pitchPos;

  // CRIANDO OS SENSORES DO SISTEMA DE PITCH E YAW
  private DigitalInput _limitLeft, _limitRight;

  // PID ENCODER
  private SparkMaxPIDController _encPID;
  private double _kP, _kI, _kD, _kIz, _kFF, _kMaxOutput, _kMinOutput, _rotations;
  private RelativeEncoder _shotEnc;

  // LIMELGHT
  private NetworkTable _table;
  private NetworkTableEntry _tx, _tv, _ty;

  // SHOOTER CONTROLE RPM
  private double _RPM;

  // PITCH
  private double _minAngle, _maxAngle, _maxPosition;

  //CALCULO DE DISTANCIA
  private double _valConvr, _catHub;


  Timer _t;

  //#endregion

  public Shooter() {

    SmartDashboard.putNumber("LL Angle", 0.0);

    _pitchPos = 0;
    _RPM      = 250.0;

    _minAngle    = 16;
    _maxAngle    = 40;
    _maxPosition = .5;

    _valConvr = 215.0;
    _catHub   = Math.pow(238.0, 2);

    //#region INICIALIZAÇAO DOS SISTEMAS

    //#region DEFININDO OS CONTROLADORES DO SISTEMA DE SHOOTER, PITCH E YAW
    //_yaw   = new VictorSPX(10);
    _left  = new WPI_TalonSRX (1);
    _right = new VictorSPX (13);

    _right.setInverted(false);
    _left.setInverted(false);
    _left.follow(_right);
    //*/
    _pitchRight = new Servo(Constants.Motors.Shooter._pitch_right);
    _pitchLeft  = new Servo(Constants.Motors.Shooter._pitch_left);
    //#endregion
    
    //#region DEFININDO OS SENSORES DO SISTEMA DE PITCH E YAW
    _limitLeft   = new DigitalInput(Constants.Sensors._limit_left);
    _limitRight  = new DigitalInput(Constants.Sensors._limit_right);
    //#endregion

    //#region TABELA DE VALORES LIMELIGHT
    _table = NetworkTableInstance.getDefault().getTable("limelight");
    _tx = _table.getEntry("tx");
    _tv = _table.getEntry("tv");
    _ty = _table.getEntry("ty");
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
    _kP         = 0.1; 
    _kI         = 1e-4;
    _kD         = 1; 
    _kIz        = 0; 
    _kFF        = 0; 
    _kMaxOutput = 1; 
    _kMinOutput = -1;
    _rotations  = 0.0;

    // DEFINE PID COEFICIENTES
    _pidController.setP(_kP);
    _pidController.setI(_kI);
    _pidController.setD(_kD);
    _pidController.setIZone(_kIz);
    _pidController.setFF(_kFF);
    _pidController.setOutputRange(_kMinOutput, _kMaxOutput);

    // RESET ENCODER
    _shoEnc.setPosition(0.0);
    //*/
    //#endregion
  
  }

  // CALCULA CORRECAO ENCODER
  public void encoderCorrection (){

    double p   = _kP;
    double i   = _kI;
    double d   = _kD;
    double iz  = _kIz;
    double ff  = _kFF;
    double max = _kMaxOutput;
    double min = _kMinOutput;
    double rot = _rotations;

    if((p != _kP)) { _encPID.setP(p); _kP = p; }
    if((i != _kI)) { _encPID.setI(i); _kI = i; }
    if((d != _kD)) { _encPID.setD(d); _kD = d; }
    if((iz != _kIz)) { _encPID.setIZone(iz); _kIz = iz; }
    if((ff != _kFF)) { _encPID.setFF(ff); _kFF = ff; }
    if((max != _kMaxOutput) || (min != _kMinOutput)) { 
      _encPID.setOutputRange(min, max); 
      _kMinOutput = min; _kMaxOutput = max; 
    }

    _encPID.setReference(rot, CANSparkMax.ControlType.kPosition);

  }

  // ATIVA O SHOOTER
  public void setActivate(double rpm){

    _right.set(ControlMode.PercentOutput, rpm / 3400);
//*/
}

  // YAW
  public void rotation(double y) {
      _yaw.set(VictorSPXControlMode.PercentOutput, y);
  
  //*/
}

  // RETORNA INDENTIFICAÇAO
  public boolean isLimelightDetected() {

    return _tv.getDouble(0.0) == 1.0 ? true : false;
  
  }

  // ATIVA CONTROLE DA LIMELIGHT NO YAW
  public void limelightYawControl(){

    _yaw.set(VictorSPXControlMode.PercentOutput, _tx.getDouble(0) * 0.1);

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
    _pitchPos = _minAngle;//_ty.getDouble(_maxPosition);//SmartDashboard.getNumber("LL Angle", 16.0);
    _pitchPos = _pitchPos - _maxAngle;
    _pitchPos = _pitchPos / ((_minAngle - _maxAngle) / _maxPosition);

    /*
    // Relaçao exponencial (linha curva)
    _pitchPos = _ty.getDouble(0.0) + 15;
    _pitchPos = _pitchPos - _maxAngle;
    _pitchPos = Math.pow(_pitchPos / (_minAngle - _maxAngle), 2);
    _pitchPos = _pitchPos * _maxPosition;
    //*/


    if (_pitchPos > _maxPosition) _pitchPos = _maxPosition;
    else if (_pitchPos < 0) _pitchPos = 0;

    //SmartDashboard.putNumber("pitchPos", _pitchPos);

    if (dual) {

    _pitchLeft.set(_pitchPos);

    }

    _pitchRight.set(-_pitchPos + 0.5);


  }

  // PITCH
  public void servoMov (double p) {

    _pitchRight.set((-p + 1.0) * _maxPosition);
    _pitchLeft.set(p * _maxPosition);

  }

  public void resetPitch () {

    _pitchLeft.set(0);
    _pitchRight.set(_maxPosition);
    
  }

  public void servoDisable () {

    _pitchRight.setDisabled();
    _pitchLeft.setDisabled();

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

    SmartDashboard.putNumber("Servo 0", _pitchRight.get());
    SmartDashboard.putNumber("Servo 1", _pitchLeft.get());
  }
}