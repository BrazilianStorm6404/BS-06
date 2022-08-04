
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
// IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.controller.PIDController;
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
  //private AHRS sn_gyro;

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
  private boolean move;

  //#endregion

  public Drivetrain() {

    // CONFIGURAÇAO GYRO
    //sn_gyro = new AHRS(Port.kMXP);

    //#region INICIALIZACAO DO SISTEMA

    // DEFININDO OS CONTROLADORES DO SISTEMA DE TRACAO 

    try {

      ct_lFront = new WPI_TalonSRX (Constants.Motors.Drivetrain.LEFT_FRONT);
      ct_lBack  = new WPI_TalonSRX (Constants.Motors.Drivetrain.LEFT_BACK);
      ct_rFront = new WPI_TalonSRX (Constants.Motors.Drivetrain.RIGHT_FRONT);
      ct_rBack  = new WPI_TalonSRX (Constants.Motors.Drivetrain.RIGHT_BACK);

    } catch (Exception ex) {

      System.out.printf("\n\nERRO NA BUSCA DE CONTROLADOR, LINHA: %s\n\n", ex.getStackTrace()[0]);
      //return;

    }
    
    // DEFININDO OS AGRUPAMENTO DOS CONTROLADORES
    left  = new MotorControllerGroup(ct_lFront, ct_lBack);
    right = new MotorControllerGroup(ct_rFront, ct_rBack);

    // DEFININDO O DIFERENCIAL DO SISTEMA DE TRACAO
    _drive = new DifferentialDrive(left, right);

    move = false;
    
    // CONFIGURAÇAO DOS ENCODERS
    ct_lBack.configFactoryDefault();
    ct_rBack.configFactoryDefault();
    
    ct_lBack.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    ct_rBack.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    
    ct_lBack.setSensorPhase(true);
    ct_rBack.setSensorPhase(false);
    
    ct_rBack.setSelectedSensorPosition(0);
    ct_lBack.setSelectedSensorPosition(0);
    
    _autEncPID = new PIDController(0.00001, 0.0, 0.0);

    _autEncPID.setSetpoint(0);

    _autEncPID.setTolerance(0.1);

    //#endregion

  }

  // FUNCAO DO SISTEMA DE TRACAO (EIXO Y, EIXO X)
  public void direction(double X, double Y) {
    _drive.arcadeDrive(X, Y);
  }

  // MOVE UMA DISTANCIA EM UMA DIREÇAO
  public boolean distancia (double v, double dist) {

    // RESET ENCODERS
    if (!move){

      ct_rBack.setSelectedSensorPosition(0);
      ct_lBack.setSelectedSensorPosition(0);
  
      //t_moveTime.reset();
  
      _autEncPID.setSetpoint(dist * 8000);

      move = true;

    }
   
    corrctVel = v * _autEncPID.calculate((ct_rBack.getSelectedSensorPosition() + ct_rBack.getSelectedSensorPosition()) / 2);
    direction(0, corrctVel);

    if (_autEncPID.atSetpoint()){

      move = false;
      return false;

    } else return true;
    
    
  }//*/

  // PERIODICA
  @Override
  public void periodic() {
    
    SmartDashboard.putNumber("rBack", ct_rBack.getSelectedSensorPosition());
    SmartDashboard.putNumber("lBack", ct_lBack.getSelectedSensorPosition());
    SmartDashboard.putNumber("vel", corrctVel * 0.5);

  }
}
