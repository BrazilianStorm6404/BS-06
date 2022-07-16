
package frc.robot;

// IMPORTS
import edu.wpi.first.wpilibj.RobotBase;

// CODE
public final class Main {
  private Main() {}

  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}
