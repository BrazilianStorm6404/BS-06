
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


// CODE
public class Collector extends SubsystemBase {
 
  // CRIACAO DA VARIAVEL
  private VictorSPX ct_coll;
  private WPI_TalonSRX at_sold;

  public Collector() {

    // INICIALIZACAO DO COLETOR
    try {
      ct_coll = new VictorSPX(Constants.Motors.Collector.COLLECTOR);
      at_sold = new WPI_TalonSRX(Constants.Soleinoid.COLETOR);

    } catch (Exception ex) {
      System.out.println("Erro na busca de controlador");

    }

  }

  // FUNCAO DO SISTEMA DE COLETOR
  public void collect(double c) {
    ct_coll.set(ControlMode.PercentOutput, c);
  }

  // CONTROLE SOLENOID
  public void collectorSolenoid (boolean sol){
    
    at_sold.set(sol ? 1 : 0);

  }

  @Override
  public void periodic() {

  }
}
