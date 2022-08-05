
package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
// IMPORTS
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;

// CODE
public class Autonomo extends CommandBase {

  //#region CRIAÇAO DAS VARIAVEIS
  //Timers e classes que iremos utilizar no autonomo
  Drivetrain sb_drive;
  Storage sb_storage;
  Shooter sb_shooter;
  Collector sb_collector;
  Timer t_drive;
  Timer t_sto;
  Timer t_sho;
  boolean isFinished, et1, et2;

  NetworkTable table;

  //#endregion

  public Autonomo(Drivetrain dr, Shooter sh, Storage st, Collector cl) {

    table = NetworkTableInstance.getDefault().getTable("limelight");

    table.getEntry("ledMode").setNumber(3);
    //table.getEntry("camMode").setNumber(1);
    //table.getEntry("pipeline").setNumber(2);


    addRequirements(dr);
    addRequirements(sh);
    addRequirements(st);
    addRequirements(cl);

    sb_drive     = dr;
    sb_shooter   = sh;
    sb_storage   = st;
    sb_collector = cl;

    t_drive = new Timer();
    t_sho   = new Timer();
    t_sto   = new Timer();

    et1 = true;
    et2 = true;

    isFinished = false;
  

  }

  @Override
  public void initialize() {
    t_drive.reset();
    t_drive.start();
    sb_drive.resetEnc();
    
  }

  // CÓDIGO NÃO TESTADO
  // ATENÇÃO QUANDO FOR LIGAR O AUTÔNOMO

  // COMANDO AUTONOMO
  @Override
  public void execute() {

    if (et1 && !sb_drive.isMove()) {

      //sb_drive.direction(0, 0.4);
      sb_drive.distancia(0.5, -200);

    } else {

      et1 = false;
      t_drive.start();

    }
    SmartDashboard.putBoolean("aaaa", sb_drive.isMove());

    if (!et1 && t_drive.get() <= 2) {

      sb_shooter.chute(true);

    } else if (!et1 && t_drive.get() <= 2.5) { 

      sb_shooter.servoDisable(false);

    } else if (!et1 && t_drive.get() <= 6) {
      
      sb_shooter.chute(false);

      sb_storage.setFeeder(-0.9);
      sb_storage.setConveyor(0.9);

    } else if (!et1 && t_drive.get() > 6) {

      sb_shooter.setActivate(0);

      sb_storage.setFeeder(0);
      sb_storage.setConveyor(0);


      if (t_drive.get() >= 8) {

        t_drive.stop();
        sb_shooter.servoDisable(true);
        isFinished = true;
      } else sb_shooter.resetPitch();

    } 



  /*
      if (et1 && t_drive.get() <= 2) {
  
        sb_shooter.chute(true);
  
      } else if (et1 && t_drive.get() <= 2.5) { 
  
        sb_shooter.servoDisable(false);
  
      } else if (et1 && t_drive.get() <= 6) {
        
        sb_shooter.chute(false);
  
        sb_storage.setFeeder(-0.9);
        sb_storage.setConveyor(0.9);
  
      } else if (et1 && t_drive.get() > 6) {
  
        sb_shooter.setActivate(0);
  
        sb_storage.setFeeder(0);
        sb_storage.setConveyor(0);
  
  
        if (t_drive.get() >= 8) {
  
          t_drive.stop();
          sb_shooter.servoDisable(true);
          et1 = false;
  
        } else sb_shooter.resetPitch();

      }

    if (!et1) {

      sb_drive.distancia(0.5, -100);
      isFinished = sb_drive.isMove();
      
    }

    //*/

  }


  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
