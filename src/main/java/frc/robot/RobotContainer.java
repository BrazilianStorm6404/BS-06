
package frc.robot;

// IMPORTS
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.Autonomo;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Climber;
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
  XboxController pilot, coPilot;

  // SUBSISTEMA COM PADRAO M_ NA FRENTE
  Autonomo cm_auto;
  Drivetrain sb_drive;
  Collector sb_collector;
  Storage sb_storge;
  Climber sb_climber;
  Shooter sb_shooter;
  Tests sb_test;
  Drive sb_Drive;
  Camera sb_Camera;

  // COMANDO COM PADRAO C_ NA FRENTE
  Timer t_tPitch, t_tStor;

  boolean reset, resetSto;


  //#endregion
 
  public RobotContainer() {

  //#region DEFINIÇAO
  
    // DEFININDO CONTROLES (PILOTO E CO_PILOTO)
    pilot   = new XboxController(Constants.Control_map.PILOT);
    coPilot = new XboxController(Constants.Control_map.CO_PILOT);

    // DEFININDO SUBSISTEMAS NO CONTAINER
    sb_shooter  = new Shooter();
    sb_storge     = new Storage();
    sb_collector = new Collector();
    //sb_test    = new Tests();
    sb_drive   = new Drivetrain();
    //sb_climber = new Climber();
    //sb_Drive   = new Drive();
    sb_Camera  = new Camera();

    // DEFININDO SENSORES, ETC
    t_tStor  = new Timer();
    t_tPitch = new Timer();

    resetSto = true;
    reset    = true;

    configureButtonBindings();
 
    //#endregion
  
  }

  private void configureButtonBindings() {

      sb_shooter.disableLed(false);

    //#region DRIVETRAIN
    try {

      sb_drive.setDefaultCommand(new RunCommand(() -> {

        sb_drive.direction(pilot.getRightX(), -pilot.getLeftY());

      }, sb_drive));

    } catch (Exception ex) {

      System.out.println("Erro ao executar funçoes da classe "+this.getClass());

    }
    //*/

    //#endregion

    //#region COLLECTOR
    try {

      sb_collector.setDefaultCommand(new RunCommand(() -> {

        // CONTROLE SOLENOID
       /* if(co_pilot.getAButton()) sb_collector.collectorSolenoid(true);
        else sb_collector.collectorSolenoid(false);
       //*/

        // COLLECTOR
        /*if      (pilot.getRightTriggerAxis() > 0) sb_collector.collect(0.7);
        else if (pilot.getLeftTriggerAxis()  > 0) sb_collector.collect(-0.5);
        else if (coPilot.getRightBumper()) sb_collector.collect(-0.5);
        else sb_collector.collect(0.0);
        //*/

        if      (pilot.getAButton()) sb_collector.collect(-0.7);
        else if (pilot.getBButton()) sb_collector.collect(0.5);
        else if (coPilot.getRightBumper()) sb_collector.collect(-0.5);
        else sb_collector.collect(0.0);
      
      }, sb_collector));

    } catch (Exception ex) {

      System.out.println("Erro ao executar funçoes da classe " + this.getClass());
      System.out.println("EEEEERRO: " + ex.getMessage());

      return;

    }
  //*/
    

    //#endregion
 
    //#region SHOOTER
    try {
    
      sb_shooter.setDefaultCommand(new RunCommand(() -> {

      // SHOOTER
      //sb_shoter.setActivate(coPilot.getAButton());
      //sb_shoter.servoMov(coPilot.getLeftTriggerAxis());

      if (pilot.getLeftTriggerAxis() > 0) { //ALTERA PRA COPILOTO DEPOIS SFD

        if (reset) {

          t_tPitch.reset();
          t_tPitch.start();

          t_tStor.reset();
          t_tStor.start();

          reset = false;

          sb_shooter.chute(true);

         } else {

          sb_shooter.chute(false);

         }

      } else {

        if (!reset) {

          t_tPitch.start();
          sb_shooter.resetPitch();
          sb_shooter.setActivate(0);

        }

        reset    = true;

        
      }

       if (t_tPitch.get() > 3) {
         sb_shooter.servoDisable();
        t_tPitch.reset();
        t_tPitch.stop();
        }

        SmartDashboard.putNumber("eeee", t_tPitch.get());
        /*
      if (sb_shooter.isLimelightDetected()) sb_shooter.limelightYawControl(); // CONTROLE AUTOMATICO PITCH/YAW LIMELIGHT
      else sb_shooter.rotation(coPilot.getRightX());
      //*/

    }, sb_shooter));

    } catch (Exception ex) {

      System.out.println("Erro ao executar funçoes da classe " + this.getClass());
      
  }
        

    //*/
    //#endregion
    
    //#region STORAGE
    try {

    sb_storge.setDefaultCommand(new RunCommand(() -> {
      
      // STORAGE
      /*if      (pilot.getRightTriggerAxis() > 0) sb_storge.setFeeder(0.75);
      else if (pilot.getLeftTriggerAxis() > 0)  sb_storge.setFeeder(-0.75);
      else sb_storge.setFeeder(0);

      if      (coPilot.getRightTriggerAxis() > 0) sb_storge.setConveyor(0.75);
      else if (coPilot.getRightBumper()) sb_storge.setConveyor(-0.75);
      else sb_storge.setFeeder(0);
      //*/

      if(pilot.getRightTriggerAxis() > 0) {
        sb_storge.setFeeder(0.8);
        sb_storge.setConveyor(-0.8);
      }
      else if (pilot.getAButton()) {
        sb_storge.setFeeder(0.8);
        sb_storge.setConveyor(-0.8);
      }
      else if (pilot.getBButton()) {
        sb_storge.setFeeder(-0.8);
        sb_storge.setConveyor(-0.8);
      }
      else sb_storge.setFeeder(0);

    }, sb_storge));

    } catch (Exception ex) {

    System.out.printf("Erro ao executar funçoes da classe %s (%s)", this.getClass(), ex.getStackTrace()[0]);

  }
    //*/
    //#endregion

    //#region TESTES
  /*
    try {

      sb_test.setDefaultCommand(new RunCommand(() -> {

        if (coPilot.getBButtonReleased()) sb_test.moveMotionMagic(100, 0);


      }, sb_test));

    } catch (Exception ex) {

      System.out.println("Erro ao executar funçoes da classe "+this.getClass());

    }
     //*/

      //#endregion
  
    //#region CLIMBER 

    try{

      sb_climber.setDefaultCommand(new RunCommand(() -> {

        if (coPilot.getBackButton()) {

          if   (coPilot.getXButton()) sb_climber.climber(true);
          else sb_climber.climber(false);
        }


      }, sb_climber));

    } catch (Exception ex) {

      System.out.println("Erro ao executar funçoes da classe " + this.getClass());

    }

    //#endregion

  }

  public void disableLed () {
    sb_shooter.disableLed(true);
  }

  // COMANDO AUTONOMO
  public Command getAutonomousCommand() {
    return null; //c_auto;
  }
}