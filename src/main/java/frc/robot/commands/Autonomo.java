
package frc.robot.commands;

// IMPORTS
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;

// CODE
public class Autonomo extends CommandBase {

  //#region CRIAÇAO DAS VARIAVEIS
  //Timers e classes que iremos utilizar no autonomo
  Drivetrain _drive;
  Storage _sto;
  Shooter _sho;
  Timer t_drive;
  Timer t_sto;
  Timer t_sho;

  //#endregion

  public Autonomo(Drivetrain d, Shooter sh, Storage st) {

    addRequirements(d);
    addRequirements(sh);
    addRequirements(st);

    _drive = d;
    _sho   = sh;
    _sto   = st;

    t_drive = new Timer();
    t_sho   = new Timer();
    t_sto   = new Timer();
  }

  @Override
  public void initialize() {
    t_drive.start();
    t_sho.start();
    t_sto.start();
  }

  // CÓDIGO NÃO TESTADO
  // ATENÇÃO QUANDO FOR LIGAR O AUTÔNOMO

  // COMANDO AUTONOMO
  @Override
  public void execute() {

    //Drive
    if (t_drive.get() < 1) {
      _drive.distancia(0.1, 0, 100); //distancia em cm
    }

    //Shooting
    if (t_sho.get() < 2) {
      //_sho.shoot(0.1);
    } else {
      //_sho.shoot(0);
    }

    //Storage
    if (t_sto.get() < 3) {
      _sto.setFeeder(0.1);
    } else {
      _sto.setFeeder(0);
    }

    //*/
  }


  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
