
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


// CODE
public class Collector extends SubsystemBase {
 
  // CRIACAO DA VARIAVEL
  private VictorSPX _coll;
  private WPI_TalonSRX _solenoid;

  public Collector() {

    // INICIALIZACAO DO COLETOR
    _coll     = new VictorSPX(Constants.Motors.Collector._collector);
    _solenoid = new WPI_TalonSRX(Constants.Soleinoid._coletor);

  }

  // FUNCAO DO SISTEMA DE COLETOR
  public void collect(double c) {
    _coll.set(ControlMode.PercentOutput, c);
  }

  // CONTROLE SOLENOID
  public void collectorSolenoid (double sol){
    
    _solenoid.set(sol);

  }

  @Override
  public void periodic() {

  }
}
