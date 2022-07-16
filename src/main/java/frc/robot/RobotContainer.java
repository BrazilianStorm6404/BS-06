
package frc.robot;

// IMPORTS
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.Motors.Climber;
import frc.robot.commands.Autonomo;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;
import frc.robot.subsystems.Tests;

// CODE
public class RobotContainer {

  //#region CRIANDO OS OBJETOS

  // CONTROLES (PILOTO E CO_PILOTO)
  XboxController pilot;
  XboxController co_pilot;

  // SUBSISTEMA COM PADRAO M_ NA FRENTE
  Autonomo c_auto;

  Drivetrain m_drive;
  Collector m_coll;
  Storage m_stor;
  Climber m_climb;
  Shooter m_shot;
  Tests m_test;
  Drive m_Drive;

  // COMANDO COM PADRAO C_ NA FRENTE
  Timer _d;

  // SENSORES, ETC PADRAO _ NA FRENTE
  Timer _t;

  boolean reset;
  boolean resetSto;


  //#endregion
 
  public RobotContainer() {

  //#region DEFINIÇAO
  
    // DEFININDO CONTROLES (PILOTO E CO_PILOTO)
    pilot    = new XboxController(Constants.Control_map._pilot);
    co_pilot = new XboxController(Constants.Control_map._co_pilot);

    // DEFININDO SUBSISTEMAS NO CONTAINER
    //m_drive = new Drivetrain();
    //m_coll  = new Collector();
    //m_shot  = new Shooter();
    //m_stor  = new Storage();
    //m_test  = new Tests();
    //m_climb = new Climber();
    m_Drive  = new Drive();

    // DEFININDO SENSORES, ETC
    _t = new Timer();
    _d = new Timer();

    resetSto = true;
    reset = true;


    configureButtonBindings();
 
    //#endregion
  
  }

  private void configureButtonBindings() {

    //#region DRIVETRAIN
    /*
    m_drive.setDefaultCommand(new RunCommand(() -> {
      m_drive.direction(pilot.getRightX(), -pilot.getLeftY());
    }, m_drive));
    //*/

    //#endregion

    //#region COLLECTOR
  /*
   m_coll.setDefaultCommand(new RunCommand(() -> {

    // CONTROLE SOLENOID
    if(co_pilot.getAButton()) {
      m_coll.collectorSolenoid(true);
    } else {
      m_coll.collectorSolenoid(false);
    }

    /*
    // MOVE COLLECTOR
    if (co_pilot.getLeftTriggerAxis() > 0) {
      m_coll.move_c(0.9);
    } else if (co_pilot.getLeftBumper()) {
      m_coll.move_c(-0.3);
    } else {
      m_coll.move_c(0.0);
    }
    

    // COLLECTOR
    if (pilot.getRightTriggerAxis() > 0) {
      m_coll.collect(0.5);
    } else if (pilot.getLeftTriggerAxis() > 0) {
      m_coll.collect(-0.5);
    } else {
      m_coll.collect(0.0);
    }
    
  }, m_coll));
  //*/
    

    //#endregion
 
    //#region SHOOTER
    /*
    m_shot.setDefaultCommand(new RunCommand(() -> {

      // SHOOTER
      //m_shot.setActivate(co_pilot.getAButton());
      //m_shot.servoMov(co_pilot.getLeftTriggerAxis()); 

      if (co_pilot.getRightTriggerAxis() > 0) {

        if (reset) {

          _t.reset();
          _t.start();

          _d.reset();
          _d.start();

          reset = false;

          m_shot.chute(true);

         } else {

          m_shot.chute(false);

         }

      } else {

        if (!reset) {

          _d.start();
          m_shot.resetPitch();
          m_shot.setActivate(0);

        }

        reset    = true;

        
      }

       if (_d.get() > 3) {
         m_shot.servoDisable();
         _d.reset();
         _d.stop();
        }

        SmartDashboard.putNumber("eeee", _d.get());
        /*
      if (!m_shot.isLimelightDetected()) m_shot.rotation(co_pilot.getRightX());
      else m_shot.limelightYawControl(); // CONTROLE AUTOMATICO PITCH/YAW LIMELIGHT

    }, m_shot));
        //*/

    //*/
    //#endregion
    
    //#region STORAGE
    /*
    m_stor.setDefaultCommand(new RunCommand(() -> {
      
      // STORAGE
      if (pilot.getAButton() && _t >= 2) {
        m_stor.stor(0.5);
      } else if (pilot.getBButton()) { // igual
        m_stor.stor(-0.5);
      } else {
        m_stor.stor(0);
      }

    }, m_stor));
    //*/
    //#endregion

    //#region TESTES
/*
    m_test.setDefaultCommand(new RunCommand(() -> {

      if (co_pilot.getBButtonReleased()){

    		m_test.moveMotionMagic(100, 0);

      }

    }, m_test));
     //*/

      //#endregion
  ///*
    m_Drive.setDefaultCommand(new RunCommand(() -> {

  	  m_Drive.jaguarDrive(-co_pilot.getLeftY(), co_pilot.getRightX());

    }, m_Drive));
    //*/
  }

  // COMANDO AUTONOMO
  public Command getAutonomousCommand() {
    return null; //c_auto;
  }
}