
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
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
  Joystick logPilot;
  

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
    logPilot = new Joystick(3);

    // DEFININDO SUBSISTEMAS NO CONTAINER
    sb_Camera    = new Camera();
    sb_shooter   = new Shooter();
    sb_storge    = new Storage();
    sb_collector = new Collector();
    //sb_test      = new Tests();
    sb_drive     = new Drivetrain();
    //sb_climber   = new Climber();
    //sb_Drive     = new Drive();
    cm_auto      = new Autonomo(sb_drive, sb_shooter, sb_storge, sb_collector);

    // DEFININDO SENSORES, ETC
    t_tStor  = new Timer();
    t_tPitch = new Timer();

    resetSto = true;
    reset    = true;

    configureButtonBindings();
 
    //#endregion
  
  }

  private void configureButtonBindings() {

    //DRIVETRAIN
    //try { 
      sb_drive.setDefaultCommand(new RunCommand(() -> {

        //SmartDashboard.putNumber("Y", logPilot.getRawAxis(1));
        //SmartDashboard.putNumber("X", logPilot.getRawAxis(4));

        sb_drive.direction(logPilot.getRawAxis(4), -logPilot.getRawAxis(1));

      }, sb_drive));// } catch (Exception ex) { errMesg(ex); }
  //*/

    //COLLECTOR
    //try { 
      sb_collector.setDefaultCommand(new RunCommand(() -> {

        // CONTROLE SOLENOID
       if(pilot.getXButton()) {

         sb_collector.collectorSolenoid(true);

       } else sb_collector.collectorSolenoid(false);
      
       /*
        // COLLECTOR
        if (pilot.getRightTriggerAxis() > 0) {

          sb_collector.collect(0.7);

        } else if (pilot.getLeftTriggerAxis()  > 0) {

          sb_collector.collect(-0.5);

        } else if (coPilot.getRightBumper()) {

          sb_collector.collect(-0.5);

        } else sb_collector.collect(0.0);
        //*/

        if (pilot.getRightTriggerAxis() >0) {

          sb_collector.collect(-0.7);

        } else if (pilot.getLeftTriggerAxis() > 0) {

          sb_collector.collect(0.5);

        } /*else if (coPilot.getRightBumper()) {

          sb_collector.collect(-0.5);

        }//*/ else sb_collector.collect(0.0);
      

      }, sb_collector));// } catch (Exception ex) { errMesg(ex); }
    //*/
  
 
    //SHOOTER
    //try { 
      sb_shooter.setDefaultCommand(new RunCommand(() -> {

      if (coPilot.getLeftTriggerAxis() > 0) { //ALTERA PRA COPILOTO DEPOIS SFD


        if (reset) {

          t_tPitch.reset();
          t_tPitch.start();

          t_tStor.reset();
          t_tStor.start();

          reset = false;

          sb_shooter.chute(true);

         } else sb_shooter.chute(false);


      } else {


        if (!reset) {

          t_tPitch.start();
          sb_shooter.resetPitch();
          sb_shooter.setActivate(0);

        }

        reset    = true;

      }


      if (t_tPitch.get() > 3) {

        sb_shooter.servoDisable(true);
        t_tPitch.reset();
        t_tPitch.stop();

      }

      //sb_shooter.rotation(coPilot.getRightX());
      //sb_shooter.rotation(coPilot.getRightX());
      
      if (sb_shooter.isLimelightDetected()) {
        
        sb_shooter.limelightYawControl(); // CONTROLE AUTOMATICO PITCH/YAW LIMELIGHT
      
      } else sb_shooter.rotation(coPilot.getRightX());
      //*/

      }, sb_shooter)); //} catch (Exception ex) { errMesg(ex); }
    //*/
    

    //STORAGE
    //try { 
      sb_storge.setDefaultCommand(new RunCommand(() -> {

        // STORAGE
        /*if (pilot.getRightTriggerAxis() > 0) {

          sb_storge.setFeeder(0.75);

        } else if (pilot.getLeftTriggerAxis() > 0) {

          sb_storge.setFeeder(-0.75);

        } else sb_storge.setFeeder(0);


        if (coPilot.getRightTriggerAxis() > 0) {

          sb_storge.setConveyor(0.75);

        } else if (coPilot.getRightBumper()) {

          sb_storge.setConveyor(-0.75);

        } else sb_storge.setFeeder(0);
     //*/

        if (coPilot.getRightTriggerAxis() > 0) {

          sb_storge.setFeeder(-0.9);
          sb_storge.setConveyor(0.9);

        } else if (pilot.getRightTriggerAxis() > 0) {

          sb_storge.setFeeder(-0.8);
          sb_storge.setConveyor(-0.9);

        } else if (pilot.getLeftTriggerAxis() > 0) {

          sb_storge.setFeeder(0.8);
          sb_storge.setConveyor(-0.8);

        } else {

          sb_storge.setConveyor(0);
          sb_storge.setFeeder(0);

        }


      }, sb_storge)); //} catch (Exception ex) { errMesg(ex); }
 
  
/*
    //CLIMBER 
    try{ sb_climber.setDefaultCommand(new RunCommand(() -> {

        if (coPilot.getBackButton()) {

          if (coPilot.getXButton()) { 

            sb_climber.climber(true);

          } else sb_climber.climber(false);

        }


      }, sb_climber)); } catch (Exception ex) { errMesg(ex); }
  
    
*/
    /*//TESTES
    try { sb_test.setDefaultCommand(new RunCommand(() -> {

        if (coPilot.getBButtonReleased()) sb_test.moveMotionMagic(100, 0);

      }, sb_test)); } catch (Exception ex) { errMesg(ex); }
     //*/

  }

  public void errMesg (Exception ex) {

    System.out.printf("\n\nERRO AO EXECUTAR FUNÇAO/FUNÇOES DA CLASSE %s, LINHA: %c\n\n", this.getClass(), ex.getStackTrace()[0]);
    

  }

  // COMANDO AUTONOMO
  public Command getAutonomousCommand() {
    return cm_auto; 
  }
}