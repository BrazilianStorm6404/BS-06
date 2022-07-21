
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


// CODE
public class Robot extends TimedRobot {

  private Command cm_autonomousCommand;
  private RobotContainer _robotContainer;


  @Override
  public void robotInit() {
    try {
      _robotContainer = new RobotContainer();
    } catch (Exception ex) {
      System.out.println("Erro em "+this.getClass()+": Erro ao iniciar" );
      return;
    }
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
	}

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    try {
        cm_autonomousCommand = _robotContainer.getAutonomousCommand();
      if (cm_autonomousCommand != null) {
        cm_autonomousCommand.schedule();
    }
    } catch (Exception ex) {
      System.out.println("Erro em "+this.getClass()+": Erro ao executar código autônomo");
      return;
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    try {

      if (cm_autonomousCommand != null) {
        cm_autonomousCommand.cancel();
      }

    } catch (Exception ex) {
      System.out.println("Erro em "+this.getClass()+": Erro ao executar código tele-operado");
      return;
    }
    
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}
}
