
package frc.robot.commands;

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

  //#endregion

  public Autonomo(Drivetrain dr, Shooter sh, Storage st, Collector cl) {

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

  }

  @Override
  public void initialize() {

    t_drive.start();

    sb_drive.distancia(0.5, -100);
    
  }

  // CÓDIGO NÃO TESTADO
  // ATENÇÃO QUANDO FOR LIGAR O AUTÔNOMO

  // COMANDO AUTONOMO
  @Override
  public void execute() {

    sb_drive.correction();

/*
    t_sho.start();
    //Shooting
    sb_shooter.chute(true);

    pause(3);

    sb_shooter.servoDisable(false);
    sb_shooter.chute(false);

    sb_storage.setFeeder(-0.9);
    sb_storage.setConveyor(0.9);

    pause(3);

    sb_shooter.setActivate(0);

    sb_storage.setFeeder(0);
    sb_storage.setConveyor(0);

    sb_shooter.resetPitch();
    pause(3);
    sb_shooter.servoDisable(true);
*/

  }


  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
