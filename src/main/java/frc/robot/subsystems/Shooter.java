
package frc.robot.subsystems;
// IMPORTS
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;
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
  private CANSparkMax ct_right, ct_left;
  private VictorSPX ct_yaw;

  // CRIANDO O SERVO
  private Servo at_pitchR, at_pitchL;
  private double pitchPos;

  // CRIANDO OS SENSORES DO SISTEMA DE PITCH E YAW
  private DigitalInput sn_limitLeft, sn_limitRight;

  // PID ENCODER
  private SparkMaxPIDController _pidEnc;
  private RelativeEncoder sn_shotEnc;
  private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

  // LIMELGHT
  private NetworkTable table;
  private NetworkTableEntry tx, tv, ty;

  // SHOOTER CONTROLE RPM
  private double RPM, kRPM;

  // PITCH
  private double minAngle, maxAngle, maxPos;

  private Timer t_time;

  //#endregion


  public Shooter() {

    //SmartDashboard.putNumber("LL Angle", 0.0);

    pitchPos = 0;
    RPM      = 21000;
    kRPM     = 0;

    minAngle = -12;
    maxAngle = 18;
    maxPos   = .5;

    sn_limitLeft  = new DigitalInput(Constants.Sensors.LIMIT_LEFT);
    sn_limitRight = new DigitalInput(Constants.Sensors.LIMIT_RIGHT);

    //#region DEFININDO OS CONTROLADORES DO SISTEMA DE SHOOTER, PITCH E YAW

      //try {

        ct_yaw   = new VictorSPX(Constants.Motors.Shooter.YAW);
        ct_left  = new CANSparkMax(Constants.Motors.Shooter.LEFT, MotorType.kBrushed);
        ct_right = new CANSparkMax (Constants.Motors.Shooter.RIGHT, MotorType.kBrushed);

      //} catch (Exception ex) {

        //System.out.printf("\n\nERRO NA BUSCA DE CONTROLADOR, LINHA: %s\n\n", ex.getStackTrace()[0]);
      

      //}

      ct_right.restoreFactoryDefaults();
      ct_left.restoreFactoryDefaults();

      ct_right.setInverted(true);
      ct_left.setInverted(false);
      //ct_right.follow(ct_left);
      

      //*/
      at_pitchR = new Servo(Constants.Motors.Shooter.PITCH_RIGHT);
      at_pitchL = new Servo(Constants.Motors.Shooter.PITCH_LEFT);
    
    //#endregion

    //#region TABELA DE VALORES LIMELIGHT
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx    = table.getEntry("tx");
    tv    = table.getEntry("tv");
    ty    = table.getEntry("ty");
    //#endregion

    //#region ENCODER SHOOTER

      // DEFINE ENCODER SHOOTER
      sn_shotEnc = ct_left.getEncoder(Type.kQuadrature, 4096);

      sn_shotEnc.setInverted(false);

      sn_shotEnc.setPosition(0.0);

      //sn_shotEnc.setVelocityConversionFactor(6000);
    
    //#endregion

    //#region PID DE CORREÇAO DO VALOR DO ENCODER
/*
      _pidEnc = ct_left.getPIDController();
      _pidEnc.setFeedbackDevice(sn_shotEnc);

      // PID COEFICIENTES
      kP         = 0.1; 
      kI         = 1e-4;
      kD         = 1; 
      kIz        = 0; 
      kFF        = 0; 
      kMaxOutput = 1; 
      kMinOutput = -1;

      // DEFINE PID COEFICIENTES
      _pidEnc.setP(kP);
      _pidEnc.setI(kI);
      _pidEnc.setD(kD);
      _pidEnc.setIZone(kIz);
      _pidEnc.setFF(kFF);
      _pidEnc.setOutputRange(kMinOutput, kMaxOutput);

    //*/
    
    //#endregion
          
  
  }


  // CALCULA CORRECAO ENCODER
  public void encoderCorrection (){
/*
    double p   = kP;
    double i   = kI;
    double d   = kD;
    double iz  = kIz;
    double ff  = kFF;
    double max = kMaxOutput;
    double min = kMinOutput;

    if (p != kP)   { _pidEnc.setP(p); kP       = p; }
    if (i != kI)   { _pidEnc.setI(i); kI       = i; }
    if (d != kD)   { _pidEnc.setD(d); kD       = d; }
    if (iz != kIz) { _pidEnc.setIZone(iz); kIz = iz; }
    if (ff != kFF) { _pidEnc.setFF(ff); kFF    = ff; }
    
    if ((max != kMaxOutput) || (min != kMinOutput)) { 
      _pidEnc.setOutputRange(min, max); 
      kMinOutput = min; 
      kMaxOutput = max;
    }

    _pidEnc.setReference(kRPM, CANSparkMax.ControlType.kVelocity);
*/

  }

  public void chute(boolean pitchDualMove) {

    //limelightPitchControl(pitchDualMove);
    setActivate(0.7);
  
    }


  //#region MANUAL CONTROL

    // ATIVA O SHOOTER
    public void setActivate(double rpm){

      ct_left.set(rpm);
      ct_right.set(ct_left.get());

    }

    // YAW
    public void rotation(double y) {
      /*
      if (sn_limitRight.get() && y > 0) { 

        ct_yaw.set(VictorSPXControlMode.PercentOutput, 0);

      } else if (sn_limitLeft.get() && y < 0) { 

        ct_yaw.set(VictorSPXControlMode.PercentOutput, 0);
      
      } else ct_yaw.set(VictorSPXControlMode.PercentOutput, y);
      //*/
      ct_yaw.set(VictorSPXControlMode.PercentOutput, y);

    //*/
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

    public void servoDisable (boolean dual) {

      /*
      if (dual){

        at_pitchR.setDisabled();
        at_pitchL.setDisabled();
      
      } else at_pitchL.setDisabled();
      */
      at_pitchR.setDisabled();
      at_pitchL.setDisabled();
    }
  
  //#endregion
  
  //#region LL CONTROLS

    // RETORNA INDENTIFICAÇAO
    public boolean isLimelightDetected() {

      return tv.getDouble(0.0) == 1.0;
  
    }
  
    // ATIVA CONTROLE DA LIMELIGHT NO YAW
    public void limelightYawControl(){
/*
      if (sn_limitRight.get() && tx.getDouble(0) > 0) {

        ct_yaw.set(VictorSPXControlMode.PercentOutput, 0);

      } else if (sn_limitLeft.get() && tx.getDouble(0) < 0) {

        ct_yaw.set(VictorSPXControlMode.PercentOutput, 0);
      
      } else ct_yaw.set(VictorSPXControlMode.PercentOutput, tx.getDouble(0) * 0.1);*/

      ct_yaw.set(VictorSPXControlMode.PercentOutput, tx.getDouble(0) * 0.1);


    }
  
    // ATIVA CONTROLE DA LIMELIGHT NO PITCH
    public void limelightPitchControl(boolean dual) {

      // Relaçao proporcional (linha direta)
      pitchPos = ty.getDouble(maxAngle);//SmartDashboard.getNumber("LL Angle", 16.0);
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
  
      //if (dual) at_pitchL.set(pitchPos);
  
      at_pitchL.set(pitchPos);
      at_pitchR.set(-pitchPos + 0.5);
  
    }
  
  //#endregion


  // PERIODICA
  @Override
  public void periodic() {

    // ATUALIZA CORREÇAO DO ENCODER DO SHOOTER
    //encoderCorrection();

    SmartDashboard.putNumber("shooter v", sn_shotEnc.getVelocity());

    SmartDashboard.putNumber("Servo 0", at_pitchR.get());
    SmartDashboard.putNumber("Servo 1", at_pitchL.get());

    SmartDashboard.putBoolean("Limit Right", sn_limitRight.get());
    SmartDashboard.putBoolean("Limit Left", sn_limitLeft.get());

    SmartDashboard.putNumber("LL Y", ty.getDouble(0));

  }

}