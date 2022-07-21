
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
  private ADXRS450_Gyro sn_gyro;

  // METODO DE CONTAGEM CONTINUA
  private double d = 0, e = 0;
  private int revD = 0, revE = 0;

  // ANGULO DE ROTAÇAO
  private double angle = 0;

  // CORRECAO DE PID
  private double corrct, corrctVel; //Correçao de velocidade 
  private PIDController _autDirPID, _autEncPID;

  // CRIANDO OS CONTROLADORES DO SISTEMA DE TRACAO
  private WPI_TalonSRX ct_lFront, ct_lBack, ct_rFront, ct_rBack;
  
  // CRIANDO O AGRUPAMENTO DOS CONTROLADORES
  private MotorControllerGroup left, right;
  
  // CRIANDO O DIFERENCIAL DO SISTEMA DE TRACAO
  private DifferentialDrive _drive;

  private Timer t_moveTime;

  //#endregion

  public Drivetrain() {

    //#region INICIALIZACAO DO SISTEMA

    // DEFININDO OS CONTROLADORES DO SISTEMA DE TRACAO 

    try {
      ct_lFront = new WPI_TalonSRX (Constants.Motors.Drivetrain.LEFT_FRONT);
      ct_lBack  = new WPI_TalonSRX (Constants.Motors.Drivetrain.LEFT_BACK);
      ct_rFront = new WPI_TalonSRX (Constants.Motors.Drivetrain.RIGHT_FRONT);
      ct_rBack  = new WPI_TalonSRX (Constants.Motors.Drivetrain.RIGHT_BACK);
    } catch (Exception ex) {
      System.out.println("Erro na busca de controlador");

    }
    
    // DEFININDO OS AGRUPAMENTO DOS CONTROLADORES
    left  = new MotorControllerGroup(ct_lFront, ct_lBack);
    right = new MotorControllerGroup(ct_rFront, ct_rBack);

    // DEFININDO O DIFERENCIAL DO SISTEMA DE TRACAO
    _drive = new DifferentialDrive(left, right);
    
    // CONFIGURAÇAO GYRO
    sn_gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
    sn_gyro.calibrate();

    // PARAMETROS PID
    _autDirPID.setPID(0.001, 0, 0);
    _autEncPID.setPID(0.001, 0, 0);
    
    // CONFIGURAÇAO DOS ENCODERS
    ct_lFront.configFactoryDefault();
    ct_rFront.configFactoryDefault();
    //ct_lFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    //ct_rFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);

    t_moveTime = new Timer();
    t_moveTime.start();

    //#endregion

  }

  // FUNCAO DO SISTEMA DE TRACAO (EIXO Y, EIXO X)
  public void direction(double Y, double X) {
    _drive.arcadeDrive(Y, X);
  }

  // RETORNA ANGULO DO GYRO
  public double gyroAngle () { 
    return sn_gyro.getAngle();
  }

  // MOVE EM UMA DIREÇAO
  public void move (double vel, double dir) {

    angle += dir;
    _autDirPID.setSetpoint(angle);
    direction(vel, corrct);

  }

  // MOVE UMA DISTANCIA EM UMA DIREÇAO
  public void distancia (double vel, double dir, double dist) {

    // RESET ENCODERS
    //_lFront.setSelectedSensorPosition(0);
    //_rFront.setSelectedSensorPosition(0);

    t_moveTime.reset();

    _autEncPID.setSetpoint(dist / 80);// * 22);

    move(corrctVel * vel, dir);
    
  }

  // CONVERTE VALOR PERIODICO DOS ENCODERS PARA CONTINUO
  public double vc (char lado) {

    if (3000 < Math.abs(d - ct_rFront.getSelectedSensorPosition(1))){

      if (d > ct_rFront.getSelectedSensorPosition(1)) revD ++;
      else revD --;

    }

    if (3000 < Math.abs(e - ct_lFront.getSelectedSensorPosition(0))){

      if (e > ct_lFront.getSelectedSensorPosition(0)) revE ++;
      else revE --;

    }

    e = ct_lFront.getSelectedSensorPosition(0);
    d = ct_rFront.getSelectedSensorPosition(1);

    if (lado == 'd') return revD * 4094  + d;
    else return revE * 4094  + e;

  }

  // PERIODICA
  @Override
  public void periodic() {

    SmartDashboard.putNumber("gyro", sn_gyro.getAngle());
    corrct    = _autDirPID.calculate(t_moveTime.get());//_gyro.getAngle());
    corrctVel = _autEncPID.calculate((vc('e') + vc('d')) / 2);

  }
}
