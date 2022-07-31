
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


// CODE
public class Collector extends SubsystemBase {
 
  // CRIACAO DA VARIAVEL
  private VictorSPX ct_coll;
  private VictorSPX at_sold;

  public Collector() {

    // INICIALIZACAO DO COLETOR
    try {
      ct_coll = new VictorSPX(Constants.Motors.Collector.COLLECTOR);
      at_sold = new VictorSPX(Constants.Soleinoid.COLETOR);

    } catch (Exception ex) {
      System.out.printf("\n\nERRO NA BUSCA DE CONTROLADOR, LINHA: %s\n\n", ex.getStackTrace()[0]);
      return;

    }
  }

  // FUNCAO DO SISTEMA DE COLETOR
  public void collect(double c) {
    ct_coll.set(ControlMode.PercentOutput, c);
  }

  // CONTROLE SOLENOID
  public void collectorSolenoid (boolean sol){
    
    at_sold.set(ControlMode.PercentOutput, sol ? 1 : 0);

  }

  @Override
  public void periodic() {

  }
}
