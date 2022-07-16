
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

// CODE
public class Climber extends SubsystemBase {
  
  // CRIANDO OS CONTROLADORES DO SISTEMA DE ESCALADA
  private VictorSPX _c_tube1, _c_tube2, _RsolEt1, _RsolEt2, _LsolEt1, _LsolEt2;

  public Climber() {

    // DEFININDO OS CONTROLADORS DO SISTEMA DE ESCALADA
    _c_tube1 = new VictorSPX(Constants.Motors.Climber._climber_tube);
    _c_tube2 = new VictorSPX(Constants.Motors.Climber._climber_tube);

    _RsolEt1 = new VictorSPX(Constants.Soleinoid._climber);
    _RsolEt2 = new VictorSPX(Constants.Soleinoid._climber);
    _LsolEt1 = new VictorSPX(Constants.Soleinoid._climber);
    _LsolEt2 = new VictorSPX(Constants.Soleinoid._climber);


  }

  // FUNCAO DO SISTEMA DE ESCALADA
  public void climbing1(double ct1) {
    _c_tube1.set(ControlMode.PercentOutput, ct1);
  }

  public void climbing2(double ct2) {
    _c_tube2.set(ControlMode.PercentOutput, ct2);
  }

  public void climberSolenoidET1 (Boolean activate){
    
    _RsolEt1.set(ControlMode.PercentOutput, activate ? 1.0 : 0);
    _LsolEt1.set(ControlMode.PercentOutput, activate ? 1.0 : 0);

  }

  public void climberSolenoidET2 (Boolean activate){
    
    _RsolEt2.set(ControlMode.PercentOutput, activate ? 1.0 : 0);
    _LsolEt2.set(ControlMode.PercentOutput, activate ? 1.0 : 0);

  }

  @Override
  public void periodic() {

  }
}
//*/