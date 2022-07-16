
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

// CODE
public class Drivetrain extends SubsystemBase {

  //#region CRIACAO DAS VARIAVEIS

  // GIROSCÓPIO
  private ADXRS450_Gyro _gyro;

  // METODO DE CONTAGEM CONTINUA
  private double _d        = 0;
  private double _e        = 0;
  private int _revolutionD = 0;
  private int _revolutionE = 0;

  // ANGULO DE ROTAÇAO
  private double _angle = 0;

  // CORRECAO DE PID
  private double _correction; //Correçao de direçao
  private double _correctionVel; //Correçao de velocidade 
  private PIDController _autDirPID;
  private PIDController _autEncPID;

  // CRIANDO OS CONTROLADORES DO SISTEMA DE TRACAO  
  private WPI_TalonSRX _lFront, _lBack, _rFront, _rBack;
  
  // CRIANDO O AGRUPAMENTO DOS CONTROLADORES
  private MotorControllerGroup _left, _right;
  
  // CRIANDO O DIFERENCIAL DO SISTEMA DE TRACAO
  private DifferentialDrive _drive;

  private Timer moveTime;

  //#endregion

  public Drivetrain() {

    //#region INICIALIZACAO DO SISTEMA

    // DEFININDO OS CONTROLADORES DO SISTEMA DE TRACAO 

    _lFront = new WPI_TalonSRX (Constants.Motors.Drivetrain._left_front);
    _lBack  = new WPI_TalonSRX (Constants.Motors.Drivetrain._left_back);
    _rFront = new WPI_TalonSRX (Constants.Motors.Drivetrain._right_front);
    _rBack  = new WPI_TalonSRX (Constants.Motors.Drivetrain._right_back);
    
    // DEFININDO OS AGRUPAMENTO DOS CONTROLADORES
    _left  = new MotorControllerGroup(_lFront, _lBack);
    _right = new MotorControllerGroup(_rFront, _rBack);

    // DEFININDO O DIFERENCIAL DO SISTEMA DE TRACAO
    _drive = new DifferentialDrive(_left, _right);
    
    // CONFIGURAÇAO GYRO
    _gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
    _gyro.calibrate();

    // PARAMETROS PID
    _autDirPID.setPID(0.001, 0, 0);
    _autEncPID.setPID(0.001, 0, 0);
    
    // CONFIGURAÇAO DOS ENCODERS
    _lFront.configFactoryDefault();
    _rFront.configFactoryDefault();
    //_lFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    //_rFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);

    moveTime = new Timer();
    moveTime.start();

    //#endregion

  }

  // FUNCAO DO SISTEMA DE TRACAO (EIXO Y, EIXO X)
  public void direction(double Y, double X) {
    _drive.arcadeDrive(Y, X);
  }

  // RETORNA ANGULO DO GYRO
  public double gyroAngle () { 
    return _gyro.getAngle();
  }

  // MOVE EM UMA DIREÇAO
  public void move (double vel, double dir) {

    _angle += dir;
    _autDirPID.setSetpoint(_angle);
    direction(vel, _correction);

  }

  // MOVE UMA DISTANCIA EM UMA DIREÇAO
  public void distancia (double vel, double dir, double dis) {

    // RESET ENCODERS
    //_lFront.setSelectedSensorPosition(0);
    //_rFront.setSelectedSensorPosition(0);

    moveTime.reset();

    _autEncPID.setSetpoint(dis / 80);// * 22);

    move(_correctionVel * vel, dir);
    
  }

  // CONVERTE VALOR PERIODICO DOS ENCODERS PARA CONTINUO
  public double vc (char lado) {

    if (3000 < Math.abs(_d - _rFront.getSelectedSensorPosition(1))){

      if (_d > _rFront.getSelectedSensorPosition(1)) _revolutionD ++;
      else _revolutionD --;

    }

    if (3000 < Math.abs(_e - _lFront.getSelectedSensorPosition(0))){

      if (_e > _lFront.getSelectedSensorPosition(0)) _revolutionE ++;
      else _revolutionE --;

    }

    _e = _lFront.getSelectedSensorPosition(0);
    _d = _rFront.getSelectedSensorPosition(1);

    if (lado == 'd') return _revolutionD * 4094  + _d;
    else return _revolutionE * 4094  + _e;

  }

  // PERIODICA
  @Override
  public void periodic() {

    SmartDashboard.putNumber("gyro", _gyro.getAngle());
    _correction    = _autDirPID.calculate(moveTime.get());//_gyro.getAngle());
    _correctionVel = _autEncPID.calculate((vc('e') + vc('d')) / 2);

  }
}
